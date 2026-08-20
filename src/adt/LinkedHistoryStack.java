package adt;

/**
 *
 * @author shujuntan
 */

public class LinkedHistoryStack<T> implements HistoryStackInterface<T> {

    private final LinkedStack<T> previousEntries = new LinkedStack<>();
    private T currentEntry;

    @Override
    public boolean record(T newEntry) {
        if (newEntry == null) {
            throw new IllegalArgumentException("History entry cannot be null.");
        }

        if (currentEntry != null) {
            previousEntries.push(currentEntry);
        }

        currentEntry = newEntry;
        return true;
    }

    @Override
    public T undo() {
        if (!canUndo()) {
            throw new IllegalStateException("There is no entry to undo.");
        }

        currentEntry = previousEntries.pop();
        return currentEntry;
    }

    @Override
    public T getCurrent() {
        return currentEntry;
    }

    @Override
    public boolean canUndo() {
        return !previousEntries.isEmpty();
    }

    @Override
    public boolean isEmpty() {
        return currentEntry == null;
    }

    @Override
    public int getNumberOfEntries() {
        return previousEntries.size() + (currentEntry == null ? 0 : 1);
    }

    @Override
    public void clear() {
        previousEntries.clear();
        currentEntry = null;
    }

    private static class LinkedStack<E> {

        private Node<E> topNode; /* Latest Added Items */
        private int numberOfEntries;

        private void push(E entry) {
            topNode = new Node<>(entry, topNode); /* Create new node and point to old top */
            numberOfEntries++;
        }

        private E pop() {
            if (isEmpty()) {
                throw new IllegalStateException("Stack is empty.");
            }

            E entry = topNode.data;
            topNode = topNode.next;
            numberOfEntries--;
            return entry;
        }

        private boolean isEmpty() {
            return topNode == null;
        }

        private int size() {
            return numberOfEntries;
        }

        private void clear() {
            topNode = null;
            numberOfEntries = 0;
        }
    }

    private static class Node<E> {

        private final E data;
        private final Node<E> next;

        private Node(E data, Node<E> next) {
            this.data = data;
            this.next = next;
        }
    }
}