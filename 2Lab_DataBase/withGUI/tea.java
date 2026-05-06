import java.util.*;
import java.sql.*;

public class tea {
    public int id;
    public String name;
    public String department;

    public tea(int id, String name, String department) {
        this.id = id;
        this.name = name;
        this.department = department;
    }

    public String toString(int id, String name, String department) {
        return id + " , " + name + " , " + department;
    }

    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS teachers (" +
                "id INT PRIMARY KEY, " +
                "name VARCHAR(100), " +
                "department VARCHAR(100))";

        try (Connection conn = db.getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("teacher table ready ...");
        } catch (SQLException e) {
            System.err.println("Error reading teacher data: " + e.getMessage());
        }
    }

    public static void addTeacher(tea t) {
        String sql = "INSERT INTO teachers (id, name, department) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department)";
        try (Connection conn = db.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, t.id);
                pstmt.setString(2, t.name);
                pstmt.setString(3, t.department);
                pstmt.executeUpdate();

        } catch (SQLException e) {
            e.getMessage();
        }
    }

    public static String getTeacherList() {
        String sql = "SELECT * FROM teachers";
        StringBuilder sb = new StringBuilder();
        try (Connection conn = db.getConnection();
                Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                sb.append(rs.getInt("id")).append(" | ")
                        .append(rs.getString("name")).append(" | ")
                        .append(rs.getString("department")).append(" | ").append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}