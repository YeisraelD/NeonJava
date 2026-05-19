package Lab2_DataBase.withGUI;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class server {
    public static void main(String[] args) {
        List<PrintWriter> clientList = new ArrayList<>();
        try {

            Registry registry = LocateRegistry.createRegistry(1099);
            remoteObj remoteobj = new remoteObj();
            registry.bind("myRemoteObject", remoteobj);
            System.out.println("remote object bind...");

            new Thread(() -> {
                try (ServerSocket serverSocket = new ServerSocket(8000)) {
                    while (true) {
                        Socket clientSocket = serverSocket.accept();
                        remoteobj.addTCPClient(clientSocket);
                    }

                } catch (Exception e) {
                    System.err.println("error: " + e);

                }

            }).start();

        } catch (Exception e) {
            System.err.println("error: " + e);
        }
    }
}