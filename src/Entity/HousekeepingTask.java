package Entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author shujuntan
 */

public class HousekeepingTask {

    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public enum RoomStatus { /* Status following the housekeeping sequence */
        
        SCHEDULED("Scheduled"),
        CLEANING_IN_PROGRESS("Cleaning In Progress"),
        INSPECTED("Inspected"),
        READY_FOR_CHECK_IN("Ready for Check-In");

        private final String displayName;

        RoomStatus(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    public static class StatusSnapshot { /* Stores copy of previous version of task so the latest change can be undo */

        private final RoomStatus status;
        private final LocalDateTime scheduledTime;
        private final LocalDateTime lastUpdated;
        private final String reason;

        private StatusSnapshot(RoomStatus status, LocalDateTime scheduledTime, LocalDateTime lastUpdated, String reason) {
            this.status = status;      
            this.scheduledTime = scheduledTime;
            this.lastUpdated = lastUpdated;
            this.reason = reason;
        }
    }

    private final String taskId;
    private final String roomNumber;
    private final int floor;
    private final String roomType;
    private final String assignedStaffId;

    private RoomStatus currentStatus;
    private LocalDateTime scheduledTime;
    private LocalDateTime lastUpdated;
    private String lastReason;

    private int rollbackCount;
    private int lateCheckoutCount;

    public HousekeepingTask(String taskId, String roomNumber, int floor, String roomType, String assignedStaffId, RoomStatus initialStatus, LocalDateTime scheduledTime) {

        required(taskId, "Task ID");
        required(roomNumber, "Room number");
        required(roomType, "Room type");
        required(assignedStaffId, "Staff ID");

        if (floor <= 0) {
            throw new IllegalArgumentException("Floor must be greater than zero.");
        }

        if (initialStatus == null || scheduledTime == null) {
            throw new IllegalArgumentException("Initial status and scheduled time are required.");
        }

        this.taskId = taskId.trim().toUpperCase();
        this.roomNumber = roomNumber.trim().toUpperCase();
        this.floor = floor;
        this.roomType = roomType.trim();
        this.assignedStaffId = assignedStaffId.trim().toUpperCase();
        this.currentStatus = initialStatus;
        this.scheduledTime = scheduledTime;
        this.lastUpdated = LocalDateTime.now();
        this.lastReason = "Initial task record";
    }

    public String getTaskId() {
        return taskId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public int getFloor() {
        return floor;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getAssignedStaffId() {
        return assignedStaffId;
    }

    public RoomStatus getCurrentStatus() {
        return currentStatus;
    }

    public String getStatusDisplayText() {
        if (currentStatus == RoomStatus.SCHEDULED && lastReason.equals( "Cleaning rescheduled after late checkout")) {
            return "Scheduled (Rescheduled)";
        }

        return currentStatus.toString();
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public String getLastReason() {
        return lastReason;
    }

    public int getRollbackCount() {
        return rollbackCount;
    }

    public int getLateCheckoutCount() {
        return lateCheckoutCount;
    }

    public void changeStatus(RoomStatus newStatus, String reason) {
        if (newStatus == null) {
            throw new IllegalArgumentException("New status is required.");
        }

        currentStatus = newStatus;
        lastUpdated = LocalDateTime.now();
        lastReason = cleanReason(reason);
    }

    public void reschedule(LocalDateTime newTime, String reason) {
        if (newTime == null) {
            throw new IllegalArgumentException("New scheduled time is required.");
        }

        scheduledTime = newTime;
        lastUpdated = LocalDateTime.now();
        lastReason = cleanReason(reason);
    }

    public StatusSnapshot createSnapshot() {
        return new StatusSnapshot(
                currentStatus,
                scheduledTime,
                lastUpdated,
                lastReason);
    }

    public void restoreSnapshot(StatusSnapshot snapshot) {
        if (snapshot == null) {
            throw new IllegalArgumentException("Snapshot is required.");
        }

        currentStatus = snapshot.status;
        scheduledTime = snapshot.scheduledTime;
        lastUpdated = snapshot.lastUpdated;
        lastReason = snapshot.reason;
    }

    public void recordRollback() {
        rollbackCount++;
    }

    public void recordLateCheckout() {
        lateCheckoutCount++;
    }

    public String getScheduledTimeText() {
        return scheduledTime.format(DISPLAY_FORMAT);
    }

    private static String cleanReason(String reason) {
        return reason == null || reason.trim().isEmpty() ? "No reason provided" : reason.trim();
    }

    private static void required(String text, String field) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException(field + " is required.");
        }
    }

    @Override
    public String toString() {
        return String.format(
                "%-7s %-6s %-5d %-10s %-11s %-25s %-16s %s",
                taskId,
                roomNumber,
                floor,
                roomType,
                assignedStaffId,
                getStatusDisplayText(),
                getScheduledTimeText(),
                lastReason
        );
    }
}
