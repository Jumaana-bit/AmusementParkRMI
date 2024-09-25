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

            String rideChoices;
            while ((rideChoices = stdIn.readLine()) != null) {
                out.println(rideChoices);
                System.out.println(in.readLine());
            }

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
