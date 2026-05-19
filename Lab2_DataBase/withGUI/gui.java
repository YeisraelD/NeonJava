package Lab2_DataBase.withGUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
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
        Label notificationLabl = new Label("waiting for live notification..");
        notificationLabl.setStyle("-fx-font-weight: bold; -fx-text-fill: green; -fx-font-size: 14px;");
        try {
            stub = (remoteInterface) Naming.lookup("rmi://localhost/myRemoteObject");

            Socket socket = new Socket("localhost", 8000);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            new Thread(() -> {
                try {
                    while (true) {
                        String msg = in.readLine();
                        if (msg != null) {
                            Platform.runLater(() -> notificationLabl.setText(msg));
                        }
                    }
                } catch (Exception e) {
                    System.err.println("disconnected");
                }
            }).start();

        } catch (Exception e) {
            System.err.println("failed to connect to the server");
            e.printStackTrace();
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
        TdeptField.setPromptText("Enter department");

        displayArea = new TextArea();
        displayArea.setEditable(false);
        displayArea.setPromptText("Students will appear here...");

        TdisplayArea = new TextArea();
        TdisplayArea.setEditable(false);
        TdisplayArea.setPromptText("teachers list will appear here..");

        addBtn = new Button("Add Student");
        showBtn = new Button("Show Student");

        TaddBtn = new Button("Add Teacher");
        TshowBtn = new Button("Show Teacher");

        addBtn.setOnAction(e -> handleAdd());
        showBtn.setOnAction(e -> handleShow());
        TaddBtn.setOnAction(e -> ThandleAdd());
        TshowBtn.setOnAction(e -> ThandleShow());

        VBox layout1 = new VBox(10); // 10px spacing
        layout1.setPadding(new Insets(20));
        layout1.getChildren().addAll(
                new Label("Student:"), idField, nameField, deptField, sectField, yearField,
                addBtn, new Separator(),
                new Label("Current Students:"), showBtn, displayArea);

        VBox layout2 = new VBox(10);
        layout2.setPadding(new Insets(20));
        layout2.getChildren().addAll(new Label("Teacher: "), TidField,
                TnameField, TdeptField, TaddBtn, new Separator(),
                new Label("Current teachers: "), TshowBtn, TdisplayArea);
        HBox layout = new HBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(layout1, layout2);

        VBox updatedLayout = new VBox();
        updatedLayout.setAlignment(Pos.CENTER);
        updatedLayout.setPadding(new Insets(10));
        updatedLayout.getChildren().addAll(layout, notificationLabl);

        Scene scene = new Scene(updatedLayout, 600, 650);
        window.setScene(scene);
        window.show();
    }

    private void handleAdd() {
        if (stub == null) {
            displayArea.setText("Error: Not connected to the server.");
            return;
        }
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
        if (stub == null) {
            TdisplayArea.setText("Error: Not connected to the server.");
            return;
        }
        try {
            int id = Integer.parseInt(TidField.getText());
            String name = TnameField.getText();
            String dep = TdeptField.getText();

            tea t = new tea(id, name, dep);
            stub.addTeacher(t);
            ThandleShow();
            TidField.clear();
            TnameField.clear();
            TdeptField.clear();
        } catch (Exception ex) {
            TdisplayArea.setText("Error: Please enter a valid number for ID!");
        }
    }

    private void handleShow() {
        if (stub == null) {
            displayArea.setText("Error: Not connected to the server.");
            return;
        }
        try {
            String list = stub.getStudentList();
            displayArea.setText(list);
        } catch (RemoteException e) {
            System.err.println("error: " + e);
        }
    }

    private void ThandleShow() {
        if (stub == null) {
            TdisplayArea.setText("Error: Not connected to the server.");
            return;
        }
        try {
            String list = stub.getTeacherList();
            TdisplayArea.setText(list);
        } catch (RemoteException e) {
            System.err.println("error: " + e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
