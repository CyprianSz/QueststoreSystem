package pl.coderampart.DAO;

import pl.coderampart.model.Codecooler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class FundraisingsDAO extends AbstractDAO {

    private Connection connection;

    public FundraisingsDAO(Connection connectionToDB) {
        connection = connectionToDB;
    }

    public List<Fundraising> readAll() throws SQLException {

        ArrayList<Fundraising> fundraisingsList = new ArrayList<>();

        String query = "SELECT * FROM fundraisings;";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Fundraising fundraising = this.createFundraisingFromResultSet(resultSet);
            fundraisingsList.add(fundraising);
        }
        return fundraisingsList;
    }

    public Codecooler getByID(String ID) throws SQLException {

        String query = "SELECT * FROM fundraisings WHERE id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, ID);
        ResultSet resultSet = statement.executeQuery();

        return this.createFundraisingFromResultSet(resultSet);;
    }

    public void create(Fundraising fundraising) throws SQLException {

        String query = "INSERT INTO fundraisings (id, name, creation_date, creator_id, is_open) VALUES (?, ?, ?, ?, ?);";
        PreparedStatement statement = connection.prepareStatement(query);
        PreparedStatement setStatement = setPreparedStatement(statement, codecooler);
        setStatement.executeUpdate();
    }


    public void delete(Fundraising fundraising) throws SQLException {
        String query = "DELETE FROM fundraisings WHERE id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, fundraising.getID());
        statement.executeUpdate();
    }

    private PreparedStatement setPreparedStatement(PreparedStatement statement, Fundraising fundraising) throws SQLException {
        statement.setString(1, fundraising.getID());
        statement.setString(2, fundraising.getName());
        statement.setString(3, fundraising.getCreationDate().toString());
        statement.setString(2, fundraising.getCreatorID());
        statement.setBoolean(5, fundraising.getIsOpen());

        return statement;
    }

    public Fundraising createFundraisingFromResultSet(ResultSet resultSet) throws SQLException {
        String ID = resultSet.getString("id");
        String name = resultSet.getString("name");
        String creationDate = resultSet.getString("creation_date");
        LocalDate creationDateObject = LocalDate.parse(creationDate);
        String creatorID = resultSet.getString("creator_id");
        Boolean isOpen = resultSet.getBoolean( "is_open" );

        return new Foundraising(ID, name, creationDateObject, creatorID, isOpen);
    }
}
