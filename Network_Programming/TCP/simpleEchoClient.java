package Network_Programming.TCP;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class SimpleEchoClient {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 5000;

        Syetem.out.println("[CLIENT] connecting to: " + host + ": " + port);
        try(Socket socket = new socket(host, port)){
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true); //send daat to the server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));//recive data from the server

            Scanner scanner = new Scanner(System.in);
            System.out.print("[CLIENT] Connected type a message and exit to quite: ")
            
        }


    }
}