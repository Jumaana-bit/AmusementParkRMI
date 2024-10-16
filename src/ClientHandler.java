import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientHandler extends Thread {
    private Socket visitorSocket;
    public static AtomicInteger connectedClients = new AtomicInteger(0);
    private List<Ride> rides;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket socket, List<Ride> rides) {
        this.visitorSocket = socket;
        this.rides = rides;
    }

    public static int getConnectedClients() {
        return connectedClients.get();
    }

    @Override
    public void run() {
        try {
            connectedClients.incrementAndGet();
            out = new PrintWriter(visitorSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(visitorSocket.getInputStream()));
            handleClient();
        } catch (IOException e) {
            System.out.println("Exception in client handler: " + e.getMessage());
        } finally {
            connectedClients.decrementAndGet();
            try {
                visitorSocket.close();
            } catch (IOException e) {
                System.out.println("Error closing socket: " + e.getMessage());
            }
        }
    }

    private void handleClient() throws IOException {
        System.out.println("Visitor connected from IP: " + visitorSocket.getInetAddress().getHostAddress() + ", Port:" + visitorSocket.getPort());

        sendAvailableRides(out);
        receiveRideChoice(in, out);
    }

    private void sendAvailableRides(PrintWriter out) {
        // Send the list of rides to the visitor
        for (Ride ride : rides) {
            out.println("Here are the available rides: " + ride.getName());
        }
        out.println("END");
    }

    private void receiveRideChoice(BufferedReader in, PrintWriter out) throws IOException {
        // Receive the ride choice from the visitor
        String rideChoice = in.readLine();
        System.out.println("Visitor chose: " + rideChoice);

        // Find the selected ride
        Ride selectedRide = null;
        for (Ride ride : rides) {
            if (ride.getName().equalsIgnoreCase(rideChoice)) {
                selectedRide = ride;
                break;
            }
        }

        if (selectedRide != null) {
            // Check if the ride has available seats
            if (selectedRide.hasAvailableSeats()) {
                selectedRide.addVisitor();
                out.println("You have chosen " + rideChoice + ". Enjoy the ride!");
            } else {
                // Add to waitlist if the ride is full
                selectedRide.addToWaitlist(this);  // Add this client to the waitlist
                out.println("The " + rideChoice + " is full. You have been added to the waitlist.");
                // Wait for notification when a seat is available
            }
        } else {
            out.println("Invalid ride choice.");
        }
    }

    public void notifyRideAvailable(String rideName) {
        out.println("A seat is now available on the " + rideName + ". You can now join the ride!");
    }

    public void closeConnection() {
        try {
            if (visitorSocket != null && !visitorSocket.isClosed()) {
                visitorSocket.close();
            }
        } catch (IOException e) {
            System.out.println("Error closing client connection: " + e.getMessage());
        }
    }
}

