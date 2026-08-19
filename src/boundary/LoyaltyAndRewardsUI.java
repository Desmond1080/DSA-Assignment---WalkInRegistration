/**
 * Author: Law Tian Xiang
 * Description: Boundary class with Clear Screen and Pause features
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
            
            clearScreen(); // NEW: Clears the screen before displaying the menu
            
            System.out.println("\n=== TARUMT Resort: Loyalty & Rewards ===");
            System.out.println("1. View Member Profile");
            System.out.println("2. Request Reward Redemption (Queue System)");
            System.out.println("3. Process Next Redemption Request (Admin)");
            System.out.println("4. Add Loyalty Points to a Member Account");
            System.out.println("5. [REPORT] Generate High-Value Member Ranking");
            System.out.println("6. [REPORT] Generate Tier Specific Roster");
            System.out.println("0. Exit");
            
            choice = readValidInt("Enter choice (0-6): ", 0, 6);

            switch (choice) {
                case 1: 
                    viewProfile(); 
                    pressEnterToContinue(); // NEW: Pauses so user can read
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
                    generateReportOne(); 
                    pressEnterToContinue(); 
                    break;
                case 6: 
                    generateReportTwo(); 
                    pressEnterToContinue(); 
                    break;
                case 0: 
                    System.out.println("Exiting Loyalty Module..."); 
                    break;
            }
        }
    }

    private void viewProfile() {
        String id = readValidString("\nEnter Member ID (e.g. M001 to M005): ");
        Object member = control.findMember(id);
        if (member != null) {
            System.out.println("\n[MEMBER DATA FOUND]:\n" + member.toString());
        } else {
            System.out.println("Error: Member ID '" + id + "' not found.");
        }
    }

    private void requestRedemption() {
        System.out.println();
        String id = readValidString("Enter Member ID: ");
        String item = readValidString("Enter Reward Item: ");
        int cost = readValidInt("Enter Points Cost: ", 1, 99999);
        
        if (control.requestReward(id, item, cost)) {
            System.out.println("Success! Request added to the queue.");
        } else {
            System.out.println("Request failed. Verify Member ID and points balance.");
        }
    }

    private void awardPoints() {
        System.out.println();
        String id = readValidString("Enter Target Member ID: ");
        int points = readValidInt("Enter Points to Add: ", 1, 99999);
        if (control.addPointsToMember(id, points)) {
            System.out.println("Points successfully added!\n" + control.findMember(id).toString());
        } else {
            System.out.println("Transaction Failed.");
        }
    }

    private void processRedemption() {
        System.out.println("\nActive Pending Requests in Queue: " + control.getPendingRequestsCount());
        
        String requestDetails = control.peekNextRequestDetails();
        if (requestDetails == null) {
            System.out.println("No pending redemptions to process.");
            return; 
        }
        
        System.out.println("\n[NEXT IN LINE] " + requestDetails);
        String confirm = readValidYesNo("Do you want to approve and process this request? (Y/N): ");
        
        if (confirm.equals("Y")) {
            System.out.println(control.processNextRedemption());
        } else {
            System.out.println("Processing cancelled. The request has been safely left at the front of the queue.");
        }
    }

    private void generateReportOne() {
        System.out.println("\n--- Report 1 Configuration ---");
        int minPts = readValidInt("Enter Minimum Points Threshold (e.g. 1000): ", 0, 99999);
        
        System.out.println(control.generatePointsRankingReport(minPts));
    }

    private void generateReportTwo() {
        System.out.println("\n--- Report 2 Configuration ---");
        String tier = readValidString("Enter Target Tier (Standard, Elite, Diamond, Platinum): ");
        
        System.out.println(control.generateTierRosterReport(tier));
    }

    // ==========================================================
    // UI EXPERIENCE HELPER METHODS
    // ==========================================================
    
    /**
     * Pauses the program until the user presses Enter.
     */
    private void pressEnterToContinue() {
        System.out.print("\nPress [ENTER] to continue...");
        scanner.nextLine();
    }

    /**
     * Simulates clearing the screen in NetBeans by printing blank lines.
     */
    private void clearScreen() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    // ==========================================================
    // INPUT VALIDATION HELPER METHODS
    // ==========================================================
    private String readValidString(String prompt) {
        String input = "";
        while (input.isEmpty()) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) System.out.println("  -> Error: Input cannot be blank.");
        }
        return input;
    }

    private int readValidInt(String prompt, int min, int max) {
        int value = -1;
        boolean valid = false;
        while (!valid) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;
            try {
                value = Integer.parseInt(input);
                if (value < min || value > max) {
                    System.out.println("  -> Error: Please enter a number between " + min + " and " + max + ".");
                } else {
                    valid = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("  -> Error: Invalid format. Digits only.");
            }
        }
        return value;
    }

    private String readValidYesNo(String prompt) {
        String input = "";
        boolean valid = false;
        while (!valid) {
            System.out.print(prompt);
            input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("Y") || input.equals("N")) {
                valid = true;
            } else {
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