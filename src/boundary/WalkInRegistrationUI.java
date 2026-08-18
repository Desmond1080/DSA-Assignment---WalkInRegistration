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
import java.time.format.DateTimeFormatter;

import java.util.Scanner;

public class WalkInRegistrationUI {
    
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    Scanner scanner = new Scanner(System.in);
    
    
    public int getMenuChoice(){
        System.out.println("\n===== Main Menu =====");
        System.out.println("1. Register Walk-In Guest ");
        System.out.println("2. Process Next Guest ");
        System.out.println("3. View All Guest ");
        System.out.println("4. Get Top Guest ");
        System.out.println("5. Report");
        System.out.println("0. Exit.... ");
        
        System.out.print("Enter choice: ");
        int choice = validateIntegerChoice();
        return choice;
    }
    
    public int getReportChoice(){
        System.out.println("\n==== Report =====");
        System.out.println("1. Generate Current Waiting Queue Report ");
        System.out.println("2. Generate Processed Guest Report ");
        
        System.out.print("Enter choice: ");
        int choice = validateIntegerChoice();
        return choice;
    }
    
    // handle filter guest type 
    public GuestType inputReportFilterChoice(){
        System.out.println("\n===== Filter Guest Type Report By =====");
        System.out.println("1. All Guests");
        System.out.println("2. Walk-In Only");
        System.out.println("3. Standard Booking Only");
        System.out.print("Enter choice: ");
        int choice = validateIntegerChoice();
        
        if(choice ==2) return GuestType.WALK_IN;
        if(choice ==3) return GuestType.STANDARD_BOOKING;
        
        return null;
    }
    
    // handle processed report option 
    public int inputProcessedReportChoice(){
        System.out.println("\n===== Processed Guest Report =====");
        System.out.println("1. Search Guest by Confirmation Number");
        System.out.println("2. View All Processed Guests (sorted by name)");
        System.out.print("Enter choice: ");
        return validateIntegerChoice();
    }
    
    // input confirmation number for processed guest 
    public String inputConfirmationNumber(){
        System.out.print("Enter 8 digit confirmation number (e.g., 00000001): ");
        return scanner.nextLine();
    }
    
    // display found guest based on the confirmation number 
    public void displayGuestFound(Guest guest){
        System.out.println("\n===== Guest Found =====");
        System.out.println(guest);
    }
    
    // handle no guest with this confirmation number 
    public void displayGuestNotFound(String confirmationNumber){
        System.out.println("\nNo guest found with confirmation number: " + confirmationNumber);
    }
    
    public void displayEmptyQueueMessage(){
        System.out.println("The queue of guest is empty. No guests waiting....");
    }
    
    public void displayEmptyProcessedGuestMessage(){
        System.out.println("No guests have been processed yet.");
    }
    
    public void displayInvalidChoiceMessage(){
        System.out.println("Please select the available choice option.");
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
    
    public void displayWaitingQueueReport(Guest[] guests){
        System.out.println("\n=========================================================================================");
        System.out.println("                         WALK-IN GUEST WAITING QUEUE REPORT");
        System.out.println("============================================================================================");
        System.out.println("Sorted by: Arrival Time (earliest first)");
        System.out.println("Total Guests: " + guests.length);
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.printf("%-4s %-12s %-18s %-15s %-12s %-16s %-10s%n","No.", "Confirm No.", "Name", "Guest Type", "Room Type", "Arrival Time", "Status");
        System.out.println("-----------------------------------------------------------------------------------");
    
        for(int i = 0; i < guests.length; i++){
            Guest g = guests[i];
            System.out.printf("%-4d %-12s %-18s %-15s %-12s %-16s %-10s%n",
                    (i + 1),
                    g.getConfirmationNumber(),
                    g.getName(),
                    g.getGuestType(),
                    g.getRequestedRoomType(),
                    g.getArrivalDateTime().format(DATE_TIME_FORMAT),
                    g.getStatus());
        }

        System.out.println("===========================================================================================");
    }
    
    
    public void displayProcessedGuestReport(Guest[] guests){
        System.out.println("\n===================================================================================");
        System.out.println("                            PROCESSED GUEST REPORT");
        System.out.println("===================================================================================");
        System.out.println("Sorted by: Name (A-Z)");
        System.out.println("Total Guests: " + guests.length);
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.printf("%-4s %-12s %-18s %-15s %-12s %-16s %-10s%n",
                "No.", "Confirm No.", "Name", "Guest Type", "Room Type", "Arrival Time", "Status");
        System.out.println("-----------------------------------------------------------------------------------");

        for(int i = 0; i < guests.length; i++){
            Guest g = guests[i];
            System.out.printf("%-4d %-12s %-18s %-15s %-12s %-16s %-10s%n",
                    (i + 1),
                    g.getConfirmationNumber(),
                    g.getName(),
                    g.getGuestType(),
                    g.getRequestedRoomType(),
                    g.getArrivalDateTime().format(DATE_TIME_FORMAT),
                    g.getStatus());
        }

        System.out.println("===================================================================================");
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
    
    public void pauseScreen(){
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
}
