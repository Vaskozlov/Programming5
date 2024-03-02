package lib;

import exceptions.NoBundleLoadedException;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Stores bundle with localized strings
 */
public class Localization {
    private static ResourceBundle bundle;

    /**
     * @param key for string
     * @return gets string from bundle using key
     */
    public static String get(String key) {
        if (bundle == null) {
            throw new NoBundleLoadedException("No bundle loaded");
        }

        return bundle.getString(key);
    }

    public static void askUserForALanguage(
            BufferedReaderWithQueueOfStreams bufferedReaderWithQueueOfStreams) throws IOException {
        System.out.println("""
                Choose language:
                0 en (default)
                1 ru""");

        String line = bufferedReaderWithQueueOfStreams.readLine();

        switch (line) {
            case "", "0", "en" -> Localization.loadBundle("localization/localization", "en");
            case "1", "ru" -> Localization.loadBundle("localization/localization", "ru");
            default -> {
                System.out.println("Invalid input. Try again.");
                askUserForALanguage(bufferedReaderWithQueueOfStreams);
            }
        }
    }

    public static void loadBundle(String filename, String locale) {
        bundle = ResourceBundle.getBundle(filename, new Locale(locale));
    }
}
