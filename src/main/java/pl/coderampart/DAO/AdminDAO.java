package pl.coderampart.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import pl.coderampart.model.*;

public class AdminDAO extends AbstractDAO {

    private Connection connection;

    public AdminDAO(Connection connectionToDB) {
        connection = connectionToDB;
    }

    public List<Admin> readAll() throws SQLException{
        List<Admin> adminList = new ArrayList<>();

        String query = "SELECT * FROM admins;";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Admin admin = this.createAdminFromResultSet(resultSet);
            adminList.add(admin);
        }
        return adminList;
    }

    public Admin getByID(String ID) throws SQLException {
        String query = "SELECT * FROM admins WHERE id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, ID);
        ResultSet resultSet = statement.executeQuery();

        return this.createAdminFromResultSet(resultSet);
    }

    public void create(Admin admin) throws SQLException {
        String query = "INSERT INTO admins (first_name, last_name, email, password, date_of_birth, id) "
                         + "VALUES (?, ?, ?, ?, ?, ?);";
        PreparedStatement statement = connection.prepareStatement(query);
        setPreparedStatement(statement, admin);
        statement.executeUpdate();
    }

    public void update(Admin admin) throws SQLException{
        String query = "UPDATE admins SET first_name = ?, " +
                "last_name = ?, email = ?, password = ?, " +
                "date_of_birth = ? WHERE id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        setPreparedStatement(statement, admin);
        statement.executeUpdate();
    }

    public void delete(Admin admin) throws SQLException{
        String query = "DELETE FROM admins WHERE id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, admin.getID());
        statement.executeUpdate();
    }

    private void setPreparedStatement(PreparedStatement statement, Admin admin) throws SQLException {
        statement.setString(1, admin.getFirstName());
        statement.setString(2, admin.getLastName());
        statement.setString(3, admin.getEmail());
        statement.setString(4, admin.getPassword());
        statement.setString(5, admin.getDateOfBirth().toString());
        statement.setString(6, admin.getID());
    }

    Admin createAdminFromResultSet(ResultSet resultSet) throws SQLException {
        String ID = resultSet.getString("id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        String dateOfBirth = resultSet.getString("date_of_birth");
        LocalDate dateOfBirthObject = LocalDate.parse(dateOfBirth);

        return new Admin(ID, firstName, lastName, dateOfBirthObject, email, password);
    }
}