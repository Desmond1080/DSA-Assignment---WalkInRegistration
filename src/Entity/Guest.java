/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Desmond
 */
public class Guest {
    // define the format for the date 
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    private static int count = 0;
    private String confirmationNumber;
    private String name;
    private String contactNumber;
    private GuestType guestType; // walk-in or standard-registration
    private RoomType requestedRoomType; // 501-single, double, queen etc 
    private int numberOfGuests;
    private String status; // waiting, registered, assigned
    private LocalDateTime arrivalDateTime; 
    
    
    // enum class for guest type for correct input 
    public enum GuestType {
        WALK_IN,
        STANDARD_BOOKING
    }
    
    public enum RoomType{
        DELUXE,
        STANDARD,
        SUITE,
        FAMILY
    }
    
    // variable value constructor
    public Guest(String name, String contactNumber, GuestType guestType, RoomType requestedRoomType, int numberOfGuests){
        count++;
        this.confirmationNumber = String.format("%08d", count);
        this.name = name;
        this.contactNumber = contactNumber;
        this.guestType = guestType;
        this.requestedRoomType = requestedRoomType;
        this.numberOfGuests = numberOfGuests;
        this.status = "Waiting";
        this.arrivalDateTime = LocalDateTime.now();
    }
    
    // get method 
    public String getName(){
        return name;
    }
    
    public String getContactNumber(){
        return contactNumber;
    }
    
    public String getConfirmationNumber(){
        return confirmationNumber;
    }
    
    public GuestType getGuestType(){
        return guestType;
    }
    
    public RoomType getRequestedRoomType(){
        return requestedRoomType;
    }
    
    public String getStatus(){
        return status;
    }
    
    public int getNumberOfGuests(){
        return numberOfGuests;
    }
    
    public LocalDateTime getArrivalDateTime(){
        return arrivalDateTime;
    }
    
    
    // set method 
    public void setName(String name){
        this.name = name;
    }
    
    public void setContactNumber(String contactNumber){
        this.contactNumber = contactNumber;
    }
    
    public void setGuestType(GuestType guestType){
        this.guestType = guestType;
    }
    
    public void setRequestedRoomType(RoomType roomType){
        this.requestedRoomType = roomType;
    }
    
    public void setStatus(String status){
        this.status = status;
    }
    
    public void setNumberOfGuests(int numberOfGuests ){
        this.numberOfGuests = numberOfGuests;
    }
    
    public void setArrivalDateTime(LocalDateTime arrivalDateTime){
        this.arrivalDateTime = arrivalDateTime;
    }
    
    public String getGuestTypeDisplay(){
        if(guestType == GuestType.WALK_IN){
            return "Walk-In";
        } else {
            return "Standard";
        }
    }
    
    // to String method 
    @Override
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
}
