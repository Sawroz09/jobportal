import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class dbconn {

    final static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/jobportal1";
    static final String USERNAME = "root";
    static final String PASSWORD = "";

    public static Connection connection() {
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            return conn;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    }
}
