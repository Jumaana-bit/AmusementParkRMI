# Amusement Park RMI Service

A Java-based RMI service simulating an amusement park. It provides ride management, join/leave ride functionality, waitlist handling, and a graceful server shutdown.

## Features
- View available rides and seat availability.
- Join/leave rides or get added to the waitlist.
- Periodically checks and updates the waitlist.
- Graceful shutdown of the server.

## Requirements
- JDK 8 or higher.

## Getting Started

### 1. Compile the Code:
```bash
javac *.java

### 2. Start the RMI Registry
Before running the server or client, you must start the RMI registry. Run the following command in a terminal:
```bash
rmiregistry

### 3. Run the server
Replace <port-number> with the desired port number (e.g., 2000). Run the following command in a terminal:
```bash
java Staff <port-number> ```

### 3. Run the Visitor client
Replace <port-number> with the matching server port number (e.g., 2000). Run the following command in a terminal:
```bash
java Staff localhost <port-number>

## Sample Commands

- **View Rides**: Lists available rides with seat information.
- **Join Ride**: Adds a visitor to the ride or the waitlist if the ride is full.
- **Leave Ride**: Removes a visitor from the ride.
- **Shutdown**: Unbinds the RMI service and shuts down the server.



