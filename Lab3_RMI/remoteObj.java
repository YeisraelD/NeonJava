package Lab3_RMI;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class remoteObj extends UnicastRemoteObject implements remoteInterface {
    public remoteObj() throws RemoteException {
        super();
    }

    public String hello(String s) throws RemoteException {
        return "hello " + s;
    }

    }

    
    
        
    

    
    
    
    



