package Network_Programming.TCP;

import java.io.*;
import java.net.*;

public class SimpleEchoServer {
    public static void main(String[] args) {
        int port = 5000; // below 1024 are reserved for system services
        System.out.println("[SERVER] Starting on port " + port);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("[SERVER] waiting for a client....");// this stops the program and waitis a client to
                                                                    // connect

            try (Socket clienSocket = serverSocket.accept()) {
                System.out.println("[SERVER] client connected! ");
            }
        }

    }

}
