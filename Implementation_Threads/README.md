
A thread library provides the programmer with an API for creating and managing threads. There are two primary ways of implementing these libraries:

1. **User-Space Library**:
    - Entirely in user space with no kernel support.
    - All code and data structures for the library exist in user space.
    - Invoking a function results in a local function call in user space, not a system call.

2. **Kernel-Level Library**:
    - Supported directly by the Operating System.
    - Code and data structures exist in kernel space.
    - Invoking an API function typically results in a system call to the kernel.

### Major Thread Libraries

- **POSIX Pthreads**: Provided as either a user-level or a kernel-level library.
- **Windows Threads**: A kernel-level library available on Windows systems.
- **Java Threads**: Implemented using the thread library available on the host system (e.g., Windows API on Windows, Pthreads on UNIX).

---

## Java Thread Creation

There are two primary ways to create a thread in Java:

1. **Extending the `Thread` class**:
   ```java
   public class MyThread extends Thread {
       public void run() {
           System.out.println("Thread is running.");
       }
   }
   ```

2. **Implementing the `Runnable` interface**:
   ```java
   public class MyRunnable implements Runnable {
       public void run() {
           System.out.println("Runnable is running.");
       }
   }
   ```

### Key `Thread` Class Methods

- `getName()`  Obtains a thread’s name. 
- `getPriority()`  Obtains a thread’s priority. 
- `isAlive()`  Determines if a thread is still running. 
- `join()`  Waits for a thread to terminate. 
- `run()`  The entry point for the thread. 
- `sleep()` Suspends a thread for a specified period. 
- `start()`  Starts a thread by calling its `run()` method.

#### test
```java
c0f4af876b310958dd8fd440a565c\redhat.java\jdt_ws\NeonJava_c8c7d442\bin MultithreadingDemo "
My thread is in running state.
Thread Name: Thread-0
Thread Priority: 5
```

```java
in RunnableDemo "
Runnable implementation: My thread is in running state.
Current Thread: Thread-0
```


### Threading Models

- **Explicit Threading**: Programs created with thousands of threads manually managed by the programmer.
- **Implicit Threading**: Creation and management of threads are handled by compilers and run-time libraries.

### Alternative Approaches
- **Thread Pools**: Creating a set number of threads at startup that wait for work.
  - *Advantages*: Faster service (no creation overhead), limits resource exhaustion by bounding the number of concurrent threads.
- **OpenMP**: Compiler directives (#pragma omp parallel) and API for C/C++/Fortran that provide support for parallel programming.
- **Grand Central Dispatch (GCD)**: An Apple technology for macOS/iOS that uses blocks and dispatch queues (serial and concurrent).
- **Intel’s Threading Building Blocks (TBB)**: Library for task-based parallelism.

