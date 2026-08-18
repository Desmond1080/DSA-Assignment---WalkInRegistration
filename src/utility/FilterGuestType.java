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
    public static Guest[] filterGuestType(Guest[] guests, GuestType selectedType){
        int n = guests.length;
        int count = 0;
        
        for(int i =0; i < n; i++){
            if(guests[i].getGuestType() == selectedType){
                count++;
            }
        }
        
         Guest[] selectedTypeArray = new Guest[count];
         int selectedCount = 0; // handle the position for the selected guest type position
         
         for(int i = 0; i < n; i++){
             if(guests[i].getGuestType() == selectedType){
                 selectedTypeArray[selectedCount] = guests[i];
                 selectedCount++;
             }
         }
        
        return selectedTypeArray;
    }
}
