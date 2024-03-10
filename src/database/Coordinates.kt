package database

import lib.CSV.CSVStreamWriter
import lib.PrettyStringBuilder
import lib.WritableToCSVStream
import lib.YamlConvertable

data class Coordinates(val x: Long?, val y: Long?) : YamlConvertable, WritableToCSVStream {
    override fun constructYaml(builder: PrettyStringBuilder): PrettyStringBuilder {
        builder.appendLine("Coordinates:")
        builder.increaseIdent()

        builder.appendLine("x: %d", x)
        builder.appendLine("y: %d", y)

        builder.decreaseIdent()
        return builder
    }

    override fun writeToStream(stream: CSVStreamWriter) {
        stream.append(x.toString())
        stream.append(y.toString())
    }
}