import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class server {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            remoteObj remobj = new remoteObj(); //skeleton
            registry.bind("myRemoteObject", remobj);
            System.out.println("remote object bound to registry");
        } catch (Exception e) {
            System.err.println("error: " + e);
            e.printStackTrace();
        }
    }
}