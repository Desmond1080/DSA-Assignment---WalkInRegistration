/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

/**
 *
 * @author Desmond
 */

import adt.QueueInterface;
import adt.LinkedQueue;
import boundary.WalkInRegistrationUI;
import Entity.Guest;
import adt.ArrayList;
import adt.ListInterface;
import utility.sortGuestUtility;

public class WalkInRegistration {
    private QueueInterface<Guest> walkInGuest = new LinkedQueue<>();
    private WalkInRegistrationUI walkInUI = new WalkInRegistrationUI();
    private ListInterface<Guest> processedGuests = new ArrayList<>(); // store list of processed guest after changing the status to registered
    
    public void registration(){
        int choice;
        do {
            choice = walkInUI.getMenuChoice();
            switch(choice){
                case 0:
                    System.out.println("\nExiting system");
                    break;
                case 1:
                    registerGuest();
                    break;
                case 2:
                    processNextGuest();
                    break;
                case 3:
                    viewGuestQueue();
                    break;
                case 4:
                    getFrontGuest();
                    break;
                case 5:
                    generateWaitingQueueReport();
                    break;
                default:
                    System.out.println("\n Invalid Choice");
            }
        }while (choice != 0);
    }
    
    // handle walk in guest registration 
    public void registerGuest(){
        Guest newGuest = walkInUI.inputGuestDetails();
        walkInGuest.enqueue(newGuest);
        walkInUI.displayRegistrationSuccess(newGuest);
    }
    
    // handle 
    public void processNextGuest(){
        if(walkInGuest.isEmpty()){
            walkInUI.displayEmptyQueueMessage();
        } else {
            Guest servedGuest = walkInGuest.dequeue();
            servedGuest.setStatus("Registered");
            processedGuests.add(servedGuest); // add the served Guest to the array list 
            walkInUI.displayServedGuest(servedGuest);
        }
    }
    
    public void viewGuestQueue(){
        walkInUI.displayQueue(walkInGuest.toString(), walkInGuest.getNumberOfEntries());
    }
    
    public void getFrontGuest(){
        if(walkInGuest.isEmpty()){
            walkInUI.displayEmptyQueueMessage();
        } else {
            walkInUI.displayNextGuest(walkInGuest.getFront());
        }
    }
    
    public void generateWaitingQueueReport(){
        Guest[] items = getQueueItem();
        sortGuestUtility.sortByArrivalTime(items);
    }
    
    // use for generating report 
    public Guest[] getQueueItem(){
        int size = walkInGuest.getNumberOfEntries();
        Guest[] items = new Guest[size];
        
        // dequeue the items from the linked queue 
        for(int i = 0; i < size; i++){
            items[i] = walkInGuest.dequeue();
        }
        
        // enqueue back the items back to the linked queue after generate the report 
        for(int i = 0; i < size; i++){
            walkInGuest.enqueue(items[i]);
        }
        
        return items;
    }
    
    public static void main(String[] args){
        WalkInRegistration registration = new WalkInRegistration();
        registration.registration();
    }
}
