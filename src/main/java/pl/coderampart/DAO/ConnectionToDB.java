package pl.coderampart.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionToDB {

    private static ConnectionToDB instance = null;
    private static Connection connection;

    public ConnectionToDB() {}

    public static ConnectionToDB getInstance() {
        if (instance == null) {
            try {
                instance = new ConnectionToDB();
            } catch (Exception e) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            }
        }
        return instance;
    }


    public Connection connectToDataBase() throws SQLException {

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/db/quest_store.db");

        return connection;
    }

}
