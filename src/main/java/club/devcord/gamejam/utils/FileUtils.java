package club.devcord.gamejam.utils;

import java.io.File;

public class FileUtils {
    public static void deleteDirectory(File file) {
        var contents = file.listFiles();

        if (contents != null) {
            for (File child : contents) {
                deleteDirectory(child);
            }
        }

        file.delete();
    }
}
