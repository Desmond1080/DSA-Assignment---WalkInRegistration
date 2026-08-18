/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utility;

import Entity.Guest;
import Entity.Guest.GuestType;

/**
 *
 * @author Desmond
 */
public class FilterGuestType {
    public static Guest[] filterGuestType(Guest[] guests, GuestType guestType){
        int n = guests.length;
        int count = 0;
        
        for(int i =0; i < n; i++){
            if(guests[i].getGuestType() == guestType){
                return null;
            }
        }
    }
}
