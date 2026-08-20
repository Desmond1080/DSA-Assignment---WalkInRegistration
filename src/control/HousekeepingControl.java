package control;

import adt.HistoryStackInterface;
import adt.LinkedHistoryStack;
import Entity.HousekeepingStaff;
import Entity.HousekeepingTask;
import Entity.HousekeepingTask.RoomStatus;
import Entity.HousekeepingTask.StatusSnapshot;
import Entity.Room;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 *
 * @author shujuntan
 */

public class HousekeepingControl {

    private static final int MAX_TASKS = 100;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(ResolverStyle.STRICT);

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm").withResolverStyle(ResolverStyle.STRICT);

    private static final String TASK_HEADER = String.format(
            "%-7s %-6s %-5s %-10s %-11s %-25s %-16s %s%n",
            "Task", "Room", "Floor", "Type", "Staff", "Status",
            "Scheduled", "Latest reason"
    );
    
    /* Main Storage */
    private final TaskSlot[] slots = new TaskSlot[MAX_TASKS];  // Each slot keeps a task together with its individual status history.
    private final Room[] rooms = createRooms();
    private final HousekeepingStaff[] staff = createStaff();

    private int taskCount;
    private String lastMessage;

    public HousekeepingControl() {
        seedData();
        lastMessage = "Housekeeping module loaded.";
    }

    public String getLastMessage() {
        return lastMessage;
    }

    int getTaskCountForReport() {
        return taskCount;
    }

    HousekeepingTask getTaskForReport(int index) {
        return slots[index].task;
    }

    String getStaffNameForReport(String staffId) {
        HousekeepingStaff member = findStaff(staffId);
        return member == null ? "Not assigned" : member.getStaffName();
    }

    public String[] getAvailableStatusNames() {
        RoomStatus[] values = RoomStatus.values();
        String[] names = new String[values.length];

        for (int i = 0; i < values.length; i++) {
            names[i] = values[i].toString();
        }

        return names;
    }

    public String validateNewTaskId(String taskId) {
        if (blank(taskId)) {
            return "Task ID is required.";
        }

        if (!taskId.trim().toUpperCase().matches("T\\d{3}")) {
            return "Task ID must be T followed by 3 digits, for example T007.";
        }

        return findByTaskId(taskId) == null ? "" : "Task ID already exists.";
    }

    public String validateRoomForNewTask(String roomNumber) {
        if (blank(roomNumber)) {
            return "Room number is required.";
        }

        if (findRoom(roomNumber) == null) {
            return "Room does not exist.";
        }

        TaskSlot activeTask = findActiveByRoom(roomNumber);

        return activeTask == null ? "" : "Room already has unfinished task " + activeTask.task.getTaskId() + ".";
    }

    public String validateRoomForStatusUpdate(String roomNumber) {
        if (blank(roomNumber)) {
            return "Room number is required.";
        }

        if (findRoom(roomNumber) == null) {
            return "Room does not exist.";
        }

        return findActiveByRoom(roomNumber) == null ? "That room does not have an unfinished housekeeping task." : "";
    }

    public String validateRoomForUndo(String roomNumber) {
        if (blank(roomNumber)) {
            return "Room number is required.";
        }

        if (findRoom(roomNumber) == null) {
            return "Room does not exist.";
        }

        TaskSlot slot = findActiveByRoom(roomNumber);

        if (slot == null) {
            return "That room does not have an active task that can be undone.";
        }

        return slot.history.canUndo() ? "" : "That task does not have a previous change to undo.";
    }

    public String validateRoomForLateCheckout(String roomNumber) {
        String error = validateRoomForStatusUpdate(roomNumber);

        if (!error.isEmpty()) {
            return error;
        }

        TaskSlot slot = findActiveByRoom(roomNumber);

        return slot.task.getCurrentStatus() == RoomStatus.SCHEDULED ? ""  : "Only rooms with Scheduled status can be rescheduled.";
    }

