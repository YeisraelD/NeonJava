import java.util.*;
import java.sql.*;

public class Teacher {
    int id;
    String name;
    String department;

    Teacher(int sid, String n, String dep) {
        this.id = sid;
        this.name = n;
        this.department = dep;
    }

    public String toString() {
        return id + "," + name + "," + department;
    }
    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS teachers (" +
                "id INT PRIMARY KEY, " +
                "name VARCHAR(100), " +
                "department VARCHAR(100))";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Teachers table ready.");
        } catch (SQLException e) {
            System.err.println("Error creating table: " + e.getMessage());
        }
    }
    public static void addTeacher(List<Teacher> list) {
        String sql = "INSERT INTO teachers (id, name, department) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (Teacher t : list) {
                pstmt.setInt(1, t.id);
                pstmt.setString(2, t.name);
                pstmt.setString(3, t.department);
                pstmt.executeUpdate();
            }
            System.out.println("Teacher data saved to database.");
        } catch (SQLException e) {
            System.err.println("Error saving teacher data: " + e.getMessage());
        }
    }

    public static void showTeacher() {
        String sql = "SELECT * FROM teachers";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("--- Teacher Records ---");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " | " +
                        rs.getString("name") + " | " +
                        rs.getString("department"));
            }
        } catch (SQLException e) {
            System.err.println("Error reading teacher data: " + e.getMessage());
        }
    }
}