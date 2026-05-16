package Lab3_RMI;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class client {
    public static void main(String[] args){
        try{
            remoteInterface stub = (remoteInterface) Naming.lookup("rmi://localhost/myRemoteObject");
            String result = stub.hello(" Yeisrael");
            System.out.println(result);

        }catch (Exception e){
            System.err.println("error: " + e);

        }
    }
}