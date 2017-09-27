package pl.coderampart.DAO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import pl.coderampart.model.*;

public class UserDAO<T> extends AbstractDAO<T> {


//    private String userType = T().getSimpleName();
    private T userType = null;
    private String tableName = this.getTableName(userType);

    public T getUserBy(String email, String password) {
        T user = null;

        try {
            Connection connection = this.connectToDataBase();
            String query = "SELECT * FROM ? WHERE email = ? AND password = ?;";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, tableName);
            statement.setString(2, email);
            statement.setString(3, password);

            ResultSet resultSet = statement.executeQuery();

            user = this.createUserFromResultSet(resultSet);

        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

        return user;
    }


    public ArrayList<T> readAll() {
        ArrayList<T> userList = new ArrayList<>();

        try {
            Connection connection = this.connectToDataBase();
            String query = "SELECT * FROM ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, tableName);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                T user = this.createUserFromResultSet(resultSet);
                userList.add(user);
            }
            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

        return userList;
    }

    public void create(T user) {

    }

    public void update(T user) {

    }

    public void delete(T user) {

    }

    private T createUserFromResultSet(ResultSet resultSet) throws Exception {
        String ID = resultSet.getString("id");
        String first_name = resultSet.getString("first_name");
        String last_name = resultSet.getString("last_name");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        String dateOfBirth = resultSet.getString("date_of_birth");

        LocalDate dateOfBirthObject = LocalDate.parse(dateOfBirth);


        return new T(ID, first_name, last_name, email, password, dateOfBirthObject);
    }

    private String getTableName(T userType) {
        String tableName = "";
        if (userType instanceof Admin) {
            tableName = "admins";
        } else if (userType instanceof Codecooler) {
            tableName = "codecoolers";
        } else if (userType instanceof Mentor) {
            tableName =  "mentors";
        }
        return tableName;
    }

    private
}