import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) {

        Student.createTable();
        Teacher.createTable();

        System.out.println("Processing student and teacher info...");

        List<Student> studentData = new ArrayList<>();
        List<Teacher> teacherData = new ArrayList<>();

        studentData.add(new Student(14, "yeisrael", "sw", "d", 2028));
        studentData.add(new Student(13, "yeab", "sw", "d", 2028));

        teacherData.add(new Teacher(101, "Dr tesema", "Cs"));
        teacherData.add(new Teacher(102, "Prof.dawit", "Math"));


        Student.addStudent(studentData);
        Teacher.addTeacher(teacherData);

        Student.showStudent();
        Teacher.showTeacher();
    }
}

