package lib;

public interface PrettyPrintable {
    /**
     *
     * @return result of building pretty string
     */
    default String toPrettyString() {
        return buildPrettyString(new PrettyStringBuilder()).toString();
    }

    PrettyStringBuilder buildPrettyString(PrettyStringBuilder builder);
}
