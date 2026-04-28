package NeonJava.HomeWork.chatApp;
import java.sql.*;

public class ChatDB {
    private static final String URL = "jdbc:mysql://localhost:3306/chatapp_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    public static void setupDatabase() {
        String query = "CREATE TABLE IF NOT EXISTS chat_messages (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "sender VARCHAR(255), " +
                "message TEXT, " +
                "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(query);
            System.out.println("Database table ready.");
        } catch (SQLException e) {
            System.out.println("Database setup error: " + e.getMessage());
        }
    }

}
