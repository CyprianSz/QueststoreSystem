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

    private Wallet createWalletFromResultSet(ResultSet resultSet) throws Exception {
        String ID = resultSet.getString("id");
        Integer balance = resultSet.getInt("balance");
        Integer earnedCoins = resultSet.getInt("earned_coins");

        return new Artifact(ID, balance, earnedCoins);
    }

}