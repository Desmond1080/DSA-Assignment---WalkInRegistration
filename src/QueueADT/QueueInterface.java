/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package QueueADT;

/**
 *
 * @author Desmond
 */
public interface QueueInterface<T> {
    public void enqueue(T newEntry);
    public T dequeue();
    public T getFront();
    public boolean isEmpty();
    public boolean isFull();
    public int getNumberOfEntries();
}
