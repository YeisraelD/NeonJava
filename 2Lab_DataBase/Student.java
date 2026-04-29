import java.util.*;
import jsvs.sql.*;

public class Student {
    public int id;
    public String name ;
    public Strign department;
    public String section;
    public int year;

    public Student (int id , String name, String depatment, String section , int year){
        this.id =id;
        this.name =name;
        this.department = depatment;
        this.section =section ;
        this.year =year;
    }
    public Strign toStrign(int id,String name, String depatment, String section , int year){
        return id+ " " +name + " " +depatment + " " +section+ " " +year;
    }
    public static createTable(){
        String sql = "CREATE TABLE IF NOT EXISTS student (" +
                      "id INT PRIMERY KEY , " +
                      "name VARCHAR(10) , " +
                      "department VARCHAR(100) ," +
                "section VARCHAR(100) ," +
                "year INT";

        try(Connection conn = DBConnection.getConnection();
            Stetement stmt = conn.createStatement()){
            stmt.excute(sql)//nonestatic
            System.out.println("student table ready ...")
        } catch (SQLException e){
            e.getMessage();
        }
    }
    public static addStudent(List<Student> list){
        String sql = "INSERT INTO student (id, name , department , section , year) VALUES (?, ?, ?,?) " +
                "ON DUPLICATED KEY UPDATE name = VALUE(name), department = VALUE(department), " +
                "section = VALUE(section), year= VALUE(year)";
        try(Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement()){
            for(Student s : list){
                pstmt.setInt(1, s.id);
                pstmt.setString(2, s.name);
                pstmt.setString(3, s.department);
                pstmt.setString(4, s.section);
                pstmt.setInt(5, s.year);
                pstmt.excuteUpdate();
            }
        }catch (SQlException e){
            e.getMessage();
        }
    }
    public static showStudent(){
        String sql = "SELECT * FROM student";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()){
            ResultSet rs = executeQuery(sql);
            Systme.out.println("---student info ---");

            while(rs.next()){
                System.out.println(rs.getInt("id") + " | " +
                        rs.getString("name") + " | " +
                        rs.getStrign("department") + " | " +
                        rs.getStrign("section")+ " | " +
                        rs.getInt("year"));
            }

        }catch (SQLException e ){
            e.getMessage();
        }
    }
}