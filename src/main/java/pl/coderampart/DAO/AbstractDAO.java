package pl.coderampart.DAO;

import java.sql.*;

abstract class AbstractDAO {


    private static Connection connection = null;


    public Connection getInstance() throws SQLException{

        if (connection == null) {

            connection = connectToDataBase();
        }
        return connection;
    }

    static Connection connectToDataBase() throws SQLException {

        try {

        Class.forName("org.sqlite.JDBC");
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/db/quest_store.db");

        return connection;
    }
}