package exceptions

class KeyboardInterruptException : Exception {
    constructor() : super()

    constructor(description: String) : super(description)

    constructor(description: String, cause: Throwable) : super(description, cause)

    constructor(cause: Throwable) : super(cause)
}