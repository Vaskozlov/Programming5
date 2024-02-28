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

    /**
     * sets base value for factory
     */
    public void setValue(int value) {
        currentId = new AtomicInteger(value);
    }

    /**
     * @return unique id
     */
    public int generateId() {
        return currentId.getAndIncrement();
    }
}
