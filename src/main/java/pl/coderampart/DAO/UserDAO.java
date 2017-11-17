package pl.coderampart.DAO;

import pl.coderampart.services.Loggable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    private Connection connection;
    private AdminDAO adminDAO;
    private MentorDAO mentorDAO;
    private CodecoolerDAO codecoolerDAO;

    public UserDAO(Connection connectionToDB) {
        this.connection = connectionToDB;
        this.adminDAO = new AdminDAO( connectionToDB );
        this.mentorDAO = new MentorDAO( connectionToDB );
        this.codecoolerDAO = new CodecoolerDAO( connectionToDB );
    }

    public String getUserHashedPassword(String userType, String email) throws SQLException {
        String query = "SELECT password FROM ?s WHERE email = ?;";

        PreparedStatement statement = connection.prepareStatement( query );
        statement.setString(1, userType);
        statement.setString(2, email);
        ResultSet resultSet = statement.executeQuery();

        return resultSet.getString( "password" );
    }

    public Loggable getLoggedUser(String userType, String email) throws SQLException {
        String query = "SELECT * FROM ?s WHERE email = ?;";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, userType);
        statement.setString(2, email);
        ResultSet resultSet = statement.executeQuery();

        if (userType.equals( "admin" )) {
            return (Loggable) adminDAO.createAdminFromResultSet(resultSet);
        } else if (userType.equals( "mentor" )) {
            return (Loggable) mentorDAO.createMentorFromResultSet(resultSet);
        } else if (userType.equals( "codecooler" )) {
            return (Loggable) codecoolerDAO.createCodecoolerFromResultSet(resultSet);
        }
        return null;
    }

}
