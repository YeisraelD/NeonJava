import java.util.*;
import java.sql.*;

public class Student{
    int id;
    String name;
    String department;
    String section;
    int year;

    public Student (int id, String name, String department, String section, int year){
        this.id = id ;
        this.name= name;
        this.department= department;
        this.section = section;
        this.year = year;
    }
    public String toString(int id, String name , String departmetn, String section , int year){
        return id + ", " + name + " ," + departmetn +" ,"+ section +" ," + year;
    }
    public static  void  createTable (){
        String sql = "CREATE TABLE IF NOT EXISTS student (" +
                "id INT PRIMARY KEY, "+
                "name VARCHAR(10), "+
                "department VARCHAR(100), " +
                "section VARCHAR(50), "+
                "year INT)";
        try(Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement()){
            stmt.execute(sql);
            System.out.println("student table ready");
        }catch (SQLException e){
            e.getMessage();
        }
    }
    public static void addStudent(List<Student> list){
        String sql= "INSERT INTO student (id, name , dpartment , section, year) VALUES (?,?,?,?,?)" +
                "ON DUPLICATE KEY UPDATE name=VALUES(name), department = VALUES(department), " +
                "section = VALUES(section) , year = VALUES(year) ";
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
            for (Student s : list){
                pstmt.setInt(1, s.id);
                pstmt.setString(2, s.name);
                pstmt.setString(3, s.department);
                pstmt.setString(4,s.section);
                pstmt.setInt(5, s.year);
                pstmt.executeUpdate();
            }

        }catch ( SQLException e){
            e.getMessage();
        }
    }
    public static void showStudent(){
        String sql = "SELECT * FROM student ";
        try (Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)){

            System.out.println("--- students ----");
            while(rs.next()){
                System.out.println(rs.getInt("id") + " | " +
                        rs.getString("name") + " | " +
                        rs.getString("department") + " | " +
                        rs.getString("section") + " | " +
                        rs.getInt("year"));
            }
        }catch (SQLException e){
            e.getMessage();
        }
    }

}