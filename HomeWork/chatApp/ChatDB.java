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

    public static void saveMessage(String sender, String content) {
        String query = "INSERT INTO chat_messages (sender, message) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, sender);
            pstmt.setString(2, content);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saving message: " + e.getMessage());
        }
    }


    public static java.util.List<String> getMessageHistory() {
        java.util.List<String> history = new java.util.ArrayList<>();
        String query = "SELECT sender, message FROM chat_messages ORDER BY timestamp DESC LIMIT 20";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String entry = rs.getString("sender") + ": " + rs.getString("message");
                history.add(entry);
            }
        } catch (SQLException e) {
            e.printStackTrace(); //log error
        }
        java.util.Collections.reverse(history);
        return history;
    }

}
