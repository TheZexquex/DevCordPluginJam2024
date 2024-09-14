package club.devcord.gamejam.logic.team;

import club.devcord.gamejam.utils.RelativeLocation;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class Team {
    private final Set<Player> teamPlayers = new HashSet<>();
    private final RelativeLocation spawnLocation;

    public Team(RelativeLocation spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public Set<Player> teamPlayers() {
        return teamPlayers;
    }

    public RelativeLocation spawnLocation() {
        return spawnLocation;
    }
}
