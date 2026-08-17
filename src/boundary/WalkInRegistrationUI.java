/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package boundary;

/**
 *
 * @author Desmond
 */

import Entity.Guest;
import Entity.Guest.GuestType;

import java.util.Scanner;

public class WalkInRegistrationUI {
    
    Scanner scanner = new Scanner(System.in);
    
    
    public int getMenuChoice(){
        System.out.println("\n===== Main Menu =====");
        System.out.println("1. Register Walk-In Guest ");
        System.out.println("2. Process Next Guest ");
        System.out.println("3. View All Guest ");
        System.out.println("4. Get Top Guest ");
        System.out.println("0. Exit.... ");
        
        System.out.print("Enter choice: ");
        int choice = validateIntegerChoice();
        return choice;
    }
    
    public void displayEmptyQueueMessage(){
        System.out.println("The queue of guest is empty. No guests waiting....");
    }
    
    public Guest inputGuestDetails(){
        System.out.print("Enter Guest Name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter Guest Contact Number: ");
        String contactNumber = scanner.nextLine();
        
        GuestType guestType = inputGuestType();
        
        System.out.print("Enter Guest Room Type: ");
        String roomType = scanner.nextLine();
        
        System.out.print("Enter Number of Guests: ");
        int numberOfGuests = validateIntegerChoice();
        
        System.out.println();
        
        return new Guest(name, contactNumber, guestType, roomType, numberOfGuests); 
        
    }
    
    public GuestType inputGuestType(){
        System.out.println("\n===== Select Guest Type =====");
        System.out.println("1. Walk-In");
        System.out.println("2. Standard Booking");
        System.out.print("Enter choice : ");
        int choice = validateIntegerChoice();
        
        if (choice == 2){
            return GuestType.STANDARD_BOOKING;
        }
        
        return GuestType.WALK_IN;
    }
    
    public void displayRegistrationSuccess(Guest newGuest){
        System.out.println("New Guest registered successfully!!");
        System.out.println(newGuest);
    }
    
    public void displayServedGuest(Guest servedGuest){
        System.out.println("Now serving... ");
        System.out.println(servedGuest);
    }
    
    public void displayQueue(String queueDetails, int numberOfGuests){
        System.out.println("Current Queue (" + numberOfGuests + " guests(s) waiting):"); 
        System.out.println(queueDetails);
    }
    
    public void displayNextGuest(Guest nextGuest){
        System.out.println("Next guest in line: ");
        System.out.println(nextGuest);
    }
    
    public int validateIntegerChoice(){
        while(!scanner.hasNextInt()){
             System.out.println("Invalid integer input. please enter a number: ");
             scanner.next();
       }
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }
}
