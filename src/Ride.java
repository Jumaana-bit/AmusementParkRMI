import java.util.LinkedList;
import java.util.List;

public class Ride {
    private String name;
    private int capacity;
    private int currentVisitors;
    private List<ClientHandler> waitlist;

    public Ride(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        this.currentVisitors = 0;
        this.waitlist = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCurrentVisitors() {
        return currentVisitors;
    }

    public boolean hasAvailableSeats() {
        return currentVisitors < capacity;
    }

    public List<ClientHandler> getWaitlist() {
        return waitlist;
    }

    // Add a visitor to the ride
    public void addVisitor() {
        if (hasAvailableSeats()) {
            currentVisitors++;  // Increase current visitor count when a seat is available
            System.out.println("A visitor has joined the ride: " + name);
        } else {
            // If the ride is full, add to the waitlist
            ClientHandler visitor = new ClientHandler(Staff.rides);  // This can be passed when creating ClientHandler
            waitlist.add(visitor);
            System.out.println("The ride is full. Adding visitor to the waitlist.");
        }
    }

    // Remove a visitor from the ride
    public void removeVisitor() {
        if (currentVisitors > 0) {
            currentVisitors--;
            System.out.println("A visitor has left the ride: " + name);

            if (waitlist != null && !waitlist.isEmpty()) {
                ClientHandler nextVisitor = waitlist.remove(0);
                if (nextVisitor != null) {
                    currentVisitors++;
                    nextVisitor.notifyRideAvailable(name);
                    System.out.println("Next visitor in the waitlist has joined the ride: " + name);
                } else {
                    System.out.println("Next visitor in waitlist is null...");
                }
            }
        } else {
            System.out.println("No visitors on the ride to remove.");
        }
    }

    // Add a visitor to the waitlist (can be used when the ride is full)
    public void addToWaitlist(ClientHandler visitor) {
        waitlist.add(visitor);
    }

    // Check if anyone on the waitlist can be added if a seat becomes available
    public void checkWaitlist() {
        if (hasAvailableSeats() && !waitlist.isEmpty()) {
            ClientHandler nextVisitor = waitlist.remove(0);  // Remove the first person from the waitlist
            currentVisitors++;  // Increase the visitor count
            nextVisitor.notifyRideAvailable(name);  // Notify the visitor that they can join
            System.out.println("Visitor added from waitlist to " + name);
        }
    }
}
