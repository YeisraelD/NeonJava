package Lab2_DataBase.withGUI;

import java.sql.*;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class remoteObj extends UnicastRemoteObject implements remoteInterface {
    public remoteObj() throws RemoteException {
        super();
        initializeDatabase();
    }

    private void initializeDatabase() {
        String createStudent = "CREATE TABLE IF NOT EXISTS student (" +
                "id INT PRIMARY KEY , " +
                "name VARCHAR(10) , " +
                "department VARCHAR(100) ," +
                "section VARCHAR(100) ," +
                "year INT)";

        String createTeacher = "CREATE TABLE IF NOT EXISTS teachers (" +
                "id INT PRIMARY KEY, " +
                "name VARCHAR(100), " +
                "department VARCHAR(100))";

        try (Connection conn = db.getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.execute(createStudent);
            stmt.execute(createTeacher);
            System.out.println("database ready ...");
        } catch (SQLException e) {
            System.err.println("Error creating table: " + e.getMessage());
        }
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
            broadcastNotification("a new student named " + s.name + "was added");
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
            System.out.println(" teacher added to DB");
            pstmt.executeUpdate();
            broadcastNotification("a new teacher named " + t.name + "was added");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getStudentList() throws RemoteException {
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

    public String getTeacherList() throws RemoteException {
        String sql = "SELECT * FROM teachers";
        StringBuilder sb = new StringBuilder();
        try (Connection conn = db.getConnection();
                Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                sb.append(rs.getInt("id")).append(" | ")
                        .append(rs.getString("name")).append(" | ")
                        .append(rs.getString("department")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private transient List<PrintWriter> clients = new ArrayList<>();

    public void addTCPClient(Socket clientSocket) {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            clients.add(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void broadcastNotification(String message) {
        for (PrintWriter c : clients) {
            c.println(message);
        }
    }

}
