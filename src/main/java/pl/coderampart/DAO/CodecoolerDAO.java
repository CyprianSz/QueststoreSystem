package pl.coderampart.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import pl.coderampart.model.*;
import pl.coderampart.services.User;
import pl.coderampart.view.CodecoolerView;

public class CodecoolerDAO extends AbstractDAO implements User<Codecooler> {

    private WalletDAO walletDAO;
    private GroupDAO groupDAO;
    private LevelDAO levelDAO;
    private TeamDAO teamDAO;
    private Connection connection;

    public CodecoolerDAO(Connection connectionToDB) {

        connection = connectionToDB;
        walletDAO = new WalletDAO(connection);
        groupDAO = new GroupDAO(connection);
        levelDAO = new LevelDAO(connection);
        teamDAO  = new TeamDAO(connection);
    }

    public Codecooler getLogged(String email, String password) throws SQLException {

        Codecooler codecooler = null;

        String query = "SELECT * FROM codecoolers WHERE email = ? AND password = ?;";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, email);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();

        codecooler = this.createCodecoolerFromResultSet(resultSet);

        return codecooler;
    }

    public ArrayList<Codecooler> readAll() throws SQLException {

        ArrayList<Codecooler> codecoolerList = new ArrayList<>();

        String query = "SELECT * FROM codecoolers;";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Codecooler codecooler = this.createCodecoolerFromResultSet(resultSet);
            codecoolerList.add(codecooler);
        }

        return codecoolerList;
    }

    public Codecooler getByID(String ID) throws SQLException {

        Codecooler codecooler = null;
        String query = "SELECT * FROM codecoolers WHERE id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, ID);
        ResultSet resultSet = statement.executeQuery();

        codecooler = this.createCodecoolerFromResultSet(resultSet);

        return codecooler;
    }


    public void create(Codecooler codecooler) throws SQLException {

        String query = "INSERT INTO codecoolers (first_name, last_name, date_of_birth, email, password, "
                + "wallet_id, group_id, level_id, team_id, id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement statement = connection.prepareStatement(query);
        PreparedStatement setStatement = setPreparedStatement(statement, codecooler);
        setStatement.executeUpdate();

    }

    public void update(Codecooler codecooler) throws SQLException {

        String query = "UPDATE codecoolers SET first_name = ?, " +
                           "last_name = ?, date_of_birth = ?, email = ?, password = ?, " +
                           "wallet_id = ?, group_id = ?, level_id = ?, team_id = ? WHERE id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        PreparedStatement setStatement = setPreparedStatement(statement, codecooler);
        setStatement.executeUpdate();
    }


    public void delete(Codecooler codecooler) throws SQLException {

        String query = "DELETE FROM codecoolers WHERE id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, codecooler.getID());
        statement.executeUpdate();

    }

    private PreparedStatement setPreparedStatement(PreparedStatement statement, Codecooler codecooler) throws SQLException {
        statement.setString(1, codecooler.getFirstName());
        statement.setString(2, codecooler.getLastName());
        statement.setString(3, codecooler.getDateOfBirth().toString());
        statement.setString(4, codecooler.getEmail());
        statement.setString(5, codecooler.getPassword());
        statement.setString(6, codecooler.getWallet().getID());
        statement.setString(7, codecooler.getGroup().getID());
        statement.setString(8, codecooler.getTeam().getID());
        statement.setString(9, codecooler.getLevel().getID());
        statement.setString(10, codecooler.getID());

        return statement;
    }

    private Codecooler createCodecoolerFromResultSet(ResultSet resultSet) throws SQLException {
        String ID = resultSet.getString("id");
        String firstName = resultSet.getString("first_name");
        String lastName= resultSet.getString("last_name");
        String dateOfBirth = resultSet.getString("date_of_birth");
        LocalDate dateOfBirthObject = LocalDate.parse(dateOfBirth);
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        String walletID = resultSet.getString("wallet_id");
        Wallet walletObject = this.walletDAO.getByID(walletID);
        String groupID = resultSet.getString("group_id");
        Group groupObject = this.groupDAO.getByID(groupID);
        String levelID = resultSet.getString("level_id");
        Level levelObject = this.levelDAO.getByID(levelID);
        String teamID = resultSet.getString("team_ID");
        Team teamObject = this.teamDAO.getByID(teamID);

        return new Codecooler(ID, firstName, lastName, dateOfBirthObject, email, password,
                             walletObject, groupObject, levelObject, teamObject);
    }
}