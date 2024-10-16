import java.util.LinkedList;
import java.util.Queue;

public class Ride {
    private String name;
    private int totalSeats;
    private int availableSeats;
    private Queue<ClientHandler> waitlist;

    public Ride(String name, int totalSeats) {
        this.name = name;
        this.totalSeats = totalSeats;
        this.availableSeats = 0;
        this.waitlist = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public boolean hasAvailableSeats() {
        return availableSeats < totalSeats;
    }

    public void addVisitor() {
        if (hasAvailableSeats()) {
            availableSeats--;
        }
    }

    public void removeVisitor() {
        if (availableSeats < totalSeats) {
            availableSeats++;
            // Notify next client on the waitlist if available
            notifyNextVisitor();
        }
    }

    public void addToWaitlist(ClientHandler clientHandler) {
        waitlist.offer(clientHandler);
    }

    public void notifyNextVisitor() {
        if (!waitlist.isEmpty()) {
            ClientHandler nextClient = waitlist.poll();
            nextClient.notifyRideAvailable(this.name);
        }
    }
}
