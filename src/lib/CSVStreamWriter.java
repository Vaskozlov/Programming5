package lib;

import java.io.IOException;
import java.io.Writer;

/**
 * Sequence of elements with an ability to read first element and append to the end
 */
public class CSVStreamWriter {
    Writer writer;
    boolean newLineStarted = true;

    public CSVStreamWriter(Writer writer) {
        this.writer = writer;
    }

    public Writer getWriter()
    {
        return writer;
    }

    public void append(CharSequence sequence) throws IOException {
        if (!newLineStarted) {
            writer.write(';');
        }

        newLineStarted = false;
        writer.append(sequence);
    }

    public void append(Number number) throws IOException {
        append(String.valueOf(number));
    }

    public void newLine() throws IOException {
        newLineStarted = true;
        writer.write('\n');
    }

}
