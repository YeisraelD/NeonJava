import java.net.*;
import java.io.*;
import java.util.*;
import java.util.function.Consumer;

public class chatSocket {
    private DataInputStream in ;
    private DataOutputStream out;

    private Consumer<Message> onMessageReceived;
    private Consumer<String> onSystemMessage;

    public chatSocket(Consumer<Message> onMessageReceived, Consumer<String> onSystemMessage){
        this.onMessageReceived=onMessageReceived;
        this.onSystemMessage = onSystemMessage;
    }

    public void startServer(int port){
        try{
            ServerSocket server = new ServerSocket(port);
            Socket socket =server.accept();

            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
        } catch (IOException e){
            onSystemMessage.accept("network error: "+e.getMessage());
        }
    }
    public void startClient(String host, int port){
        try{
            Socket socket = new Socket(host, port);

            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
        }catch (IOException e){
            onSystemMessage.accept("networl error: " + e.getMessage());
            onSystemMessage.accept("make sure u are running the server");
        }
    }
    public void sendMessage(Message msg){
        if(out != null) {
            try{
                out.writeBoolean(msg.isFile);

                if (msg.isFile){
                    out.writeUTF(msg.fileName);
                    out.writeInt(msg.fileBytes.length);
                    out.write(msg.fileBytes);
                }else {
                    out.writeUTF(msg.text);
                }
                out.flush();
            } catch (IOException e){
            onSystemMessage.accept("error sending message");
        }
    }
}
public void messagePoll(){
    try{
        if (in != null && in.available() > 0){
            boolean isFile = in.readBoolean();

            if(isFile){
                String fileName = in.readUTF();
                int fileSize = in.readInt();
                byte[] fileData = new byte[fileSize];
                in.readFully(fileData);//its like dowloading

                onMessageReceived.accept(new Message(fileName, fileData));

            }else {
                String text = in.readUTF();
                onMessageReceived.accept(new Message(text));
            }
        }
    }catch(IOException e){
    }
}
}
