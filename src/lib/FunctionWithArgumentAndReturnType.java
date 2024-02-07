package lib;

public interface FunctionWithArgumentAndReturnType<R, A> {
    R invoke(A arg);
}