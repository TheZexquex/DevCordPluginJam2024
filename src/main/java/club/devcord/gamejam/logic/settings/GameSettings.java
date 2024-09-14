package club.devcord.gamejam.logic.settings;

import club.devcord.gamejam.utils.RelativeLocation;

import java.time.Duration;
import java.util.Set;

public class GameSettings {
    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS_PER_TEAM = 3;
    public static final int LOBBY_COUNTDOWN_SECONDS = 10;

    public static final double HEALTH_REGENERATION_RATE = 0.2;

    public static final RelativeLocation SPAWN_LOCATION = RelativeLocation.of(0, 118, 0);

    public static final Set<RelativeLocation> SHOP_LOCATIONS = Set.of(
           RelativeLocation.of(-5.5, 66, -78.5),
           RelativeLocation.of(6.5, 66, 79.5),
           RelativeLocation.of(79.5, 66, -5.5),
           RelativeLocation.of(-78.5, 66, 6.5)
    );
  
    public static final Duration IRON_SPAWN_DELAY = Duration.ofMillis(500);
    public static final Duration GOLD_SPAWN_DELAY = Duration.ofSeconds(15);
    public static final Duration DIAMOND_SPAWN_DELAY = Duration.ofSeconds(30);

    public static final Set<RelativeLocation> IRON_SPAWN_LOCATIONS = Set.of(
            RelativeLocation.of(-82.5, 66, 0.5),
            RelativeLocation.of(83.5, 66, 0.5),
            RelativeLocation.of(0.5, 66, 83.5),
            RelativeLocation.of(0.5, 66, -82.5)
    );

    public static final Set<RelativeLocation> GOLD_SPAWN_LOCATIONS = Set.of(
            RelativeLocation.of(31.5, 68, 29.5),
            RelativeLocation.of(-30.5, 68, -28.5),
            RelativeLocation.of(29.5, 68, -30.5),
            RelativeLocation.of(-28.5, 68, 31.5)
    );

    public static final Set<RelativeLocation> DIAMOND_SPAWN_LOCATIONS = Set.of(
            RelativeLocation.of(8.5, 72.5, 12.5),
            RelativeLocation.of(-6.5, 72.5, -10.5)
    );
}
