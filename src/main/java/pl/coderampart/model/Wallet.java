package pl.coderampart.model;

import java.util.ArrayList;
// import coderampart.DAO.WalletDAO;  TODO: uncomment when ItemDAO created

public class Wallet {

    private Integer balance;
    private Integer earnedCoins;

    public Integer getBalance() { return this.balance; }
    public Integer getEarnedCoins() { return this.earnedCoins; }
    public ArrayList<Item> getItemList() {
        //return ItemDAO.itemList;  TODO: uncomment when ItemDAO created
        return null;
    }

    public void addItem(Item item) {
        //ItemDAO.itemList().add(item); TODO: uncomment when ItemDAO created
    }

    public String toString() {
        String walletData = "\nbalance: " + this.getBalance()
                          + "\nearnedCoins: " + this.getEarnedCoins();

        return walletData;
    }
}
