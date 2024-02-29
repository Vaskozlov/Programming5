package lib;

import lib.functions.FunctionWithVoidReturnAndOneArgument;

import java.util.Arrays;

/**
 * Stores limited number of elements, when user attempts to add more overrides the oldest-added element
 */
public class CircledStorage<T> {
    private final T[] buffer;
    private int currentIndex = 0;

    /**
     * @param size can not be less than 0
     */
    public CircledStorage(int size) throws IllegalArgumentException {
        if (size < 0) {
            throw new IllegalArgumentException("Size of CircleStorage can not contain less than 0 elements");
        }

        buffer = newArray(size);
    }

    public int size() {
        return buffer.length;
    }

    /**
     * Appends value to the CircleStorage, if storage is out of spaces overrides the oldest-added element
     */
    public void add(T value) {
        set(value, currentIndex++);
    }

    public void applyFunctionOnAllElements(FunctionWithVoidReturnAndOneArgument<T> function) {
        for (int i = buffer.length; i >= 1; --i) {
            T elem = get(currentIndex - i);

            if (elem != null) {
                function.invoke(elem);
            }
        }
    }

    private T get(int index) {
        return buffer[getIndexInCircle(index)];
    }

    private void set(T value, int index) {
        buffer[getIndexInCircle(index)] = value;
    }

    @SafeVarargs
    private T[] newArray(int size, T... array) {
        return Arrays.copyOf(array, size);
    }

    private int getIndexInCircle(int index) {
        int result = index % buffer.length;
        return result >= 0 ? result : result + buffer.length;
    }
}
