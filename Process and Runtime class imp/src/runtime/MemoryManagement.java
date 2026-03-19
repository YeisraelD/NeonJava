package runtime;

// import java.io.IOException;

public class MemoryManagement {
    public static void main(String[] args) {
        Runtime runtime = Runtime.getRuntime(); // Runtime this actually represents the jvm environment
        // we can not create it using new, unlike other obj
        // if we call Runtime.getRuntime(); returns the current jvm environment


        System.out.println("=== Memory Management Demonstration ===");

        long totalMemory = runtime.totalMemory(); // thsi is total heap memory allocated to jvm
        long freeMemoryBefore = runtime.freeMemory();// just unused heap memo

        System.out.printf("Total Memory: %d bytes%n", totalMemory);
        System.out.printf("Free Memory (Before GC): %d bytes%n", freeMemoryBefore);
        System.out.printf("Used Memory: %d bytes%n", (totalMemory - freeMemoryBefore));

        System.out.println("\nCreating garbage objects...");
        for (int i = 0; i < 10000; i++) {
            new String("Garbage " + i);
        } // 10000 unreachable objects

        long freeMemoryAfterGarbage = runtime.freeMemory();
        System.out.printf("Free Memory after garbage creation: %d bytes%n", freeMemoryAfterGarbage);

        System.out.println("\nRunning GC...");
        runtime.gc(); //"please run garbage collector now..."

        long freeMemoryAfterGC = runtime.freeMemory();
        System.out.printf("Free Memory after GC: %d bytes%n", freeMemoryAfterGC);
        System.out.println("=======================================");
    }
}


