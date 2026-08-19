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
import utility.ValidationUtility;

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
        int choice = ValidationUtility.validateIntegerChoice();
        return choice;
    }
    
    public int getReportChoice(){
        System.out.println("\n==== Report =====");
        System.out.println("1. Generate Current Waiting Queue Report ");
        System.out.println("2. Generate Processed Guest Report ");
        
        System.out.print("Enter choice: ");
        int choice = ValidationUtility.validateIntegerChoice();
        return choice;
    }
    
    // handle filter guest type 
    public GuestType inputReportFilterChoice(){
        System.out.println("\n===== Filter Guest Type Report By =====");
        System.out.println("1. All Guests");
        System.out.println("2. Walk-In Only");
        System.out.println("3. Standard Booking Only");
        System.out.print("Enter choice: ");
        int choice = ValidationUtility.validateIntegerChoice();
        
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
        return ValidationUtility.validateIntegerChoice();
    }
    
    // input confirmation number for processed guest 
    public String inputConfirmationNumber(){
        String confirmationNumber;
        do {
            System.out.print("Enter 8-digit confirmation number (e.g., 00000001): ");
            confirmationNumber = scanner.nextLine().trim();

            if(!confirmationNumber.matches("[0-9]{8}")){
                System.out.println("Invalid format. Confirmation number must be exactly 8 digits.");
            }
        } while(!confirmationNumber.matches("[0-9]{8}"));

        return confirmationNumber;
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
        String name = inputGuestName();
        
        String contactNumber = inputContactNumber();
        
        GuestType guestType = inputGuestType();
        
        String roomType = validateEmptyField("Enter Guest Room Type: ");
        
        int numberOfGuests = inputNumberOfGuest();
        
        System.out.println();
         
       return new Guest(name, contactNumber, guestType, roomType, numberOfGuests); 
        
    }
    
    public String inputGuestName(){
        String name;
        do{
            System.out.print("Enter Guest Name: ");
            name = scanner.nextLine().trim();
            if(name.isEmpty()){
                System.out.println("Name cannot be empty.");
            }else if (name.length() > 50){
                System.out.println("Name is too long. Please limit to 50 characters.");
            }
        }while(name.isEmpty() || name.length()>50);
        
        return name;
    }
    
    public int inputNumberOfGuest(){
        int n;
        do{
            System.out.print("Enter Number Of Guests: ");
            n = ValidationUtility.validateIntegerChoice();
            if(n <= 0){
                System.out.println("Number of guests must be at least 1.");
            }
            
        }while(n <= 0);
        return n;
    }
    
    public String inputContactNumber(){
        String contactNumber;
        do{
            System.out.print("Enter Guest Contact Number: ");
            contactNumber = scanner.nextLine().trim();
            
            if(!contactNumber.matches("[0-9]{9,11}")){
                System.out.println("Invalid Contact Number. Please enter 9-11 digits only.");
            }
        }while(!contactNumber.matches("[0-9]{9,11}"));
        
        return contactNumber;
    }
    
    public GuestType inputGuestType(){
        int choice;
        do {
            System.out.println("\n===== Select Guest Type =====");
            System.out.println("1. Walk-In");
            System.out.println("2. Standard Booking");
            System.out.print("Enter choice : ");
            choice = ValidationUtility.validateIntegerChoice();

            if(choice != 1 && choice != 2){
                System.out.println("Invalid choice. Please select 1 or 2.");
            }
        } while(choice != 1 && choice != 2);

        if(choice == 2){
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
    
    public void displayQueue(Guest[] items){
        System.out.println("\n===========================================================================================================================");
        System.out.println("                            CURRENT GUEST WAITING QUEUE ");
        System.out.println("=============================================================================================================================");
        System.out.println("Current Queue (" + items.length + " guests(s) waiting):"); 
        System.out.println("---------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-4s %-12s %-12s %-18s %-18s %-12s %-16s %-12s %-10s%n","No.", "Confirm No.", "Name", "Contact Number","Guest Type", "Room Type", "Arrival Time","Number of Guests", "Status");
        System.out.println("---------------------------------------------------------------------------------------------------------------------------");
        
        for(int i = 0; i < items.length; i++){
            Guest g = items[i];
            System.out.printf("%-4s %-12s %-18s %-18s %-12s %-16s %-10s%n",
                    (i + 1),
                    g.getConfirmationNumber(),
                    truncate(g.getName(), 18),
                    g.getContactNumber(),
                    g.getGuestTypeDisplay(),
                    g.getRequestedRoomType(),
                    g.getArrivalDateTime().format(DATE_TIME_FORMAT),
                    g.getNumberOfGuests(),
                    g.getStatus());
        }
    }
    
    public void displayNextGuest(Guest nextGuest){
        System.out.println("Next guest in line: ");
        System.out.println(nextGuest);
    }
    
    public void displayWaitingQueueReport(Guest[] guests){
        System.out.println("\n=============================================================================================");
        System.out.println("                         WALK-IN GUEST WAITING QUEUE REPORT");
        System.out.println("=============================================================================================");
        System.out.println("Sorted by: Arrival Time (earliest first)");
        System.out.println("Total Guests: " + guests.length);
        System.out.println("-------------------------------------------------------------------------------------------");
        System.out.printf("%-4s %-12s %-18s %-18s %-12s %-16s %-10s%n","No.", "Confirm No.", "Name", "Guest Type", "Room Type", "Arrival Time", "Status");
        System.out.println("-------------------------------------------------------------------------------------------");
    
        for(int i = 0; i < guests.length; i++){
            Guest g = guests[i];
            System.out.printf("%-4s %-12s %-18s %-18s %-12s %-16s %-10s%n",
                    (i + 1),
                    g.getConfirmationNumber(),
                    truncate(g.getName(), 18),
                    g.getGuestTypeDisplay(),
                    g.getRequestedRoomType(),
                    g.getArrivalDateTime().format(DATE_TIME_FORMAT),
                    g.getStatus());
        }

        System.out.println("=============================================================================================");
    }
    
    
    public void displayProcessedGuestReport(Guest[] guests){
        System.out.println("\n=============================================================================================");
        System.out.println("                            PROCESSED GUEST REPORT");
        System.out.println("=============================================================================================");
        System.out.println("Sorted by: Name (A-Z)");
        System.out.println("Total Guests: " + guests.length);
        System.out.println("---------------------------------------------------------------------------------------------");
        System.out.printf("%-4s %-12s %-18s %-15s %-12s %-16s %-10s%n",
                "No.", "Confirm No.", "Name", "Guest Type", "Room Type", "Arrival Time", "Status");
        System.out.println("---------------------------------------------------------------------------------------------");

        for(int i = 0; i < guests.length; i++){
            Guest g = guests[i];
            System.out.printf("%-4s %-12s %-18s %-15s %-12s %-16s %-10s%n",
                    (i + 1),
                    g.getConfirmationNumber(),
                    truncate(g.getName(), 18),
                    g.getGuestTypeDisplay(),
                    g.getRequestedRoomType(),
                    g.getArrivalDateTime().format(DATE_TIME_FORMAT),
                    g.getStatus());
        }

        System.out.println("=============================================================================================");
    }
    
    public void pauseScreen(){
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    // handle maximum character of the report, if over the limit show (....)
    public String truncate(String text, int maxLength){
        if(text.length() > maxLength){
            return text.substring(0, maxLength -3) + "...";
        }
        
        return text;
    }
    
    // validate the text field cannot be empty 
    public String validateEmptyField(String text){
        String input;
        do{
            System.out.print(text);
            input = scanner.nextLine().trim();
            if(input.isEmpty()){
                System.out.println("Input field cannot be empty. Please fill in value!!");
            }
        }while(input.isEmpty());
        
        return input;
    }
}
