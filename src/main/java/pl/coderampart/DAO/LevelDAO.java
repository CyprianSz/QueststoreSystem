package pl.coderampart.DAO;

import pl.coderampart.model.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LevelDAO extends AbstractDAO {

    private Connection connection;

    public LevelDAO(Connection connectionToDB) {

        connection = connectionToDB;
    }

    public ArrayList<Level> readAll() throws SQLException {
        ArrayList<Level> levelList = new ArrayList<>();

        String query = "SELECT * FROM levels ORDER BY required_experience;";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Level level = this.createLevelFromResultSet(resultSet);
            levelList.add(level);
        }
        return levelList;
    }

    public Level getByID(String ID) throws SQLException {

        Level level = null;
        String query = "SELECT * FROM levels WHERE id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, ID);
        ResultSet resultSet = statement.executeQuery();

        level = this.createLevelFromResultSet(resultSet);

        return level;
    }

    public Level getFirstLevel() throws SQLException {

        Level level = null;
        String query = "SELECT * FROM levels WHERE rank = 0;";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        level = this.createLevelFromResultSet(resultSet);

        return level;
    }

    public void create(Level level) throws SQLException {

        String query = "INSERT INTO levels (rank, required_experience, description, id) VALUES (?, ?, ?, ?);";
        PreparedStatement statement = connection.prepareStatement(query);
        PreparedStatement setStatement = setPreparedStatement(statement, level);
        statement.executeUpdate();

    }


    public void update(Level level) throws SQLException {

        String query = "UPDATE levels SET rank = ?, required_experience = ?, description = ? WHERE id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        PreparedStatement setStatement = setPreparedStatement(statement, level);
        setStatement.executeUpdate();
    }


    public void delete(Level level) throws SQLException {

        String query = "DELETE FROM levels WHERE id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, level.getID());
        statement.executeUpdate();
    }


    private PreparedStatement setPreparedStatement(PreparedStatement statement, Level level) throws SQLException {
        statement.setInt(1, level.getRank());
        statement.setInt(2, level.getRequiredExperience());
        statement.setString(3, level.getDescription());
        statement.setString(4, level.getID());

        return statement;
    }

    private Level createLevelFromResultSet(ResultSet resultSet) throws SQLException {
        String ID = resultSet.getString("id");
        Integer rank = resultSet.getInt("rank");
        Integer requiredExperience = resultSet.getInt("required_experience");
        String description = resultSet.getString("description");

        return new Level(ID, rank, requiredExperience, description);
    }
}