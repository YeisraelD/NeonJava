package Lab3_RMI;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface remoteInterface extends Remote {

    public String hello(String s) throws RemoteException;
}