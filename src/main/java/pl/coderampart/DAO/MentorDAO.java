package pl.coderampart.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import pl.coderampart.model.*;

public class MentorDAO extends AbstractDAO {

    private GroupDAO groupDAO;
    private Connection connection;

    public MentorDAO(Connection connectionToDB) {
        connection = connectionToDB;
        groupDAO = new GroupDAO(connection);
    }

    public Mentor getLogged(String email) throws SQLException{
        String query = "SELECT * FROM mentors WHERE email = ?;";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, email);
        ResultSet resultSet = statement.executeQuery();

        return this.createMentorFromResultSet(resultSet);
    }

    public ArrayList<Mentor> readAll() throws SQLException{
        ArrayList<Mentor> mentorList = new ArrayList<>();
        String query = "SELECT * FROM mentors;";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Mentor mentor = this.createMentorFromResultSet(resultSet);
            mentorList.add(mentor);
        }
        return mentorList;
    }

    public void create(Mentor mentor) throws SQLException {
        String query = "INSERT INTO mentors (first_name, last_name, date_of_birth, email, password, group_id, id) "
                         + "VALUES (?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement statement = connection.prepareStatement(query);
        PreparedStatement setStatement = setPreparedStatement(statement, mentor);
        setStatement.executeUpdate();
    }

    public void update(Mentor mentor) throws SQLException{
        String query = "UPDATE mentors SET first_name = ?, " +
                    "last_name = ?, date_of_birth = ?, email = ?, " +
                    "password = ?, group_id = ? WHERE id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        PreparedStatement setStatement = setPreparedStatement(statement, mentor);
        setStatement.executeUpdate();
    }

    public void delete(Mentor mentor) throws SQLException {
        String query = "DELETE FROM mentors WHERE id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, mentor.getID());
        statement.executeUpdate();
    }

    private PreparedStatement setPreparedStatement(PreparedStatement statement, Mentor mentor) throws SQLException {

        statement.setString(1, mentor.getFirstName());
        statement.setString(2, mentor.getLastName());
        statement.setString(3, mentor.getDateOfBirth().toString());
        statement.setString(4, mentor.getEmail());
        statement.setString(5, mentor.getPassword());
        statement.setString(6, mentor.getGroup().getID());
        statement.setString(7, mentor.getID());

        return statement;
    }

    public Mentor createMentorFromResultSet(ResultSet resultSet) throws SQLException {
        String ID = resultSet.getString("id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String dateOfBirth = resultSet.getString("date_of_birth");
        LocalDate dateOfBirthObject = LocalDate.parse(dateOfBirth);
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        String groupID = resultSet.getString("group_id");
        Group groupObject = groupDAO.getByID(groupID);

        return new Mentor(ID, firstName, lastName, dateOfBirthObject, email, password, groupObject);
    }

    public String getHashedPassword(String email) throws SQLException {
        String query = "SELECT password FROM mentors WHERE email = ?;";

        PreparedStatement statement = connection.prepareStatement( query );
        statement.setString(1, email);
        ResultSet resultSet = statement.executeQuery();

        return resultSet.getString("password");
    }
}