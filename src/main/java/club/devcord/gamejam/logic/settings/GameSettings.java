package club.devcord.gamejam.logic.settings;

import club.devcord.gamejam.utils.RelativeLocation;

import java.time.Duration;
import java.util.Set;

public class GameSettings {
    public static int MIN_PLAYERS = 2;
    public static int MAX_PLAYERS_PER_TEAM = 3;

    public static RelativeLocation SPAWN_LOCATION = new RelativeLocation(0, 118, 0);

    public static final Set<RelativeLocation> SHOP_LOCATIONS = Set.of(
           new RelativeLocation(-5.5, 66, -78.5),
           new RelativeLocation(6.5, 66, 79.5),
           new RelativeLocation(79.5, 66, -5.5),
           new RelativeLocation(-78.5, 55.0, 6.5)
    );
  
    public static final Duration IRON_SPAWN_DELAY = Duration.ofMillis(500);
    public static final Duration GOLD_SPAWN_DELAY = Duration.ofSeconds(15);
    public static final Duration DIAMOND_SPAWN_DELAY = Duration.ofSeconds(30);

    public static final Set<RelativeLocation> IRON_SPAWN_LOCATIONS = Set.of(
            new RelativeLocation(-82.5, 66, 0.5),
            new RelativeLocation(83.5, 66, 0.5),
            new RelativeLocation(0.5, 66, 83.5),
            new RelativeLocation(0.5, 66, -82.5)
    );

    public static final Set<RelativeLocation> GOLD_SPAWN_LOCATIONS = Set.of(
            new RelativeLocation(31.5, 68, 29.5),
            new RelativeLocation(-30.5, 68, -28.5),
            new RelativeLocation(29.5, 68, -30.5),
            new RelativeLocation(-28.5, 68, 31.5)
    );

    public static final Set<RelativeLocation> DIAMOND_SPAWN_LOCATIONS = Set.of(
            new RelativeLocation(8.5, 72, 12.5),
            new RelativeLocation(-7.5, 72, -11.5)
    );
}
