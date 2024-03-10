package lib.net.udp

import com.fasterxml.jackson.databind.ObjectMapper
import database.NetworkCode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import network.client.udp.ResultFrame
import network.client.udp.User
import org.apache.logging.log4j.kotlin.Logging
import java.net.DatagramPacket
import java.net.DatagramSocket

abstract class Server protected constructor(private val port: Int) : Logging {
    private val socket = DatagramSocket(this.port)
    private var running = false

    protected var objectMapper: ObjectMapper = ObjectMapper()

    suspend fun send(user: User, frame: ResultFrame, dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        val json = objectMapper.writeValueAsString(frame)
        val bytesToSend = json.toByteArray()
        val packet = DatagramPacket(bytesToSend, bytesToSend.size, user.address, user.port)

        withContext(dispatcher) {
            println("main runBlocking: I'm working in thread ${Thread.currentThread().threadId()}")
            socket.send(packet)
        }
    }

    suspend fun send(user: User, code: NetworkCode, str: String?, dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        send(user, ResultFrame(code, str), dispatcher)
    }

    protected abstract suspend fun handlePacket(packet: DatagramPacket)

    suspend fun run() = coroutineScope {
        running = true
        logger.trace("Server is running")

        while (running) {
            val buffer = ByteArray(bufferSize)

            val packet = DatagramPacket(buffer, buffer.size)

            try {
                socket.receive(packet)
                handlePacket(packet)
            } catch (e: Exception) {
                logger.error("Error while receiving packet: $e")
            }
        }

        socket.close()
    }

    fun stop() {
        running = false
    }

    companion object {
        private const val bufferSize = 2048

        fun getUserFromPacket(packet: DatagramPacket): User {
            return User(packet.address, packet.port)
        }
    }
}
