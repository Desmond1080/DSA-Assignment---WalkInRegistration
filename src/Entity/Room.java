package Entity;

/**
 *
 * @author shujuntan
 */

/*
 Represents a hotel room that can receive housekeeping tasks.
 Room information is fixed after the object is created.
*/
public class Room {

    private final String roomNumber;
    private final int floor;
    private final String roomType;

    public Room(String roomNumber, int floor, String roomType) {
        if (roomNumber == null || roomNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Room number is required.");
        }

        if (floor <= 0) {
            throw new IllegalArgumentException("Floor must be greater than zero.");
        }

        if (roomType == null || roomType.trim().isEmpty()) {
            throw new IllegalArgumentException("Room type is required.");
        }

        this.roomNumber = roomNumber.trim().toUpperCase();
        this.floor = floor;
        this.roomType = roomType.trim();
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
}