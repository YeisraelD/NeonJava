package Network_Programming.Advanced;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

// * This server handles multiple clients using a SINGLE thread.
 
public class NIOServer {
    public static void main(String[] args) {
        int port = 7000;

        try {
            // 1. Open a Selector (The "Traffic Controller")
            Selector selector = Selector.open();

            // 2. Open a ServerSocketChannel
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress(port));
            
            // 3. Set to NON-BLOCKING mode
            serverChannel.configureBlocking(false);

            // 4. Register the channel with the Selector for "Accept" events
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("[NIO SERVER] Started on port " + port);

            while (true) {
                // Wait for events (this blocks until at least one channel is ready)
                selector.select();

                // Get all ready keys
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKeys.iterator();

                while (iter.hasNext()) {
                    SelectionKey key = iter.next();

                    if (key.isAcceptable()) {
                        // Handle new connection
                        registerClient(selector, serverChannel);
                    }

                    if (key.isReadable()) {
                        // Handle data coming from client
                        echoData(key);
                    }

                    // Remove the key from the set so we don't process it twice
                    iter.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void registerClient(Selector selector, ServerSocketChannel serverChannel) throws IOException {
        SocketChannel client = serverChannel.accept();
        client.configureBlocking(false);
        // Register client for "Read" events
        client.register(selector, SelectionKey.OP_READ);
        System.out.println("[NIO SERVER] New client connected: " + client.getRemoteAddress());
    }

    private static void echoData(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(256);
        
        int bytesRead = client.read(buffer);
        
        if (bytesRead == -1) {
            System.out.println("[NIO SERVER] Client disconnected.");
            client.close();
        } else {
            // Flip buffer from "writing into it" to "reading from it"
            buffer.flip();
            
            String msg = new String(buffer.array(), 0, bytesRead).trim();
            System.out.println("[NIO SERVER] Echoing: " + msg);
            
            // Send back to client
            client.write(buffer);
            buffer.clear();
        }
    }
}
