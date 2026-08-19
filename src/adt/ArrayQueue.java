package adt;

import adt.QueueInterface;

public class ArrayQueue<T> implements QueueInterface<T> {
    private T[] array;
    private int frontIndex;
    private int backIndex;
    private int numberOfEntries;
    private static final int DEFAULT_CAPACITY = 50;

    public ArrayQueue() {
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public ArrayQueue(int initialCapacity) {
        array = (T[]) new Object[initialCapacity];
        frontIndex = 0;
        backIndex = -1;
        numberOfEntries = 0;
    }

    @Override
    public void enqueue(T newEntry) {
        if (!isFull()) {
            backIndex = (backIndex + 1) % array.length;
            array[backIndex] = newEntry;
            numberOfEntries++;
        }
    }

    @Override
    public T dequeue() {
        T front = null;
        if (!isEmpty()) {
            front = array[frontIndex];
            array[frontIndex] = null;
            frontIndex = (frontIndex + 1) % array.length;
            numberOfEntries--;
        }
        return front;
    }

    @Override
    public T getFront() {
        T front = null;
        if (!isEmpty()) {
            front = array[frontIndex];
        }
        return front;
    }

    @Override
    public boolean isEmpty() {
        return numberOfEntries == 0;
    }

    @Override
    public int getNumberOfEntries() {
        return numberOfEntries;
    }

    @Override
    public boolean isFull() {
        return numberOfEntries == array.length;
    }
}