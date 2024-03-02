package lib.CSV;

import java.io.IOException;
import java.util.Arrays;

/**
 * Sequence of elements with an ability to read first element and append to the end
 */
public class CSVStreamLikeReader {
    private final String[][] data;
    private int lineIndex = 0;
    private int column = 0;

    private record ReadResult(String elem, int lineIndex, int column) {
    }

    public CSVStreamLikeReader(String data) {
        this.data = Arrays.stream(data.split("\n"))
                .map(line -> line.split(";"))
                .toArray(String[][]::new);
    }

    public boolean isEndOfLine() {
        return column >= data[lineIndex].length;
    }

    public boolean isEndOfStream() {
        return lineIndex + (isEndOfLine() ? 1 : 0) >= data.length;
    }

    public int getElementLeftInLine() {
        return data[lineIndex].length - column;
    }

    public String readElem() throws IOException {
        ReadResult result = readNextElement(lineIndex, column);

        lineIndex = result.lineIndex;
        column = result.column;

        return result.elem;
    }

    public String getNext() throws IOException {
        return readNextElement(lineIndex, column).elem;
    }

    private ReadResult readNextElement(int lineIndex, int column) throws IOException {
        if (data.length <= lineIndex) {
            throw new IOException("No more elements");
        }

        if (column >= data[lineIndex].length) {
            return readNextElement(++lineIndex, 0);
        }

        return new ReadResult(data[lineIndex][column], lineIndex, column + 1);
    }
}
