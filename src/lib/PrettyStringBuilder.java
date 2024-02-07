package lib;

import exceptions.BadIdentException;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Helps to build pretty string with different idents in thread safe way
 */
public class PrettyStringBuilder {
    private final StringBuffer stringBuilder = new StringBuffer();
    private final AtomicInteger ident = new AtomicInteger(0);
    private final AtomicInteger identSize = new AtomicInteger(4);
    private boolean initialized = false;

    @Override
    public String toString() {
        return stringBuilder.toString();
    }

    public void increaseIdent() {
        ident.incrementAndGet();
    }

    public void decreaseIdent() {
        ident.decrementAndGet();

        if (ident.get() < 0) {
            throw new BadIdentException("Ident must not be below zero");
        }
    }

    public void changeIdentSize(int newIdentSize) {
        identSize.set(newIdentSize);
    }

    public void appendPrettyObject(PrettyPrintable prettyPrintable) {
        prettyPrintable.buildPrettyString(this);
    }

    public void appendLine(String line) {
        addNewLine();
        stringBuilder.append(" ".repeat(ident.get() * identSize.get())).append(line);
    }

    public void appendLine(String line, Object... objects) {
        addNewLine();
        stringBuilder.append(" ".repeat(ident.get() * identSize.get())).append(String.format(line, objects));
    }

    private void addNewLine() {
        if (initialized) {
            stringBuilder.append('\n');
        } else {
            initialized = true;
        }
    }
}
