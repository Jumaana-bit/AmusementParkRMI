import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Staff {
    public static final int MAX_Customers = 2;
    private List<Ride> rides;

    public Staff() {
        rides = createRides();
    }
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: java Staff <port number>");
            System.exit(1);
        }
        int portNumber = Integer.parseInt(args[0]);
        new Staff().startServer(portNumber);
    }

    private List<Ride> createRides() {
        // Create and store the rides in a list
        List<Ride> rides = new ArrayList<>();
        rides.add(new Ride("Ferris Wheel", 50));
        rides.add(new Ride("Roller Coaster", 65));
        return rides;
    }

    private void startServer(int portNumber) {
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Amusement Park Server is running on port " + portNumber);

            while (true) {
                Socket visitorSocket = serverSocket.accept();
                handleNewConnection(visitorSocket);
            }
        } catch (IOException e) {
            System.out.println("Exception in server: " + e.getMessage());
        }
    }

    private void handleNewConnection(Socket visitorSocket) throws IOException {
        // Synchronize the check to prevent race conditions
        synchronized (ClientHandler.connectedClients) {
            if (ClientHandler.getConnectedClients() >= MAX_Customers) {
                rejectConnection(visitorSocket);
            } else {
                acceptConnection(visitorSocket);
            }
        }
    }

    private void rejectConnection(Socket visitorSocket) throws IOException {
        DataOutputStream dos = new DataOutputStream(visitorSocket.getOutputStream());
        dos.writeBytes("The maximum occupancy for this ride has been reached. Please try again later.\n");
        visitorSocket.close();
        System.out.println("Connection is rejected because the ride is closed.");
    }

    private void acceptConnection(Socket visitorSocket) {
        // Increment the client count before creating a thread to ensure capacity is respected
        ClientHandler.connectedClients.incrementAndGet();
        ClientHandler clientHandler = new ClientHandler(visitorSocket, rides);
        clientHandler.start();
    }
}
