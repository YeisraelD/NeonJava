package HomeWork.chatApp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

import java.io.*;
import java.nio.file.Files;

public class serverGUI extends Application {
    private VBox messageBox;
    private ScrollPane scrollPane;
    private TextField inputField;
    private Stage mainStage;
    private ChatServer serverBackend;

    @Override
    public void start(Stage window) {
        this.mainStage = window;
        window.setTitle("ChatApp [Host Server]");

        messageBox = new VBox(4);
        messageBox.setPadding(new Insets(5));
        messageBox.setFillWidth(true);

        scrollPane = new ScrollPane(messageBox);
        scrollPane.setFitToWidth(true);
        messageBox.heightProperty().addListener((obs, o, n) -> scrollPane.setVvalue(1.0));

        inputField = new TextField();
        HBox.setHgrow(inputField, Priority.ALWAYS);
        inputField.setOnAction(e -> handleSendMessage());

        Button sendFileBtn = new Button("Send File");
        sendFileBtn.setOnAction(e -> handleSendFile());

        HBox btnBar = new HBox();
        btnBar.getChildren().addAll(inputField, sendFileBtn);

        BorderPane root = new BorderPane();
        root.setCenter(scrollPane);
        root.setBottom(btnBar);

        // Set up database schema
        ChatDB.setupDatabase();

        // Load chat history from the DB
        java.util.List<String> history = ChatDB.getMessageHistory();
        for (String record : history) {
            if (record.startsWith("[Host]")) {
                addSentMessage(record);
            } else {
                addReceivedMessage(record);
            }
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

    //sent message — right side
    private void addSentMessage(String text) {
        Platform.runLater(() -> {
            Label label = new Label(text);
            label.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 14px;");
            label.setWrapText(true);
            HBox row = new HBox(label);
            row.setAlignment(Pos.CENTER_RIGHT);
            messageBox.getChildren().add(row);
        });
    }

    //Incoming message — left side
    private void addReceivedMessage(String text) {
        Platform.runLater(() -> {
            Label label = new Label(text);
            label.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 14px;");
            label.setWrapText(true);
            HBox row = new HBox(label);
            row.setAlignment(Pos.CENTER_LEFT);
            messageBox.getChildren().add(row);
        });
    }

    private void handleSendMessage() {
        String text = inputField.getText();
        if (!text.isEmpty()) {
            addSentMessage("[You] " + text);
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
                addSentMessage("[You] Sent File: " + file.getName());
                serverBackend.broadcastFileFromHost(file.getName(), bytes);
            } catch (IOException e) {
                addReceivedMessage("System: Error reading file from local storage.");
            }
        }
    }

    public void handleIncomingMessage(String clientName, Message msg) {
        if (msg.isFile) {
            addReceivedMessage(clientName + " sent a file: " + msg.fileName);

            Platform.runLater(() -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setInitialFileName(msg.fileName);
                File saveLocation = fileChooser.showSaveDialog(mainStage);

                if (saveLocation != null) {
                    try (FileOutputStream fos = new FileOutputStream(saveLocation)) {
                        fos.write(msg.fileBytes);
                        addReceivedMessage("System: File saved successfully to " + saveLocation.getName());
                    } catch (IOException e) {
                        addReceivedMessage("System: Error saving file from " + clientName);
                    }
                }
            });
        } else {
            addReceivedMessage(clientName + ": " + msg.text);
        }
    }

    public void appendSystemMessage(String msg) {
        addReceivedMessage("System: " + msg);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
