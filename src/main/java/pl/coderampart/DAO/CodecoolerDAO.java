package pl.coderampart.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import pl.coderampart.model.*;
import pl.coderampart.services.User;

public class CodecoolerDAO extends AbstractDAO implements User<Codecooler> {

    private WalletDAO walletDAO = new WalletDAO();
    private GroupDAO groupDAO = new GroupDAO();
    private LevelDAO levelDAO = new LevelDAO();
    private TeamDAO teamDAO = new TeamDAO();

    public Codecooler getLogged(String email, String password) throws Exception {
        Codecooler codecooler = null;

        Connection connection = this.connectToDataBase();
        String query = "SELECT * FROM codecoolers WHERE email = ? AND password = ?;";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, email);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();

        codecooler = this.createCodecoolerFromResultSet(resultSet);
        connection.close();

        return codecooler;
    }

    public ArrayList<Codecooler> readAll() {
        ArrayList<Codecooler> codecoolerList = new ArrayList<>();

        try {
            Connection connection = this.connectToDataBase();
            String query = "SELECT * FROM codecoolers;";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Codecooler codecooler = this.createCodecoolerFromResultSet(resultSet);
                codecoolerList.add(codecooler);
            }
            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

        return codecoolerList;
    }

    public void create(Codecooler codecooler) {
        try {
            Connection connection = this.connectToDataBase();
            String query = "INSERT INTO codecoolers VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(query);
            PreparedStatement setStatement = setPreparedStatement(statement, codecooler);
            setStatement.executeUpdate();

            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public void update(Codecooler codecooler) {
        try {
            Connection connection = this.connectToDataBase();
            String query = "UPDATE codecoolers SET id = ?, first_name = ?, " +
                           "last_name = ?, date_of_birth = ?, email = ?, password = ?, " +
                           "wallet_id = ?, group_id = ?, level_id = ?, team_id = ?;";

            PreparedStatement statement = connection.prepareStatement(query);
            PreparedStatement setStatement = setPreparedStatement(statement, codecooler);
            setStatement.executeUpdate();

            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public void delete(Codecooler codecooler) {
        try {
            Connection connection = this.connectToDataBase();
            String query = "DELETE FROM codecoolers WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, codecooler.getID());
            statement.executeUpdate();

            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    private PreparedStatement setPreparedStatement(PreparedStatement statement, Codecooler codecooler) throws Exception {
        statement.setString(1, codecooler.getID());
        statement.setString(2, codecooler.getFirstName());
        statement.setString(3, codecooler.getLastName());
        statement.setString(4, codecooler.getEmail());
        statement.setString(5, codecooler.getPassword());
        statement.setString(6, codecooler.getDateOfBirth().toString());
        statement.setString(7, codecooler.getWallet().getID());
        statement.setString(8, codecooler.getGroup().getID());
        statement.setString(9, codecooler.getLevel().getID());
        statement.setString(10, codecooler.getTeam().getID());

        return statement;
    }

    private Codecooler createCodecoolerFromResultSet(ResultSet resultSet) throws Exception {
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