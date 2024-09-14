package club.devcord.gamejam.logic;

import club.devcord.gamejam.CursedBedwarsPlugin;
import club.devcord.gamejam.logic.settings.GameSettings;
import club.devcord.gamejam.logic.shop.ShopNPC;
import club.devcord.gamejam.logic.team.Team;
import club.devcord.gamejam.message.Messenger;
import club.devcord.gamejam.timer.Countdown;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class Game {
    private final CursedBedwarsPlugin plugin;
    private GameStage gameStage;
    private GameMap gameMap;
    private final HashMap<NamedTextColor, Team> teams = new HashMap<>();
    private ScoreboardManager scoreboardManager;
    private Scoreboard teamsScoreboard;
    private ShopNPC shopNPC;

    private final Team spectatorTeam = new Team();

    public Game(CursedBedwarsPlugin plugin) {
        this.gameStage = GameStage.LOBBY;
        this.plugin = plugin;
    }

    public void startLobbyPhase() {
        teams.put(NamedTextColor.RED, new Team());
        teams.put(NamedTextColor.GREEN, new Team());
        teams.put(NamedTextColor.BLUE, new Team());
        teams.put(NamedTextColor.YELLOW, new Team());

        // Spectator Team
        teams.put(NamedTextColor.GRAY, new Team());

        this.scoreboardManager = plugin.getServer().getScoreboardManager();
        this.teamsScoreboard = scoreboardManager.getNewScoreboard();

        for (NamedTextColor teamColor : teams.keySet()) {
            var team = teamsScoreboard.registerNewTeam(teamColor.toString());
            team.prefix(Component.text("").color(NamedTextColor.GRAY)
                    .append(Component.text("[").color(NamedTextColor.GRAY))
                    .append(Component.text(String.valueOf(teamColor.toString().charAt(0)).toUpperCase())).color(teamColor)
                    .append(Component.text("] ").color(NamedTextColor.GRAY)));
            team.color(teamColor);
        }

        this.gameMap = new GameMap();

        gameMap.bukkitWorld().setDifficulty(Difficulty.PEACEFUL);
        gameMap.bukkitWorld().setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        gameMap.bukkitWorld().setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
    }

    public void startGameCountDown() {

        var countdown = new Countdown();

        countdown.start(30, TimeUnit.SECONDS, (second) -> {
            if (second == 30 || second == 20 || second == 15 || (second <= 10 && second != 0)) {
                plugin.messenger().broadCast(Messenger.PREFIX + "<green>Das Spiel startet in <yellow>" + second + " <green>Sekunden");
                plugin.getServer().getOnlinePlayers().forEach(player -> {
                    player.playSound(Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.MASTER, 1.0F, 1.0F));
                });
            }
        }, () -> {
            Bukkit.getScheduler().runTask(plugin, this::startGame);

            plugin.getServer().getOnlinePlayers().forEach(player -> {
                player.playSound(Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.MASTER, 1.0F, 1.0F));
            });

            plugin.messenger().broadCast(Messenger.PREFIX + "<green>Das Spiel startet");
        });
    }

    public void startGame() {
        this.gameStage = GameStage.IN_GAME;

        gameMap.bukkitWorld().getEntities().stream()
                .filter(entity -> entity.getType() == EntityType.ITEM)
                .forEach(Entity::remove);


    }

    public void tearDown() {
        World world = gameMap.bukkitWorld();

        Bukkit.unloadWorld(world, false);

        try {
            FileUtils.deleteDirectory(world.getWorldFolder());
        } catch (IOException ignored) {
            // Not much we can do
        }

        shopNPC.removeAll();

        Bukkit.shutdown();
        // plugin.serverApi().requestRestart();
    }

    public GameStage gameStage() {
        return gameStage;
    }

    public GameMap gameMap() {
        return gameMap;
    }

    public void switchPlayerToTeam(Player player, NamedTextColor color) {
        var teamOpt = getTeam(player);
        teamOpt.ifPresent(team -> team.teamPlayers().remove(player));
        var sbTeam = teamsScoreboard.getPlayerTeam(player);
        if (sbTeam != null) {
            sbTeam.removePlayer(player);
        }

        var newSbTeam = teamsScoreboard.getTeam(color.toString());
        newSbTeam.addPlayer(player);

        var newTeam = teams.get(color);
        newTeam.teamPlayers().add(player);
        newTeam.teamPlayers().forEach(player1 -> player.sendMessage(player1.name()));

        player.setGlowing(true);
    }

    public void setupNPCs() {
        this.shopNPC = new ShopNPC();
        shopNPC.removeAll();
        shopNPC.create(this);
    }

    public boolean isPlayerInTeam(Player player, NamedTextColor color) {
        return teams.get(color).teamPlayers().contains(player);
    }

    public boolean isPlayerInAnyTeam(Player player) {
        return teams.keySet().stream().anyMatch(color -> isPlayerInTeam(player, color));
    }

    public Optional<Team> getTeam(Player player) {
        return teams.values().stream().filter(color -> color.teamPlayers().contains(player)).findFirst();
    }

    public Optional<NamedTextColor> getTeamColor(Player player) {
        return teams.keySet().stream().filter(namedTextColor -> teams.get(namedTextColor).teamPlayers().contains(player)).findFirst();
    }

    public ShopNPC shopNPC() {
        return shopNPC;
    }

    public @NotNull Scoreboard teamsScoreBoard() {
        return teamsScoreboard;
    }
}