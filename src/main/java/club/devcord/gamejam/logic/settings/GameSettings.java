package club.devcord.gamejam.logic.settings;

import club.devcord.gamejam.utils.RelativeLocation;

import java.time.Duration;
import java.util.Set;

public class GameSettings {
    public static int MIN_PLAYERS = 2;
    public static int MAX_PLAYERS_PER_TEAM = 3;

    public static RelativeLocation SPAWN_LOCATION = RelativeLocation.of(0, 118, 0);

    public static final Set<RelativeLocation> SHOP_LOCATIONS = Set.of(
           RelativeLocation.of(-5, 66, -78),
           RelativeLocation.of(6, 66, 79),
           RelativeLocation.of(79, 66, -5),
           RelativeLocation.of(-78, 55.0, 6)
    );
  
    public static final Duration IRON_SPAWN_DELAY = Duration.ofMillis(500);
    public static final Duration GOLD_SPAWN_DELAY = Duration.ofSeconds(15);
    public static final Duration DIAMOND_SPAWN_DELAY = Duration.ofSeconds(30);

    public static final Set<RelativeLocation> IRON_SPAWN_LOCATIONS = Set.of(
            RelativeLocation.of(-82, 66, 0),
            RelativeLocation.of(83, 66, 0),
            RelativeLocation.of(0, 66, 83),
            RelativeLocation.of(0, 66, -82)
    );

    public static final Set<RelativeLocation> GOLD_SPAWN_LOCATIONS = Set.of(
            RelativeLocation.of(31, 68, 29),
            RelativeLocation.of(-30, 68, -28),
            RelativeLocation.of(29, 68, -30),
            RelativeLocation.of(-28, 68, 31)
    );

    public static final Set<RelativeLocation> DIAMOND_SPAWN_LOCATIONS = Set.of(
            RelativeLocation.of(8, 72, 12),
            RelativeLocation.of(-7, 72, -11)
    );
}
