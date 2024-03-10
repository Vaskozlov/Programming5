package database

import lib.CSV.CSVStreamWriter
import lib.PrettyStringBuilder
import lib.WritableToCSVStream
import lib.YamlConvertable

/**
 * @param z    can not be null
 * @param name nullable, length can not be greater than 933
 */
data class Location(val x: Double?, val y: Float?, val z: Long?, val name: String?) : YamlConvertable,
    WritableToCSVStream {

    init {
        requireNotNull(z) { "OrganizationDatabase.Location() z must not be null" }

        require(!(name != null && name.length > 933)) { "OrganizationDatabase.Location() name is too long! It can not contain more than than 933 symbols." }
    }

    override fun constructYaml(builder: PrettyStringBuilder): PrettyStringBuilder {
        builder.appendLine("OrganizationDatabase.Location:")
        builder.increaseIdent()

        builder.appendLine("x: %f", x)
        builder.appendLine("y: %f", y)
        builder.appendLine("z: %d", z)
        builder.appendLine("name: %s", name)

        builder.decreaseIdent()

        return builder
    }

    override fun writeToStream(stream: CSVStreamWriter) {
        stream.append(x)
        stream.append(y)
        stream.append(z)
        lib.writeNullableToStream(
            stream,
            name
        ) { sequence -> stream.append(sequence) }
    }
}
