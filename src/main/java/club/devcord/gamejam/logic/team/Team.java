package club.devcord.gamejam.logic.team;

import club.devcord.gamejam.event.TeamBedDestroyEvent;
import club.devcord.gamejam.event.TeamBedRebuildEvent;
import club.devcord.gamejam.utils.RelativeLocation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.HashSet;
import java.util.Set;

public class Team {
    private final TeamColor teamColor;
    private final RelativeLocation spawnLocation;
    private final Set<Player> teamPlayers;
    private boolean alive = true;
    private boolean empty = false;

    public Team(TeamColor teamColor, RelativeLocation spawnLocation) {
        this.teamColor = teamColor;
        this.spawnLocation = spawnLocation;
        this.teamPlayers = new HashSet<>();
    }

    public String getFormattedName() {
        return String.format("<%s>%s", teamColor.textColor().asHexString(), teamColor.displayName());
    }

    public String mmColor() {
        return String.format("<%s>", teamColor.textColor().asHexString());
    }

    public String getShortPrefix() {
        return String.format("<dark_gray>[%s%s<dark_gray>] ", mmColor(), String.valueOf(teamColor.displayName().charAt(0)).toUpperCase());
    }

    public TeamColor teamColor() {
        return teamColor;
    }

    public Set<Player> teamPlayers() {
        return teamPlayers;
    }

    public RelativeLocation spawnLocation() {
        return spawnLocation;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
        Bukkit.getPluginManager().callEvent(alive ? new TeamBedRebuildEvent(this) : new TeamBedDestroyEvent(this));
    }

    public boolean alive() {
        return alive;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public boolean empty() {
        return empty;
    }
}
