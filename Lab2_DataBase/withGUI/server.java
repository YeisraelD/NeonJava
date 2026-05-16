import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class server {
    public static void main(String[] args) {
        try{
            Registry registry = LocateRegistry.createRegistry(1099);
            remoteObj remoteobj = new remoteObj();
            registry.bind("myRemoteObject", remoteobj);
            System.out.println("remote object bind...");

        }catch(Exception e){
            System.err.println("error: " + e);
        }
    }
}