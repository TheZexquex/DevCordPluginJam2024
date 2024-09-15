package club.devcord.gamejam.utils;

import java.time.Duration;

public class TimeUtils {
    public static long durationToTicks(Duration duration) {
        return (long) ((duration.toMillis() / 1000.0) * 20L);
    }
}
