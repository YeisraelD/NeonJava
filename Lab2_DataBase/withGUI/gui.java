import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.rmi.Naming;
import java.util.ArrayList;
import java.util.List;

public class gui extends Application {

    remoteInterface stub;

    TextField idField, nameField, deptField, sectField, yearField;
    TextArea displayArea;
    Button addBtn, showBtn;

    TextField TidField, TnameField, TdeptField;
    TextArea TdisplayArea;
    Button TaddBtn, TshowBtn;

    @Override
    public void start(Stage window) {
        try {
            stub = (remoteInterface) Naming.lookup("rmi://localhost/myRemoteObject");
        } catch (Exception e) {
            System.err.println("failed to connect to the server");
        }
        window.setTitle("University Student Manager");

        idField = new TextField();
        idField.setPromptText("Enter ID");
        nameField = new TextField();
        nameField.setPromptText("Enter Name");
        deptField = new TextField();
        deptField.setPromptText("Enter Department");
        sectField = new TextField();
        sectField.setPromptText("Enter Section");
        yearField = new TextField();
        yearField.setPromptText("Enter Year");

        TidField = new TextField();
        TidField.setPromptText("Enter id");
        TnameField = new TextField();
        TnameField.setPromptText("Enter name");
        TdeptField = new TextField();
        TnameField.setPromptText("Enter department");

        displayArea = new TextArea();
        displayArea.setEditable(false);
        displayArea.setPromptText("Students will appear here...");

        TdisplayArea = new TextArea();
        TdisplayArea.setEditable(false);
        TdisplayArea.setPromptText("teachers list will appear here..");

        addBtn = new Button("Add Student to Database");
        showBtn = new Button("Show Student");

        TaddBtn = new Button("Add new Teacher");
        TshowBtn = new Button("Show Teacher");

        addBtn.setOnAction(e -> handleAdd());
        showBtn.setOnAction(e -> handleShow());
        TaddBtn.setOnAction(e -> ThandleAdd());
        TshowBtn.setOnAction(e -> ThandleShow());

        VBox layout1 = new VBox(10); // 10px spacing
        layout1.setPadding(new Insets(20));
        layout1.getChildren().addAll(
                new Label("Add New Student:"), idField, nameField, deptField, sectField, yearField,
                addBtn, new Separator(),
                new Label("Current Students:"), showBtn, displayArea);

        VBox layout2 = new VBox(10);
        layout2.setPadding(new Insets(20));
        layout2.getChildren().addAll(new Label("Add new teacher: "), TidField,
                TnameField, TdeptField, TaddBtn, new Separator(),
                new Label("Current teachers: "), TshowBtn, TdisplayArea);
        HBox layout = new HBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(layout1, layout2);

        Scene scene = new Scene(layout, 400, 600);
        window.setScene(scene);
        window.show();
    }

    private void handleAdd() {
        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            String dept = deptField.getText();
            String sect = sectField.getText();
            int year = Integer.parseInt(yearField.getText());

            stu s = new stu(id, name, dept, sect, year);
            stub.addStudent(s);

            idField.clear();
            nameField.clear();
            deptField.clear();
            sectField.clear();
            yearField.clear();
            handleShow();

        } catch (Exception ex) {
            displayArea.setText("Error: Please enter valid numbers for ID and Year!");
        }
    }

    private void ThandleAdd() {
        try {
            int id = Integer.parseInt(TidField.getText());
            String name = TnameField.getText();
            String dep = TdeptField.getText();

            tea t = new tea(id, name, dep);
            tea.addTeacher(t);
            TidField.clear();
            TnameField.clear();
            TdeptField.clear();
        } catch (Exception ex) {
            TdisplayArea.setText("Error: Please enter valid numbers for ID and Year!");
        }
    }

    private void handleShow() {
        String list = stu.getStudentList();
        displayArea.setText(list);
    }

    private void ThandleShow() {
        String list = tea.getTeacherList();
        TdisplayArea.setText(list);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
