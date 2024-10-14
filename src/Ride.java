public class Ride {
    private String name;
    private int totalSeats;
    private int occupiedSeats;

    public Ride(String name, int totalSeats) {
        this.name = name;
        this.totalSeats = totalSeats;
        this.occupiedSeats = 0;
    }

    public String getName() {
        return name;
    }

    public boolean hasAvailableSeats() {
        return occupiedSeats < totalSeats;
    }

    public void addVisitor() {
        if (hasAvailableSeats()) {
            occupiedSeats++;
        }
    }

    public void removeVisitor() {
        if (occupiedSeats > 0) {
            occupiedSeats--;
        }
    }

    public int getAvailableSeats() {
        return totalSeats - occupiedSeats;
    }
}
