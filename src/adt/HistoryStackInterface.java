package adt;

/**
 *
 * @author shujuntan
 */

/**
 * Generic Linear ADT for recording room-status history.
 *
 * @param <T> type of entry stored
 */

public interface HistoryStackInterface<T> {

    boolean record(T newEntry); /* Store new state */

    T undo(); /* Return to previous state */

    T getCurrent(); /* Get current entry */

    boolean canUndo();  /* Check whether there is previous state or not */

    boolean isEmpty();  

    int getNumberOfEntries();

    void clear();
}