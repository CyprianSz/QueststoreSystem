package pl.coderampart.DAO;

import pl.coderampart.model.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class LevelDAO extends AbstractDAO {

    public ArrayList<Level> readAll() {
        ArrayList<Level> levelList = new ArrayList<>();

        try {
            Connection connection = this.connectToDataBase();
            String query = "SELECT * FROM levels;";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Level level = this.createLevelFromResultSet(resultSet);
                levelList.add(level);
            }
            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

        return levelList;
    }


    public Level getFirstLevel() {
        Level level = null;

        try {
            Connection connection = this.connectToDataBase();
            String query = "SELECT * FROM levels WHERE rank = 0;";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            level = this.createLevelFromResultSet(resultSet);

            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

        return level;
    }

    public void create(Level level) {
        try {
            Connection connection = this.connectToDataBase();
            String query = "INSERT INTO levels VALUES (?, ?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(query);
            PreparedStatement setStatement = setPreparedStatement(statement, level);
            statement.executeUpdate();

            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public void update(Level level) {
        try {
            Connection connection = this.connectToDataBase();
            String query = "UPDATE levels SET id = ?, rank = ?, required_experience = ?, description = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            PreparedStatement setStatement = setPreparedStatement(statement, level);
            setStatement.executeUpdate();

            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public void delete(Level level) {
        try {
            Connection connection = this.connectToDataBase();
            String query = "DELETE FROM levels WHERE ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, level.getID());
            statement.executeUpdate();

            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    private PreparedStatement setPreparedStatement(PreparedStatement statement, Level level) throws Exception {
        statement.setString(1, level.getID());
        statement.setInt(2, level.getRank());
        statement.setInt(3, level.getRequiredExperience());
        statement.setString(4, level.getDescription());

        return statement;
    }

    private Level createLevelFromResultSet(ResultSet resultSet) throws Exception {
        String ID = resultSet.getString("id");
        Integer rank = resultSet.getInt("rank");
        Integer requiredExperience = resultSet.getInt("required_experience");
        String description = resultSet.getString("description");


        return new Level(ID, rank, requiredExperience, description);
    }
}