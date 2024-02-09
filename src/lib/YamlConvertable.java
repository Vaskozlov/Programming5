package lib;

public interface YamlConvertable {
    /**
     *
     * @return result of building pretty string
     */
    default String toYaml() {
        return constructYaml(new PrettyStringBuilder()).toString();
    }

    PrettyStringBuilder constructYaml(PrettyStringBuilder builder);
}
