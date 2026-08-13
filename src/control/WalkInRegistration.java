/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

/**
 *
 * @author Desmond
 */

import QueueADT.QueueInterface;
import QueueADT.LinkedQueue;
import boundary.WalkInRegistrationUI;
import Entity.Guest;

public class WalkInRegistration {
    private QueueInterface<Guest> walkInGuest = new LinkedQueue<>();
    private WalkInRegistrationUI walkInUI = new WalkInRegistrationUI();
    
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
            walkInUI.displayServedGuest(servedGuest);
        }
    }
    
    public void viewGuestQueue(){
        walkInUI.displayQueue(walkInGuest.toString(), walkInGuest.getNumberOfEntries());
    }
    
    public void getFrontGuest(){
        if(walkInGuest.isEmpty()){
            walk+InUI.displayEmptyQueueMessage();
        } else {
            walkInUI.displayNextGuest(walkInGuest.getFront());
        }
    }
    
    public static void main(String[] args){
        WalkInRegistration registration = new WalkInRegistration();
        registration.registration();
    }
}
