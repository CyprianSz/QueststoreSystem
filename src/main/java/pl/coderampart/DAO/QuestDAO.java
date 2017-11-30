package pl.coderampart.DAO;

import pl.coderampart.model.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestDAO extends AbstractDAO {

    private Connection connection;

    public QuestDAO(Connection connectionToDB) {
        connection = connectionToDB;
    }

    public List<Quest> readAll() throws SQLException {
        List<Quest> questList = new ArrayList<>();

        String query = "SELECT * FROM quests;";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Quest quest = this.createQuestFromResultSet(resultSet);
            questList.add(quest);
        }
        return questList;
    }

    public Quest getByID(String ID) throws SQLException {
        Quest quest;
        String query = "SELECT * FROM quests WHERE id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, ID);
        ResultSet resultSet = statement.executeQuery();

        quest = this.createQuestFromResultSet(resultSet);

        return quest;
    }

    public Quest getByName(String name) throws SQLException {
        Quest quest;

        String query = "SELECT * FROM quests WHERE name = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, name);
        ResultSet resultSet = statement.executeQuery();

        return this.createQuestFromResultSet(resultSet);
    }

    public void create(Quest quest) throws SQLException{
        String query = "INSERT INTO quests (name, description, reward, id) VALUES (?, ?, ?, ?);";
        PreparedStatement statement = connection.prepareStatement(query);
        setPreparedStatement(statement, quest);
        statement.executeUpdate();
    }

    public void update(Quest quest) throws SQLException {
        String query = "UPDATE quests SET name = ?, description = ?, reward = ? WHERE id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        setPreparedStatement(statement, quest);
        statement.executeUpdate();
    }

    public void delete(Quest quest) throws SQLException {
        String query = "DELETE FROM quests WHERE id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, quest.getID());
        statement.executeUpdate();
    }

    private void setPreparedStatement(PreparedStatement statement, Quest quest) throws SQLException {
        statement.setString(1, quest.getName());
        statement.setString(2, quest.getDescription());
        statement.setInt(3, quest.getReward());
        statement.setString(4, quest.getID());
    }

    private Quest createQuestFromResultSet(ResultSet resultSet) throws SQLException {
        String ID = resultSet.getString("id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        Integer reward = resultSet.getInt("reward");

        return new Quest(ID, name, description, reward);
    }
}