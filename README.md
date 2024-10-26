# Amusement Park Ride Selector

This project is a client-server Java application that simulates a ride selection and waitlist system for an amusement park. Visitors can connect to the server to select rides, and if a ride is full, they are added to a waitlist. The server periodically checks for vacancies and notifies waitlisted visitors when they can join the ride.

## Table of Contents

- [Features](#features)
- [Project Structure](#project-structure)
- [Setup and Usage](#setup-and-usage)
  - [Prerequisites](#prerequisites)
  - [Running the Server](#running-the-server)
  - [Running the Visitor Client](#running-the-visitor-client)
- [Components](#components)
- [Future Improvements](#future-improvements)

## Features

- **Ride Selection**: Visitors can select a ride from the available options.
- **Waitlist System**: If a ride reaches maximum occupancy, the visitor is added to a waitlist.
- **Automated Notifications**: The server checks for vacant seats every 15 minutes and automatically notifies visitors in the waitlist.
- **Shutdown Command**: Staff can manually shut down the server, which stops accepting new visitors and closes existing connections.

## Project Structure

- `Staff`: Manages the amusement park server, ride availability, and client connections.
- `Visitor`: GUI client that allows a visitor to connect to the server, choose a ride, and receive responses.
- `ClientHandler`: Handles communication with each connected visitor, manages ride waitlists, and sends notifications.
- `Ride`: Represents a single ride with a name, capacity, current visitor count, and waitlist.

## Setup and Usage

### Prerequisites

- **Java Development Kit (JDK)**: This project requires JDK 8 or higher. You can download it from [here](https://www.oracle.com/java/technologies/javase-downloads.html).
- **IDE (Optional)**: Using an IDE like IntelliJ IDEA or Eclipse is recommended for easier code management.

### Running the Server

1. Open a terminal or command prompt.
2. Navigate to the project directory.
3. Compile the code using:
    ```bash
    javac Staff.java ClientHandler.java Ride.java
    ```
4. Start the server with:
    ```bash
    java Staff <port-number>
    ```
   Replace `<port-number>` with the port you want the server to listen on (e.g., `8080`).
   
   - Once the server is running, it will periodically check for available seats on rides and notify visitors in the waitlist accordingly.

### Running the Visitor Client

1. Open another terminal or command prompt.
2. Compile the `Visitor` class:
    ```bash
    javac Visitor.java
    ```
3. Run the Visitor client with:
    ```bash
    java Visitor <host-name> <port-number>
    ```
   Replace `<host-name>` with the server's hostname (e.g., `localhost`) and `<port-number>` with the port the server is listening on.
   
   - A GUI window will appear, allowing the visitor to select a ride and receive server responses.

## Components

1. **Staff Class**: Manages the server, initiates rides, handles client connections, and includes a scheduled task to check for ride vacancies every 15 minutes.
2. **Visitor Class**: Represents a client (visitor) with a GUI for selecting rides and viewing responses from the server.
3. **ClientHandler Class**: Dedicated thread to handle each visitor’s connection, manage their ride choice, and handle waitlist notifications.
4. **Ride Class**: Manages individual ride details, including the current number of visitors and a waitlist.

## Future Improvements

- **Dynamic Ride Creation**: Allow rides to be added or removed dynamically.
- **Customizable Check Interval**: Make the vacancy check interval configurable.
- **Enhanced Visitor Management**: Add persistence for waitlists or allow visitors to cancel their position in the waitlist.
- **Enhanced GUI**: Improve the Visitor GUI to show real-time status updates on waitlist position.

## License

This project is open-source and available for educational purposes.
