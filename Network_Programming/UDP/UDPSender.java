package Network_Programming.UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UDPSender {
    public static void main(String[] args) throws IOException {
        String host = "127.0.0.1";
        int port = 6000;

        try (DatagramSocket socket = new DatagramSocket();
                Scanner scanner = new Scanner(System.in)) {

            InetAddress address = InetAddress.getByName(host);
            System.out.println("[SENDER] Ready to send packets to " + host + ":" + port);

            while (true) {
                System.out.print("> ");
                String message = scanner.nextLine();

                // Convert our message to bytes
                byte[] buffer = message.getBytes();

                // Create the packet with: data, length, destination address, and port
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);

                // Send it!
                socket.send(packet);

                if ("exit".equalsIgnoreCase(message))
                    break;
            }
        }
    }
}
