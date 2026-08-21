package boundary;

import control.HousekeepingControl;
import control.HousekeepingReportControl;
import java.util.Scanner;

/**
 * Handles all console menus, user input and output for the housekeeping module.
 *
 * @author shujuntan
 */

public class HousekeepingUI {

    private final Scanner scanner = new Scanner(System.in);

    private final HousekeepingControl control;
    private final HousekeepingReportControl reports;

    public HousekeepingUI(HousekeepingControl control, HousekeepingReportControl reports) {

        this.control = control;
        this.reports = reports;
    }

    public void run() {
        boolean running = true;

        System.out.println("Welcome to TARUMT Resort " + "Housekeeping Module");

        while (running) {
            showMainMenu();

            try {
                switch (readInt("Choice: ")) {
                    case 1 ->
                        taskMenu();
                    case 2 ->
                        statusMenu();
                    case 3 ->
                        reportMenu();

                    case 0 -> {
                        running = false;
                        System.out.println("Housekeeping module closed.");
                    }

                    default ->
                        System.out.println("Please enter 0 to 3.");
                }

            } catch (BackException exception) {
                System.out.println("[CANCELLED] Returned to " + "the main menu.");
            }
        }
    }

    private void showMainMenu() {
        System.out.println("\n========== HOUSEKEEPING " + "AND TASK LOG ==========");

        System.out.println("1. Task management");
        System.out.println("2. Status and rollback");
        System.out.println("3. Reports");
        System.out.println("0. Exit");

        System.out.println("Enter B at any input field " + "to return here.");
    }

    
    private void taskMenu() {
        boolean open = true;

        while (open) {
            System.out.println( "\n--- TASK MANAGEMENT ---");

            System.out.println( "1. Display task records");

            System.out.println("2. Add task");

            System.out.println("0. Back");

            switch (readInt("Choice: ")) {
                case 1 ->
                    System.out.println( control.getTaskRecordsTable());

                case 2 ->
                    addTask();

                case 0 ->
                    open = false;

                default ->
                    System.out.println( "Please enter 0 to 2.");
            }
        }
    }

    
    private void statusMenu() {
        boolean open = true;

        while (open) {
            System.out.println(control.getTaskStatusOverview());

            System.out.println( "--- STATUS AND ROLLBACK ---");

            System.out.println("1. Update task status");

            System.out.println(  "2. Reschedule for late checkout");

            System.out.println(  "3. Undo latest change");

            System.out.println("0. Back");

            switch (readInt("Choice: ")) {
                case 1 ->
                    updateStatus();
                case 2 ->
                    lateCheckout();
                case 3 ->
                    undoStatus();
                case 0 ->
                    open = false;

                default ->
                    System.out.println( "Please enter 0 to 3.");
            }
        }
    }

  
    private void reportMenu() {
        boolean open = true;

        while (open) {
            System.out.println("\n--- REPORTS ---");

            System.out.println("1. Room status report");

            System.out.println( "2. Staff task summary report");

            System.out.println("0. Back");

            switch (readInt("Choice: ")) {
                case 1 ->
                    roomReport();
                case 2 ->
                    staffReport();
                case 0 ->
                    open = false;

                default ->
                    System.out.println( "Please enter 0 to 2.");
            }
        }
    }

    /* Collects task details and sends them to the control for processing. */
    private void addTask() {
        System.out.println("\n--- ADD HOUSEKEEPING TASK ---");

        String taskId = readTaskId();

        System.out.println( control.getAvailableRoomsForTaskTable());

        String roomNumber = readNewTaskRoom();

        System.out.println( control.getAvailableStaffTable(null));

        String staffId = readStaffId();

        String date = readDate("Scheduled cleaning date "+ "(dd-MM-yyyy): ");

        String time = readTime( "Scheduled cleaning time " + "(HH:mm): ");

        showResult(control.addTask(
                taskId,
                roomNumber,
                staffId,
                date,
                time)
        );
    }

    private void updateStatus() {
        System.out.println( "\n--- UPDATE TASK STATUS ---");

        String roomNumber = readStatusRoom();

        boolean continueUpdating = true;

        while (continueUpdating) {
            System.out.print(control.getValidNextStatusOptions(roomNumber));

            int statusNumber = readStatusNumber(roomNumber);

            showResult(control.updateStatus(roomNumber, statusNumber));

            if (!control.validateRoomForStatusUpdate(roomNumber).isEmpty()) {
                
                System.out.println( "Task completed and room released.");
                continueUpdating = false;
                
            } else {
                continueUpdating = readYesNo("Continue updating this room? (Y/N): ");
                
                if (continueUpdating) {
                    System.out.println();
                }
            }
        }
    }


    private void lateCheckout() {
        System.out.println(  "\n--- RESCHEDULE FOR LATE CHECKOUT ---");

        System.out.println(  control.getScheduledRoomsForRescheduleTable());

        String roomNumber = readLateCheckoutRoom();

        String date = readDate( "Revised cleaning date " + "(dd-MM-yyyy): ");

        String time = readTime( "Revised cleaning time " + "(HH:mm): ");

        showResult( control.processLateCheckout(roomNumber, date, time));
    }