    public String validateAvailableStaffId( String staffId, boolean blankAllowed, String excludedTaskId) {

        if (blank(staffId)) {
            return blankAllowed ? "" : "Staff ID is required.";
        }

        if (findStaff(staffId) == null) {
            return "Staff ID does not exist.";
        }

        TaskSlot busyTask = findUnfinishedByStaff(staffId, excludedTaskId);

        return busyTask == null ? "" : "Staff is assigned to unfinished task " + busyTask.task.getTaskId() + ".";
    }

    public String validateDate(String text) {
        try {
            LocalDate.parse(text == null ? "" : text.trim(), DATE_FORMAT);
            return "";
        } catch (DateTimeParseException exception) {
            return "Invalid date. Use dd-MM-yyyy, for example 21-08-2026.";
        }
    }

    public String validateTime(String text) {
        try {
            LocalTime.parse(text == null ? "" : text.trim(), TIME_FORMAT);
            return "";
        } catch (DateTimeParseException exception) {
            return "Invalid time. Use HH:mm, for example 14:30.";
        }
    }

    public String getValidNextStatusOptions(String roomNumber) {
        TaskSlot slot = findByRoom(roomNumber);

        if (slot == null) {
            return "No status options are available.\n";
        }

        StringBuilder output = new StringBuilder("Valid next statuses:\n");
        RoomStatus[] values = RoomStatus.values();

        for (int i = 0; i < values.length; i++) {
            if (validTransition(slot.task.getCurrentStatus(), values[i])) {
                output.append(i + 1)
                        .append(". ")
                        .append(getTransitionDisplayText(
                                slot.task.getCurrentStatus(),
                                values[i]))
                        .append('\n');
            }
        }

        return output.toString();
    }

    public String validateNextStatusNumber(String roomNumber, int statusNumber) {
        TaskSlot slot = findByRoom(roomNumber);
        RoomStatus[] values = RoomStatus.values();

        if (slot == null) {
            return "Room does not have a housekeeping task.";
        }

        if (statusNumber < 1 || statusNumber > values.length) {
            return "Choose one of the displayed status numbers.";
        }

        return validTransition( slot.task.getCurrentStatus(),  values[statusNumber - 1]) ? "" : "That status is not valid from " + slot.task.getCurrentStatus() + ".";
    }

    public boolean addTask(String taskId,String roomNumber, String staffId, String date, String time) {

        if (taskCount == MAX_TASKS) {
            return fail("Task storage is full.");
        }

        String error = validateNewTaskId(taskId);

        if (!error.isEmpty()) {
            return fail(error);
        }

        error = validateRoomForNewTask(roomNumber);

        if (!error.isEmpty()) {
            return fail(error);
        }

        error = validateAvailableStaffId(staffId, false, null);

        if (!error.isEmpty()) {
            return fail(error);
        }

        error = validateDate(date);

        if (!error.isEmpty()) {
            return fail(error);
        }

        error = validateTime(time);

        if (!error.isEmpty()) {
            return fail(error);
        }

        Room room = findRoom(roomNumber);

        addSlot(new HousekeepingTask(
                taskId,
                roomNumber,
                room.getFloor(),
                room.getRoomType(),
                staffId,
                RoomStatus.SCHEDULED,
                parseDateTime(date, time)));

        return succeed("Housekeeping task added successfully.");
    }

    public boolean updateStatus(
            String roomNumber,
            int statusNumber) {

        TaskSlot slot = findByRoom(roomNumber);
        RoomStatus[] values = RoomStatus.values();

        if (slot == null) {
            return fail("Room was not found.");
        }

        if (statusNumber < 1 || statusNumber > values.length) {
            return fail("Invalid status selection.");
        }

        RoomStatus oldStatus = slot.task.getCurrentStatus();
        RoomStatus newStatus = values[statusNumber - 1];

        if (!validTransition(oldStatus, newStatus)) {
            return fail(
                    "Invalid transition: "
                    + oldStatus + " -> "
                    + newStatus + ".");
        }

        applyStatus(
                slot,
                newStatus,
                defaultStatusReason(newStatus));

        return succeed(
                "Room status changed from "
                + oldStatus + " to "
                + newStatus + ".");
    }

