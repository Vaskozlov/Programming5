package lib;

@FunctionalInterface
public interface FunctionWithArgumentAndReturnType<R, A> {
    R invoke(A arg);
}