package pl.coderampart.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionToDB {

    public static Connection getConnection() {
        try {
            Class.forName( "org.sqlite.JDBC" );
            return DriverManager.getConnection( "jdbc:sqlite:src/main/resources/db/quest_store.db" );
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
