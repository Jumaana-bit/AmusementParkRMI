import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

class ClientHandler extends Thread {
    private Socket visitorSocket;
    public static AtomicInteger connectedClients = new AtomicInteger(0);
    private List<Ride> rides;

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

        PrintWriter out = new PrintWriter(visitorSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(visitorSocket.getInputStream()));

        sendAvailableRides(out);
        receiveRideChoice(in, out);
    }

    private void sendAvailableRides(PrintWriter out) {
        // Send the list of rides to the visitor
        for (Ride ride : rides) {
            out.println("Here are the available rides: " + ride.getName());
        }
        // Send a termination message to signal the end of the rides list
        out.println("END");
    }

    private void receiveRideChoice(BufferedReader in, PrintWriter out) throws IOException {
        // Receive the ride choice from the visitor
        String rideChoice = in.readLine();

        // If rideChoice is null or empty, handle the case
        if (rideChoice == null || rideChoice.trim().isEmpty()) {
            out.println("Invalid choice, please select a valid ride.");
            return;
        }

        System.out.println("Visitor chose: " + rideChoice);

        // Send a confirmation response back to the visitor
        out.println("You have chosen " + rideChoice + ", enjoy the ride!");
    }
}

