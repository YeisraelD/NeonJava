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

public class clientGUI extends Application {
    private TextArea chatArea;
    private TextField inputField;
    private Stage mainStage;
    private chatSocket backend;

    @Override
    public void start(Stage window) {
        this.mainStage = window;
        window.setTitle("ChatApp [Client]");

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

        backend = new chatSocket(this);
        backend.startClient("localhost", 5000);

        Scene scene = new Scene(root, 500, 400);
        window.setScene(scene);
        window.setOnCloseRequest(e -> System.exit(0));
        window.show();
    }

    private void handleSendMessage() {
        String text = inputField.getText();
        if (!text.isEmpty()) {
            Message msg = new Message(text);
            backend.sendMessage(msg);
            chatArea.appendText("[You] " + text + "\n");
            inputField.clear();
        }
    }

    private void handleSendFile() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(mainStage);
        
        if (file != null) {
            try {
                byte[] bytes = Files.readAllBytes(file.toPath());
                Message msg = new Message(file.getName(), bytes);
                backend.sendMessage(msg);
                chatArea.appendText("[You] Sent File: " + file.getName() + "\n");
            } catch (IOException e) {
                chatArea.appendText("System: Error reading file from local storage.\n");
            }
        }
    }

    public void handleIncomingMessage(Message msg) {
        if (msg.isFile) {
            chatArea.appendText("Incoming File: " + msg.fileName + "\n");

            // Prompt user to save the file
            Platform.runLater(() -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setInitialFileName(msg.fileName);
                File saveLocation = fileChooser.showSaveDialog(mainStage);

                if (saveLocation != null) {
                    try (FileOutputStream fos = new FileOutputStream(saveLocation)) {
                        fos.write(msg.fileBytes);
                        chatArea.appendText("System: File saved successfully to " + saveLocation.getName() + "\n");
                    } catch (IOException e) {
                        chatArea.appendText("System: Error saving file to local storage.\n");
                    }
                }
            });
        } else {
            chatArea.appendText(msg.text + "\n");
        }
    }

    public void appendMessage(String msg) {
        chatArea.appendText(msg + "\n");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
