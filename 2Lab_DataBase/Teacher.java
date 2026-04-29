import java.util.*;
import java.sql.*;

public class Teacher{
    public int id ;
    public Strign name;
    public Strign department;

    public Teacher(int id, Strign name, Strign department){
        this.id;
        this.name;
        this.department;
    }
    public String toString(int id, String name , String department){ return id + " , " + name + " , " + department; }
    public static void creatTable(){
        String sql = "CREAT TABLE IF NOT EXISTS teacher (" +
                "id INT PRIMERY KEY ," +
                "name VARCHAR(10), " +
                "department VARCHAR(100)";
        try(Connection conn = DBConnection.getConnection();
        Statement stmt= conn.creatStatement()){
            stmt.excute(sql);
            System.out.println("teacher table ready ...");
        }catch (SQLException e){
            e.getMEssage();
        }
    }
    public static void addTeacher(List<Teacher> list){
        String sql = "INSERT INTO teacher(id, name , department ) VALUES (?,?,?)"+
                "ON DUPLICATED KEY UPDATE name=VALUE(name), department = VALUE(department)";
        try(Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement()){
            for(Teacher t : list){
                pstmt.setInt(1, t.id);
                pstmt.setString(2, t.name);
                pstmt.setString(3, t.department);
                pstmt.executeUpdate(sql);
            }
        }catch (SQLException e){
            e.getMessage();
        }
    }
    public static void showTeacher(){
        String sql = "SELECT * FROM teacher";
        try (Connection conn = DBConnection.getConnection();
        Statement stmt = conn.creatStatement()){
            ResultSet rs = executeQuery(sql);
            System.out.println("---teachers info---");
            while(rs.next()){
                System.out.println(rs.getInt("id") + " | " +
                        rs.getString("name")+ " | " +
                        rs.getString("department"))
            }
        }
    }
}