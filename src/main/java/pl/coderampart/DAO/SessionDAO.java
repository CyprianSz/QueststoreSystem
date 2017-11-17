package pl.coderampart.DAO;

import pl.coderampart.model.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SessionDAO {

    private Connection connection;

    public SessionDAO(Connection connectionToDB) {
        connection = connectionToDB;
    }

    public Session getByID(String ID) throws SQLException, ClassNotFoundException {
        String query = "SELECT * FROM sessions WHERE id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, ID);
        ResultSet resultSet = statement.executeQuery();

        return this.createSessionFromResultSet(resultSet);
    }

    public void create(Session session) throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO sessions (id, user_id, user_first_name, user_last_name, user_email, user_type) " +
                       "VALUES (?, ?, ?, ?, ?, ?);";

        PreparedStatement statement = connection.prepareStatement(query);
        PreparedStatement setStatement = setPreparedStatement(statement, session);
        statement.executeUpdate();
    }

    public void deleteByID(String sessionID) throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM sessions WHERE id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, sessionID);
        statement.executeUpdate();
    }

    private PreparedStatement setPreparedStatement(PreparedStatement statement, Session session) throws SQLException {
        statement.setString(1, session.getID());
        statement.setString(2, session.getUserID());
        statement.setString(3, session.getUserFirstName());
        statement.setString(4, session.getUserLastName());
        statement.setString(5, session.getUserEmail());
        statement.setString(6, session.getUserType());

        return statement;
    }

    private Session createSessionFromResultSet(ResultSet resultSet) {
        try {
            String ID = resultSet.getString( "id" );
            String userID = resultSet.getString( "user_id" );
            String userFirstName = resultSet.getString( "user_first_name" );
            String userLastName = resultSet.getString( "user_last_name" );
            String userEmail = resultSet.getString( "user_email" );
            String userType = resultSet.getString( "user_type" );
            
            return new Session(ID, userID, userFirstName, userLastName, userEmail, userType);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

