package club.devcord.gamejam.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StringUtils {
    public static String capitalizeEnumConstant(String string) {
        return Arrays.stream(string.toLowerCase().split("_"))
                .map(org.apache.commons.lang3.StringUtils::capitalize)
                .collect(Collectors.joining(" "));
    }
}
