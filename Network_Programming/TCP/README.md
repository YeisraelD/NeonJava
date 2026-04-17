TCP is a **connection-oriented** protocol. This means that before data can be sent, a connection (a handshake) must be established between the client and the server.

#### Key Classes in Java:
1.  **`java.net.ServerSocket`**: Used by the server to listen for incoming connection requests on a specific port.
2.  **`java.net.Socket`**: Used by both the client and the server to communicate. It represents one end of the connection.

#### The Lifecycle of a TCP Connection:
1.  **Server Listens**: The server creates a `ServerSocket` and calls `accept()`, which blocks (stops execution) until a client connects.
2.  **Client Connects**: The client creates a `Socket` using the server's IP and Port.
3.  **Communication**: Both sides exchange data using `InputStream` and `OutputStream`.
4.  **Close**: Both sides close their streams and sockets to free up resources.

#### Today's Goal:
Build an **Echo Server**. When the client sends "Hello", the server responds with "Echo: Hello".

<img width="1114" height="166" alt="image" src="https://github.com/user-attachments/assets/af767230-a0ed-4c9a-b771-a8cc6705cb97" />

<img width="1128" height="149" alt="image" src="https://github.com/user-attachments/assets/fcac8cf0-7c90-4ff3-863e-665247ad1b07" />


