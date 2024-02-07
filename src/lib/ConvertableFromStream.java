package lib;

public interface ConvertableFromStream {
    /**
     * @param function, which will be invoked in case of non-null values at the beginning of the stream
     * @return null if stream contains null, otherwise calls function with stream argument
     */
    static <T> T convertNullableFromStream(StringStream stream, FunctionWithArgumentAndReturnType<T, StringStream> function) {
        if (stream.getFirst().equals("null")) {
            stream.read();
            return null;
        } else {
            return function.invoke(stream);
        }
    }
}
