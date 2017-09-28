package pl.coderampart.DAO;

import java.sql.*;

abstract class AbstractDAO {

    Connection connectToDataBase() throws Exception {
        Connection connection = null;

        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:../../../main/resources/db/codecoolers.db");

        return connection;
    }
}