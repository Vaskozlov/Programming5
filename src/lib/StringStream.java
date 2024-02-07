package lib;

import java.util.List;

/**
 * Stream of Strings with an ability to writeAny argument, which is convertable to String
 */
public class StringStream extends Stream<String> {
    public StringStream() {
        super();
    }

    public StringStream(List<String> initialData) {
        super(initialData);
    }

    public <U> void writeAny(U argument) {
        if (argument instanceof String) {
            write((String) argument);
        } else {
            write(String.valueOf(argument));
        }
    }
}
