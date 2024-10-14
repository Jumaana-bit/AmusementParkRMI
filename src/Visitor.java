import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Visitor {

    private JFrame frame;
    private JComboBox<String> rideDropdown;
    private JTextArea responseArea;
    private Socket visitorSocket;
    private PrintWriter out;
    private BufferedReader in;

    public Visitor(String hostName, int portNumber) {
        initializeGUI();
        connectToServer(hostName, portNumber);
    }

    private void initializeGUI() {
        // Create the main window
        frame = new JFrame("Amusement Park Ride Selector");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a panel to hold the dropdown and button
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        // Create a label
        JLabel rideLabel = new JLabel("Select a ride:");
        panel.add(rideLabel);

        // Create a dropdown for ride selection
        rideDropdown = new JComboBox<>();
        panel.add(rideDropdown);

        // Create a button to submit the ride choice
        JButton selectButton = new JButton("Choose Ride");
        panel.add(selectButton);

        // Create a text area to show responses from the server
        responseArea = new JTextArea(5, 20);
        responseArea.setLineWrap(true);
        responseArea.setWrapStyleWord(true);
        responseArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(responseArea);

        // Add the panel and the response area to the frame
        frame.add(panel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Add action listener for the button
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedRide = (String) rideDropdown.getSelectedItem();
                sendRideChoice(selectedRide);
            }
        });

        frame.setVisible(true);
    }

    // Connect to the server and receive available rides
    private void connectToServer(String hostName, int portNumber) {
        try {
            visitorSocket = new Socket(hostName, portNumber);
            out = new PrintWriter(visitorSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(visitorSocket.getInputStream()));

            // Read available rides from server
            String staffMessage;
            while ((staffMessage = in.readLine()) != null) {
                if (staffMessage.equals("END")) {
                    break;  // End of rides list
                }
                rideDropdown.addItem(staffMessage.replace("Here are the available rides: ", ""));
            }

        } catch (UnknownHostException e) {
            responseArea.append("Don't know about host: " + hostName + "\n");
        } catch (IOException e) {
            responseArea.append("Couldn't get I/O for the connection to: " + hostName + "\n");
        }
    }

    // Send the selected ride to the server
    private void sendRideChoice(String rideChoice) {
        if (out != null) {
            out.println(rideChoice);

            // Receive confirmation from server
            try {
                String serverResponse = in.readLine();
                responseArea.append("Server response: " + serverResponse + "\n");
            } catch (IOException e) {
                responseArea.append("Error receiving response from server.\n");
            }
        }
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java Visitor <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        new Visitor(hostName, portNumber);
    }
}