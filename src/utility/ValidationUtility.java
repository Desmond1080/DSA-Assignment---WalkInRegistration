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
public class ValidationUtility {
    private static Scanner scanner = new Scanner(System.in);
    
    public static int validateIntegerChoice(){
        while(!scanner.hasNextInt()){
             System.out.println("Invalid integer input. please enter a number: ");
             scanner.next();
       }
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }
}
