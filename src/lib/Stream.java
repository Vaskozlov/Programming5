package lib;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.List;

/**
 * Sequence of elements with an ability to read first element and append to the end
 *
 * @param <T>
 */
public class Stream<T> implements Iterable<T> {
    private ArrayDeque<T> storage = new ArrayDeque<>();

    public Stream() {
    }

    public Stream(List<T> initialData) {
        storage = new ArrayDeque<>(initialData);
    }

    @Override
    public Iterator<T> iterator() {
        return storage.iterator();
    }

    public T read() {
        return storage.removeFirst();
    }

    public void write(T object) {
        storage.addLast(object);
    }

    public T getFirst() {
        return storage.getFirst();
    }

    public int elementsLeft() {
        return storage.size();
    }
}
