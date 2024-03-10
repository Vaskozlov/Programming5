import application.*

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val filename = System.getenv("DATABASE_PATH")
        val application = Application()

        application.start(filename)
    }
}