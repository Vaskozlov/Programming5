package lib;

import java.io.IOException;

@FunctionalInterface
public interface FunctionWithArgumentAndReturnType<R, A> {
    R invoke(A arg) throws IOException;
}