    public boolean processLateCheckout(
            String roomNumber,
            String date,
            String time) {

        TaskSlot slot = findByRoom(roomNumber);

        if (slot == null) {
            return fail("Room was not found.");
        }

        if (slot.task.getCurrentStatus() != RoomStatus.SCHEDULED) {
            return fail(
                    "Late checkout can only reschedule a task before cleaning starts.");
        }

        String error = validateDate(date);

        if (!error.isEmpty()) {
            return fail(error);
        }

        error = validateTime(time);

        if (!error.isEmpty()) {
            return fail(error);
        }

        slot.task.reschedule(
                parseDateTime(date, time),
                "Cleaning rescheduled after late checkout");

        slot.task.recordLateCheckout();
        slot.history.record(slot.task.createSnapshot());

        return succeed(
                "Cleaning rescheduled for late checkout to "
                + slot.task.getScheduledTimeText() + ".");
    }

    public boolean undoLatestChange(String roomNumber) {
        TaskSlot slot = findActiveByRoom(roomNumber);

        if (slot == null) {
            return fail( "Only active housekeeping tasks can be undone.");
        }

        if (!slot.history.canUndo()) {
            return fail("No previous status is available for this room.");
        }

        StatusSnapshot currentSnapshot = slot.task.createSnapshot();
        slot.task.restoreSnapshot(slot.history.undo());

        if (hasStaffConflict(slot.task)) {
            slot.task.restoreSnapshot(currentSnapshot);
            slot.history.record(currentSnapshot);

            return fail( "Undo cancelled because the staff member is now busy.");
        }

        slot.task.recordRollback();

        return succeed( "Latest change was undone. Current status: " + slot.task.getCurrentStatus() + ", scheduled time: " + slot.task.getScheduledTimeText() + ".");
    }

    public String getTaskRecordsTable() {
        StringBuilder output = new StringBuilder("\nACTIVE HOUSEKEEPING TASKS\n")
                .append(TASK_HEADER)
                .append("-".repeat(119))
                .append('\n');

        int activeCount = 0;

        for (int i = 0; i < taskCount; i++) {
            HousekeepingTask task = slots[i].task;

            if (!finished(task)) {
                output.append(task).append('\n');
                activeCount++;
            }
        }

        output.append("Active tasks: ")
                .append(activeCount)
                .append('\n');

        output.append("\nFINISHED TASK RECORDS\n")
                .append(TASK_HEADER)
                .append("-".repeat(119))
                .append('\n');

        int finishedCount = 0;

        for (int i = 0; i < taskCount; i++) {
            HousekeepingTask task = slots[i].task;

            if (finished(task)) {
                output.append(task).append('\n');
                finishedCount++;
            }
        }

        return output.append("Finished tasks: ")
                .append(finishedCount)
                .append('\n')
                .toString();
    }

    public String getTaskStatusOverview() {
        StringBuilder output =
                new StringBuilder("\nHOUSEKEEPING TASK STATUS\n")
                        .append(String.format(
                                "%-7s %-8s %-6s %-12s %-11s %-25s %-16s %s%n",
                                "Task", "Room", "Floor",
                                "Type", "Staff", "Status",
                                "Scheduled", "Latest note"))
                        .append("-".repeat(125))
                        .append('\n');

        for (int i = 0; i < taskCount; i++) {
            HousekeepingTask task = slots[i].task;

            if (!finished(task)) {
                output.append(String.format(
                        "%-7s %-8s %-6d %-12s %-11s %-25s %-16s %s%n",
                        task.getTaskId(),
                        task.getRoomNumber(),
                        task.getFloor(),
                        task.getRoomType(),
                        task.getAssignedStaffId(),
                        task.getStatusDisplayText(),
                        task.getScheduledTimeText(),
                        task.getLastReason()));
            }
        }

        return output.toString();
    }

