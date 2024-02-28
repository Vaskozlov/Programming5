package lib;

import java.io.IOException;

public interface WritableToCSVStream {
    /**
     * If object is null writes null to stream, otherwise calls the function
     *
     * @param function, which will be called in case object is not null
     */
    static <T> void writeNullableToStream(
            CSVStreamWriter stream,
            T object,
            FunctionWithVoidReturnOneArgumentAndError<T, IOException> function
    ) throws IOException {
        if (object == null) {
            stream.append("null");
        } else {
            function.invoke(object);
        }
    }

    void writeToStream(CSVStreamWriter stream) throws IOException;
}
