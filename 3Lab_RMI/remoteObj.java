import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class remoteObj extends UnicastRemoteObject implements remoteInterface {
    public remoteObj() throws RemoteException {
        super();
    }

    public String hello(String s) throws RemoteException {
        return "hello " + s;
    }
}
