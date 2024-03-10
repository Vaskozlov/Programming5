package lib.functions

fun interface FunctionWithVoidReturnAndOneArgument<A> {
    fun invoke(arg: A)
}