package pl.coderampart.DAO;

import pl.coderampart.model.*;
import pl.coderampart.model.Item;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ItemDAO extends AbstractDAO {

    private ArtifactDAO artifactDAO;
    private WalletDAO walletDAO;
    private Connection connection;

    public ItemDAO(Connection connectionToDB) {

        connection = connectionToDB;
        walletDAO = new WalletDAO(connection);
        artifactDAO = new ArtifactDAO(connection);
    }

    public ArrayList<Item> readAll() throws SQLException {

        ArrayList<Item> itemList = new ArrayList<>();
        String query = "SELECT * FROM items;";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Item item = this.createItemFromResultSet(resultSet);
            itemList.add(item);
        }

        return itemList;
    }

    public ArrayList<Item> getUserItems(String walletID) throws SQLException {

        ArrayList<Item> itemList = new ArrayList<>();
        String query = "SELECT * FROM items WHERE wallet_id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, walletID);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Item item = this.createItemFromResultSet(resultSet);
            itemList.add(item);
        }

        return itemList;
    }

    public void create(Item item) throws SQLException {

        String query = "INSERT INTO items (artifact_id, wallet_id, creation_date, is_spent, id) VALUES (?, ?, ?, ?, ?);";
        PreparedStatement statement = connection.prepareStatement(query);
        PreparedStatement setStatement = setPreparedStatement(statement, item);
        statement.executeUpdate();
    }

    public void update(Item item) throws SQLException {

        String query = "UPDATE items SET artifact_id = ?, wallet_id = ?, " +
                "creation_date = ?, is_spent = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        PreparedStatement setStatement = setPreparedStatement(statement, item);
        setStatement.executeUpdate();
    }

    public void delete(Item item) throws SQLException {

        String query = "DELETE FROM items WHERE id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, item.getID());
        statement.executeUpdate();

    }


    private PreparedStatement setPreparedStatement(PreparedStatement statement, Item item) throws SQLException {
        statement.setString(1, item.getArtifact().getID());
        statement.setString(2, item.getWallet().getID());
        statement.setString(3, item.getCreationDate().toString());
        statement.setBoolean(4, item.getMark());
        statement.setString(5, item.getID());

        return statement;
    }

    private Item createItemFromResultSet(ResultSet resultSet) throws SQLException {
        String ID = resultSet.getString("id");
        String artifact_id = resultSet.getString("artifact_id");
        Artifact artifact = artifactDAO.getByID(artifact_id);
        String wallet_id = resultSet.getString("wallet_id");
        Wallet wallet = walletDAO.getByID(wallet_id);
        String creationDate = resultSet.getString("creation_date");
        LocalDate creationDateObject = LocalDate.parse(creationDate);
        Boolean isSpent = resultSet.getBoolean("is_spent");

        return new Item(ID, artifact, wallet, creationDateObject, isSpent);
    }
}