    public String getAvailableStaffTable(String excludedTaskId) {
        StringBuilder output =
                new StringBuilder("\nAVAILABLE HOUSEKEEPING STAFF\n")
                        .append(String.format(
                                "%-10s %-24s %-12s%n",
                                "Staff ID", "Name", "Shift"))
                        .append("-".repeat(50))
                        .append('\n');

        int availableCount = 0;

        for (HousekeepingStaff member : staff) {
            if (findUnfinishedByStaff(
                    member.getStaffId(),
                    excludedTaskId) == null) {

                output.append(String.format(
                        "%-10s %-24s %-12s%n",
                        member.getStaffId(),
                        member.getStaffName(),
                        member.getShift()));

                availableCount++;
            }
        }

        if (availableCount == 0) {
            output.append("No staff members are available.\n");
        }

        return output.append("Available staff: ")
                .append(availableCount)
                .append('\n')
                .toString();
    }

    public String getAvailableRoomsForTaskTable() {
        StringBuilder output =
                new StringBuilder("\nAVAILABLE ROOMS FOR HOUSEKEEPING TASK\n")
                        .append(String.format(
                                "%-8s %-7s %-12s %s%n",
                                "Room", "Floor", "Type", "Task status"))
                        .append("-".repeat(48))
                        .append('\n');

        int availableCount = 0;

        for (Room room : rooms) {
            if (findActiveByRoom(room.getRoomNumber()) == null) {
                output.append(String.format(
                        "%-8s %-7d %-12s %s%n",
                        room.getRoomNumber(),
                        room.getFloor(),
                        room.getRoomType(),
                        "No active task"));

                availableCount++;
            }
        }

        if (availableCount == 0) {
            output.append("No rooms are available for a new task.\n");
        }

        return output.append("Available rooms: ")
                .append(availableCount)
                .append('\n')
                .toString();
    }

    public String getScheduledRoomsForRescheduleTable() {
        StringBuilder output =
                new StringBuilder("\nROOMS AVAILABLE FOR RESCHEDULING\n")
                        .append(String.format(
                                "%-7s %-8s %-11s %-25s %-16s%n",
                                "Task", "Room", "Staff", "Status", "Scheduled"))
                        .append("-".repeat(72))
                        .append('\n');

        int scheduledCount = 0;

        for (int i = 0; i < taskCount; i++) {
            HousekeepingTask task = slots[i].task;

            if (task.getCurrentStatus() == RoomStatus.SCHEDULED) {
                output.append(String.format(
                        "%-7s %-8s %-11s %-25s %-16s%n",
                        task.getTaskId(),
                        task.getRoomNumber(),
                        task.getAssignedStaffId(),
                        task.getStatusDisplayText(),
                        task.getScheduledTimeText()));

                scheduledCount++;
            }
        }

        if (scheduledCount == 0) {
            output.append("No Scheduled rooms are available.\n");
        }

        return output.append("Scheduled rooms: ")
                .append(scheduledCount)
                .append('\n')
                .toString();
    }

    public String getUndoableTasksTable() {
        StringBuilder output =
                new StringBuilder("\nTASKS AVAILABLE FOR UNDO\n")
                        .append(String.format(
                                "%-7s %-8s %-11s %-25s %-16s%n",
                                "Task", "Room", "Staff", "Status", "Scheduled"))
                        .append("-".repeat(72))
                        .append('\n');

        int undoableCount = 0;

        for (int i = 0; i < taskCount; i++) {
            TaskSlot slot = slots[i];
            HousekeepingTask task = slot.task;

            if (!finished(task) && slot.history.canUndo()) {
                output.append(String.format(
                        "%-7s %-8s %-11s %-25s %-16s%n",
                        task.getTaskId(),
                        task.getRoomNumber(),
                        task.getAssignedStaffId(),
                        task.getStatusDisplayText(),
                        task.getScheduledTimeText()));

                undoableCount++;
            }
        }

        if (undoableCount == 0) {
            output.append("No active tasks have a change to undo.\n");
        }

        return output.append("Undoable tasks: ")
                .append(undoableCount)
                .append('\n')
                .toString();
    }

