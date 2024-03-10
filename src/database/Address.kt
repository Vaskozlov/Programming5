package database

import lib.CSV.CSVStreamWriter
import lib.PrettyStringBuilder
import lib.WritableToCSVStream
import lib.YamlConvertable

/**
 * @param zipCode nullable, must contain at least 3 character
 * @param town    can not be null
 */
class Address(val zipCode: String?, val town: Location?) : YamlConvertable, WritableToCSVStream {
    fun validate() {
        require(!(zipCode != null && zipCode.length < 3)) { "Invalid zip code" }

        requireNotNull(town) { "OrganizationDatabase.Address town must not be null" }
    }

    override fun constructYaml(builder: PrettyStringBuilder): PrettyStringBuilder {
        builder.appendLine("OrganizationDatabase.Address:")
        builder.increaseIdent()

        builder.appendLine("zipCode: %s", zipCode)
        town!!.constructYaml(builder)

        builder.decreaseIdent()

        return builder
    }

    override fun writeToStream(stream: CSVStreamWriter) {
        lib.writeNullableToStream(
            stream,
            zipCode
        ) { sequence -> stream.append(sequence) }
        town!!.writeToStream(stream)
    }
}