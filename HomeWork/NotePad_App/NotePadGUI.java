
import HomeWork.NotePad_App.model.NotePad;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javafx.scene.input.KeyCombination;

public class NotePadGUI extends Application {
    // ui components to be accessed across many methods im going to write
    private TabPane tabPane;
    private Label posLabel;
    private Label charCountLabel;

    private NotePad backend = new NotePad();

    public void start(Stage window) {
        window.setTitle("My Notepad App");

        tabPane = new TabPane();
        BorderPane root = new BorderPane();
        root.setCenter(tabPane);
        // the top menubar
        MenuBar menuBar = new MenuBar();
        Menu file = new Menu("File");

        //file menus
        MenuItem new_ = new MenuItem("New File");
        new_.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
        new_.setOnAction(e -> createNewTab("Untitled", ""));

        MenuItem open = new MenuItem("Open...");
        open.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        open.setOnAction(e -> handleOpening(window));

        MenuItem save = new MenuItem("Save");
        save.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        save.setOnAction(e -> handleNormalSaving(window));

        MenuItem saveAs = new MenuItem("Save as...");
        saveAs.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+S"));
        saveAs.setOnAction(e -> handleSaving(window));

        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(e -> Platform.exit());

        file.getItems().addAll(new_, open, save, saveAs, new SeparatorMenuItem(), exit);

        // edit menus
        Menu edit = new Menu("Edit");

        MenuItem undo = new MenuItem("Undo");
        undo.setAccelerator(KeyCombination.keyCombination("Ctrl+Z"));
        undo.setOnAction(e -> { TextArea ta = getCurrentTextArea(); if (ta != null) ta.undo(); });

        MenuItem redo = new MenuItem("Redo");
        redo.setAccelerator(KeyCombination.keyCombination("Ctrl+Y"));
        redo.setOnAction(e -> { TextArea ta = getCurrentTextArea(); if (ta != null) ta.redo(); });

        MenuItem cut = new MenuItem("Cut");
        cut.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
        cut.setOnAction(e -> { TextArea ta = getCurrentTextArea(); if (ta != null) ta.cut(); });

        MenuItem copy = new MenuItem("Copy");
        copy.setAccelerator(KeyCombination.keyCombination("Ctrl+C"));
        copy.setOnAction(e -> { TextArea ta = getCurrentTextArea(); if (ta != null) ta.copy(); });

        MenuItem paste = new MenuItem("Paste");
        paste.setAccelerator(KeyCombination.keyCombination("Ctrl+V"));
        paste.setOnAction(e -> { TextArea ta = getCurrentTextArea(); if (ta != null) ta.paste(); });

        MenuItem selectAll = new MenuItem("Select All");
        selectAll.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));
        selectAll.setOnAction(e -> { TextArea ta = getCurrentTextArea(); if (ta != null) ta.selectAll(); });

        edit.getItems().addAll(undo, redo, new SeparatorMenuItem(),
                cut, copy, paste, new SeparatorMenuItem(),
                selectAll);

        menuBar.getMenus().addAll(file, edit);
        root.setTop(menuBar);

        HBox statusBar = createStatusBar();
        root.setBottom(statusBar);

        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateStatusBar());
        createNewTab("untitled", "");

        Scene scene = new Scene(root, 800, 600);
        window.setScene(scene);
        window.show();

    }

    private HBox createStatusBar() {
        HBox statusBar = new HBox(20);
        statusBar.setPadding(new Insets(5, 15, 5, 15));
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

        statusBar.getChildren().addAll(posLabel, charCountLabel, spacer, formatLabel, crlfLabel, encodingLabel);
        return statusBar;
    }

    private TextArea getCurrentTextArea() {
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        if (selectedTab != null && selectedTab.getContent() instanceof TextArea) {
            return (TextArea) selectedTab.getContent();
        }
        return null;
    }

    private void createNewTab(String title, String content) {
        createNewTab(title, content, null);
    }

    private void createNewTab(String title, String content, File file) {
        Tab tab = new Tab(title);
        tab.setUserData(file); // Store the File reference in the tab
        TextArea textArea = new TextArea(content);

        textArea.setStyle("-fx-font-family: 'Consolas' , monospace; -fx-font-size: 14px");

        textArea.textProperty().addListener((obs, oldVal, newVal) -> updateStatusBar());
        textArea.caretPositionProperty().addListener((obs, oldVal, newVal) -> updateStatusBar());

        tab.setContent(textArea);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }

    private void updateStatusBar() {
        TextArea textArea = getCurrentTextArea();
        if (textArea != null) {
            int caretpos = textArea.getCaretPosition();
            String text = textArea.getText();

            String txtUpTOcaret = text.substring(0, caretpos);
            String[] lines = txtUpTOcaret.split("\n", -1);
            int line = lines.length;
            int col = lines[lines.length - 1].length() + 1;

            posLabel.setText(String.format("Ln %d, col %d", line, col));
            charCountLabel.setText(text.length() + " characters");
        } else {
            posLabel.setText("Ln 1, col 1");
            charCountLabel.setText("0 characters");
        }
    }

    public void handleOpening(Stage stage) {
        try {
            NotePad.FileData data = backend.open(stage);
            if (data != null) {
                createNewTab(data.name, data.content, data.file);
            }
        } catch (IOException e) {
            alert("error opening file: " + e.getMessage());
        }
    }

    public void handleSaving(Stage stage) {
        Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
        TextArea textArea = getCurrentTextArea();
        if (currentTab != null && textArea != null) {
            try {
                File savedFile = backend.save(stage, textArea.getText(), currentTab.getText());
                if (savedFile != null) {
                    currentTab.setText(savedFile.getName());
                    currentTab.setUserData(savedFile); // Save it to the tab
                }
            } catch (IOException e) {
                alert("error saving file: " + e.getMessage());
            }
        }
    }

    public void handleNormalSaving(Stage stage) {
        Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
        TextArea textArea = getCurrentTextArea();

        if (currentTab != null && textArea != null) {
            File savedFile = (File) currentTab.getUserData();
            if (savedFile != null) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(savedFile))) {
                    writer.write(textArea.getText());
                } catch (IOException e) {
                    alert("Error saving file: " + e.getMessage());
                }
            } else {
                handleSaving(stage);
            }
        }
    }

    private void alert(String message) {
        Alert error_alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        error_alert.showAndWait();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
