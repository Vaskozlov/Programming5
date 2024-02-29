package lib.functions;

@FunctionalInterface
public interface FunctionWithVoidReturnOneArgumentAndError<A, E extends Throwable> {
    void invoke(A arg) throws E;
}