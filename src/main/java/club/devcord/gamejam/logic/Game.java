package club.devcord.gamejam.logic;

import club.devcord.gamejam.CursedBedwarsPlugin;
import club.devcord.gamejam.Messenger;
import club.devcord.gamejam.logic.team.Team;
import club.devcord.gamejam.timer.Countdown;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
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
    private final HashMap<Team.Color, Team> teams = new HashMap<>();
    private ScoreboardManager scoreboardManager;
    private Scoreboard teamsScoreboard;

    private final Team spectatorTeam = new Team();

    private boolean isShuttingDown;

    public Game(CursedBedwarsPlugin plugin) {
        this.gameStage = GameStage.LOBBY;
        this.plugin = plugin;

        teams.put(Team.Color.RED, new Team());
        teams.put(Team.Color.GREEN, new Team());
        teams.put(Team.Color.BLUE, new Team());
        teams.put(Team.Color.YELLOW, new Team());
        teams.put(Team.Color.SPECTATOR, new Team());

        this.scoreboardManager = plugin.getServer().getScoreboardManager();
        this.scoreboardManager = scoreboardManager.getNewScoreboard()

        this.gameMap = new GameMap();
    }

    public void startGameCountDown() {
        var countdown = new Countdown();

        countdown.start(30, TimeUnit.SECONDS, (second) -> {
            plugin.messenger().broadCast(Messenger.PREFIX + "<green>Das Spiel startet in <yellow>" + second + " <green>Sekunden");
            plugin.getServer().getOnlinePlayers().forEach(player -> {
                //player.playSound(Sound.sound());
            });
        }, () ->{
            startGame();
            plugin.messenger().broadCast(Messenger.PREFIX + "<green>Das Spiel startet");
        });
    }

    public void startGame() {
        this.gameStage = GameStage.IN_GAME;
    }

    public void tearDown() {
        if (isShuttingDown) return;
        isShuttingDown = true;

        World world = gameMap.getBukkitWorld();
        Bukkit.unloadWorld(world, false);

        try {
            FileUtils.deleteDirectory(world.getWorldFolder());
        } catch (IOException ignored) {
            // Not much we can do
        }

        plugin.serverApi().requestRestart();
    }

    public GameStage gameStage() {
        return gameStage;
    }

    public GameMap gameMap() {
        return gameMap;
    }

    public void switchPlayerToTeam(Player player, Team.Color color) {
        var teamOpt = getTeamColor(player);
        teamOpt.ifPresent(team -> team.teamPlayers().remove(player));

        teams.get(color).teamPlayers().add(player);

    }

    public boolean isPlayerInTeam(Player player, Team.Color color) {
        return teams.get(color).teamPlayers().contains(player);
    }

    public boolean isPlayerInAnyTeam(Player player) {
        return teams.keySet().stream().anyMatch(color -> isPlayerInTeam(player, color));
    }

    public Optional<Team> getTeamColor(Player player) {
        return teams.values().stream().filter(color -> color.teamPlayers().contains(player)).findFirst();
    }
}