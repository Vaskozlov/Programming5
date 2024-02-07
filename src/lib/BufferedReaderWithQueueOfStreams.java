package lib;

import java.io.*;
import java.util.ArrayDeque;

/**
 * BufferedReader with an ability to push new stream which will be used instead of a previous one,
 * when stream returns null reader will come back to the previous stream
 */
public class BufferedReaderWithQueueOfStreams {
    private final ArrayDeque<BufferedReader> readers = new ArrayDeque<>();
    private BufferedReader currentReader;

    public BufferedReaderWithQueueOfStreams(String filename) throws FileNotFoundException {
        pushStream(filename);
    }

    public BufferedReaderWithQueueOfStreams(Reader in) {
        pushStream(in);
    }

    public String readLine() throws IOException {
        String result = currentReader.readLine();

        if (result == null) {
            popStream();
            return readLine();
        }

        return result;
    }

    public void pushStream(String filename) throws FileNotFoundException {
        FileReader file = new FileReader(filename);
        pushStream(file);
    }

    public void pushStream(Reader in) {
        readers.addLast(new BufferedReader(in));
        currentReader = readers.getLast();
    }

    public void popStream() throws IOException {
        readers.removeLast();

        if (readers.isEmpty()) {
            throw new IOException("No available streams left.");
        }

        currentReader = readers.getLast();
    }
}
