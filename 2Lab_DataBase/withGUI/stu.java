import java.util.*;
import java.sql.*;

public class stu {
    public int id;
    public String name;
    public String department;
    public String section;
    public int year;

    public stu(int id, String name, String depatment, String section, int year) {
        this.id = id;
        this.name = name;
        this.department = depatment;
        this.section = section;
        this.year = year;
    }

    public String toString(int id, String name, String depatment, String section, int year) {
        return id + " " + name + ", " + depatment + ", " + section + " ," + year;
    }

    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS student (" +
                "id INT PRIMERY KEY , " +
                "name VARCHAR(10) , " +
                "department VARCHAR(100) ," +
                "section VARCHAR(100) ," +
                "year INT";

        try (Connection conn = db.getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.execute(sql);// nonestatic
            System.out.println("student table ready ...");
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    public static void addStudent(stu s) {
        String sql = "INSERT INTO student (id, name , department , section, year) VALUES (?,?,?,?,?)" +
                "ON DUPLICATE KEY UPDATE name=VALUES(name), department = VALUES(department), " +
                "section = VALUES(section) , year = VALUES(year) ";
        try (Connection conn = db.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, s.id);
            pstmt.setString(2, s.name);
            pstmt.setString(3, s.department);
            pstmt.setString(4, s.section);
            pstmt.setInt(5, s.year);
            pstmt.executeUpdate();
            System.out.println("✅ Student added to DB");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getStudentList() {
        String sql = "SELECT * FROM student";
        StringBuilder sb = new StringBuilder();
        try (Connection conn = db.getConnection();
                Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                sb.append(rs.getInt("id")).append(" | ")
                  .append(rs.getString("name")).append(" | ")
                  .append(rs.getString("department")).append(" | ")
                  .append(rs.getString("section")).append(" | ")
                  .append(rs.getInt("year")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}