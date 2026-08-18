/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utility;

import Entity.Guest;

/**
 *
 * @author Desmond
 */
public class sortGuestUtility {
    
    // sort by arrival time 
    public static void sortByArrivalTime(Guest[] guests){
        int n = guests.length;
        
        for (int i = 0; i < (n-1); i++){
            for (int j = 0; j < (n-1-i); j++){
                if((guests[j].getArrivalDateTime()).isAfter(guests[j+1].getArrivalDateTime())){
                    Guest temp = guests[j]; // store temp value for original value 
                    guests[j] = guests[j+1];
                    guests[j+1] = temp;
                }
            }
        }
    }
    
    // sort by guest name (ascending order)
    public static void sortByGuestName(Guest[] guests){
        int n = guests.length;
        
        for (int i = 0; i < (n-1); i++){
            for (int j = 0; j < (n-1-i); j++){
                // guest [i] comes after guest[j+1] which means, guest[j] have higher alphabetical order
                if (guests[j].getName().compareTo(guests[j+1].getName()) > 0){
                    Guest temp = guests[j]; // store temp value for original value 
                    guests[j] = guests[j+1];
                    guests[j+1] = temp;
                }
            }
        }
    }
}
