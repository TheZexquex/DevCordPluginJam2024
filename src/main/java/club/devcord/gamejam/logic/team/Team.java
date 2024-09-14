package club.devcord.gamejam.logic.team;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class Team {
    private final Set<Player> teamPlayers = new HashSet<>();

    public Set<Player> teamPlayers() {
        return teamPlayers;
    }
}
