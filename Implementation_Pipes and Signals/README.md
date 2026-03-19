focusing on Inter-Process Communication (IPC) and Runtime Signals.

Demonstrates how to use `PipedInputStream` and `PipedOutputStream` for communication between two threads, and how to handle OS signals (like `SIGTERM`) using `sun.misc.Signal`

note: Uses `sun.misc.Signal`, which is an internal/unsupported API and may require `--add-exports` on modern JDK versions (Java 9+).

