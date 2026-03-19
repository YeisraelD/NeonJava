import sun.misc.Signal;
import sun.misc.SignalHandler;

//  demonstrating signal handling using the sun.misc.Signal internal API.
// Note: These APIs are non-standard and might not be available on all JVM implementations or future versions.

public class Signalx {
    public static void main(String[] args) throws InterruptedException {
        // Create a signal handler
        SignalHandler handler = new SignalHandler() {
            @Override
            public void handle(Signal signal) {
                System.out.println("\n[Handler] Received signal: " + signal.getName());
                System.out.println("[Handler] Performing cleanup...");
                // In a real application, wed elegantly shut down resources here.
                System.exit(0);
            }
        };

        // Register the signal handler for common signals
        try {
            // Register SIGTERM (kill -15 <pid>)
            Signal.handle(new Signal("TERM"), handler);
            System.out.println("Signal handler registered for SIGTERM.");

            //Signal.handle(new Signal("INT"), handler); // Uncomment to catch Ctrl+C explicitly
            
        } catch (IllegalArgumentException e) {
            System.err.println("Signal not supported on this platform: " + e.getMessage());
        }

        System.out.println("Process ID: " + ProcessHandle.current().pid());
        System.out.println("Waiting for TERM signal... (Kill the process or trigger a signal)");
        
        // Keep running
        while (true) {
            Thread.sleep(1000);
        }
    }
}
