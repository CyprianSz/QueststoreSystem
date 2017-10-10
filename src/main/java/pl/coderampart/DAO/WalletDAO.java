package pl.coderampart.DAO;

import pl.coderampart.model.Artifact;
import pl.coderampart.model.Wallet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class WalletDAO extends AbstractDAO {

    public Wallet getByID(String ID) {
        Wallet wallet = null;

        try {
            Connection connection = this.connectToDataBase();
            String query = "SELECT * FROM wallets WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, ID);
            ResultSet resultSet = statement.executeQuery();

            wallet = this.createWalletFromResultSet(resultSet);
            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

        return wallet;
    }

    public void create(Wallet wallet) {
        try {
            Connection connection = this.connectToDataBase();
            String query = "INSERT INTO wallets (balance, earned_coins, id) VALUES (?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(query);
            PreparedStatement setStatement = setPreparedStatement(statement, wallet);

            setStatement.executeUpdate();

            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public void update(Wallet wallet) {
        try {
            Connection connection = this.connectToDataBase();
            String query = "UPDATE wallets SET balance = ?, earned_coins = ? WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            PreparedStatement setStatement = setPreparedStatement(statement, wallet);
            setStatement.executeUpdate();

            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public void delete(Wallet wallet) {
        try {
            Connection connection = this.connectToDataBase();
            String query = "DELETE FROM wallets WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, wallet.getID());
            statement.executeUpdate();

            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    private PreparedStatement setPreparedStatement(PreparedStatement statement, Wallet wallet) throws Exception {
        statement.setInt(1, wallet.getBalance());
        statement.setInt(2, wallet.getEarnedCoins());
        statement.setString(3, wallet.getID());

        return statement;
    }
    
    private Wallet createWalletFromResultSet(ResultSet resultSet) throws Exception {
        String ID = resultSet.getString("id");
        Integer balance = resultSet.getInt("balance");
        Integer earnedCoins = resultSet.getInt("earned_coins");

        return new Wallet(ID, balance, earnedCoins);
    }
}