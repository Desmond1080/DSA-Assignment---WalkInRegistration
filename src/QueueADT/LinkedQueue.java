/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QueueADT;

/**
 *
 * @author Desmond
 */
public class LinkedQueue<T> implements QueueInterface<T> {
    
    private Node firstNode; // front of the queue 
    private Node lastNode; // rear of the queue
    private int count;

    // constructor for default value
    public LinkedQueue(){
        firstNode = null;
        lastNode = null;
        count = 0;
    }
    
    @Override
    public void enqueue(T newEntry) {
        Node newNode = new Node(newEntry, null);
        
        if(isEmpty()){
            firstNode = newNode;
            count++;
        }else{
            lastNode.next = newNode;
            count++;
        }
        
        lastNode = newNode;
    }

    @Override
    public T dequeue() {
        T front = null;
        
        if (!isEmpty()){
            front = firstNode.data;
            firstNode = firstNode.next;
            count--;
            
            if (firstNode == null){
                lastNode = null;
            }
        }
        return front;
    }

    @Override
    public T getFront() {
        T front = null;
        
        if(!isEmpty()){
            front = firstNode.data;
        }
        return front;
    }

    @Override
    public boolean isEmpty() {
        return (firstNode == null ) && (lastNode == null); // rear and front are empty 
    }
    
    @Override
    public int getNumberOfEntries(){
        return count;
    }

    // linked queue has no limited capacity, so it never full
    @Override
    public boolean isFull() {
        return false;
    }
    
    // extract data from each node in string format
    @Override
    public String toString(){
        String output = "";
        Node currentNode = firstNode;
        
        while(currentNode != null){
            output += currentNode.data + "\n";
            currentNode = currentNode.next;
        }
        
        return output;
            
    }
    
    private class Node{
        private T data;
        private Node next;
        
        private Node(T data){
            this.data = data;
            this.next = null;
        }
        
        private Node(T data, Node next){
            this.data = data;
            this.next = next;
        }
    }  
}
