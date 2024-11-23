import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Scanner;

public class Staff extends UnicastRemoteObject implements AmusementParkRMI {
    public static List<Ride> rides;

    private final List<ClientHandler> clientHandlers;
    private final AtomicBoolean isServerRunning;
    private final ScheduledExecutorService scheduler;

    public Staff() throws RemoteException {
        super(); // Call the superclass constructor
        rides = createRides(); // Initialize the rides list
        clientHandlers = new ArrayList<>();
        isServerRunning = new AtomicBoolean(true);

        // Schedule periodic checks for vacant seats
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::checkRidesForVacancy, 1, 1, TimeUnit.MINUTES);
    }

    @Override
    public List<String> getAvailableRides() throws RemoteException {
        List<String> availableRides = new ArrayList<>();
        for (Ride ride : rides) {
            int availableSeats = ride.getCapacity() - ride.getCurrentVisitors();
            availableRides.add(ride.getName() + " (" + availableSeats + " seats available)");
        }
        return availableRides;
    }

    @Override
    public String joinRide(String rideName) throws RemoteException {
        for (Ride ride : rides) {
            if (ride.getName().equalsIgnoreCase(rideName)) {
                if (ride.hasAvailableSeats()) {
                    ride.addVisitor();
                    return "Successfully joined " + rideName + "!";
                } else {
                    ride.addToWaitlist(null); // null for RMI clients, actual ClientHandler will be added in other parts
                    return rideName + " is full. You've been added to the waitlist.";
                }
            }
        }
        return "Ride not found.";
    }

    @Override
    public String leaveRide(String rideName) throws RemoteException {
        for (Ride ride : rides) {
            if (ride != null && ride.getName().equalsIgnoreCase(rideName)) {
                if (ride.getCurrentVisitors() > 0) {
                    ride.removeVisitor();
                    return "Left the ride: " + rideName;
                } else {
                    return "You are not on the ride: " + rideName;
                }
            }
        }
        return "Ride not found.";
    }

    @Override
    public String getWaitlist(String rideName) throws RemoteException {
        for (Ride ride : rides) {
            if (ride.getName().equalsIgnoreCase(rideName)) {
                return "Waitlist size for " + rideName + ": " + ride.getWaitlist().size();
            }
        }
        return "Ride not found.";
    }

    private List<Ride> createRides() {
        List<Ride> rides = new ArrayList<>();
        rides.add(new Ride("Roller Coaster", 2));
        rides.add(new Ride("Ferris Wheel", 8));
        rides.add(new Ride("Bumper Cars", 10));
        return rides;
    }

    private void checkRidesForVacancy() {
        for (Ride ride : rides) {
            ride.checkWaitlist();
        }
    }

    // Method to gracefully shut down the server by unbinding the service
    private static void shutdownServer(int portNumber, ScheduledExecutorService scheduler) {
        try {
            // Unbind the service from the RMI registry
            Registry registry = LocateRegistry.getRegistry(portNumber);
            registry.unbind("AmusementParkService");
            System.out.println("Amusement Park RMI Service has been unbound.");

            // Shutdown scheduler and threads
            if (scheduler != null && !scheduler.isShutdown()) {
                scheduler.shutdown();
                System.out.println("Scheduler service stopped.");
            }

            System.out.println("Server shutdown complete.");
        } catch (Exception e) {
            System.err.println("Error shutting down server: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: java Staff <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);
        Staff staff = new Staff();

        // Start RMI registry
        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(portNumber);
            Naming.rebind("//localhost:" + portNumber + "/AmusementParkService", staff);
            System.out.println("Amusement Park RMI Service is running...");
        } catch (Exception e) {
            System.err.println("RMI Server exception: " + e.toString());
            e.printStackTrace();
        }

        // Server will continue running until a shutdown command is given
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type 'shutdown' to stop the server.");

        while (true) {
            String command = scanner.nextLine();
            if ("shutdown".equalsIgnoreCase(command)) {
                shutdownServer(portNumber, staff.scheduler);  // Shutdown using the correct port number
                break; // Break the loop and stop the server
            }
        }

        // Clean up and exit
        System.exit(0);
    }
}
