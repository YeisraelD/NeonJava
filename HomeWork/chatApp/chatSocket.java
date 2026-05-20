package HomeWork.chatApp;

import java.net.*;
import java.io.*;
import javafx.application.Platform;

public class chatSocket {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private clientGUI gui;

    public chatSocket(clientGUI gui) {
        this.gui = gui;
    }

    public void startClient(String host, int port) {
        new Thread(() -> {
            try {
                socket = new Socket(host, port);
                out = new DataOutputStream(socket.getOutputStream());
                in = new DataInputStream(socket.getInputStream());
                Platform.runLater(() -> gui.appendMessage("System: Connected to server."));
                
                readLoop();
            } catch (IOException e) {
                Platform.runLater(() -> gui.appendMessage("System: Connection error. Make sure the server is running."));
            }
        }).start();
    }

    private void readLoop() {
        try {
            while (true) {
                boolean isFile = in.readBoolean();
                if (isFile) {
                    String fileName = in.readUTF();
                    int fileSize = in.readInt();
                    byte[] fileData = new byte[fileSize];
                    in.readFully(fileData);
                    
                    Message msg = new Message(fileName, fileData);
                    Platform.runLater(() -> gui.handleIncomingMessage(msg));
                } else {
                    String text = in.readUTF();
                    Message msg = new Message(text);
                    Platform.runLater(() -> gui.handleIncomingMessage(msg));
                }
            }
        } catch (IOException e) {
            Platform.runLater(() -> gui.appendMessage("System: Disconnected from server."));
        }
    }

    public void sendMessage(Message msg) {
        new Thread(() -> {
            try {
                if (out != null) {
                    out.writeBoolean(msg.isFile);
                    if (msg.isFile) {
                        out.writeUTF(msg.fileName);
                        out.writeInt(msg.fileBytes.length);
                        out.write(msg.fileBytes);
                    } else {
                        out.writeUTF(msg.text);
                    }
                    out.flush();
                }
            } catch (IOException e) {
                Platform.runLater(() -> gui.appendMessage("System: Error sending message."));
            }
        }).start();
    }
}
