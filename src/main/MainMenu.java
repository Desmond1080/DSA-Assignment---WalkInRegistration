/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

/**
 *
 * @author Desmond
 */

import boundary.HousekeepingUI;
import boundary.LoyaltyAndRewardsUI;
import control.WalkInRegistration;
import boundary.WalkInRegistrationUI;
import control.HousekeepingControl;
import control.HousekeepingReportControl;
import control.LoyaltyAndRewardsControl;
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
                    HousekeepingControl control = new HousekeepingControl(); /* Create rooms, staff and dummy tasks */
                    HousekeepingReportControl reports = new HousekeepingReportControl(control); /* Receive same control object to read task information*/
                    HousekeepingUI ui = new HousekeepingUI(control, reports); /* Receieve two controller */
                    ui.run();
                    break;
                case 3:
                    LoyaltyAndRewardsUI loyalty = new LoyaltyAndRewardsUI();
                    loyalty.startMenu();
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
        System.out.println("3. Loyalty and Rewards ");
        System.out.println("0. Exit System ");
        
        System.out.print("Enter choice: ");
        return ValidationUtility.validateIntegerChoice();
    }
    
    public static void main(String[] args){
        MainMenu main = new MainMenu();
        main.handleModulesMenu();
    }
}
