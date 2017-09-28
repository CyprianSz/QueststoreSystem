package pl.coderampart.DAO;

import pl.coderampart.model.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class QuestDAO extends AbstractDAO {

    public ArrayList<Quest> readAll() {
        ArrayList<Quest> questList = new ArrayList<>();

        try {
            Connection connection = this.connectToDataBase();
            String query = "SELECT * FROM quests;";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Quest quest = this.createQuestFromResultSet(resultSet);
                questList.add(quest);
            }
            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

        return questList;
    }

    public void create(Quest quest) {
        try {
            Connection connection = this.connectToDataBase();
            String query = "INSERT INTO quests VALUES (?, ?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(query);
            PreparedStatement setStatement = setPreparedStatement(statement, quest);
            statement.executeUpdate();

            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public void update(Quest quest) {
        try {
            Connection connection = this.connectToDataBase();
            String query = "UPDATE quests SET id = ?, name = ?, description = ?, reward = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            PreparedStatement setStatement = setPreparedStatement(statement, quest);
            setStatement.executeUpdate();

            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public void delete(Quest quest) {
        try {
            Connection connection = this.connectToDataBase();
            String query = "DELETE FROM quests WHERE ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, quest.getID());
            statement.executeUpdate();

            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    private PreparedStatement setPreparedStatement(PreparedStatement statement, Quest quest) throws Exception {
        statement.setString(1, quest.getID());
        statement.setString(2, quest.getName());
        statement.setString(3, quest.getDescription());
        statement.setInt(4, quest.getReward());

        return statement;
    }

    private Quest createQuestFromResultSet(ResultSet resultSet) throws Exception {
        String ID = resultSet.getString("id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        Integer reward = resultSet.getInt("reward");


        return new Quest(ID, name, description, reward);
    }
}