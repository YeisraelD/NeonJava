import java.io.*;
import javafx.stage.FileChooser; //took too long to figure this out 
import javafx.stage.Stage;

import javax.swing.plaf.FileChooserUI;
public class notePad { //here handle all back logic what i think the app do
    public static class FileData{// the helper class just to handle 2 string at the same time , name and content
        public String name;
        public String content;

        public FileData(String name, String content){
            this.name = name;
            this.content= content;
        }
    }
    public FileData open(Stage stage){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);

        if(file != null){
            try(BufferedReader reader = new BufferedReader(new FileReader(file))){
                StringBuilder content = new StringBuilder();
                String line;

                while((line = reader.readLine()) != null){
                    content.append(line).append("\n");
                }

                return new FileData(file.getName(), content.toString() );
            }
        } return null;
    }
    public String save(Stage stage, String content, String init_name) throws IOException{
        FileChooser fileChooser = new FileChooserUI();
        File file = fileChooser.showOpenDialog(file);

        if(file != null){
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
                writer.write(content);
                return file.getName();
            }

        }return null;
    }


}
