package lib

import lib.CSV.CSVStreamLikeReader

object ConvertToStreamHelper {
    /**
     * @param function, which will be invoked in case of non-null values at the beginning of the stream
     * @return null if stream contains null, otherwise calls function with stream argument
     */
    fun <T> convertNullableFromStream(
        stream: CSVStreamLikeReader,
        function: (CSVStreamLikeReader) -> T
    ): T? {
        if (stream.next == "null") {
            stream.readElem()
            return null
        } else {
            return function.invoke(stream)
        }
    }
}
