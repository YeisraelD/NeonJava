package Lab2_DataBase.withGUI;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.sql.*;

public class stu implements Serializable {
    public int id;
    public String name;
    public String department;
    public String section;
    public int year;

    public stu(int id, String name, String depatment, String section, int year) throws RemoteException {
        super(); // tells
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