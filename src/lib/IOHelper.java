package lib;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class IOHelper {
    /**
     *
     * @return null if unable to read file, otherwise file content
     */
    public static String readFile(String filename) {
        try (FileInputStream file = new FileInputStream(filename); BufferedInputStream stream = new BufferedInputStream(file)) {
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            return null;
        }
    }
}
