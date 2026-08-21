package Entity;

/**
 * 
 *
 * @author shujuntan
 */

/* Store the fixed basic information of the housekeeping staff */
public class HousekeepingStaff {

    private final String staffId;
    private final String staffName;
    private final String shift;

    public HousekeepingStaff(String staffId, String staffName, String shift) {
        if (blank(staffId) || blank(staffName) || blank(shift)) {
            throw new IllegalArgumentException( "Staff ID, name and shift are required.");
        }

        this.staffId = staffId.trim().toUpperCase();
        this.staffName = staffName.trim();
        this.shift = shift.trim();
    }

    public String getStaffId() {
        return staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public String getShift() {
        return shift;
    }

    private static boolean blank(String text) {
        return text == null || text.trim().isEmpty();
    }
}
