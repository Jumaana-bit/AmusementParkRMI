import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Staff {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: java Staff <port number>");
            System.exit(1);
        }
        int portNumber = Integer.parseInt(args[0]);
        ServerSocket server = new ServerSocket(Integer.parseInt(args[0]));

        while (true) {
            Socket visitorSocket = server.accept();
            System.out.println("Visitor connected from IP: " + visitorSocket.getInetAddress().getHostAddress() + ", Port:" + visitorSocket.getPort());
            System.out.println("Amusement Park Server is running");

            PrintWriter out = new PrintWriter(visitorSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(visitorSocket.getInputStream()));

            Ride ride1 = new Ride("Ferris Wheel", 50);
            Ride ride2 = new Ride("Roller Coaster", 65);

            //list all my rides at once
            //Store the rides in a list
            List<Ride> rides = new ArrayList<>();
            for (Ride ride : rides) {
                out.println("Here are the the available rides " + ride.getName());
            }


        }
    }
}
