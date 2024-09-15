package club.devcord.gamejam.logic;

import club.devcord.gamejam.BuggyBedwarsPlugin;
import club.devcord.gamejam.logic.scoreboard.SideBarScoreboard;
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
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.title.Title;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Game {
    private final BuggyBedwarsPlugin plugin;
    private GameStage gameStage;
    private GameMap gameMap;
    private final Set<Team> teams = new HashSet<>();
    private final Set<Player> spectators = new HashSet<>();
    private final BlockRegistry blockRegistry = new BlockRegistry();
    private final DamagerRegistry damagerRegistry = new DamagerRegistry();

    private SideBarScoreboard sideBarScoreboard;
    private ScoreboardManager scoreboardManager;
    private Scoreboard teamsScoreboard;
    private ShopNPC shopNPC;
    private Countdown lobbyCountdown = new Countdown();
    private Stopwatch actionBarInfoStopWatch;
    private Inventory enderchest;

    public Game(BuggyBedwarsPlugin plugin) {
        this.gameStage = GameStage.LOBBY;
        this.plugin = plugin;
    }

    public void startLobbyPhase() {
        teams.add(new Team(TeamColor.RED, RelativeLocation.of(79.5, 66, 0.5, 90, 0), RelativeLocation.of(66.4, 67.6, 0.5)));
        teams.add(new Team(TeamColor.GREEN, RelativeLocation.of(-78.5, 66, 0.5, -90, 0), RelativeLocation.of(-65.5, 67.6, 0.5)));
        teams.add(new Team(TeamColor.BLUE, RelativeLocation.of(0.5, 66, 79.5, 180, 0), RelativeLocation.of(0.5, 67.6, 66.5)));
        teams.add(new Team(TeamColor.YELLOW, RelativeLocation.of(0.5, 66, -78.5, 0, 0), RelativeLocation.of(0.5, 67.6, -65.5)));

        this.scoreboardManager = plugin.getServer().getScoreboardManager();
        this.teamsScoreboard = scoreboardManager.getNewScoreboard();

        for (var team : teams) {
            var teamColor = team.teamColor();
            var scoreboardTeam = teamsScoreboard.registerNewTeam(teamColor.toString());
            scoreboardTeam.prefix(MiniMessage.miniMessage().deserialize(team.getShortPrefix()));
            scoreboardTeam.color(teamColor.textColor());
            scoreboardTeam.setAllowFriendlyFire(false);
        }

        this.gameMap = new GameMap();
        this.sideBarScoreboard = new SideBarScoreboard(plugin);
        this.enderchest = Bukkit.createInventory(null, 3 * 9, Component.text("Ender Chest"));

        var world = gameMap.bukkitWorld();
        world.setTime(0);
        world.setWeatherDuration(world.getClearWeatherDuration());
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(GameRule.DO_ENTITY_DROPS, false);
        world.setGameRule(GameRule.DO_FIRE_TICK, false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);

        sendPreCountdownActionbar();
    }

    public void sendPreCountdownActionbar() {
        this.actionBarInfoStopWatch = new Stopwatch();

        actionBarInfoStopWatch.start(1, TimeUnit.SECONDS, duration -> {}, duration -> {
            var dots = ".".repeat((int) (duration.getSeconds() % 4));
            var message = MiniMessage.miniMessage().deserialize(
                    "<gray>Warten auf Spieler" + dots + " (<yellow><online><gray>/<yellow><required><gray>)",
                    Placeholder.parsed("online", String.valueOf(plugin.getServer().getOnlinePlayers().size())),
                    Placeholder.parsed("required", String.valueOf(GameSettings.MIN_PLAYERS)));

            plugin.getServer().getOnlinePlayers().forEach(player -> player.sendActionBar(message));
        }, () -> {});
    }

    public void startGameCountDown(int seconds) {
        actionBarInfoStopWatch.abort();

        lobbyCountdown.start(seconds, TimeUnit.SECONDS, (second) -> {
            if (second == 30 || second == 20 || second == 15 || (second <= 10 && second != 0)) {
                plugin.messenger().broadcast(Messenger.PREFIX + "<green>Das Spiel startet in <yellow>" + second + " <green>Sekunden");
                plugin.getServer().getOnlinePlayers().forEach(player -> {
                    player.playSound(Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.MASTER, 1.0F, 1.0F));
                });
            }
        }, () -> {
            Bukkit.getScheduler().runTask(plugin, this::startGame);

            plugin.getServer().getOnlinePlayers().forEach(player -> {
                player.playSound(Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.MASTER, 1.0F, 1.0F));
            });

            plugin.messenger().broadcast(Messenger.PREFIX + "<green>Das Spiel startet");
        });
    }

    public void startGame() {
        this.gameStage = GameStage.IN_GAME;

        distributeRemainingPlayers();

        teams.stream()
                .filter(team -> team.teamPlayers().isEmpty())
                .forEach(team -> team.setEmpty(true));

        gameMap.bukkitWorld().getEntities().stream()
                .filter(entity -> entity.getType() == EntityType.ITEM)
                .forEach(Entity::remove);

        teams.forEach(team -> {
            team.teamPlayers().forEach(player -> {
                player.teleport(team.spawnLocation().toBukkitLocation(Bukkit.getWorld("game")));
                player.setGlowing(false);
                sideBarScoreboard.show(player);
            });
        });
    }

    public void initShutdown(Team winningTeam) {
        if (winningTeam != null) {
            plugin.getServer().getOnlinePlayers().forEach(player -> {
                player.showTitle(Title.title(
                        MiniMessage.miniMessage().deserialize("<green>Das Spiel ist vorbei"),
                        MiniMessage.miniMessage().deserialize("<gray>Team " + winningTeam.getFormattedName() + " <gray>hat gewonnen")));
            });
        } else {
            plugin.getServer().getOnlinePlayers().forEach(player -> {
                player.showTitle(Title.title(
                        MiniMessage.miniMessage().deserialize("<green>Das Spiel ist vorbei"), Component.empty()
                ));
            });
        }

        var shutDownCountdown = new Countdown();
        shutDownCountdown.start(GameSettings.SHUTDOWN_COUNTDOWN_SECONDS, TimeUnit.SECONDS, (second) -> {
            if (second == 30 || second == 20 || second == 15 || (second <= 10 && second != 0)) {
                plugin.messenger().broadcast(Messenger.PREFIX + "<red>Der Server schließt in" + second + " <red>Sekunden");
                plugin.getServer().getOnlinePlayers().forEach(player -> {
                    player.playSound(Sound.sound(Key.key("block.note_block.bass"), Sound.Source.MASTER, 1.0F, 1.0F));
                });
            }
        }, () -> {
            Bukkit.getScheduler().runTask(plugin, this::startGame);

            plugin.getServer().getOnlinePlayers().forEach(player -> {
                player.playSound(Sound.sound(Key.key("block.note_block.bass"), Sound.Source.MASTER, 1.0F, 1.0F));
            });

            plugin.messenger().broadcast(Messenger.PREFIX + "<red>Der Server schließt JETZT!");
        });
    }

    private void distributeRemainingPlayers() {
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> !isPlayerInAnyTeam(player))
                .forEach(player -> {
                    var team = findSmallestTeam();
                    var teamColor = team.teamColor();

                    switchPlayerToTeam(player, team);
                    player.sendRichMessage(Messenger.PREFIX + "<gray>Du bist jetzt in Team " + team.getFormattedName());
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
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            shopNPC.create(this);
        }, 20);
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

    public Countdown lobbyCountdown() {
        return lobbyCountdown;
    }

    public Set<Team> teams() {
        return teams;
    }

    public Set<Player> spectators() {
        return spectators;
    }

    public BlockRegistry blockRegistry() {
        return blockRegistry;
    }

    public DamagerRegistry damagerRegistry() {
        return damagerRegistry;
    }

    public SideBarScoreboard sideBarScoreboard() {
        return sideBarScoreboard;
    }

    public Inventory enderchest() {
        return enderchest;
    }

    public void handleKill(Player player) {
        damagerRegistry.remove(player);

        getTeam(player).ifPresent(team -> {
            if (team.alive()) {
                player.teleport(team.spawnLocation().toBukkitLocation(gameMap.bukkitWorld()));
                player.playSound(Sound.sound(Key.key("entity.player.death"), Sound.Source.MASTER, 1.0F, 1.0F));
                player.setHealth(20);
            } else {
                clearTeam(player);
                spectators().add(player);
                player.setGameMode(GameMode.SPECTATOR);
                player.teleport(GameSettings.SPAWN_LOCATION.toBukkitLocation(gameMap.bukkitWorld()));
                player.playSound(Sound.sound(Key.key("entity.player.death"), Sound.Source.MASTER, 1.0F, 1.0F));
                player.sendRichMessage(Messenger.PREFIX + "<red><i>Du wurdest eliminiert!");

                if (team.teamPlayers().isEmpty()) {
                    plugin.messenger().broadcast(Messenger.PREFIX + "<i><dark_red>Das Team " + team.getFormattedName() + " <dark_red><i>wurde eliminiert!");
                    plugin.getServer().getOnlinePlayers().forEach(onlinePlayer -> {
                        onlinePlayer.playSound(Sound.sound(Key.key("entity.lightning_bolt.thunder"), Sound.Source.MASTER, 1.0F, 1.0F));
                    });
                }

                handlePotentialWin();
            }
        });
    }

    public void handlePotentialWin() {
        var teamsAlive = teams.stream().filter(Team::alive).toList();

        if (teamsAlive.size() == 1) {
            var winningTeam = teamsAlive.getFirst();

            initShutdown(winningTeam);
        }

        if (teamsAlive.isEmpty()) {
            initShutdown(null);
        }
    }
}
