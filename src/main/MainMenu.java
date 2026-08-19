/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

/**
 *
 * @author Desmond
 */

import control.WalkInRegistration;
import boundary.WalkInRegistrationUI;
import control.WalkInRegistration;
import utility.ValidationUtility;


public class MainMenu {
    
    public void handleModulesMenu(){
        System.out.println("\n======================================================");
        System.out.println("            Welcome To TARUMT Resort System ");
        System.out.println("=======================================================");
        
        int choice;
        do{
            choice = handleModulesChoice();
            switch(choice){
                case 0:
                    System.out.println("Existing system....");
                    break;
                case 1:
                    WalkInRegistration walkInRegistration = new WalkInRegistration();
                    walkInRegistration.registration();
                    break;
                case 2:
                    System.out.println("yet to integrate housekeeping");
                    break;
                case 3:
                    System.out.println("yet to integrate loyalty point");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter again!!");
            }
        }while(choice != 0);
    }
    
    public int handleModulesChoice(){
        System.out.println("\n==== Modules Choice =====");
        System.out.println("1. Walk-In / Standard Booking ");
        System.out.println("2. HouseKeeping and TaskLog ");
        System.out.println("3. Loyalty Point ");
        System.out.println("0. Exit System ");
        
        System.out.print("Enter choice: ");
        return ValidationUtility.validateIntegerChoice();
    }
    
    public static void main(String[] args){
        MainMenu main = new MainMenu();
        main.handleModulesMenu();
    }
}
