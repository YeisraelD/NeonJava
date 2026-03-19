Runtime class and memory management in Java

The program begins by obtaining the current Runtime instance using `Runtime.getRuntime()`. The Runtime class represents the Java Virtual Machine environment and provides methods to interact with memory and system resources.

It first retrieves the total memory available to the JVM using `totalMemory()`. This represents the total heap currently allocated to the JVM.

Next, it retrieves the free memory using `freeMemory()`. This is the amount of unused memory within the allocated heap. The used memory is calculated by subtracting free memory from total memory.

The program then creates a large number of temporary String objects inside a loop. These objects are not stored in any variable, so after each iteration they immediately become unreachable. Unreachable objects are eligible for garbage collection.

After creating these temporary objects, the program checks the free memory again to observe the change in available heap space.

It then explicitly requests garbage collection by calling runtime.gc(). This does not guarantee immediate execution but suggests to the JVM that now would be a good time to reclaim unused memory.

Finally, it checks free memory again to see how much memory has been reclaimed after garbage collection.
<img width="1636" height="250" alt="image" src="https://github.com/user-attachments/assets/e908cba4-7abe-453e-adee-927e18c57946" />


This program helps illustrate three important concepts:

How to inspect JVM memory usage.

How objects become eligible for garbage collection when unreachable.

How the garbage collector reclaims memory automatically.
