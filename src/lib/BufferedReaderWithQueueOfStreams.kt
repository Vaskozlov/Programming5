package lib

import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.io.Reader
import java.util.*

/**
 * BufferedReader with an ability to push new stream which will be used instead of a previous one,
 * when stream returns null reader will come back to the previous stream. If BufferReader runs out of streams
 * readLine/popStream will throw IOException.
 */
class BufferedReaderWithQueueOfStreams {
    private val readers = ArrayDeque<BufferedReader>()
    private var currentReader: BufferedReader

    constructor(filename: String) : this(FileReader(filename))

    constructor(input: Reader) {
        readers.addLast(BufferedReader(input))
        currentReader = readers.last
    }

    /**
     * Reads line from last stream, throws IOException when there are not any streams left.
     */
    fun readLine(): String {
        val result: String?

        try {
            result = currentReader.readLine()
        } catch (e: IOException) {
            popStream()
            return readLine()
        }

        if (result == null) {
            popStream()
            return readLine()
        }

        return result
    }

    fun pushStream(filename: String) {
        val file = FileReader(filename)
        pushStream(file)
    }

    fun pushStream(input: Reader) {
        readers.addLast(BufferedReader(input))
        currentReader = readers.last
    }

    /**
     * Removes last stream, if there are no streams left after popping IOException will be thrown.
     */
    fun popStream() {
        val removedReader = readers.removeLast()
        removedReader.close()

        if (readers.isEmpty()) {
            throw IOException("No available streams left.")
        }

        currentReader = readers.last
    }
}
