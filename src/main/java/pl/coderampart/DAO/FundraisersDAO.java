package pl.coderampart.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FundraisersDAO extends AbstractDAO {

    private Connection connection;

    public FundraisersDAO(Connection connectionToDB) {
        connection = connectionToDB;
    }

    public List<String> getFundraisersIDs(String fundraisingID) throws SQLException {
        List<String> fundraisersIDsList = new ArrayList<>();

        String query = "SELECT fundraiser_id FROM fundraisers WHERE fundraising_id = ?";
        PreparedStatement statement = connection.prepareStatement( query );
        statement.setString( 1, fundraisingID );
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            String fundraiserID = resultSet.getString("fundraiser_id");
            fundraisersIDsList.add(fundraiserID);
        }
        return fundraisersIDsList;
    }

    public List<String> getFundraisingsIDs(String fundraiserID) throws SQLException {
        List<String> fundraisingsIDsList = new ArrayList<>();

        String query = "SELECT fundraising_id FROM fundraisers WHERE fundraiser_id = ?";
        PreparedStatement statement = connection.prepareStatement( query );
        statement.setString( 1, fundraiserID );
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            String fundraisingID = resultSet.getString("fundraising_id");
            fundraisingsIDsList.add(fundraisingID);
        }
        return fundraisingsIDsList;
    }

    public void create(String fundraiserID, String fundraisingID) throws SQLException {
        String query = "INSERT INTO fundraisers (fundraiser_id, fundraising_id) VALUES (?, ?);";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, fundraiserID);
        statement.setString(2, fundraisingID);
        statement.executeUpdate();
    }
}