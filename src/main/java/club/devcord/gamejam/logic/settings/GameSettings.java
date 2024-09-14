package club.devcord.gamejam.logic.settings;

import com.github.juliarn.npclib.api.Position;
import org.bukkit.Location;

import java.util.Set;

public class GameSettings {

    public static int MIN_PLAYERS = 1;
    public static int MAX_PLAYERS_PER_TEAM = 3;

    public static final Set<Position> SHOP_LOCATIONS = Set.of(
            Position.position(-79.0, 66.0, 6.0, "world")
    );
}
