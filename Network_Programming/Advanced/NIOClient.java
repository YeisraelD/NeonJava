package Network_Programming.Advanced;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * Advanced Java Networking: NIO Client
 */
public class NIOClient {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 7000;

        try (SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(host, port))) {
            System.out.println("[NIO CLIENT] Connected to server.");
            
            Scanner scanner = new Scanner(System.in);
            ByteBuffer buffer = ByteBuffer.allocate(256);

            while (true) {
                System.out.print("Enter message (or 'exit'): ");
                String message = scanner.nextLine();

                if ("exit".equalsIgnoreCase(message)) {
                    break;
                }

                // Send message to server
                buffer.clear();
                buffer.put(message.getBytes());
                buffer.flip();
                socketChannel.write(buffer);

                // Receive response from server
                buffer.clear();
                int bytesRead = socketChannel.read(buffer);
                if (bytesRead > 0) {
                    buffer.flip();
                    String response = new String(buffer.array(), 0, bytesRead);
                    System.out.println("[NIO CLIENT] Server echoed: " + response);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
