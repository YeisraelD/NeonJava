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

}
