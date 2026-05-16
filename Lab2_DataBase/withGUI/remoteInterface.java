
package Lab2_DataBase.withGUI;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface remoteInterface extends Remote {
    public void addStudent(stu s)throws RemoteException;
    public void addTeacher(tea t) throws RemoteException;
    public String getStudentList() throws RemoteException;
    public String getTeacherList() throws RemoteException;
}
