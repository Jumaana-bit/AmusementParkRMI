import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class Staff {
    public static final int MAX_Customers = 2;
    private List<Ride> rides;
    private ServerSocket serverSocket;
    private List<ClientHandler> clientHandlers;
    private AtomicBoolean isServerRunning;

    public Staff() {
        rides = createRides();
        clientHandlers = new ArrayList<>();
        isServerRunning = new AtomicBoolean(true);
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: java Staff <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);
        Staff staff = new Staff();

        // Start server in a new thread
        new Thread(() -> staff.startServer(portNumber)).start();

        // Handle server shutdown commands from staff input
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Type 'shutdown' to stop the rides:");
            String command = scanner.nextLine();
            if (command.equalsIgnoreCase("shutdown")) {
                staff.shutdownServer();
                break;
            }
        }
    }

    private List<Ride> createRides() {
        // Create and store the rides in a list
        List<Ride> rides = new ArrayList<>();
        rides.add(new Ride("Ferris Wheel", 50));
        rides.add(new Ride("Roller Coaster", 65));
        return rides;
    }

    private void startServer(int portNumber) {
        try {
            serverSocket = new ServerSocket(portNumber);
            System.out.println("Amusement Park Server is running on port " + portNumber);

            while (isServerRunning.get()) {
                Socket visitorSocket = serverSocket.accept();
                handleNewConnection(visitorSocket);
            }
        } catch (IOException e) {
            if (isServerRunning.get()) {
                System.out.println("Exception in server: " + e.getMessage());
            }
        }
    }

    private void handleNewConnection(Socket visitorSocket) throws IOException {
        if (ClientHandler.getConnectedClients() >= MAX_Customers) {
            rejectConnection(visitorSocket);
        } else {
            acceptConnection(visitorSocket);
        }
    }

    private void rejectConnection(Socket visitorSocket) throws IOException {
        visitorSocket.getOutputStream().write("The maximum occupancy for this ride has been reached. Please try again later.\n".getBytes());
        visitorSocket.close();
        System.out.println("Connection rejected because the ride is full.");
    }

    private void acceptConnection(Socket visitorSocket) {
        ClientHandler clientHandler = new ClientHandler(visitorSocket, rides);
        clientHandlers.add(clientHandler);
        clientHandler.start();
    }

    // Method to shut down the server
    private void shutdownServer() {
        isServerRunning.set(false);
        System.out.println("Shutting down the server...");

        // Close the server socket to stop accepting new clients
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }

            // Close all active client connections
            for (ClientHandler clientHandler : clientHandlers) {
                clientHandler.closeConnection();
            }
            System.out.println("All rides have been stopped.");
        } catch (IOException e) {
            System.out.println("Error during shutdown: " + e.getMessage());
        }
    }
}
