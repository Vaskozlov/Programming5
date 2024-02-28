package lib;

import exceptions.NoBundleLoadedException;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Stores bundle with localized strings
 */
public class Localization {
    private static ResourceBundle bundle;

    /**
     *
     * @param key for string
     * @return gets string from bundle using key
     */
    public static String get(String key) {
        if (bundle == null) {
            throw new NoBundleLoadedException("No bundle loaded");
        }

        return bundle.getString(key);
    }

    public static void loadBundle(String filename, String locale) {
        bundle = ResourceBundle.getBundle(filename, new Locale(locale));
    }
}