    private void seedData() {
        LocalDateTime now =
                LocalDateTime.now().withSecond(0).withNano(0);

        addSlot(new HousekeepingTask(
                "T001", "0101", 1, "Deluxe", "S005",
                RoomStatus.SCHEDULED, now.plusMinutes(30)));

        addSlot(new HousekeepingTask(
                "T002", "0104", 1, "Suite", "S006",
                RoomStatus.SCHEDULED, now.plusMinutes(45)));

        addSlot(new HousekeepingTask( "T003", "0202", 2, "Deluxe", "S001",
                RoomStatus.SCHEDULED, now.plusMinutes(15)));

        addSlot(new HousekeepingTask(
                "T004", "0205", 2, "Family", "S003",
                RoomStatus.SCHEDULED, now.plusMinutes(20)));

        applyStatus(
                findByRoom("0202"),
                RoomStatus.CLEANING_IN_PROGRESS,
                "Cleaner started work");

        applyStatus(
                findByRoom("0205"),
                RoomStatus.CLEANING_IN_PROGRESS,
                "Cleaner started work");

        applyStatus(
                findByRoom("0205"),
                RoomStatus.INSPECTED,
                "Inspection passed");

        applyStatus(
                findByRoom("0205"),
                RoomStatus.READY_FOR_CHECK_IN,
                "Room released to front desk");

        LocalDateTime revised = now.plusHours(2);

        processLateCheckout(
                "0104",
                revised.toLocalDate().format(DATE_FORMAT),
                revised.toLocalTime().format(TIME_FORMAT));
    }

    private Room[] createRooms() {
        return new Room[]{
            new Room("0101", 1, "Deluxe"),
            new Room("0102", 1, "Standard"),
            new Room("0103", 1, "Suite"),
            new Room("0104", 1, "Suite"),
            new Room("0201", 2, "Standard"),
            new Room("0202", 2, "Deluxe"),
            new Room("0205", 2, "Family"),
            new Room("0301", 3, "Suite"),
            new Room("0305", 3, "Family"),
            new Room("0308", 3, "Deluxe")
        };
    }

    private HousekeepingStaff[] createStaff() {
        return new HousekeepingStaff[]{
            new HousekeepingStaff("S001", "Aisha Rahman", "Morning"),
            new HousekeepingStaff("S002", "Daniel Lee", "Morning"),
            new HousekeepingStaff("S003", "Nur Izzati", "Evening"),
            new HousekeepingStaff("S004", "Kavin Raj", "Evening"),
            new HousekeepingStaff("S005", "Mei Chen", "Night"),
            new HousekeepingStaff("S006", "Farah Lim", "Night")
        };
    }

    private boolean validTransition(RoomStatus current, RoomStatus next) {
        return switch (current) {
            case SCHEDULED ->
                next == RoomStatus.CLEANING_IN_PROGRESS;

            case CLEANING_IN_PROGRESS ->
                next == RoomStatus.INSPECTED;

            case INSPECTED ->
                next == RoomStatus.READY_FOR_CHECK_IN;

            case READY_FOR_CHECK_IN -> false;
        };
    }

    private String getTransitionDisplayText( RoomStatus current, RoomStatus next) {

        if (current == RoomStatus.INSPECTED && next == RoomStatus.READY_FOR_CHECK_IN) {
            return "Ready for Check-In (inspection passed)";
        }

        return next.toString();
    }

    private String defaultStatusReason(RoomStatus status) {
        return switch (status) {
            case SCHEDULED -> "Task scheduled for cleaning";
            case CLEANING_IN_PROGRESS -> "Cleaner started work";
            case INSPECTED -> "Inspection completed";
            case READY_FOR_CHECK_IN -> "Room released to front desk";
        };
    }

