package pl.coderampart.DAO;

import pl.coderampart.model.Artifact;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ArtifactDAO extends AbstractDAO {

    public Artifact getByID(String ID) {
        Artifact artifact = null;

        try {
            Connection connection = this.connectToDataBase();
            String query = "SELECT * FROM artifacts WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, ID);
            ResultSet resultSet = statement.executeQuery();

            artifact = this.createArtifactFromResultSet(resultSet);
            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

        return artifact;
    }

    public ArrayList<Artifact> readAll() {
        ArrayList<Artifact> artifactList = new ArrayList<>();

        try {
            Connection connection = this.connectToDataBase();
            String query = "SELECT * FROM artifacts;";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Artifact artifact = this.createArtifactFromResultSet(resultSet);
                artifactList.add(artifact);
            }
            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

        return artifactList;
    }

    public void create(Artifact artifact) {
        try {
            Connection connection = this.connectToDataBase();
            String query = "INSERT INTO artifacts VALUES (?, ?, ?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(query);
            PreparedStatement setStatement = setPreparedStatement(statement, artifact);
            setStatement.executeUpdate();

            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public void update(Artifact artifact) {
        try {
            Connection connection = this.connectToDataBase();
            String query = "UPDATE artifacts SET id = ?, name = ?, type = ?, description = ?, value = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            PreparedStatement setStatement = setPreparedStatement(statement, artifact);
            setStatement.executeUpdate();

            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public void delete(Artifact artifact) {
        try {
            Connection connection = this.connectToDataBase();
            String query = "DELETE FROM artifacts WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, artifact.getID());
            statement.executeUpdate();

            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    private PreparedStatement setPreparedStatement(PreparedStatement statement, Artifact artifact) throws Exception {
        statement.setString(1, artifact.getID());
        statement.setString(2, artifact.getName());
        statement.setString(3, artifact.getDescription());
        statement.setString(4, artifact.getType());
        statement.setInt(5, artifact.getValue());

        return statement;
    }

    private Artifact createArtifactFromResultSet(ResultSet resultSet) throws Exception {
        String ID = resultSet.getString("id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        String type = resultSet.getString("type");
        Integer value = resultSet.getInt("value");

        return new Artifact(ID, name, description, type, value);
    }
}