import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.*;
import java.nio.file.Files;

public class serverGUI  extends Application{
    private TextArea chatArea;
    private TextField inputField;
    private Stage mainStage;

    private chatSocket backend;

    public void start(Stage window){
        this.mainStage = window;
        window.setTitle("ChatApp [HOT]");

        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 14px;"); 
        inputField = new TextField();
        HBox.setHgrow(inputField, Priority.ALWAYS);
        inputField.setOnAction(e->handleSendMessage());

        Button sendFileBtn = new Button("Send File");
        sendFileBtn.setOnAction(e->handleSendFile());

        HBox btnBar = new HBox();
        btnBar.getChildren().addAll(inputField, sendFileBtn);

        BorderPane root = new BorderPane();
        root.setCenter(chatArea);
        root.setBottom(btnBar);

        backend = new chatSocket(msg->handleIncomingMessage(msg), 
                                 sysmsg->System.out.println(sysmsg));
        backend.startServer(5000);

        // Start the JavaFX timer in the GUI, which tells the controller to poll the network
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), e->backend.messagePoll()));// Create a timer that ticks every 100ms
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        Scene scene = new Scene(root, 500, 400);
        window.setScene(scene);
        window.setOnCloseRequest(e->System.exit(0));
        window.show();

    }
    private void handleSendMessage(){
        String text = inputField.getText();
        if (!text.isEmpty()){
            Message msg = new Message(text);
            backend.sendMessage(msg);
            chatArea.appendText("[You] "+ text +"\n");
            inputField.clear();
        }
    }
    private void handleSendFile(){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(mainStage);
        
        if (file != null){
            try{ //read the all file into byte arry
                byte[] bytes =Files.readAllBytes(file.toPath()); //loading from driver to ram

                Message msg = new Message(file.getName(), bytes);
                backend.sendMessage(msg);
                chatArea.appendText("[You] " + file.getName()+ "\n");
            }catch (IOException e){
                chatArea.appendText("error reading file form the hard driver.\n");
            }
        }

    }
    private void handleIncomingMessage(Message msg){
        if (msg.isFile){
            chatArea.appendText("[Client] "+ msg.fileName + "\n");

            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName(msg.fileName);
            File saveLocation = fileChooser.showSaveDialog(mainStage);

            if (saveLocation != null ) {
                try(FileOutputStream fos = new FileOutputStream(saveLocation)){
                    fos.write(msg.fileBytes);
                }catch(IOException e) {
                    chatArea.appendText("error saving the file on hard driver.\n");
                }
            }

        } else {
            chatArea.appendText("[Client] " + msg.text+ "\n");
        }
    }
    public static void main(String[] args){
        launch(args);
    }
}
