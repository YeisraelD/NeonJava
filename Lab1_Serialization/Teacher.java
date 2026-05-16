package University;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;
import java.util.*;

public class Teacher{
     int id;
     String name;
     String department;
Teacher(int sid, String n, String dep){
    this.id=sid;
    this.name=n;
    this.department=dep;

}
public String toString(){
    return  id + "," + name + "," + department ;
}
    public static void addTeacher(List<Teacher> list){

          try  { FileOutputStream fr = new FileOutputStream("teacher.txt", true);
                ObjectOutputStream fw = new ObjectOutputStream(fr);
               
                fw.writeObject(list);
  } catch (IOException e){
    e.getMessage();

  }

    }

    public static void showTeacher(){
        try{
            FileInputStream fw = new FileInputStream("teacher.txt");
            ObjectInputStream fr = new ObjectInputStream(fw);
    List<Teacher> list = (List<Teacher>) fr.readObject();

    for(Teacher t : list){
        System.out.println(t);
    }
    
        }catch (IOException | ClassNotFoundException e){
            e.getMessage();
        }
        

    }
}