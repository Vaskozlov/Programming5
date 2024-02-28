package lib;

@FunctionalInterface
public interface FunctionWithArgumentReturnTypeAndError<R, A, E extends Throwable> {
    R invoke(A arg) throws E;
}