package pl.coderampart.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import pl.coderampart.model.*;
import pl.coderampart.services.User;

public class CodecoolerDAO extends AbstractDAO implements User<Codecooler> {

    public Codecooler getLogged(String email, String password) {
        Codecooler codecooler = null;

        try {
            Connection connection = this.connectToDataBase();
            String query = "SELECT * FROM codecoolers WHERE email = ? AND password = ?;";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            codecooler = this.createCodecoolerFromResultSet(resultSet);

        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

        return codecooler;
    }

    public ArrayList<Codecooler> readAll() {
        ArrayList<Codecooler> codecoolerList = new ArrayList<>();

        try {
            Connection connection = this.connectToDataBase();
            String query = "SELECT * FROM codecoolers;";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Codecooler codecooler = this.createCodecoolerFromResultSet(resultSet);
                codecoolerList.add(codecooler);
            }
            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

        return codecoolerList;
    }

    public void create(Codecooler codecooler) {
        try {
            Connection connection = this.connectToDataBase();
            String query = "INSERT INTO codecoolers VALUES (?, ?, ?, ?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(query);
            PreparedStatement setStatement = setPreparedStatement(statement, codecooler);
            setStatement.executeUpdate();

            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public void update(Codecooler codecooler) {
        try {
            Connection connection = this.connectToDataBase();
            String query = "UPDATE codecoolers SET id = ?, first_name = ?, " +
                           "last_name = ?, email = ?, password = ?, " +
                           "date_of_birth = ?;";

            PreparedStatement statement = connection.prepareStatement(query);
            PreparedStatement setStatement = setPreparedStatement(statement, codecooler);
            setStatement.executeUpdate();

            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public void delete(Codecooler codecooler) {
        try {
            Connection connection = this.connectToDataBase();
            String query = "DELETE FROM codecoolers WHERE ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, codecooler.getID());
            statement.executeUpdate();

            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    private PreparedStatement setPreparedStatement(PreparedStatement statement, Codecooler codecooler) throws Exception {
        statement.setString(1, codecooler.getID());
        statement.setString(2, codecooler.getFirstName());
        statement.setString(3, codecooler.getLastName());
        statement.setString(4, codecooler.getEmail());
        statement.setString(5, codecooler.getPassword());
        statement.setString(6, codecooler.getDateOfBirth().toString());

        return statement;
    }

    private Codecooler createCodecoolerFromResultSet(ResultSet resultSet) throws Exception {
        String ID = resultSet.getString("id");
        String first_name = resultSet.getString("first_name");
        String last_name = resultSet.getString("last_name");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        String dateOfBirth = resultSet.getString("date_of_birth");
        LocalDate dateOfBirthObject = LocalDate.parse(dateOfBirth);

        return new Codecooler(ID, first_name, last_name, email, password, dateOfBirthObject);
    }
}