package lib;

import lib.functions.FunctionWithArgumentAndReturnType;

import java.io.IOException;

public class ConvertToStreamHelper {
    /**
     * @param function, which will be invoked in case of non-null values at the beginning of the stream
     * @return null if stream contains null, otherwise calls function with stream argument
     */
    public static <T> T convertNullableFromStream(
            CSVStreamLikeReader stream,
            FunctionWithArgumentAndReturnType<T, CSVStreamLikeReader> function
    ) throws IOException {
        if (stream.getNext().equals("null")) {
            stream.readElem();
            return null;
        } else {
            return function.invoke(stream);
        }
    }
}
