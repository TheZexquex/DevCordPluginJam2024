package club.devcord.gamejam.logic.settings;

import club.devcord.gamejam.utils.RelativeLocation;
import com.github.juliarn.npclib.api.Position;

import java.time.Duration;
import java.util.Set;

public class GameSettings {
    public static int MIN_PLAYERS = 1;
    public static int MAX_PLAYERS_PER_TEAM = 3;

    public static RelativeLocation SPAWN_LOCATION = new RelativeLocation(0, 118, 0);

    public static final Set<Position> SHOP_LOCATIONS = Set.of(
            Position.position(-79.0, 66.0, 6.0, "world")
    );
  
    public static final Duration IRON_SPAWN_DELAY = Duration.ofMillis(500);
    public static final Duration GOLD_SPAWN_DELAY = Duration.ofSeconds(15);
    public static final Duration DIAMOND_SPAWN_DELAY = Duration.ofSeconds(30);

    public static final Set<RelativeLocation> IRON_SPAWN_LOCATIONS = Set.of(
            new RelativeLocation(-82, 65, 0),
            new RelativeLocation(82, 65, 0),
            new RelativeLocation(0, 65, 82),
            new RelativeLocation(0, 65, -82)
    );

    public static final Set<RelativeLocation> GOLD_SPAWN_LOCATIONS = Set.of(
            new RelativeLocation(31, 67, 29),
            new RelativeLocation(-31, 67, -29),
            new RelativeLocation(29, 67, -31),
            new RelativeLocation(-29, 67, 31)
    );

    public static final Set<RelativeLocation> DIAMOND_SPAWN_LOCATIONS = Set.of(
            new RelativeLocation(8, 71, 12),
            new RelativeLocation(-7, 71, -11)
    );
}
