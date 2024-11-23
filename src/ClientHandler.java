import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;
import java.io.Serializable;

public class ClientHandler extends Thread implements Serializable {
    public static AtomicInteger connectedClients = new AtomicInteger(0);
    private List<Ride> rides;

    public ClientHandler(List<Ride> rides) {
        this.rides = rides;
    }

    public static int getConnectedClients() {
        return connectedClients.get();
    }

    private Ride currentRide;

    public Ride getCurrentRide() {
        return currentRide;
    }

    public void setCurrentRide(Ride ride) {
        this.currentRide = ride;
    }

    public boolean isOnARide() {
        return currentRide != null;
    }

    @Override
    public void run() {
        connectedClients.incrementAndGet();
        try {
            handleClient();
        } finally {
            connectedClients.decrementAndGet();
        }
    }

    private void handleClient() {
        // Simulate interaction with rides
        sendAvailableRides();
        // Example: simulate a ride choice and processing
        String rideChoice = "Roller Coaster"; // This can be simulated or replaced with RMI inputs
        processRideChoice(rideChoice);
    }

    private void sendAvailableRides() {
        System.out.println("Here are the available rides:");
        for (Ride ride : rides) {
            System.out.println("- " + ride.getName());
        }
    }

    private void processRideChoice(String rideChoice) {
        for (Ride ride : rides) {
            if (ride.getName().equalsIgnoreCase(rideChoice)) {
                if (ride.hasAvailableSeats()) {
                    ride.addVisitor();  // Pass the current ClientHandler object
                    System.out.println("You have joined the " + rideChoice + ". Enjoy!");
                } else {
                    ride.addToWaitlist(this); // Add to waitlist
                    System.out.println("The " + rideChoice + " is full. You have been added to the waitlist.");
                }
                return;
            }
        }
        System.out.println("Invalid ride choice.");
    }


    public void notifyRideAvailable(String rideName) {
        System.out.println("A seat is now available on the " + rideName + ". You can now join the ride!");
    }
}
