package pl.coderampart.DAO;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import pl.coderampart.model.*;
import pl.coderampart.services.User;
import pl.coderampart.view.View;

public class MentorDAO extends AbstractDAO implements User<Mentor> {

    private GroupDAO groupDAO = new GroupDAO();
    private View view = new View();

    public Mentor getLogged(String email, String password) throws Exception{
        Mentor mentor = null;

        Connection connection = this.connectToDataBase();
        String query = "SELECT * FROM mentors WHERE email = ? AND password = ?;";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, email);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();

        mentor = this.createMentorFromResultSet(resultSet);
        connection.close();

        return mentor;
    }

    public ArrayList<Mentor> readAll() {
        ArrayList<Mentor> mentorList = new ArrayList<>();

        try {
            Connection connection = this.connectToDataBase();
            String query = "SELECT * FROM mentors;";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Mentor mentor = this.createMentorFromResultSet(resultSet);
                mentorList.add(mentor);
            }
            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

        return mentorList;
    }

    public void create(Mentor mentor) {
        try {
            Connection connection = this.connectToDataBase();
            String query = "INSERT INTO mentors (first_name, last_name, date_of_birth, email, password, group_id, id) "
                         + "VALUES (?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(query);
            PreparedStatement setStatement = setPreparedStatement(statement, mentor);

            setStatement.executeUpdate();

            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public void update(Mentor mentor) {
        try {
            Connection connection = this.connectToDataBase();
            String query = "UPDATE mentors SET first_name = ?, " +
                    "last_name = ?, date_of_birth = ?, email = ?, " +
                    "password = ?, group_id = ? WHERE id = ?;";

            PreparedStatement statement = connection.prepareStatement(query);
            PreparedStatement setStatement = setPreparedStatement(statement, mentor);
            setStatement.executeUpdate();

            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public void delete(Mentor mentor) {
        try {
            Connection connection = this.connectToDataBase();
            String query = "DELETE FROM mentors WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, mentor.getID());
            statement.executeUpdate();

            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    private PreparedStatement setPreparedStatement(PreparedStatement statement, Mentor mentor) throws Exception {
        statement.setString(1, mentor.getFirstName());
        statement.setString(2, mentor.getLastName());
        statement.setString(3, mentor.getDateOfBirth().toString());
        statement.setString(4, mentor.getEmail());
        statement.setString(5, mentor.getPassword());
        statement.setString(6, mentor.getGroup().getID());
        statement.setString(7, mentor.getID());

        return statement;
    }

    private Mentor createMentorFromResultSet(ResultSet resultSet) throws Exception {
        String ID = resultSet.getString("id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String dateOfBirth = resultSet.getString("date_of_birth");
        LocalDate dateOfBirthObject = view.stringToDate(dateOfBirth);
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        String groupID = resultSet.getString("group_id");
        Group groupObject = groupDAO.getByID(groupID);

        return new Mentor(ID, firstName, lastName, dateOfBirthObject, email, password, groupObject);
    }
}