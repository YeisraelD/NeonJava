package Network_Programming.TCP;

import java.io.*;
import java.net.*;

public class simpleEchoServer {
    public static void main(String[] args) {
        int port = 5000; // below 1024 are reserved for system services
        System.out.println("[SERVER] Starting on port " + port);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("[SERVER] waiting for a client....");// this stops the program and waitis a client to
                                                                    // connect

            try (Socket clienSocket = serverSocket.accept()) {
                System.out.println("[SERVER] client connected! ");

                BufferedReader in = new BufferedReader(new InputStreamReader (clienSocket.getInputStream()));// read the client data
                PrintWriter out = new PrintWriter(clienSocket.getOutputStream(), true); //send data to the client 
                
                String clientMessage;
                while((clientMessage = in.readLine()) != null){
                    System.out.println("[SERVER] Recived: " + clientMessage);

                    out.println("Echo: " + clientMessage);

                    if("exit".equalsIgnoreCase(clientMessage)){
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
