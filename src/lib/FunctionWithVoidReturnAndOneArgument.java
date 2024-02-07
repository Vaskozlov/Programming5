package lib;

@FunctionalInterface
public interface FunctionWithVoidReturnAndOneArgument<A> {
    void invoke(A arg);
}