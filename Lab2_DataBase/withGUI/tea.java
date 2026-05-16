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

}