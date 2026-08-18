/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utility;

import Entity.Guest;
import adt.ListInterface;

/**
 *
 * @author Desmond
 */
public class SearchGuestUtility {
    public static Guest searchByConfirmationNumber(ListInterface<Guest> guests, String confirmationNumber){
        for(int i = 1; i <= guests.getNumberOfEntries(); i++){
            Guest currentGuest = guests.getEntry(i);
            if(confirmationNumber.equals(currentGuest.getConfirmationNumber())){
                return currentGuest;
            }
        }
        return null;
    }
}
