package Lab2_DataBase.withGUI;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;
import java.sql.*;

public class tea implements Serializable {
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




}