package Network_Programming.TCP;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class SimpleEchoClient {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 5000;

        System.out.println("[CLIENT] connecting to: " + host + ": " + port);
        try (Socket socket = new Socket(host, port)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // send daat to the server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// recive data from
                                                                                                   // the server

            Scanner scanner = new Scanner(System.in);
            System.out.println("[CLIENT] Connected type a message and exit to quite: ");

            while (true) {
                System.out.print("[CLiENT] message: ");
                String clientMessage = scanner.nextLine();
                out.println(clientMessage); // send it
                String response = in.readLine();

                System.out.println("[client] server response: " + response);

                if ("exit".equalsIgnoreCase(clientMessage)) {
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("client error: " + e.getMessage());
        }

    }
}