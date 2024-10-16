import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.LinkedList;
import java.util.Queue;


public class RideTest {

    private Ride ferrisWheel;

    @BeforeEach
    public void setUp() {
        ferrisWheel = new Ride("Ferris Wheel", 50); // Set up a ride with 50 seats
    }

    @Test
    public void testHasAvailableSeats() {
        assertTrue(ferrisWheel.hasAvailableSeats(), "Ride should have available seats at the start.");
    }

    @Test
    public void testAddVisitorFull() {
        // Simulate filling all the seats
        for (int i = 0; i < 50; i++) {
            ferrisWheel.addVisitor();
        }
        assertFalse(ferrisWheel.hasAvailableSeats(), "Ride should be full.");
    }}

   /* @Test
    public void testAddToWaitlist() {
        ClientHandler client1 = new ClientHandler(null, new LinkedList<>());
        ferrisWheel.addToWaitlist(client1);
        assertEquals(1, ferrisWheel.getWaitlistSize(), "Waitlist should have one client.");
    }

    @Test
    public void testNotifyNextVisitorFromWaitlist() {
        ClientHandler client1 = new ClientHandler(null, new LinkedList<>());
        ferrisWheel.addToWaitlist(client1);
        ferrisWheel.notifyNextVisitor();
        assertEquals(0, ferrisWheel.getWaitlistSize(), "Waitlist should be empty after notifying.");
    }
}*/
