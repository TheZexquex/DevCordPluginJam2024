package club.devcord.gamejam.logic;

import club.devcord.gamejam.CursedBedwarsPlugin;
import club.devcord.gamejam.logic.settings.GameSettings;
import club.devcord.gamejam.logic.shop.ShopNPC;
import club.devcord.gamejam.logic.team.Team;
import club.devcord.gamejam.logic.team.TeamColor;
import club.devcord.gamejam.message.Messenger;
import club.devcord.gamejam.timer.Countdown;
import club.devcord.gamejam.timer.Stopwatch;
import club.devcord.gamejam.utils.RelativeLocation;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Game {
    private final CursedBedwarsPlugin plugin;
    private GameStage gameStage;
    private GameMap gameMap;
    private final Set<Team> teams = new HashSet<>();
    private final Set<Player> spectators = new HashSet<>();

    private ScoreboardManager scoreboardManager;
    private Scoreboard teamsScoreboard;
    private ShopNPC shopNPC;
    private Stopwatch actionBarInfoStopWatch;

    public Game(CursedBedwarsPlugin plugin) {
        this.gameStage = GameStage.LOBBY;
        this.plugin = plugin;
    }

    public void startLobbyPhase() {
        teams.add(new Team(TeamColor.RED, RelativeLocation.of(79.5, 66, 0.5, 90, 0)));
        teams.add(new Team(TeamColor.GREEN, RelativeLocation.of(-78.5, 66, 0.5, -90, 0)));
        teams.add(new Team(TeamColor.BLUE, RelativeLocation.of(0.5, 66, 79.5, 180, 0)));
        teams.add(new Team(TeamColor.YELLOW, RelativeLocation.of(0.5, 66, -78.5, 0, 0)));

        this.scoreboardManager = plugin.getServer().getScoreboardManager();
        this.teamsScoreboard = scoreboardManager.getNewScoreboard();

        for (var team : teams) {
            var teamColor = team.teamColor();
            var scoreboardTeam = teamsScoreboard.registerNewTeam(teamColor.toString());
            scoreboardTeam.prefix(Component.text("").color(NamedTextColor.GRAY)
                    .append(Component.text("[").color(NamedTextColor.GRAY))
                    .append(Component.text(String.valueOf(teamColor.toString().charAt(0)).toUpperCase())).color(teamColor.textColor())
                    .append(Component.text("] ").color(NamedTextColor.GRAY)));
            scoreboardTeam.color(teamColor.textColor());
            scoreboardTeam.setAllowFriendlyFire(false);
        }

        this.gameMap = new GameMap();

        GameSettings.SHOP_LOCATIONS.forEach(relativeLocation -> {
            var location = relativeLocation.toBukkitLocation(gameMap.bukkitWorld());

            location.getChunk().setForceLoaded(true);
        });

        gameMap.bukkitWorld().setDifficulty(Difficulty.PEACEFUL);
        gameMap.bukkitWorld().setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        gameMap.bukkitWorld().setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        gameMap.bukkitWorld().setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);

        this.actionBarInfoStopWatch = new Stopwatch();

        actionBarInfoStopWatch.start(1, TimeUnit.SECONDS, duration -> {}, duration -> {
            var dots = ".".repeat((int) (duration.getSeconds() % 4));
            var message = MiniMessage.miniMessage().deserialize(
                    "<gray>Warten" + dots + " (<yellow><online><gray>/<yellow><required><gray>)",
                    Placeholder.parsed("online", String.valueOf(plugin.getServer().getOnlinePlayers().size())),
                    Placeholder.parsed("required", String.valueOf(GameSettings.MIN_PLAYERS)));

            plugin.getServer().getOnlinePlayers().forEach(player -> player.sendActionBar(message));
        }, () -> {});
    }

    public void startGameCountDown() {
        actionBarInfoStopWatch.abort();
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

        distributeRemainingPlayers();

        gameMap.bukkitWorld().getEntities().stream()
                .filter(entity -> entity.getType() == EntityType.ITEM)
                .forEach(Entity::remove);

        teams.forEach(team -> {
            team.teamPlayers().forEach(player -> {
                player.teleport(team.spawnLocation().toBukkitLocation(Bukkit.getWorld("game")));
                player.setGlowing(false);
            });
        });
    }

    private void distributeRemainingPlayers() {
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> !isPlayerInAnyTeam(player))
                .forEach(player -> {
                    var team = findSmallestTeam();
                    var teamColor = team.teamColor();

                    switchPlayerToTeam(player, team);
                    player.sendRichMessage(Messenger.PREFIX + "<gray>Du bist jetzt in Team <" + teamColor.textColor() + ">"  + teamColor.displayName());
                });
    }

    private Team findSmallestTeam() {
        var shuffledTeams = new ArrayList<>(teams);
        Collections.shuffle(shuffledTeams);

        return shuffledTeams.stream()
                .min(Comparator.comparingInt(team -> team.teamPlayers().size()))
                .orElse(null); // Never null, but it looks prettier
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
        // TODO: Replace the line above with "plugin.serverApi().requestRestart();"
    }

    public GameStage gameStage() {
        return gameStage;
    }

    public GameMap gameMap() {
        return gameMap;
    }

    public void switchPlayerToTeam(Player player, Team newTeam) {
        clearTeam(player);

        var newSbTeam = teamsScoreboard.getTeam(newTeam.teamColor().toString());
        newSbTeam.addPlayer(player);

        newTeam.teamPlayers().add(player);

        if (gameStage != GameStage.IN_GAME) {
            player.setGlowing(true);
        }
    }

    public void clearTeam(Player player) {
        var teamOpt = getTeam(player);
        teamOpt.ifPresent(team -> team.teamPlayers().remove(player));
        var sbTeam = teamsScoreboard.getPlayerTeam(player);
        if (sbTeam != null) {
            sbTeam.removePlayer(player);
        }
    }

    public void setupNPCs() {
        this.shopNPC = new ShopNPC();
        shopNPC.removeAll();
        shopNPC.create(this);
    }

    public boolean isPlayerInAnyTeam(Player player) {
        return teams.stream().anyMatch(team -> team.teamPlayers().contains(player));
    }

    public Optional<Team> getTeam(Player player) {
        return teams.stream()
                .filter(team -> team.teamPlayers().contains(player))
                .findFirst();
    }

    public Team getTeamFromColor(TeamColor teamColor) {
        return teams.stream()
                .filter(team -> team.teamColor() == teamColor)
                .findAny()
                .orElse(null); // Never null, but it looks prettier
    }

    public ShopNPC shopNPC() {
        return shopNPC;
    }

    public @NotNull Scoreboard teamsScoreBoard() {
        return teamsScoreboard;
    }

    public Set<Player> spectators() {
        return spectators;
    }
}
