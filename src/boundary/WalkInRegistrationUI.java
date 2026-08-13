/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package boundary;

/**
 *
 * @author Desmond
 */

import control.WalkInRegistration;
import Entity.Guest;

import java.util.Scanner;

public class WalkInRegistrationUI {
    
    Scanner scanner = new Scanner(System.in);
    
    
    public int getMenuChoice(){
        System.out.println("Main Menu\n");
        System.out.println("1. Register Walk-In Guest ");
        System.out.println("2. Process Next Guest ");
        System.out.println("3. View All Guest ");
        System.out.println("4. Get Top Guest ");
        System.out.println("0. Exit.... ");
        
        System.out.println("Enter choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        System.out.println();
        return choice;
    }
    
    public void displayEmptyQueueMessage(){
        System.out.println("The queue of guest is empty. ");
    }
    
    public String toString(){
        return "Confirmation No: " + confirmationNumber + "\n" +
            "Name: " + name + "\n" +
            "Contact: " + contactNumber + "\n" +
            "Guest Type: " + guestType + "\n" +
            "Room Type: " + requestedRoomType + "\n" +
            "Arrival: " + arrivalDateTime.format(DATE_TIME_FORMAT) + "\n" +
            "Number of Guests: " + numberOfGuests + "\n" +
            "Status: " + status;
    }
    public String inputGuestDetails(){
        System.out.println("1. Enter Guest Name: ");
        String name = scanner.nextLine();
        
        System.out.println("2. Enter Guest Contact Number: ");
        String contactNumber = scanner.nextLine();
        
        System.out.println("3. Enter Guest Room Type: ");
        String roomType = scanner.nextLine();
        
        System.out.println("4. Enter ");
    }
}
