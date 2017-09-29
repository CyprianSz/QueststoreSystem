package pl.coderampart.DAO;

import pl.coderampart.model.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class TeamDAO extends AbstractDAO {

    private GroupDAO groupDAO = new GroupDAO();

    public ArrayList<Team> readAll() {
        ArrayList<Team> teamList = new ArrayList<>();

        try {
            Connection connection = this.connectToDataBase();
            String query = "SELECT * FROM teams;";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Team team = this.createTeamFromResultSet(resultSet);
                teamList.add(team);
            }
            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

        return teamList;
    }

    public Team getByID(String ID) {
        Team team = null;

        try {
            Connection connection = this.connectToDataBase();
            String query = "SELECT * FROM teams WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, ID);
            ResultSet resultSet = statement.executeQuery();

            team = this.createTeamFromResultSet(resultSet);
            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

        return team;
    }

    public void create(Team team) {
        try {
            Connection connection = this.connectToDataBase();
            String query = "INSERT INTO teams VALUES (?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(query);
            PreparedStatement setStatement = setPreparedStatement(statement, team);
            statement.executeUpdate();

            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public void update(Team team) {
        try {
            Connection connection = this.connectToDataBase();
            String query = "UPDATE teams SET id = ?, group_id = ?, name = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            PreparedStatement setStatement = setPreparedStatement(statement, team);
            setStatement.executeUpdate();

            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public void delete(Team team) {
        try {
            Connection connection = this.connectToDataBase();
            String query = "DELETE FROM teams WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, team.getID());
            statement.executeUpdate();

            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    private PreparedStatement setPreparedStatement(PreparedStatement statement, Team team) throws Exception {
        statement.setString(1, team.getID());
        statement.setString(2, team.getName());
        statement.setString(3, team.getGroup().getID());

        return statement;
    }

    private Team createTeamFromResultSet(ResultSet resultSet) throws Exception {
        String ID = resultSet.getString("id");
        String name = resultSet.getString("name");
        String groupID = resultSet.getString("group_id");
        Group groupObject = groupDAO.getByID(groupID);

        return new Team(ID, name, groupObject);
    }
}