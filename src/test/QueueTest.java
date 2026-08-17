/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

/**
 *
 * @author Desmond
 */

import Entity.Guest;
import Entity.Guest.GuestType;
import adt.QueueInterface;
import adt.LinkedQueue;

public class QueueTest {
     public static void main(String[] args) {
        
        QueueInterface<Guest> walkInQueue = new LinkedQueue<>();
        
        // 1. Test isEmpty on a fresh queue
        System.out.println("Is queue empty? " + walkInQueue.isEmpty());  // expect: true
        
        // 2. Create and enqueue a few guests
        Guest g1 = new Guest("Ali", "0123456789", GuestType.WALK_IN, "Single", 1);
        Guest g2 = new Guest("Siti", "0198765432", GuestType.STANDARD_BOOKING, "Double", 2);
        Guest g3 = new Guest("Ravi", "0134567890", GuestType.WALK_IN, "Suite", 3);
        
        walkInQueue.enqueue(g1);
        walkInQueue.enqueue(g2);
        walkInQueue.enqueue(g3);
        
        System.out.println("\nNumber of entries: " + walkInQueue.getNumberOfEntries()); // expect: 3
        System.out.println("Is queue empty now? " + walkInQueue.isEmpty()); // expect: false
        
        // 3. Test peek — should show Ali (first in), queue should NOT shrink
        System.out.println("\nPeek (front of queue): \n" + walkInQueue.getFront());
        System.out.println("Entries after peek (should still be 3): " + walkInQueue.getNumberOfEntries());
        
        // 4. Test dequeue — should remove Ali (FIFO order)
        Guest served = walkInQueue.dequeue();
        System.out.println("\nDequeued guest: \n" + served);
        System.out.println("Entries after dequeue (should be 2): " + walkInQueue.getNumberOfEntries());
        
        // 5. Peek again — should now show Siti (next in line)
        System.out.println("\nNew front of queue: \n" + walkInQueue.getFront());
        
        // 6. Dequeue remaining guests to confirm full FIFO order: Siti, then Ravi
        System.out.println("\nDequeued: \n" + walkInQueue.dequeue());
        System.out.println("\nDequeued: \n" + walkInQueue.dequeue());
        
        // 7. Queue should be empty now
        System.out.println("\nIs queue empty? " + walkInQueue.isEmpty()); // expect: true
        
        // 8. Test dequeue on empty queue — should return null, not crash
        Guest emptyResult = walkInQueue.dequeue();
        System.out.println("Dequeue on empty queue returns: " + emptyResult); // expect: null
    }
}
