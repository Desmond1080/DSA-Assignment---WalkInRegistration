package control;

import Entity.HousekeepingTask;
import Entity.HousekeepingTask.RoomStatus;

/**
 *
 * @author shujuntan
 */

public class HousekeepingReportControl {

    private final HousekeepingControl control;

    public HousekeepingReportControl(HousekeepingControl control) {
        this.control = control;
    }

    public String generateRoomStatusReport(int floor, int statusNumber) {
        RoomStatus status = null;
        RoomStatus[] values = RoomStatus.values();

        if (statusNumber > values.length) {
            return "Invalid status filter.";
        }

        if (statusNumber > 0) {
            status = values[statusNumber - 1];
        }

        int total = control.getTaskCountForReport();
        HousekeepingTask[] rows = new HousekeepingTask[total];
        int size = 0;

        for (int i = 0; i < total; i++) {
            HousekeepingTask task = control.getTaskForReport(i);

            boolean floorMatches = floor == 0 || task.getFloor() == floor;
            boolean statusMatches =
                    status == null || task.getCurrentStatus() == status;

            if (floorMatches && statusMatches) {
                rows[size] = task;
                size++;
            }
        }

        sortTasks(rows, size);

        StringBuilder output =
                new StringBuilder("\nROOM STATUS MANAGEMENT REPORT\n");

        output.append("Filters: floor=")
                .append(floor == 0 ? "ALL" : floor)
                .append(", status=")
                .append(status == null ? "ALL" : status)
                .append('\n');

        output.append("Algorithm: linear filter + insertion sort\n");

        output.append(String.format(
                "%-7s %-8s %-11s %-25s %-16s%n",
                "Task", "Room", "Staff", "Status", "Scheduled"));

        output.append("-".repeat(72)).append('\n');

        for (int i = 0; i < size; i++) {
            HousekeepingTask task = rows[i];

            output.append(String.format(
                    "%-7s %-8s %-11s %-25s %-16s%n",
                    task.getTaskId(),
                    task.getRoomNumber(),
                    task.getAssignedStaffId(),
                    task.getStatusDisplayText(),
                    task.getScheduledTimeText()));
        }

        return output.toString();
    }

    public String generateStaffTaskSummaryReport(
            String filter,
            int taskRecordFilter) {

        if (taskRecordFilter < 0
                || taskRecordFilter > 2) {
            return "Invalid task record filter.";
        }

        int total = control.getTaskCountForReport();
        StaffRow[] allRows = new StaffRow[total];
        int allSize = 0;

        for (int i = 0; i < total; i++) {
            HousekeepingTask task =
                    control.getTaskForReport(i);

            String staffId =
                    task.getAssignedStaffId();

            int index = findStaff(
                    allRows,
                    allSize,
                    staffId);

            if (index < 0) {
                allRows[allSize] =
                        new StaffRow(
                                staffId,
                                control.getStaffNameForReport(
                                        staffId));

                index = allSize;
                allSize++;
            }

            allRows[index].include(task);
        }

        String wanted =
                filter == null || filter.trim().isEmpty()
                        ? "ALL"
                        : filter.trim().toUpperCase();

        StaffRow[] matches =
                new StaffRow[allSize];

        int matchCount = 0;

        for (int i = 0; i < allSize; i++) {
            boolean staffMatches =
                    wanted.equals("ALL")
                    || allRows[i].staffId
                            .equalsIgnoreCase(wanted);

            boolean recordMatches =
                    switch (taskRecordFilter) {
                        case 1 -> allRows[i].active > 0;
                        case 2 -> allRows[i].completed > 0;
                        default -> true;
                    };

            if (staffMatches && recordMatches) {
                matches[matchCount] = allRows[i];
                matchCount++;
            }
        }

        sortStaff(matches, matchCount);

        StringBuilder output =
                new StringBuilder(
                        "\nSTAFF TASK SUMMARY REPORT\n");

        output.append("Filters: staff=")
                .append(wanted)
                .append(", task record=")
                .append(taskRecordFilterName(
                        taskRecordFilter))
                .append('\n');

        output.append(
                "Algorithm: linear aggregation/filter "
                + "+ insertion sort\n");

        output.append(String.format(
                "%-10s %-20s %8s %8s %9s %10s%n",
                "Staff", "Name", "Assigned", "Active",
                "Completed", "Rollbacks"));

        output.append("-".repeat(72)).append('\n');

        for (int i = 0; i < matchCount; i++) {
            output.append(matches[i].format())
                    .append('\n');
        }

        output.append("Staff records shown: ")
                .append(matchCount)
                .append('\n');

        return output.toString();
    }

    private String taskRecordFilterName(
            int taskRecordFilter) {

        return switch (taskRecordFilter) {
            case 1 -> "HAS ACTIVE TASK";
            case 2 -> "HAS COMPLETED TASK";
            default -> "ALL";
        };
    }

    private void sortTasks(
            HousekeepingTask[] rows,
            int size) {

        for (int i = 1; i < size; i++) {
            HousekeepingTask current = rows[i];
            int j = i - 1;

            while (j >= 0
                    && compareTask(rows[j], current) > 0) {

                rows[j + 1] = rows[j];
                j--;
            }

            rows[j + 1] = current;
        }
    }

    private int compareTask(
            HousekeepingTask first,
            HousekeepingTask second) {

        int result =
                priority(first.getCurrentStatus())
                - priority(second.getCurrentStatus());

        if (result != 0) {
            return result;
        }

        result = first.getScheduledTime()
                .compareTo(second.getScheduledTime());

        if (result != 0) {
            return result;
        }

        return first.getRoomNumber()
                .compareToIgnoreCase(
                        second.getRoomNumber());
    }

    private int priority(RoomStatus status) {
        return switch (status) {
            case CLEANING_IN_PROGRESS -> 1;
            case INSPECTED -> 2;
            case READY_FOR_CHECK_IN -> 3;
            case SCHEDULED -> 4;
        };
    }

    private int findStaff(
            StaffRow[] rows,
            int size,
            String staffId) {

        for (int i = 0; i < size; i++) {
            if (rows[i].staffId
                    .equalsIgnoreCase(staffId)) {
                return i;
            }
        }

        return -1;
    }

    private void sortStaff(
            StaffRow[] rows,
            int size) {

        for (int i = 1; i < size; i++) {
            StaffRow current = rows[i];
            int j = i - 1;

            while (j >= 0
                    && compareStaff(rows[j], current) > 0) {

                rows[j + 1] = rows[j];
                j--;
            }

            rows[j + 1] = current;
        }
    }

    private int compareStaff(
            StaffRow first,
            StaffRow second) {

        if (first.active != second.active) {
            return second.active
                    - first.active;
        }

        if (first.completed != second.completed) {
            return second.completed
                    - first.completed;
        }

        return first.staffId
                .compareToIgnoreCase(
                        second.staffId);
    }

    private static class StaffRow {

        private final String staffId;
        private final String staffName;

        private int assigned;
        private int active;
        private int completed;
        private int rollbacks;

        private StaffRow(
                String staffId,
                String staffName) {

            this.staffId = staffId;
            this.staffName = staffName;
        }

        private void include(
                HousekeepingTask task) {

            assigned++;
            rollbacks += task.getRollbackCount();

            if (task.getCurrentStatus()
                    == RoomStatus.READY_FOR_CHECK_IN) {

                completed++;
            } else {
                active++;
            }
        }

        private String format() {
            return String.format(
                    "%-10s %-20s %8d %8d %9d %10d",
                    staffId,
                    staffName,
                    assigned,
                    active,
                    completed,
                    rollbacks);
        }
    }
}
