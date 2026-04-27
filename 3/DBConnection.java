import java.sql.Connection;
import  java.sql.DriverManager;
import  java.sql.SQLException;

public class DBConnection {
    private static final String url="jdbc:mysql://localhost:3306/university_db";
    private static final String user="root";
    private static final String  password="";

    public static Connection getConnection(){
        Connection conn = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("connected ");
        }catch (ClassNotFoundException e){
            e.getMessage();
        }catch (SQLException e){
            e.getMessage();
        }
        return conn;

    }
}