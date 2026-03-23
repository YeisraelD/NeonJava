package Implementation_Pipes_and_Signals;
 
import java.io.*;

//   demonstrating the use of PipedInputStream and PipedOutputStream
//   for communication between threads.

public class Pips {
    public static void main(String[] args) throws IOException {
        // Create a PipedInputStream and a PipedOutputStream
        // We connect the output stream to the input stream
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);

        // Writing to a pipe should be done in a separate thread to avoid deadlock.
        // PipedInputStream.read() blocks until data is available in the connected output stream.
        Thread writerThread = new Thread(() -> {
            try {
                System.out.println("[Writer] Writing 'Hello, world!' to the pipe...");
                out.write("Hello, world!".getBytes());
                out.flush();
                out.close(); // Signals EOF for the reader
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        writerThread.start();

        // Read the data from the input stream, yo yo , yoyo yyyyyyyoooooo, relax, relix, relax 
        System.out.println("[Reader] Waiting for data from the pipe...");
        byte[] buffer = new byte[1024];
        int bytesRead = in.read(buffer);
        
        if (bytesRead != -1) {
            String message = new String(buffer, 0, bytesRead);
            System.out.println("[Reader] Received: " + message);
        }

        // Close the input stream
        in.close();

        try {
            writerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
