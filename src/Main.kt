import application.*

fun main() {
    val filename = System.getenv("DATABASE_PATH")
    val application = Application()
    application.start(filename)
}