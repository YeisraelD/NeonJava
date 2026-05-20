package HomeWork.chatApp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

import java.io.*;
import java.nio.file.Files;

public class serverGUI extends Application {
    private TextArea chatArea;
    private TextField inputField;
    private Stage mainStage;
    private ChatServer serverBackend;

    @Override
    public void start(Stage window) {
        this.mainStage = window;
        window.setTitle("ChatApp [Host Server]");

        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 14px;"); 
        
        inputField = new TextField();
        HBox.setHgrow(inputField, Priority.ALWAYS);
        inputField.setOnAction(e -> handleSendMessage());

        Button sendFileBtn = new Button("Send File");
        sendFileBtn.setOnAction(e -> handleSendFile());

        HBox btnBar = new HBox();
        btnBar.getChildren().addAll(inputField, sendFileBtn);

        BorderPane root = new BorderPane();
        root.setCenter(chatArea);
        root.setBottom(btnBar);

        // Set up database schema
        ChatDB.setupDatabase();
        
        // Load chat history from the DB
        java.util.List<String> history = ChatDB.getMessageHistory();
        for (String record : history) {
            chatArea.appendText(record + "\n");
        }

        // Start multithreaded socket server
        serverBackend = new ChatServer(this, 5000);
        serverBackend.start();

        Scene scene = new Scene(root, 500, 400);
        window.setScene(scene);
        window.setOnCloseRequest(e -> {
            serverBackend.stop();
            System.exit(0);
        });
        window.show();
    }

    private void handleSendMessage() {
        String text = inputField.getText();
        if (!text.isEmpty()) {
            chatArea.appendText("[You] " + text + "\n");
            serverBackend.broadcastFromHost("[Host]: " + text);
            inputField.clear();
        }
    }

    private void handleSendFile() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(mainStage);
        
        if (file != null) {
            try {
                byte[] bytes = Files.readAllBytes(file.toPath());
                chatArea.appendText("[You] Sent File: " + file.getName() + "\n");
                serverBackend.broadcastFileFromHost(file.getName(), bytes);
            } catch (IOException e) {
                chatArea.appendText("System: Error reading file from local storage.\n");
            }
        }
    }

    public void handleIncomingMessage(String clientName, Message msg) {
        if (msg.isFile) {
            chatArea.appendText(clientName + " sent a file: " + msg.fileName + "\n");
            
            // Save file prompt
            Platform.runLater(() -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setInitialFileName(msg.fileName);
                File saveLocation = fileChooser.showSaveDialog(mainStage);

                if (saveLocation != null) {
                    try (FileOutputStream fos = new FileOutputStream(saveLocation)) {
                        fos.write(msg.fileBytes);
                        chatArea.appendText("System: File saved successfully to " + saveLocation.getName() + "\n");
                    } catch (IOException e) {
                        chatArea.appendText("System: Error saving file from " + clientName + "\n");
                    }
                }
            });
        } else {
            chatArea.appendText(clientName + ": " + msg.text + "\n");
        }
    }

    public void appendSystemMessage(String msg) {
        chatArea.appendText("System: " + msg + "\n");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
