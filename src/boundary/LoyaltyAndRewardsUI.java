/**
 * Author: Law Tian Xiang
 * 
 */
package boundary;

import control.LoyaltyAndRewardsControl;
import java.util.Scanner;

public class LoyaltyAndRewardsUI {
    private LoyaltyAndRewardsControl control = new LoyaltyAndRewardsControl();
    private Scanner scanner = new Scanner(System.in);

    public void startMenu() {
        int choice = -1;
        
        while (choice != 0) {
            
            clearScreen(); 
            
            System.out.println("\n=== TARUMT Resort: Loyalty & Rewards ===");
            System.out.println("1. View All Member Profiles");
            System.out.println("2. Request Reward Redemption");
            System.out.println("3. Process Next Redemption Request");
            System.out.println("4. Add Loyalty Points to a Member Account");
            System.out.println("5. Report");
            System.out.println("0. Exit");
            
            choice = readValidInt("Enter choice (0-5): ", 0, 5);

            switch (choice) {
                case 1: 
                    viewProfile();
                    pressEnterToContinue(); 
                    break;
                case 2: 
                    requestRedemption();
                    pressEnterToContinue();
                    break;
                case 3:
                    processRedemption();
                    pressEnterToContinue(); 
                    break;
                case 4:
                    awardPoints(); 
                    pressEnterToContinue();
                    break;
                case 5: 
                    reportHub(); 
                    break;
                case 0:
                    System.out.println("Exiting Loyalty Module..."); 
                    break;
            }
        }
    }

    // view all member profile
    private void viewProfile() {
        System.out.println("\n=== ALL MEMBER PROFILES ===");
        System.out.println(control.getAllMemberProfiles());
    }
    
    // request redemption
    private void requestRedemption() {
        System.out.println();
        String id = readValidString("Enter Member ID: ");
        
        int currentPoints = control.getMemberPoints(id);
        
        if (currentPoints == -1) {
            System.out.println("Error: Member ID not found.");
            
            return;
        }

        String item1 = "RM50 Dining Voucher"; int cost1 = 1000;
        String item2 = "RM100 Spa Voucher";   int cost2 = 2000;
        String item3 = "Free 1-Night Stay";   int cost3 = 4000;

        System.out.println("\n--- Available Rewards for " + id + " (Current Points: " + currentPoints + ") ---");
        
        if(currentPoints >= cost1){
            System.out.println("1. " + item1 + " (Cost: " + cost1 + " pts) " + "[AVAILABLE]");
        }
        else{
            System.out.println("1. " + item1 + " (Cost: " + cost1 + " pts) " + "[LOCKED - Insufficient Points]");
        }
        
        if(currentPoints >= cost2){
            System.out.println("2. " + item2 + " (Cost: " + cost2 + " pts) " + "[AVAILABLE]");
        }
        else{
            System.out.println("2. " + item2 + " (Cost: " + cost2 + " pts) " + "[LOCKED - Insufficient Points]");
        }
        
        if(currentPoints >= cost3){
            System.out.println("3. " + item3 + " (Cost: " + cost3 + " pts) " + "[AVAILABLE]");
        }
        else{
            System.out.println("3. " + item3 + " (Cost: " + cost3 + " pts) " + "[LOCKED - Insufficient Points]");
        }
        
        System.out.println("0. Cancel");

        String selectedItem = "";
        int selectedCost = 0;
        
        while (true) {
            int choice = readValidInt("Select a reward to redeem (0-3): ", 0, 3);
            
            if (choice == 0) {
                System.out.println("Redemption cancelled.");
                
                return;
            }
            
            if (choice == 1) 
            { 
                selectedItem = item1;
                selectedCost = cost1; 
            }
            else if (choice == 2) 
            { 
                selectedItem = item2;
                selectedCost = cost2; 
            }
            else if (choice == 3) 
            { 
                selectedItem = item3; 
                selectedCost = cost3; 
            }
            
            if (currentPoints >= selectedCost) {
                break;
            } 
            else {
                System.out.println("  -> Error: You do not have enough points for this reward. Please choose another.");
            }
        }
        
        if (control.requestReward(id, selectedItem, selectedCost)) {
            System.out.println("Success! '" + selectedItem + "' has been requested and added to the queue.");
        } 
        else {
            System.out.println("Request failed.");
        }
    }

