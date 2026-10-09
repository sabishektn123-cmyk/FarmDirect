package farmdirect.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:sqlite:farmdirect.db";

    private DatabaseConnection() {
        // Prevent object creation
    }

    public static Connection getConnection()
            throws SQLException {

        return DriverManager.getConnection(URL);
    }
}