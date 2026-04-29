import java.util.ArrayList;
import java.util.List;

public class main {
    public static void main(String[] args){
        Student.createTable();
        Teacher.createTable();
        System.out.println("Teachers and students information ...");

        List<Student> studentData = new ArrayList<>();
        List<Teacher> teacherData = new ArrayList<>();

        studentData.add(new Student(12, "eden", "sw", "d", 2028));
        teacherData.add(new Teacher(12, "eden", "sw"));

        Student.addStudent(studentData);
        Teacher.addTeacher(teacherData);

        Student.showStudent();
        Teacher.showTeacher();

    }
}