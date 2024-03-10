package lib.functions

fun interface FunctionWithArgumentAndReturnType<R, A> {
    fun invoke(arg: A): R
}