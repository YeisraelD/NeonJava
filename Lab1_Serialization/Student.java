package University;


import java.util.*;
import java.io.*;

public class Student {
     int id;
     String name;
     String department;
     String section;
     int year;
Student(int sid, String n, String dep, String sec, int y){
    this.id=sid;
    this.name=n;
    this.department=dep;
    this.section=sec;
    this.year=y;
    

}
public String toString(){
    return  id + "," + name + "," + department +"," + section+ "," + year ;
}

    public static void addStudent(List<Student> list) {
          Scanner sc = new Scanner(System.in);

          try  { FileWriter fw = new FileWriter("student.txt");
            for(Student s : list) {
                    fw.write(s.toString() + "\n"); // This works!
            }
  } catch (IOException e){
    e.getMessage();

  }
}

public static void showStudent() {
    try {
        FileInputStream fw = new FileInputStream("student.txt");
        ObjectInputStream fr = new ObjectInputStream(fw);
    List<Student> list = (List<Student>) fr.readObject();

    for(Student s : list){
        System.out.println(s);
    }
    }catch(IOException | ClassNotFoundException e){
        e.getMessage();
    }
    
}
}