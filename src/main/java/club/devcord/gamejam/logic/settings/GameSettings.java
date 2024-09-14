package club.devcord.gamejam.logic.settings;

import club.devcord.gamejam.utils.RelativeLocation;

import java.time.Duration;
import java.util.Set;

public class GameSettings {
    public static int MIN_PLAYERS = 1;
    public static int MAX_PLAYERS_PER_TEAM = 3;

    public static RelativeLocation SPAWN_LOCATION = RelativeLocation.of(0, 118, 0);

    public static final Set<RelativeLocation> SHOP_LOCATIONS = Set.of(
           RelativeLocation.of(-6, 66, -80),
           RelativeLocation.of(6, 66, 80),
           RelativeLocation.of(80, 66, -6),
           RelativeLocation.of(-80, 66, 6)
    );
  
    public static final Duration IRON_SPAWN_DELAY = Duration.ofMillis(500);
    public static final Duration GOLD_SPAWN_DELAY = Duration.ofSeconds(15);
    public static final Duration DIAMOND_SPAWN_DELAY = Duration.ofSeconds(30);

    public static final Set<RelativeLocation> IRON_SPAWN_LOCATIONS = Set.of(
            RelativeLocation.of(-82, 65, 0),
            RelativeLocation.of(82, 65, 0),
            RelativeLocation.of(0, 65, 82),
            RelativeLocation.of(0, 65, -82)
    );

    public static final Set<RelativeLocation> GOLD_SPAWN_LOCATIONS = Set.of(
            RelativeLocation.of(31, 67, 29),
            RelativeLocation.of(-31, 67, -29),
            RelativeLocation.of(29, 67, -31),
            RelativeLocation.of(-29, 67, 31)
    );

    public static final Set<RelativeLocation> DIAMOND_SPAWN_LOCATIONS = Set.of(
            RelativeLocation.of(8, 71, 12),
            RelativeLocation.of(-7, 71, -11)
    );
}
