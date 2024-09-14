package club.devcord.gamejam.logic;

import club.devcord.gamejam.CursedBedwarsPlugin;
import club.devcord.gamejam.logic.team.Team;
import club.devcord.gamejam.message.Messenger;
import club.devcord.gamejam.timer.Countdown;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class Game {
    private final CursedBedwarsPlugin plugin;
    private GameStage gameStage;
    private final GameMap gameMap;
    private final HashMap<NamedTextColor, Team> teams = new HashMap<>();
    private ScoreboardManager scoreboardManager;
    private Scoreboard teamsScoreboard;

    private final Team spectatorTeam = new Team();

    public Game(CursedBedwarsPlugin plugin) {
        this.gameStage = GameStage.LOBBY;
        this.plugin = plugin;

        teams.put(NamedTextColor.RED, new Team());
        teams.put(NamedTextColor.GREEN, new Team());
        teams.put(NamedTextColor.BLUE, new Team());
        teams.put(NamedTextColor.YELLOW, new Team());

        // Spectator Team
        teams.put(NamedTextColor.GRAY, new Team());

        this.scoreboardManager = plugin.getServer().getScoreboardManager();
        this.teamsScoreboard = scoreboardManager.getNewScoreboard();

        for (NamedTextColor teamColor : teams.keySet()) {
            teamsScoreboard.registerNewTeam(teamColor.toString());
            var team = teamsScoreboard.getTeam(teamColor.toString());
            if (team != null) {
                team.prefix(Component.text(teamColor.toString() + " | "));
                team.color(teamColor);
            }
        }

        this.gameMap = new GameMap();
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
        var teamOpt = getTeamColor(player);
        teamOpt.ifPresent(team -> team.teamPlayers().remove(player));

        teams.get(color).teamPlayers().add(player);

    }

    public boolean isPlayerInTeam(Player player, NamedTextColor color) {
        return teams.get(color).teamPlayers().contains(player);
    }

    public boolean isPlayerInAnyTeam(Player player) {
        return teams.keySet().stream().anyMatch(color -> isPlayerInTeam(player, color));
    }

    public Optional<Team> getTeamColor(Player player) {
        return teams.values().stream().filter(color -> color.teamPlayers().contains(player)).findFirst();
    }
}