    // add point to a member
    private void awardPoints() {
        System.out.println();
        String id = readValidString("Enter Target Member ID: ");
        int points = readValidInt("Enter Points to Add: ", 1, 99999);
        
        if (control.addPointsToMember(id, points)) {
            System.out.println("Points successfully added!\n" + control.findMember(id).toString());
        } else {
            System.out.println("Transaction Failed. Member ID not found.");
        }
    }

    // process next redemption request
    private void processRedemption() {
        System.out.println("\nActive Pending Requests in Queue: " + control.getPendingRequestsCount());
        
        String requestDetails = control.peekNextRequestDetails();
        
        if (requestDetails == null) {
            System.out.println("No pending redemptions to process.");
            
            return; 
        }
        
        System.out.println("\n" + requestDetails);
        String confirm = readValidYesNo("Do you want to approve and process this request? (Y/N): ");
        
        if (confirm.equals("Y")) {
            System.out.println(control.processNextRedemption());
        } 
        else {
            System.out.println("Processing cancelled. The request has been safely left at the front of the queue.");
        }
    }

    // report menu
    private void reportHub() {
        System.out.println("\n--- Report Generation Menu ---");
        System.out.println("1. Member Point Ranking");
        System.out.println("2. Tier Specific Ranking");
        System.out.println("0. Back to Main Menu");
        
        int repChoice = readValidInt("Select a report (0-2): ", 0, 2);
        
        if (repChoice == 1) {
            generateReportOne();
            pressEnterToContinue();
        } 
        else if (repChoice == 2) {
            generateReportTwo();
            pressEnterToContinue();
        }
    }

    // high value member ranking report
    private void generateReportOne() {
        System.out.println("\n--- Report 1 Configuration ---");
        int minPts = readValidInt("Enter Minimum Points Threshold (e.g. 1000): ", 0, 99999);
        System.out.println(control.generatePointsRankingReport(minPts));
    }

    // tier specific activity roster
    private void generateReportTwo() {
        System.out.println("\n--- Report 2 Configuration ---");
        System.out.println("Select Target Tier:");
        System.out.println("1. Bronze");
        System.out.println("2. Silver");
        System.out.println("3. Gold");
        
        int tierChoice = readValidInt("Enter choice (1-3): ", 1, 3);
        String targetTier = "";
        
        if (tierChoice == 1){
            targetTier = "Bronze";
        }
        else if (tierChoice == 2){
            targetTier = "Silver";
        }
        else if (tierChoice == 3) {
            targetTier = "Gold";
        }
        
        System.out.println(control.generateTierRosterReport(targetTier));
    }

    // handle press enter to continue
    private void pressEnterToContinue() {
        System.out.print("\nPress [ENTER] to continue...");
        scanner.nextLine();
    }

    // handle clear screen 
    private void clearScreen() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    // null input validation 
    private String readValidString(String prompt) {
        String input = "";
        while (input.isEmpty()) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            
            if (input.isEmpty()){
                System.out.println("  -> Error: Input cannot be blank.");
            }
        }
        return input;
    }

    // input format validation 
    private int readValidInt(String prompt, int min, int max) {
        int value = -1;
        boolean valid = false;
        
        while (!valid) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                continue;
            }
            
            try {
                value = Integer.parseInt(input);
                
                if (value < min || value > max) {
                    System.out.println("  -> Error: Please enter a number between " + min + " and " + max + ".");
                }
                else {
                    valid = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("  -> Error: Invalid format. Digits only.");
            }
        }
        return value;
    }

    // yes/no input validation 
    private String readValidYesNo(String prompt) {
        String input = "";
        boolean valid = false;
        while (!valid) {
            System.out.print(prompt);
            input = scanner.nextLine().trim().toUpperCase();
            
            if (input.equals("Y") || input.equals("N")) {
                valid = true;
            } 
            else {
                System.out.println("  -> Error: Please enter 'Y' for Yes or 'N' for No.");
            }
        }
        return input;
    }
    
    public static void main(String[] args) {
        LoyaltyAndRewardsUI ui = new LoyaltyAndRewardsUI();
        ui.startMenu();
    }
}