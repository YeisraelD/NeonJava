import java.util.*;
import java.sql.*;

public class Student {
    public int id;
    public String name ;
    public String department;
    public String section;
    public int year;

    public Student (int id , String name, String depatment, String section , int year){
        this.id =id;
        this.name =name;
        this.department = depatment;
        this.section =section ;
        this.year =year;
    }
    public String toStrign(int id,String name, String depatment, String section , int year){
        return id+ " " +name + ", " +depatment + ", " +section+ " ," +year;
    }
    public static void createTable(){
        String sql = "CREATE TABLE IF NOT EXISTS student (" +
                      "id INT PRIMERY KEY , " +
                      "name VARCHAR(10) , " +
                      "department VARCHAR(100) ," +
                "section VARCHAR(100) ," +
                "year INT";

        try(Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement()){
            stmt.execute(sql);//nonestatic
            System.out.println("student table ready ...");
        } catch (SQLException e){
            e.getMessage();
        }
    }
    public static void addStudent(List<Student> list){
        String sql = "INSERT INTO student (id, name , department , section, year) VALUES (?,?,?,?,?)" +
                "ON DUPLICATE KEY UPDATE name=VALUES(name), department = VALUES(department), " +
                "section = VALUES(section) , year = VALUES(year) ";
        try(Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
            for(Student s : list){
                pstmt.setInt(1, s.id);
                pstmt.setString(2, s.name);
                pstmt.setString(3, s.department);
                pstmt.setString(4, s.section);
                pstmt.setInt(5, s.year);
                pstmt.executeUpdate();
            }
        }catch (SQLException e){
            e.getMessage();
        }
    }
    public static void showStudent(){
        String sql = "SELECT * FROM student";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()){
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("---student info ---");

            while(rs.next()){
                System.out.println(rs.getInt("id") + " | " +
                        rs.getString("name") + " | " +
                        rs.getString("department") + " | " +
                        rs.getString("section")+ " | " +
                        rs.getInt("year"));
            }

        }catch (SQLException e ){
            e.getMessage();
        }
    }
}