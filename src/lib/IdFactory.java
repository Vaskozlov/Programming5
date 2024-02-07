package lib;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Creates unique ids in thread safe way
 */
public class IdFactory {
    private AtomicInteger currentId;

    public IdFactory() {
        this(0);
    }

    public IdFactory(int value) {
        this.currentId = new AtomicInteger(value);
    }

    public void setValue(int value) {
        currentId = new AtomicInteger(value);
    }

    public int generateId() {
        return currentId.getAndIncrement();
    }
}
