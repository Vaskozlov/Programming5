package lib.functions;

@FunctionalInterface
public interface FunctionWithVoidReturnAndOneArgument<A> {
    void invoke(A arg);
}