package process;

import java.io.File;
import java.io.IOException;

public class AdvancedProcessBuilder {
    public static void main(String[] args) {
        try {
            System.out.println("=== ProcessBuilder Demonstration ===");

            ProcessBuilder pb = new ProcessBuilder("notepad.exe", "test_builder.txt");

            pb.directory(new File(System.getProperty("user.home")));
            System.out.println("Setting working directory to: " + pb.directory().getAbsolutePath());

            File logFile = new File("process_log.txt");
            pb.redirectOutput(ProcessBuilder.Redirect.appendTo(logFile));
            pb.redirectError(ProcessBuilder.Redirect.INHERIT);

            System.out.println("Launching Process...");
            Process process = pb.start();

            System.out.println("Process PID: " + process.pid());
            
            int exitCode = process.waitFor();
            System.out.println("Process finished with code: " + exitCode);
            System.out.println("====================================");

            

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