    /* Displays undoable tasks and asks the control to restore one task. */
    private void undoStatus() {
        System.out.println("\n--- UNDO LATEST CHANGE ---");

        System.out.println( control.getUndoableTasksTable());

        String roomNumber = readUndoRoom();

        showResult(control.undoLatestChange(roomNumber));
    }


    private void roomReport() {
        System.out.println( "\n--- ROOM STATUS REPORT ---");

        int floor = readNonNegative(  "Floor (0 for all): ");

        String[] statuses = control.getAvailableStatusNames();

        System.out.println( "0. All statuses");

        for (int i = 0;i < statuses.length; i++) {

            System.out.println( (i + 1)  + ". " + statuses[i]);
        }

        int statusNumber = readNonNegative( "Status number: ");

        System.out.println(reports.generateRoomStatusReport(floor,statusNumber));
    }


    private void staffReport() {
        System.out.println( "\n--- STAFF TASK SUMMARY REPORT ---");

        String staffId = required("Staff ID (0 for all): ");
        
        if (staffId.equals("0")) {
            staffId = "ALL";
        }

        System.out.println("Task record filter:");
        System.out.println("0. All");
        System.out.println("1. Has active task");
        System.out.println("2. Has completed task");

        int taskRecordFilter = readNumberInRange( "Task record filter: ",   0,  2);

        System.out.println(reports.generateStaffTaskSummaryReport(  staffId, taskRecordFilter));
    }

    private String readTaskId() {
        while (true) {
            String value = required("Task ID: ");

            String error = control.validateNewTaskId(value);

            if (error.isEmpty()) {
                return value;
            }

            System.out.println(error);
        }
    }

    private String readNewTaskRoom() {
        while (true) {
            String value = required("Room number: ");

            String error = control.validateRoomForNewTask(value);

            if (error.isEmpty()) {
                return value;
            }

            System.out.println(error);
        }
    }

    private String readStatusRoom() {
        while (true) {
            String value = required("Room number: ");

            String error = control.validateRoomForStatusUpdate(value);

            if (error.isEmpty()) {
                return value;
            }

            System.out.println(error);
        }
    }

    private String readUndoRoom() {
        while (true) {
            String value = required("Room number: ");

            String error = control.validateRoomForUndo( value);

            if (error.isEmpty()) {
                return value;
            }

            System.out.println(error);
        }
    }

    private String readLateCheckoutRoom() {
        while (true) {
            String value = required("Room number: ");

            String error = control.validateRoomForLateCheckout(value);

            if (error.isEmpty()) {
                return value;
            }

            System.out.println(error);
        }
    }

    private String readStaffId() {
        while (true) {
            String value = required("Staff ID: ");

            String error = control.validateAvailableStaffId( value,false,null);

            if (error.isEmpty()) {
                return value;
            }

            System.out.println(error);
        }
    }

    private int readStatusNumber(
            String roomNumber) {

        while (true) {
            int value = readInt( "New status number: ");

            String error  = control.validateNextStatusNumber( roomNumber, value);

            if (error.isEmpty()) {
                return value;
            }

            System.out.println(error);
        }
    }

    private String readDate(String prompt) {
        while (true) {
            String value = required(prompt);
            String error = control.validateDate(value);

            if (error.isEmpty()) {
                return value;
            }

            System.out.println(error);
        }
    }

    private String readTime(String prompt) {
        while (true) {
            String value = required(prompt);
            String error = control.validateTime(value);

            if (error.isEmpty()) {
                return value;
            }

            System.out.println(error);
        }
    }

    private int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(input(prompt));

            } catch (NumberFormatException exception) {
                System.out.println( "Please enter a whole number.");
            }
        }
    }

    private int readNonNegative(
            String prompt) {

        while (true) {
            int value = readInt(prompt);

            if (value >= 0) {
                return value;
            }

            System.out.println( "Value cannot be negative.");
        }
    }

    private int readNumberInRange( String prompt, int minimum, int maximum) {

        while (true) {
            int value = readInt(prompt);

            if (value >= minimum && value <= maximum) {
                return value;
            }

            System.out.println("Please enter " + minimum + " to " + maximum + ".");
        }
    }

    private boolean readYesNo(String prompt) {
        while (true) {
            String value = input(prompt);

            if (value.equalsIgnoreCase("Y") || value.equalsIgnoreCase("YES")) {
                return true;
            }

            if (value.equalsIgnoreCase("N") || value.equalsIgnoreCase("NO")) {
                return false;
            }

            System.out.println( "Please enter Y or N.");
        }
    }

    private String required(String prompt) {
        while (true) {
            String value = input(prompt);

            if (!value.isEmpty()) {
                return value;
            }

            System.out.println( "This field is required.");
        }
    }

    private String input(String prompt) {
        System.out.print(prompt);

        String value = scanner.nextLine().trim();

        if (value.equalsIgnoreCase("B") || value.equalsIgnoreCase("BACK")) {

            throw new BackException();
        }

        return value;
    }

    /* Displays the latest success or failure message prepared by the control. */
    private void showResult(boolean success) {
        System.out.println((success  ? "[SUCCESS] " : "[FAILED] ") + control.getLastMessage());
    }

    /* Immediately returns control to the main menu when B is entered. */
    private static class BackException
            extends RuntimeException {

        private static final long serialVersionUID = 1L;
    }
}