    private void addSlot(HousekeepingTask task) {
        slots[taskCount] = new TaskSlot(task);
        taskCount++;
    }

    private void applyStatus( TaskSlot slot, RoomStatus status, String reason) {

        slot.task.changeStatus(status, reason);
        slot.history.record(slot.task.createSnapshot());
        
    }

    private TaskSlot findByRoom(String roomNumber) {
        TaskSlot activeTask = findActiveByRoom(roomNumber);

        if (activeTask != null) {
            return activeTask;
        }

        if (!blank(roomNumber)) {
            for (int i = taskCount - 1; i >= 0; i--) {
                if (slots[i].task.getRoomNumber()
                        .equalsIgnoreCase(roomNumber.trim())) {
                    return slots[i];
                }
            }
        }

        return null;
    }

    private TaskSlot findActiveByRoom(String roomNumber) {
        if (!blank(roomNumber)) {
            for (int i = taskCount - 1; i >= 0; i--) {
                HousekeepingTask task = slots[i].task;

                if (task.getRoomNumber()
                        .equalsIgnoreCase(roomNumber.trim())
                        && !finished(task)) {
                    return slots[i];
                }
            }
        }

        return null;
    }

    private TaskSlot findByTaskId(String taskId) {
        if (!blank(taskId)) {
            for (int i = 0; i < taskCount; i++) {
                if (slots[i].task.getTaskId()
                        .equalsIgnoreCase(taskId.trim())) {
                    return slots[i];
                }
            }
        }

        return null;
    }

    private Room findRoom(String roomNumber) {
        if (!blank(roomNumber)) {
            for (Room room : rooms) {
                if (room.getRoomNumber()
                        .equalsIgnoreCase(roomNumber.trim())) {
                    return room;
                }
            }
        }

        return null;
    }

    private HousekeepingStaff findStaff(String staffId) {
        if (!blank(staffId)) {
            for (HousekeepingStaff member : staff) {
                if (member.getStaffId()
                        .equalsIgnoreCase(staffId.trim())) {
                    return member;
                }
            }
        }

        return null;
    }

    private TaskSlot findUnfinishedByStaff(
            String staffId,
            String excludedTaskId) {

        if (!blank(staffId)) {
            for (int i = 0; i < taskCount; i++) {
                HousekeepingTask task = slots[i].task;

                boolean excluded =
                        !blank(excludedTaskId)
                        && task.getTaskId()
                                .equalsIgnoreCase(excludedTaskId.trim());

                if (!excluded
                        && !finished(task)
                        && task.getAssignedStaffId()
                                .equalsIgnoreCase(staffId.trim())) {
                    return slots[i];
                }
            }
        }

        return null;
    }

    private boolean hasStaffConflict(HousekeepingTask task) {
        return !finished(task)
                && findUnfinishedByStaff(
                        task.getAssignedStaffId(),
                        task.getTaskId()) != null;
    }

    private boolean finished(HousekeepingTask task) {
        return task.getCurrentStatus()
                == RoomStatus.READY_FOR_CHECK_IN;
    }

    private LocalDateTime parseDateTime(String date, String time) {
        return LocalDateTime.of(
                LocalDate.parse(date.trim(), DATE_FORMAT),
                LocalTime.parse(time.trim(), TIME_FORMAT));
    }

    private boolean succeed(String message) {
        lastMessage = message;
        return true;
    }

    private boolean fail(String message) {
        lastMessage = message;
        return false;
    }

    private boolean blank(String text) {
        return text == null || text.trim().isEmpty();
    }

    private static class TaskSlot {

        private final HousekeepingTask task;

        private final HistoryStackInterface<StatusSnapshot> history =
                new LinkedHistoryStack<>();

        private TaskSlot(HousekeepingTask task) {
            this.task = task;
            history.record(task.createSnapshot());
        }
    }
}
