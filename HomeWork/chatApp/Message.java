package NeonJava.HomeWork.chatApp;

public class Message {
    public boolean isFile;
    public String text;
    public String fileName;
    public byte[] fileBytes;

    public Message(String text) {
        this.isFile = false;
        this.text = text;
    }

    public Message(String fileName, byte[] fileBytes) {
        this.isFile = true;
        this.fileName = fileName;
        this.fileBytes = fileBytes;
    }
}