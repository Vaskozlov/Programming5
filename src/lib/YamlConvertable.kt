package lib

interface YamlConvertable {
    fun toYaml(): String {
        return constructYaml(PrettyStringBuilder()).toString()
    }

    fun constructYaml(builder: PrettyStringBuilder): PrettyStringBuilder
}
