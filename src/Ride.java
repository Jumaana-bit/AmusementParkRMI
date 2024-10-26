import java.util.LinkedList;
import java.util.Queue;

public class Ride {
    private String name;
    private int capacity;
    private int currentVisitors;
    private Queue<ClientHandler> waitlist;

    public Ride(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        this.currentVisitors = 0;
        this.waitlist = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public boolean hasAvailableSeats() {
        return currentVisitors < capacity;
    }

    public void addVisitor() {
        currentVisitors++;
    }

    public void removeVisitor() {
        if (currentVisitors > 0) {
            currentVisitors--;
        }

        // Check if there is anyone on the waitlist to notify
        checkWaitlist();
    }

    public void addToWaitlist(ClientHandler clientHandler) {
        waitlist.offer(clientHandler);
    }

    // Method to check the waitlist and notify the next visitor if a seat is available
    public void checkWaitlist() {
        if (!waitlist.isEmpty() && hasAvailableSeats()) {
            ClientHandler nextVisitor = waitlist.poll();
            nextVisitor.notifyRideAvailable(name); // Notify the visitor they can join
            addVisitor(); // Increment the visitor count for the ride
        }
    }


    public Queue<ClientHandler> getWaitlist() {
        return waitlist;  // Expose the waitlist
    }

}

