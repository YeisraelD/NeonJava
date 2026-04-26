import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.IOException;



public class notePadGUI extends Application {
    //ui components to be accessed across many methods im going to write
    private TabPane tabPane;
    private Label posLabel;
    private Label charCountLabel;

    private notePad backend = new notePad();

    public void start(Stage window){
        window.setTitle("My Notepad App");

        tabPane = new TabPane();
        BorderPane root = new BorderPane();
        root.setCenter(tabPane);
        //the top menubar
        MenuBar menuBar = new MenuBar();
        Menu file = new Menu("File");

        MenuItem new_ = new MenuItem("New File");
        new_.setOnAction(e -> createNewTab("Untitled",""));
        MenuItem open = new MenuItem("Open...");
        open.setOnAction(e-> handleOpening(window));
        MenuItem save = new MenuItem("Save as...");
        save.setOnAction(e-> handleSaving(window));
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(e->Platform.exit());

        file.getItems().addAll(new_, open, save, new SeparatorMenuItem(), exit);
        menuBar.getMenus().add(file);
        root.setTop(menuBar);

        HBox statusBar = createStatusBar();
        root.setBottom(statusBar);

        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal)->updateStatusBar());
        createNewTab("untitled", "");

        Scene scene = new Scene( root,800, 600);
        window.setScene(scene);
        window.show();

    }
    private HBox createStatusBar(){
        HBox statusBar = new HBox(20);
        statusBar.setPadding(new Insets(5,15,5,15));
        statusBar.setStyle("-fx-background-color: #007acc;");

        posLabel = new Label("Ln 1, Col 1");
        posLabel.setStyle("-fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-size: 12px;");

        charCountLabel = new Label("0 characters");
        charCountLabel.setStyle("-fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-size: 12px;");
        
        Label formatLabel = new Label("Plain text");
        formatLabel.setStyle("-fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-size: 12px;");

        Label crlfLabel = new Label("Windows (CRLF)");
        crlfLabel.setStyle("-fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-size: 12px;");

        Label encodingLabel = new Label("UTF-8");
        encodingLabel.setStyle("-fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-size: 12px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        statusBar.getChildren().addAll(posLabel, charCountLabel,spacer, formatLabel,crlfLabel, encodingLabel);
        return statusBar;
    }
    private TextArea getCurrentTextArea(){
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        if(selectedTab != null && selectedTab.getContent() instanceof TextArea){
            return (TextArea) selectedTab.getContent();
        } return null;
    }
    private void createNewTab(String title, String content){
        Tab tab = new Tab(title);
        TextArea textArea = new TextArea(content);

        textArea.setStyle("-fx-font-family: 'Consolas' , monospace; -fx-font-size: 14px");

        textArea.textProperty().addListener((obs, oldVal, newVal)->updateStatusBar());
        textArea.caretPositionProperty().addListener((obs, oldVal, newVal)->updateStatusBar());

        tab.setContent(textArea);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }
    private void updateStatusBar(){
        TextArea textArea = getCurrentTextArea();
        if(textArea != null){
            int caretpos = textArea.getCaretPosition();
            String text = textArea.getText();

            String txtUpTOcaret = text.substring(0, caretpos);
            String[] lines = txtUpTOcaret.split("\n", -1);
            int line = lines.length;
            int col =lines[lines.length -1].length() + 1;

            posLabel.setText(String.format("Ln %d, col %d",txtUpTOcaret, lines));
            charCountLabel.setText(text.length() + "characters");
        } else {
            posLabel.setText("Ln 1, col 1");
            charCountLabel.setText("0 characters");
        }
    }

    public void handleOpening(Stage stage){
        try{
            notePad.FileData data = backend.open(stage);
            if(data != null){
                createNewTab(data.name, data.content);
            }
        } catch (IOException e){
            alert("error opening file: "  + e.getMessage());
        }
    }
    public void handleSaving(Stage stage){
        Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
        TextArea textArea = getCurrentTextArea();
        if (currentTab != null && textArea != null){
            try{
            String newname = backend.save(stage, textArea.getText(), currentTab.getText() );
            if(newname != null ){
                currentTab.setText(newname);
            }
        } catch (IOException e){
            alert("error saving file: "  + e.getMessage());
        }
        }
    }

    private void alert(String message){
        Alert error_alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        error_alert.showAndWait();
    }
    public static void main(String[] args){
        launch(args);
    }
}
