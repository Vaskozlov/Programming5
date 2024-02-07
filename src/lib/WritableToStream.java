package lib;

public interface WritableToStream {
    /**
     * If object is null writes null to stream, otherwise calls the function
     *
     * @param function, which will be called in case object is not null
     */
    static <T> void writeNullableToStream(StringStream stream, T object, FunctionWithVoidReturnAndOneArgument<T> function) {
        if (object == null) {
            stream.write("null");
        } else {
            function.invoke(object);
        }
    }

    void writeToStream(StringStream stream);
}
