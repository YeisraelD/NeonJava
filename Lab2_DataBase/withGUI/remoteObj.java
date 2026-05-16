package Lab2_DataBase.withGUI;
import java.sql.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class remoteObj extends UnicastRemoteObject implements remoteInterface {
    public remoteObj() throws RemoteException {
        super();
    }

    public void addStudent(stu s) throws RemoteException {
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
            System.out.println(" Student added to DB");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addTeacher(tea t) throws RemoteException {
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
    public  String getStudentList() {
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
    public String getTeacherList() {
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
