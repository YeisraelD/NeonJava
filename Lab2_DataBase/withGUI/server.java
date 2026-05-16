package Lab2_DataBase.withGUI;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class server {
    public static void main(String[] args) {
        try{
            // initialize database tables before starting the server
            stu.createTable();
            tea.createTable();

            Registry registry = LocateRegistry.createRegistry(1099);
            remoteObj remoteobj = new remoteObj();
            registry.bind("myRemoteObject", remoteobj);
            System.out.println("remote object bind...");

        }catch(Exception e){
            System.err.println("error: " + e);
        }
    }
}