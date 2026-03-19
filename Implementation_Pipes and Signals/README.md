focusing on Inter-Process Communication (IPC) and Runtime Signals.

Demonstrates how to use `PipedInputStream` and `PipedOutputStream` for communication between two threads,

<img width="1083" height="102" alt="image" src="https://github.com/user-attachments/assets/c0280896-ed1d-4347-b6d9-d21ac27f7a65" />

and how to handle OS signals (like `SIGTERM`) using `sun.misc.Signal`

<img width="1044" height="100" alt="image" src="https://github.com/user-attachments/assets/5fdc6448-c487-49db-9e41-a43f83be5d54" />


note: Uses `sun.misc.Signal`, which is an internal/unsupported API and may require `--add-exports` on modern JDK versions (Java 9+).

