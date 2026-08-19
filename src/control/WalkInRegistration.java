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
import Entity.Guest.GuestType;
import adt.ArrayList;
import adt.ListInterface;
import java.time.LocalDateTime;
import utility.FilterGuestType;
import utility.ScreenUtility;
import utility.SearchGuestUtility;
import utility.SortGuestUtility;

/* handle walk-in guests and standard booking guest (book fews day ago or repeat booking) without VIP priority
handle arrival and request for booking reservation
*/

public class WalkInRegistration {
    private QueueInterface<Guest> walkInGuest = new LinkedQueue<>();
    private WalkInRegistrationUI walkInUI = new WalkInRegistrationUI();
    private ListInterface<Guest> processedGuests = new ArrayList<>(); // store list of processed guest after changing the status to registered
    
    public WalkInRegistration(){
        loadDummyData();
    }
    
    private void loadDummyData(){
        Guest g1 = new Guest("Ali Bin Ahmad", "0123456789", GuestType.WALK_IN, "Single", 1);
        g1.setArrivalDateTime(LocalDateTime.now().minusMinutes(30));  

        Guest g2 = new Guest("Siti Nurhaliza", "0198765432", GuestType.STANDARD_BOOKING, "Double", 2);
        g2.setArrivalDateTime(LocalDateTime.now().minusMinutes(15)); 
        
        walkInGuest.enqueue(g1);
        walkInGuest.enqueue(g2);
        walkInGuest.enqueue(new Guest("Ravi Kumar", "0134567890", GuestType.WALK_IN, "Suite", 3));
        walkInGuest.enqueue(new Guest("Tan Wei Ming", "0145678901", GuestType.STANDARD_BOOKING, "Deluxe", 2));
        
        // pre-populate some "already processed" guests too, so Report 2 has data immediately
        Guest processed1 = new Guest("Lim Mei Ling", "0156789012", GuestType.WALK_IN, "Single", 1);
        processed1.setStatus("Registered");
        processedGuests.add(processed1);
        
        Guest processed2 = new Guest("Muthu Raj", "0167890123", GuestType.STANDARD_BOOKING, "Double", 4);
        processed2.setStatus("Registered");
        processedGuests.add(processed2);
    }
    
    public void registration(){
        int choice;
                
        do {
            ScreenUtility.clearScreen();
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
                    cancelWalkInRegistration();
                    break;
                case 6:
                    handleReportMenu();
                    break;
                default:
                    System.out.println("\n Invalid Choice");
            }
        }while (choice != 0);
    }
    
    public void handleReportMenu(){
        int choice = walkInUI.getReportChoice();
        
        switch(choice){
            case 1:
                GuestType filterGuestType = walkInUI.inputReportFilterChoice();
                generateWaitingQueueReport(filterGuestType);
                break;
            case 2:
                generateProcessedGuestReport();
                break;
            default:
                walkInUI.displayInvalidChoiceMessage();
        }
    }
    
    public void cancelWalkInRegistration(){
        if(walkInGuest.isEmpty()){
            walkInUI.displayEmptyQueueMessage();
            walkInUI.pauseScreen();
            return;
        }
        
        boolean retry = true;
        
        while(retry){
            String confirmationNumber = walkInUI.inputConfirmationNumber();
            Guest removed = removeGuestFromQueue(confirmationNumber);

            if(removed != null){
                walkInUI.displayCancelGuestSuccess(removed);
                retry = false;
            } else {
                walkInUI.displayGuestNotFound(confirmationNumber);
                retry = walkInUI.promptRetry();
            }
        }
        
        walkInUI.pauseScreen();
    }
    
    // handle removing from queue 
    public Guest removeGuestFromQueue(String confirmationNumber){
        int size = walkInGuest.getNumberOfEntries();
        Guest[] items = new Guest[size];
        Guest removed = null;
        
        // store guest data from queue to array 
        for(int i = 0; i < size; i++){
            items[i] = walkInGuest.dequeue();
        }
        
        for(int i = 0; i < size; i++){
            if(items[i].getConfirmationNumber().equals(confirmationNumber)){
                removed = items[i];
            }else {
                walkInGuest.enqueue(items[i]);
            }
        }
        
        return removed;
        
    }
    
    // handle walk in guest registration 
    public void registerGuest(){
        Guest newGuest = walkInUI.inputGuestDetails();
        walkInGuest.enqueue(newGuest);
        walkInUI.displayRegistrationSuccess(newGuest);
        walkInUI.pauseScreen();
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
        walkInUI.pauseScreen();
    }
    
    public void viewGuestQueue(){
        Guest[] items = getQueueItem();
        walkInUI.displayQueue(items);
        walkInUI.pauseScreen();
    }
    
    public void getFrontGuest(){
        if(walkInGuest.isEmpty()){
            walkInUI.displayEmptyQueueMessage();
        } else {
            walkInUI.displayNextGuest(walkInGuest.getFront());
        }
        walkInUI.pauseScreen();
    }
    
    public void generateWaitingQueueReport(GuestType filterType){
        Guest[] items = getQueueItem();
        if (items.length == 0){
            walkInUI.displayEmptyQueueMessage();
            return;
        }
        
        Guest[] reportData;
        
        if(filterType == null){
            reportData = items;
        }else {
            reportData = FilterGuestType.filterGuestType(items, filterType);
        }
        
        SortGuestUtility.sortByArrivalTime(reportData);
        
        walkInUI.displayWaitingQueueReport(reportData);
        
        walkInUI.pauseScreen();
    }
    
    public void generateProcessedGuestReport(){
        if(processedGuests.isEmpty()){
            walkInUI.displayEmptyProcessedGuestMessage();
            walkInUI.pauseScreen();
            return;
        }
        
        int choice = walkInUI.inputProcessedReportChoice();
        
        switch (choice) {
            case 1:
                searchProcessedGuest();
                break;
            case 2:
                Guest[] items = getProcessedGuestArray();
                SortGuestUtility.sortByGuestName(items);
                walkInUI.displayProcessedGuestReport(items);
                walkInUI.pauseScreen();
                break;
            default:
                walkInUI.displayInvalidChoiceMessage();
                walkInUI.pauseScreen();
                break;
        }
    }
    
    public void searchProcessedGuest(){
        boolean retry = true;
        
        while(retry){
            String confirmationNumber = walkInUI.inputConfirmationNumber();
            Guest result = SearchGuestUtility.searchByConfirmationNumber(processedGuests, confirmationNumber);

            if(result == null){
                walkInUI.displayGuestNotFound(confirmationNumber);
                retry = walkInUI.promptRetry();
            } else {
                walkInUI.displayGuestFound(result);
                retry = false;
            }
        }
        walkInUI.pauseScreen();
    }
    
    // use for generating report 
    public Guest[] getQueueItem(){
        int size = walkInGuest.getNumberOfEntries();
        Guest[] items = new Guest[size];
        
        // dequeue the items from the linked queue 
        for(int i = 0; i < size; i++){
            items[i] = walkInGuest.dequeue();
        }
        
        // enqueue back the items back to the linked queue (restore back original state)
        for(int i = 0; i < size; i++){
            walkInGuest.enqueue(items[i]);
        }
        
        return items;
    }
    
    // handle processed guest array list to array 
    public Guest[] getProcessedGuestArray(){
        int size = processedGuests.getNumberOfEntries();
        Guest[] items = new Guest[size];

        for(int i = 1; i <= size; i++){
            items[i - 1] = processedGuests.getEntry(i);
        }

        return items;
    }
}
