
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String url = "jdbc:mysql://localhost:3306/University_db";
    private static final String user = "root";
    private static final String password = "";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Load the driver (important for some older setups)
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            System.out.println(" Database Connected Successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println(" MySQL Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println(" Database Connection Failed!");
            e.printStackTrace(); // This will tell us if it's a wrong password or missing DB
        }
        return conn;
    }
}