package process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcessExecutor {
    public static void main(String[] args) {
        try {
            System.out.println("=== Process Execution Demonstration ===");
            
            System.out.println("Launching Notepad...");
            Process process = Runtime.getRuntime().exec("notepad.exe test_runtime.txt");

            if (process.isAlive()) {
                System.out.println("Notepad is running.");
            }

            System.out.println("Waiting for Notepad to be closed...");
            int exitCode = process.waitFor();

            System.out.println("Process exited with code: " + exitCode);
            System.out.println("======================================");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}