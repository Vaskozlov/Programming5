package lib;

import exceptions.BadIdentException;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Helps to build pretty string with different idents in thread safe way
 */
public class PrettyStringBuilder {
    private final StringBuffer stringBuilder = new StringBuffer();
    private final AtomicInteger ident = new AtomicInteger(0);
    private final AtomicInteger identSize;
    private boolean initialized = false;

    public PrettyStringBuilder() {
        this(2);
    }

    public PrettyStringBuilder(int identSize) {
        this.identSize = new AtomicInteger(identSize);
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }

    public void increaseIdent() {
        ident.incrementAndGet();
    }

    /**
     * decreases ident, if it is less than zero throws BadIdentException exception
     */
    public void decreaseIdent() {
        ident.decrementAndGet();

        if (ident.get() < 0) {
            throw new BadIdentException("Ident must not be below zero");
        }
    }

    public void appendLine(String line) {
        addNewLine();
        stringBuilder.append(" ".repeat(ident.get() * identSize.get())).append(line);
    }

    public void appendLine(String format, Object... objects) {
        addNewLine();
        stringBuilder.append(" ".repeat(ident.get() * identSize.get())).append(String.format(format, objects));
    }

    private void addNewLine() {
        if (initialized) {
            stringBuilder.append('\n');
        } else {
            initialized = true;
        }
    }
}
