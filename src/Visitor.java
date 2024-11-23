import java.rmi.Naming;
import java.util.Scanner;

public class Visitor {
    private static String currentRide = null;  // Track the current ride the visitor is on

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java Visitor <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try {
            // Attempting to connect to the RMI service
            System.out.println("Attempting to connect to RMI service at: " + "//" + hostName + ":" + portNumber + "/AmusementParkService");
            AmusementParkRMI service = (AmusementParkRMI) Naming.lookup("//" + hostName + ":" + portNumber + "/AmusementParkService");
            System.out.println("Connected to Amusement Park!");

            // Use try-with-resources to ensure scanner is closed
            try (Scanner scanner = new Scanner(System.in)) {
                while (true) {
                    System.out.println("1. View available rides");
                    System.out.println("2. Join a ride");
                    System.out.println("3. Leave a ride");
                    System.out.println("4. Get waitlist");
                    System.out.println("5. Exit");

                    // Validate the user's choice
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    if (choice < 1 || choice > 5) {
                        System.out.println("Invalid option. Please choose between 1 and 5.");
                        continue;
                    }

                    switch (choice) {
                        case 1:
                            System.out.println(service.getAvailableRides());
                            break;
                        case 2:
                            System.out.print("Enter ride name: ");
                            String rideName = scanner.nextLine();
                            System.out.println(service.joinRide(rideName));  // Pass 'this' as the second argument
                            currentRide = rideName;  // Store the selected ride
                            break;
                        case 3:
                            System.out.print("Enter ride name: ");
                            rideName = scanner.nextLine();
                            System.out.println(service.leaveRide(rideName));
                            currentRide = null;  // Clear the current ride when leaving
                            break;
                        case 4:
                            System.out.print("Enter ride name: ");
                            rideName = scanner.nextLine();
                            System.out.println(service.getWaitlist(rideName));
                            break;
                        case 5:
                            System.out.println("Exiting...");
                            if (currentRide != null) {
                                service.leaveRide(currentRide);  // Leave the current ride if the visitor exists
                            }
                            return;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("RMI Client exception: " + e);
            e.printStackTrace();
        }
    }
}



