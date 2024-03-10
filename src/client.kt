import application.*
import client.RemoteDatabase

object Client {
    @JvmStatic
    fun main(args: Array<String>) {
        val filename = System.getenv("DATABASE_PATH")
        val application = Application(RemoteDatabase("localhost", 8080))
        application.start(filename)
    }
}