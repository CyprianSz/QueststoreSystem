package pl.coderampart.DAO;

import java.util.ArrayList;
import java.sql.*;
import java.io.IOException;
import java.io.File;

public abstract class AbstractDAO<T> {

    public abstract ArrayList<T> readAll();
    public abstract void create(T);
    public abstract void update(T);
    public abstract void delete(T);

    public Connection connectToDataBase() throws Exception {
        Connection connection = null; // TODO: check if necessary

        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:../../../main/resources/db/codecoolers.db");

        return connection;
    }

    public ResultSet getResultSet(String sql) throws Exception {
        Connection connection = this.connectToDataBase();
        Statement statement = connection.createStatement();

        return statement.executeQuery(sql);
    }

    public void insertUpdateDelete(String sql) {
        Statement statement;
        Connection connection = this.connectToDataBase();

        try {
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            statement.executeUpdate(sql);
            statement.close();
            connection.commit();
            connection.close();
            view.output("Operation done successfully");
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

}