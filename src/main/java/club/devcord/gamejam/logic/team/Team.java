package club.devcord.gamejam.logic.team;

import club.devcord.gamejam.utils.RelativeLocation;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class Team {
    private final TeamColor teamColor;
    private final RelativeLocation spawnLocation;
    private final Set<Player> teamPlayers;

    public Team(TeamColor teamColor, RelativeLocation spawnLocation) {
        this.teamColor = teamColor;
        this.spawnLocation = spawnLocation;
        this.teamPlayers = new HashSet<>();
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
}
