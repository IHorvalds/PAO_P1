package orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBContext {
    
    private static final String DB_URL = "jdbc:mariadb://" + Setup.IP + ":3306/pao_project";
    private static final String USER = "java_project";
    private static final String PASSWORD = "bl4ck_arr0w";

    
    private DBContext() {}; // don't instantiate
    

    // Connection aquisition and release
    private static Connection _databaseConnection;

    public static Connection getDatabaseConnection() {
        try {
            if (_databaseConnection == null || _databaseConnection.isClosed()) {
                // Class.forName("com.mysql.jdbc.Driver"); 
                _databaseConnection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return _databaseConnection;
    }

    public static void closeDatabaseConnection() {
        try {
            if (_databaseConnection != null && !_databaseConnection.isClosed()) {
                _databaseConnection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
