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

        // Notify the next visitor in the waitlist if there's one
        if (!waitlist.isEmpty()) {
            ClientHandler nextVisitor = waitlist.poll();  // Get the next visitor from the waitlist
            nextVisitor.notifyRideAvailable(name);  // Notify them that a seat is available
        }
    }

    public void addToWaitlist(ClientHandler clientHandler) {
        waitlist.offer(clientHandler);
    }

    public Queue<ClientHandler> getWaitlist() {
        return waitlist;  // Expose the waitlist
    }
}

