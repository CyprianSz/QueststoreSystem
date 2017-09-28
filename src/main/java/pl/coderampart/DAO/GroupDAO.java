package pl.coderampart.DAO;

import pl.coderampart.model.Group;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class GroupDAO extends AbstractDAO {

    public ArrayList<Group> readAll() {
        ArrayList<Group> groupList = new ArrayList<>();

        try {
            Connection connection = this.connectToDataBase();
            String query = "SELECT * FROM groups;";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Group group = this.createGroupFromResultSet(resultSet);
                groupList.add(group);
            }
            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

        return groupList;
    }

    public Group getByID(String ID) {
        Group group = null;

        try {
            Connection connection = this.connectToDataBase();
            String query = "SELECT * FROM groups WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, ID);
            ResultSet resultSet = statement.executeQuery();

            group = this.createGroupFromResultSet(resultSet);
            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

        return group;
    }

    public void create(Group group) {
        try {
            Connection connection = this.connectToDataBase();
            String query = "INSERT INTO groups VALUES (?, ?);";
            PreparedStatement statement = connection.prepareStatement(query);
            PreparedStatement setStatement = setPreparedStatement(statement, group);
            statement.executeUpdate();

            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public void update(Group group) {
        try {
            Connection connection = this.connectToDataBase();
            String query = "UPDATE groups SET id = ?, name = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            PreparedStatement setStatement = setPreparedStatement(statement, group);
            setStatement.executeUpdate();

            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public void delete(Group group) {
        try {
            Connection connection = this.connectToDataBase();
            String query = "DELETE FROM groups WHERE ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, group.getID());
            statement.executeUpdate();

            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    private PreparedStatement setPreparedStatement(PreparedStatement statement, Group group) throws Exception {
        statement.setString(1, group.getID());
        statement.setString(2, group.getName());

        return statement;
    }

    private Group createGroupFromResultSet(ResultSet resultSet) throws Exception {
        String ID = resultSet.getString("id");
        String name = resultSet.getString("name");

        return new Group(ID, name);
    }
}