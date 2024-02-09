package lib;

public interface YamlConvertable {
    default String toYaml() {
        return constructYaml(new PrettyStringBuilder()).toString();
    }

    PrettyStringBuilder constructYaml(PrettyStringBuilder builder);
}
