package pl.coderampart.DAO;

import java.sql.Connection;

public class FundraisersDAO extends AbstractDAO {

    private Connection connection;

    public FundraisersDAO(Connection connectionToDB) {
        connection = connectionToDB;
    }


}