package process;

import java.lang.ProcessHandle;
import java.util.Optional;

public class ModernProcessFeatures {
    public static void main(String[] args) {
        System.out.println("=== Modern Java Features (JDK 9+) ===");

        Runtime.Version version = Runtime.version();
        System.out.println("Java Major Version: " + version.major());
        System.out.println("Full Version String: " + version.toString());

        System.out.println("\n--- Current Process Information (ProcessHandle) ---");
        
        ProcessHandle current = ProcessHandle.current();
        System.out.println("Current PID: " + current.pid());

        ProcessHandle.Info info = current.info();
        info.command().ifPresent(c -> System.out.println("Command: " + c));
        info.user().ifPresent(u -> System.out.println("User: " + u));

        System.out.println("\nAll running processes handle example:");
        ProcessHandle.allProcesses()
                     .limit(5)
                     .forEach(ph -> System.out.println("PID: " + ph.pid() + " IsAlive: " + ph.isAlive()));

        System.out.println("======================================");
    }
}
