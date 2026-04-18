package Network_Programming.UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPReceiver {
    public static void main(String[] args) throws IOException {
        int port = 6000;

        // Create a socket to listen on port 6000
        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("[RECEIVER] Listening on port " + port);

            // Create a buffer (byte array) to hold incoming data
            byte[] buffer = new byte[1024];

            while (true) {
                // Prepare the packet 'envelope' to be filled
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                // This blocks until a packet arrives
                socket.receive(packet);

                // Convert the bytes back into a String
                String message = new String(packet.getData(), 0, packet.getLength());

                System.out.println("[RECEIVER] Received: " + message);

                if ("exit".equalsIgnoreCase(message))
                    break;
            }
        }
    }
}
