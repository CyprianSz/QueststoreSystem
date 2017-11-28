package pl.coderampart.DAO;

import pl.coderampart.model.Artifact;
import pl.coderampart.model.Codecooler;
import pl.coderampart.model.Fundraising;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FundraisingDAO extends AbstractDAO {

    private Connection connection;
    private ArtifactDAO artifactDAO;
    private CodecoolerDAO codecoolerDAO;

    public FundraisingDAO(Connection connectionToDB) {
        this.connection = connectionToDB;
        this.artifactDAO = new ArtifactDAO( connection );
        this.codecoolerDAO = new CodecoolerDAO( connection );
    }

    public List<Fundraising> readAll() throws SQLException {
        List<Fundraising> fundraisingsList = new ArrayList<>();

        String query = "SELECT * FROM fundraisings ORDER BY date(creation_date) DESC;";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Fundraising fundraising = this.createFundraisingFromResultSet(resultSet);
            fundraisingsList.add(fundraising);
        }
        return fundraisingsList;
    }

    public Fundraising getByID(String ID) throws SQLException {
        String query = "SELECT * FROM fundraisings WHERE id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, ID);
        ResultSet resultSet = statement.executeQuery();

        return this.createFundraisingFromResultSet(resultSet);
    }


    public Fundraising getOpenFundraisingsByID(String ID) throws SQLException {
        String query = "SELECT * FROM fundraisings WHERE id = ? AND is_open = 1;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, ID);
        ResultSet resultSet = statement.executeQuery();

        return this.createFundraisingFromResultSet(resultSet);
    }

    public void create(Fundraising fundraising) throws SQLException {
        String query = "INSERT INTO fundraisings (artifact_id, name, creation_date, creator_id, is_open, id) " +
                       "VALUES (?, ?, ?, ?, ?, ?);";
        PreparedStatement statement = connection.prepareStatement(query);
        setPreparedStatement(statement, fundraising);
        statement.executeUpdate();
    }

    public void update(Fundraising fundraising) throws SQLException{
        String query = "UPDATE fundraisings SET artifact_id = ?, " +
                "name = ?, creation_date = ?, creator_id = ?, " +
                "is_open = ? WHERE id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        setPreparedStatement(statement, fundraising);
        statement.executeUpdate();
    }

    public void delete(Fundraising fundraising) throws SQLException {
        String query = "DELETE FROM fundraisings WHERE id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, fundraising.getID());
        statement.executeUpdate();
    }

    private void setPreparedStatement(PreparedStatement statement, Fundraising fundraising) throws SQLException {
        statement.setString(1, fundraising.getArtifact().getID());
        statement.setString(2, fundraising.getName());
        statement.setString(3, fundraising.getCreationDate().toString());
        statement.setString(4, fundraising.getCreator().getID());
        statement.setBoolean(5, fundraising.getIsOpen());
        statement.setString(6, fundraising.getID());
    }

    private Fundraising createFundraisingFromResultSet(ResultSet resultSet) throws SQLException {
        String ID = resultSet.getString("id");
        String artifactID = resultSet.getString( "artifact_id" );
        Artifact artifact = artifactDAO.getByID(artifactID);
        String name = resultSet.getString("name");
        String creationDate = resultSet.getString("creation_date");
        LocalDate creationDateObject = LocalDate.parse(creationDate);
        String creatorID = resultSet.getString("creator_id");
        Codecooler creator = codecoolerDAO.getByID(creatorID);
        Boolean isOpen = resultSet.getBoolean( "is_open" );

        return new Fundraising(ID, artifact, name, creationDateObject, creator, isOpen);
    }
}
