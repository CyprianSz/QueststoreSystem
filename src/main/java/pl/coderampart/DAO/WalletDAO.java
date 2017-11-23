package pl.coderampart.DAO;

import pl.coderampart.model.Item;
import pl.coderampart.model.Wallet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class WalletDAO extends AbstractDAO {

    private Connection connection;

    public WalletDAO(Connection connectionToDB) {
        connection = connectionToDB;
    }

//    public ArrayList<Item> readItemsByUserID(String ID);

    public Wallet getByID(String ID) throws SQLException {

        Wallet wallet = null;

        String query = "SELECT * FROM wallets WHERE id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, ID);
        ResultSet resultSet = statement.executeQuery();

        wallet = this.createWalletFromResultSet(resultSet);

        return wallet;
    }

    public void create(Wallet wallet) throws SQLException {
        String query = "INSERT INTO wallets (balance, earned_coins, id) VALUES (?, ?, ?);";
        PreparedStatement statement = connection.prepareStatement(query);
        PreparedStatement setStatement = setPreparedStatement(statement, wallet);
        setStatement.executeUpdate();
    }

    public void update(Wallet wallet) throws SQLException {
        String query = "UPDATE wallets SET balance = ?, earned_coins = ? WHERE id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        PreparedStatement setStatement = setPreparedStatement(statement, wallet);
        setStatement.executeUpdate();
    }

    public void delete(Wallet wallet) throws SQLException {
        String query = "DELETE FROM wallets WHERE id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, wallet.getID());
        statement.executeUpdate();
    }

    private PreparedStatement setPreparedStatement(PreparedStatement statement, Wallet wallet) throws SQLException {
        statement.setInt(1, wallet.getBalance());
        statement.setInt(2, wallet.getEarnedCoins());
        statement.setString(3, wallet.getID());

        return statement;
    }

    private Wallet createWalletFromResultSet(ResultSet resultSet) throws SQLException {
        String ID = resultSet.getString("id");
        Integer balance = resultSet.getInt("balance");
        Integer earnedCoins = resultSet.getInt("earned_coins");

        return new Wallet(ID, balance, earnedCoins);
    }
}