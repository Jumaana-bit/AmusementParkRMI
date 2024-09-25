import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Visitor {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println(
                    "Usage: java Visitor <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try {
            Socket visitorSocket = new Socket(hostName, portNumber);

            PrintWriter out = new PrintWriter(visitorSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(visitorSocket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            //print out staff's message
            String staffMessage;
            while ((staffMessage = in.readLine()) != null) {
                if (staffMessage.equals("END")) {
                    // End of rides list, break out of the loop
                    break;
                }
                System.out.println(staffMessage);
            }

            // Ask for user input after displaying the rides
            System.out.println("Please type the name of the ride you want to choose:");
            String rideChoice = stdIn.readLine();

            // Send the selected ride to the server
            out.println(rideChoice);

            // Print out the server's confirmation response
            String serverConfirmation = in.readLine();
            System.out.println("Server response: " + serverConfirmation);

        }catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }
    }
}
