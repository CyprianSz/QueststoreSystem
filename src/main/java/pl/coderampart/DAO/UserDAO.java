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
        String query = "SELECT password FROM " + userType + "s WHERE email = ?;";

        PreparedStatement statement = connection.prepareStatement( query );
        statement.setString( 1, email );
        ResultSet resultSet = statement.executeQuery();

        return resultSet.getString( "password" );
    }

    public Loggable getLoggedUser(String userType, String email) throws SQLException {
        String query = "SELECT * FROM " + userType + "s WHERE email = ?;";

        PreparedStatement statement = connection.prepareStatement( query );
        statement.setString( 1, email );
        ResultSet resultSet = statement.executeQuery();

        switch (userType) {
            case "admin":
                return adminDAO.createAdminFromResultSet( resultSet );
            case "mentor":
                return mentorDAO.createMentorFromResultSet( resultSet );
            case "codecooler":
                return codecoolerDAO.createCodecoolerFromResultSet( resultSet );
        }
        return null;
    }

    public void updateUserPassword(Loggable user, String newHashedPassword) throws SQLException {
        String userTable = user.getType().toLowerCase() + "s";
        String userID = user.getID();

        String query = "UPDATE " + userTable + " SET password = \"" + newHashedPassword + "\" WHERE id = \"" + userID + "\";";

        PreparedStatement statement = connection.prepareStatement( query );
        statement.executeUpdate();
    }
}