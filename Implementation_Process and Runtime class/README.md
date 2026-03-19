### 1.Runtime class and memory management in Java

The program begins by obtaining the current Runtime instance using `Runtime.getRuntime()`. 
The Runtime class represents the Java Virtual Machine environment and provides methods to interact with memory and system resources.

It first retrieves the total memory available to the JVM using `totalMemory()`. 
This represents the total heap currently allocated to the JVM.

Next, it retrieves the free memory using `freeMemory()`. This is the amount of unused memory within the allocated heap. The used memory is calculated by subtracting free memory from total memory.

The program then creates a large number of temporary String objects inside a loop. These objects are not stored in any variable, so after each iteration they immediately become unreachable. Unreachable objects are eligible for garbage collection.

After creating these temporary objects, the program checks the free memory again to observe the change in available heap space.

It then explicitly requests garbage collection by calling runtime.gc(). 
This does not guarantee immediate execution but suggests to the JVM that now would be a good time to reclaim unused memory.

Finally, it checks free memory again to see how much memory has been reclaimed after garbage collection.
<img width="1636" height="250" alt="image" src="https://github.com/user-attachments/assets/e908cba4-7abe-453e-adee-927e18c57946" />

This helps illustrate three important concepts:

* How to inspect JVM memory usage.
* How objects become eligible for garbage collection when unreachable.
* How the garbage collector reclaims memory automatically.

### 2. Process Execution Demonstration in Java

It then launches an external program using Runtime.getRuntime().exec(). In this example, the program executed is notepad.exe with a file name argument test_runtime.txt. This means the Windows Notepad application will open and attempt to load or create the specified file.

The exec() method returns a Process object. This object represents the running external program and allows the Java application to interact with it.

After launching Notepad, the program checks whether the process is still running by calling process.isAlive(). If the process is active, a message is printed confirming that Notepad is running.

Next, the program calls process.waitFor(). This method causes the current Java thread to pause execution until the external process (Notepad) finishes. In this case, the program waits until the user closes Notepad.

When Notepad is closed, waitFor() returns an integer exit code. By convention, an exit code of 0 usually indicates that the program terminated successfully. The exit code is then printed to the console.

The entire execution is wrapped in a try-catch block to handle possible exceptions. An IOException may occur if the system cannot find or start the specified program. An InterruptedException may occur if the waiting thread is interrupted while waiting for the process to complete.

here we sow
* How to execute external programs from Java.
* How to obtain and use a Process object.
* How to check if a process is still running.
* How to wait for a process to finish and retrieve its exit code.

### 3.Modern Process Features and Advanced Process Management in Java

ModernProcessFeatures

It retrieves version information about the currently running Java environment by calling Runtime.version(). This returns a Runtime.Version object, which encapsulates structured version data.

The program prints:

* The major version number using version.major(). This represents the main release version (for example, 17, 21, etc.).
* The full version string using version.toString(), which includes additional details such as minor and security versions.

Next, the program demonstrates process inspection using ProcessHandle.

ProcessHandle.current() obtains a handle to the currently running Java process. From this handle:

* pid() returns the process ID of the running Java application.
* info() returns a ProcessHandle.Info object containing metadata about the process.

The ProcessHandle.Info object provides optional values. The program uses ifPresent() to safely access:

* The command used to start the process.
* The user who started the process.

Then the program demonstrates how to access all running processes on the system using ProcessHandle.allProcesses(). It limits the output to the first five processes and prints:

* The process ID.
* Whether the process is alive using isAlive().

### 4. AdvancedProcessBuilder

A ProcessBuilder instance is created with two arguments:

* "notepad.exe" as the program to execute.
* "test_builder.txt" as the file argument passed to Notepad.

The working directory of the process is set using pb.directory(). It uses the user's home directory by retrieving it with System.getProperty("user.home"). This means the external program will execute as if it was started from that directory.

The program then configures output redirection:

* redirectOutput(ProcessBuilder.Redirect.appendTo(logFile)) appends standard output to a file named process_log.txt.
* redirectError(ProcessBuilder.Redirect.INHERIT) causes error output from the subprocess to be displayed in the same console as the Java program.

The process is started by calling pb.start(), which returns a Process object.

The program prints the PID of the started process using process.pid().

It then waits for the external process to finish by calling process.waitFor(). Once the process exits, the exit code is printed.

