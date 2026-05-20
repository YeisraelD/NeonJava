package HomeWork.NotePad_App.model;

import java.io.*;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

public class NotePad { // here handle all back logic what i think the app do
    public static class FileData {// the helper class just to handle 2 string at the same time , name and content
        public String name;
        public String content;
        public File file; // holds my file object (the reference to the actual file on the disk)

        public FileData(String name, String content, File file) {
            this.name = name;
            this.content = content;
            this.file = file;
        }
    }

    public FileData open(Stage stage) throws IOException {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder content = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }

                return new FileData(file.getName(), content.toString(), file);
            }
        }
        return null;
    }

    public File save(Stage stage, String content, String init_name) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(init_name);
        File file = fileChooser.showSaveDialog(stage); // Use showSaveDialog for saving!

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(content);
                return file;
            }
        }
        return null;
    }

}
