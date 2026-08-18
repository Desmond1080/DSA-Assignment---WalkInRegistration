/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utility;

import java.util.Scanner;

/**
 *
 * @author Desmond
 */
public class ScreenUtility {
    Scanner scanner = new Scanner(System.in);
    
    public static void clearScreen() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
    
    public void pauseScreen(){
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
}
