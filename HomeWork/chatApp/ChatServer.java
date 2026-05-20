package HomeWork.chatApp;

import java.io.*;
import java.net.*;
import java.util.*;
import javafx.application.Platform;

public class ChatServer {
    private ServerSocket serverSocket;
    private final List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());
    private serverGUI gui;
    private int port;
    private boolean isRunning = false;

    public ChatServer(serverGUI gui, int port) {
        this.gui = gui;
        this.port = port;
    }

    public void start() {
        isRunning = true;
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                Platform.runLater(() -> gui.appendSystemMessage("Server started on port " + port));
                
                while (isRunning) {
                    Socket socket = serverSocket.accept();
                    
                    ClientHandler handler = new ClientHandler(socket);
                    clients.add(handler);
                    Platform.runLater(() -> gui.appendSystemMessage("New connection: " + handler.clientName));
                    new Thread(handler).start();
                }
            } catch (IOException e) {
                if (isRunning) {
                    Platform.runLater(() -> gui.appendSystemMessage("Server error: " + e.getMessage()));
                }
            }
        }).start();
    }

    public void stop() {
        isRunning = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            synchronized (clients) {
                for (ClientHandler client : clients) {
                    client.closeConnection();
                }
                clients.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcast(Message msg, ClientHandler sender) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (client != sender) {
                    client.sendMessage(msg);
                }
            }
        }
    }

    public void broadcastFromHost(String text) {
        Message msg = new Message(text);
        ChatDB.saveMessage("Host", text);
        broadcast(msg, null);
    }

    public void broadcastFileFromHost(String fileName, byte[] fileBytes) {
        Message msg = new Message(fileName, fileBytes);
        broadcast(msg, null);
    }

    class ClientHandler implements Runnable {
        private Socket socket;
        private DataInputStream in;
        private DataOutputStream out;
        public String clientName;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                this.in = new DataInputStream(socket.getInputStream());
                this.out = new DataOutputStream(socket.getOutputStream());
                this.clientName = "Client-" + socket.getPort();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                // Send chat history to this client upon connection
                List<String> history = ChatDB.getMessageHistory();
                for (String record : history) {
                    sendMessage(new Message(record));
                }

                while (isRunning) {
                    boolean isFile = in.readBoolean();
                    if (isFile) {
                        String fileName = in.readUTF();
                        int fileSize = in.readInt();
                        byte[] fileData = new byte[fileSize];
                        in.readFully(fileData);
                        
                        Message msg = new Message(fileName, fileData);
                        Platform.runLater(() -> gui.handleIncomingMessage(clientName, msg));
                        broadcast(msg, this);
                    } else {
                        String text = in.readUTF();
                        Message msg = new Message(text);
                        Platform.runLater(() -> gui.handleIncomingMessage(clientName, msg));
                        
                        ChatDB.saveMessage(clientName, text);
                        broadcast(new Message(clientName + ": " + text), this);
                    }
                }
            } catch (IOException e) {
                Platform.runLater(() -> gui.appendSystemMessage(clientName + " disconnected."));
            } finally {
                closeConnection();
            }
        }

        public void sendMessage(Message msg) {
            try {
                out.writeBoolean(msg.isFile);
                if (msg.isFile) {
                    out.writeUTF(msg.fileName);
                    out.writeInt(msg.fileBytes.length);
                    out.write(msg.fileBytes);
                } else {
                    out.writeUTF(msg.text);
                }
                out.flush();
            } catch (IOException e) {
                closeConnection();
            }
        }

        public void closeConnection() {
            clients.remove(this);
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
