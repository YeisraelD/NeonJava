# Multithreaded ChatApp: Sockets & Database Persistence Report

This is the breakdown of my desktop-based multi-user chat and file-sharing application using standard Java TCP sockets and multithreading, backed by a persistent MySQL relational database.

## System Architecture

The application is decomposed into three distinct layers:
1. **Presentation Layer (`clientGUI.java`, `serverGUI.java`)**: JavaFX-based user interfaces.
2. **Network Communications Layer (`chatSocket.java`, `ChatServer.java`, `Message.java`)**: Manages full-duplex socket connections, ClientHandler threads, and packet serialization.
3. **Data Persistence Layer (`ChatDB.java`)**: Manages MySQL connection pooling, schema initialization, and transactional message logs.

---

## Technical Implementations & Design Decisions

### 1. Multi-User Threaded Server Socket
Instead of a single-client blocking architecture, the server runs a main thread to accept incoming connections and spawns a dedicated worker thread (`ClientHandler`) for each client:

```java
while (isRunning) {
    Socket socket = serverSocket.accept();
    ClientHandler handler = new ClientHandler(socket);
    clients.add(handler);
    new Thread(handler).start();
}
```

This ensures that the server can handle an arbitrary number of concurrent chat clients simultaneously.

### 2. Client-Side Background Reader Thread
I replaced the JavaFX `Timeline` polling loop with a dedicated background thread on the client side. The thread loops on the socket's `DataInputStream` and waits for incoming packets:

```java
private void readLoop() {
    try {
        while (true) {
            boolean isFile = in.readBoolean();
            if (isFile) {
                // Deserialise file bytes...
                Platform.runLater(() -> gui.handleIncomingMessage(msg));
            } else {
                String text = in.readUTF();
                Platform.runLater(() -> gui.handleIncomingMessage(new Message(text)));
            }
        }
    } catch (IOException e) {
        Platform.runLater(() -> gui.appendMessage("System: Disconnected."));
    }
}
```

Since the reader thread is running in the background, blocking on I/O reads does not affect the JavaFX Application Thread, keeping the user interface smooth and responsive.

### 3. Synchronization and Broadcasting
When a client sends a message:
1. The server's `ClientHandler` thread receives the raw frame.
2. The server logs the message into the centralized MySQL database via `ChatDB.saveMessage(clientName, text)`.
3. The server prints the message to the `serverGUI` console.
4. The server broadcasts the prefixed message (e.g., `Client-54321: Hello!`) to all other connected client handlers:

```java
public void broadcast(Message msg, ClientHandler sender) {
    synchronized (clients) {
        for (ClientHandler client : clients) {
            if (client != sender) { // Prevents sending the message back to the sender
                client.sendMessage(msg);
            }
        }
    }
}
```

### 4. Binary Frame Protocol for Sockets
File transferring uses a prefix-length framing protocol over raw socket streams rather than base64 encoding. This minimizes RAM overhead and avoids encoding/decoding performance bottlenecks:
- **`isFile` (Boolean)**: 1 byte flag to determine frame routing.
- **`fileName` (UTF)**: String encoding the target file namespace.
- **`fileSize` (Int)**: 4-byte header specifying the body length.
- **`fileBytes` (Raw Bytes)**: Binary array matching the file contents.

### 5. Centralized Database Persistence
Database integration is handled by `ChatDB.java`. It connects using JDBC (`com.mysql.cj.jdbc.Driver`) and initializes the schema dynamically if it is not present.

- **Query Execution**: Prepared statements prevent SQL injection and cache query plans.
- **Failure Tolerance**: Every database transaction is encapsulated in a try-catch block catching `SQLException`. If the local MySQL database is offline or not configured, the chat app outputs database transaction warnings to stdout but continues running chat/networking operations normally without crashing.
- **Historical Synchronization**: When a client establishes a socket connection, the server queries the database for the last 20 messages and transmits them sequentially to the client's socket before enabling the real-time chat broadcast loop. This ensures new participants immediately see the historical conversation log.
