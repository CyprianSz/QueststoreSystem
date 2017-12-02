package pl.coderampart.DAO;

import com.sun.org.apache.regexp.internal.RE;
import pl.coderampart.model.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeamDAO extends AbstractDAO {

    private GroupDAO groupDAO;
    private Connection connection;

    public TeamDAO(Connection connectionToDB) {
        connection = connectionToDB;
        groupDAO = new GroupDAO(connection);
    }

    public List<Team> readAll() throws SQLException {
        List<Team> teamList = new ArrayList<>();

        String query = "SELECT * FROM teams;";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Team team = this.createTeamFromResultSet(resultSet);
            teamList.add(team);
        }
        return teamList;
    }

    public Team getByID(String ID) throws SQLException {
        String query = "SELECT * FROM teams WHERE id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, ID);
        ResultSet resultSet = statement.executeQuery();

        return this.createTeamFromResultSet(resultSet);
    }

    public Team getByName(String name) throws SQLException {
        String query = "SELECT * FROM teams WHERE name = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, name);
        ResultSet resultSet = statement.executeQuery();

        return this.createTeamFromResultSet(resultSet);
    }

    public void create(Team team) throws SQLException {
        String query = "INSERT INTO teams (name, group_id, id) VALUES (?, ?, ?);";
        PreparedStatement statement = connection.prepareStatement(query);
        setPreparedStatement(statement, team);
        statement.executeUpdate();

    }

    public void update(Team team) throws SQLException {
        String query = "UPDATE teams SET name = ?, group_id = ? WHERE id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        setPreparedStatement(statement, team);
        statement.executeUpdate();
    }

    public void delete(Team team) throws SQLException {
        String query = "DELETE FROM teams WHERE id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, team.getID());
        statement.executeUpdate();
    }

    private void setPreparedStatement(PreparedStatement statement, Team team) throws SQLException {
        statement.setString(1, team.getName());
        statement.setString(2, team.getGroup().getID());
        statement.setString(3, team.getID());
    }

    private Team createTeamFromResultSet(ResultSet resultSet) throws SQLException {
        String ID = resultSet.getString("id");
        String name = resultSet.getString("name");
        String groupID = resultSet.getString("group_id");
        Group groupObject = groupDAO.getByID(groupID);

        return new Team(ID, name, groupObject);
    }
}