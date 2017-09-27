package pl.coderampart.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import pl.coderampart.model.*;
import pl.coderampart.services.User;

public class MentorDAO extends AbstractDAO implements User<Mentor> {

    public Mentor getLogged(String email, String password) {
        Mentor mentor = null;

        try {
            Connection connection = this.connectToDataBase();
            String query = "SELECT * FROM mentors WHERE email = ? AND password = ?;";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            mentor = this.createMentorFromResultSet(resultSet);

        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

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
            String query = "INSERT INTO mentors VALUES (?, ?, ?, ?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, mentor.getID());
            statement.setString(2, mentor.getFirstName());
            statement.setString(3, mentor.getLastName());
            statement.setString(4, mentor.getEmail());
            statement.setString(5, mentor.getPassword());
            statement.setString(6, mentor.getDateOfBirth().toString());

            statement.executeUpdate();

            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public void update(Mentor mentor) {
        try {
            Connection connection = this.connectToDataBase();
            String query = "UPDATE mentors SET first_name = ?, " +
                    "last_name = ?, email = ?, password = ?, " +
                    "date_of_birth = ?;";

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
            String query = "DELETE FROM mentors WHERE ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, mentor.getID());
            statement.executeUpdate();

            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    private PreparedStatement setPreparedStatement(PreparedStatement statement, Mentor mentor) throws Exception {
        statement.setString(1, mentor.getID());
        statement.setString(2, mentor.getFirstName());
        statement.setString(3, mentor.getLastName());
        statement.setString(4, mentor.getEmail());
        statement.setString(5, mentor.getPassword());
        statement.setString(6, mentor.getDateOfBirth().toString());

        return statement;
    }

    private Mentor createMentorFromResultSet(ResultSet resultSet) throws Exception {
        String ID = resultSet.getString("id");
        String first_name = resultSet.getString("first_name");
        String last_name = resultSet.getString("last_name");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        String dateOfBirth = resultSet.getString("date_of_birth");
        LocalDate dateOfBirthObject = LocalDate.parse(dateOfBirth);

        return new Mentor(ID, first_name, last_name, email, password, dateOfBirthObject);
    }
}