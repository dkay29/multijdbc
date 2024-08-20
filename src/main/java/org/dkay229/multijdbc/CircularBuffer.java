package org.dkay229.multijdbc;

import java.util.Collection;
import java.util.List;

public class CircularBuffer<T> {
    private T[] buffer;
    private int size;
    private int writeIndex;
    private int readIndex;
    private boolean isFull;

    @SuppressWarnings("unchecked")
    public CircularBuffer(int size) {
        this.size = size;
        this.buffer = (T[]) new Object[size];  // Generic array creation
        this.writeIndex = 0;
        this.readIndex = 0;
        this.isFull = false;
    }

    public void add(T item) {
        buffer[writeIndex] = item;
        writeIndex = (writeIndex + 1) % size;

        if (isFull) {
            readIndex = (readIndex + 1) % size;  // Overwrite oldest
        }

        isFull = writeIndex == readIndex;
    }

    public T get() {
        if (isEmpty()) {
            return null;  // Buffer is empty
        }

        T item = buffer[readIndex];
        readIndex = (readIndex + 1) % size;
        isFull = false;

        return item;
    }

    public boolean isEmpty() {
        return (!isFull && (writeIndex == readIndex));
    }

    public boolean isFull() {
        return isFull;
    }

    public int size() {
        if (isFull) {
            return size;
        } else if (writeIndex >= readIndex) {
            return writeIndex - readIndex;
        } else {
            return size + writeIndex - readIndex;
        }
    }
    public int freeSpace() {
        return size - size();  // Free space is the total size minus the current number of elements
    }
    public void addAll(List<T> resultRowsResponse) {
    }
}


