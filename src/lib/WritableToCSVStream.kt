package lib

import lib.CSV.CSVStreamWriter
import lib.functions.FunctionWithVoidReturnAndOneArgument

/**
 * If object is null writes null to stream, otherwise calls the function
 *
 * @param function, which will be called in case object is not null
 */
fun <T> writeNullableToStream(
    stream: CSVStreamWriter,
    value: T?,
    function: FunctionWithVoidReturnAndOneArgument<T>
) {
    if (value == null) {
        stream.append("null")
    } else {
        function.invoke(value)
    }
}

fun interface WritableToCSVStream {
    fun writeToStream(stream: CSVStreamWriter)
}
