import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface AmusementParkRMI extends Remote {
    List<String> getAvailableRides() throws RemoteException;
    public String joinRide(String rideName) throws RemoteException;
    public String leaveRide(String rideName) throws RemoteException;
    String getWaitlist(String rideName) throws RemoteException;
}
