package lib.net.udp

import database.NetworkCode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import lib.json.toJson
import network.client.udp.ResultFrame
import network.client.udp.User
import org.apache.logging.log4j.kotlin.Logging
import java.net.DatagramPacket

abstract class Server protected constructor(port: Int) : Logging, CommonNetwork(port) {
    private var running = false

    protected abstract suspend fun handlePacket(jsonHolder: JsonHolder)

    private suspend fun send(
        user: User,
        frame: ResultFrame,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ) {
        val json = jsonMapper.toJson(frame)
        val bytesToSend = json.toByteArray()
        val packet = DatagramPacket(bytesToSend, bytesToSend.size, user.address, user.port)

        send(packet, dispatcher)
    }

    protected suspend fun send(
        user: User,
        code: NetworkCode,
        str: String?,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ) {
        send(user, ResultFrame(code, str), dispatcher)
    }

    suspend fun run() = coroutineScope {
        running = true
        logger.trace("Server is running")

        while (running) {
            loopCycle();
        }

        close()
    }

    private suspend fun loopCycle() {
        val buffer = ByteArray(bufferSize)
        val packet = DatagramPacket(buffer, buffer.size)

        try {
            receive(packet)
            handlePacket(packet.constructJsonHolder(jsonMapper.objectMapper))
        } catch (e: Exception) {
            logger.error("Error while receiving packet: $e")
        }
    }
}

