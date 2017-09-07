package coderampart.model;

import java.util.ArrayList;
// import coderampart.DAO.WalletDAO;     UNCOMENT WHEN CREATE ItemDAO

public class Wallet {

    private Integer balance;
    private Integer earnedCoins;

    public Integer getBalance() { return this.balance; }
    public Integer getEarnedCoins() { return this.earnedCoins; }
    public ArrayList<Item> getItemList() {
        //return ItemDAO.itemList;   UNCOMENT WHEN CREATE ItemDAO
    }

    public void addItem(Item item) {
        //ItemDAO.itemList().add(item);  UNCOMMENT WHEN CREATE ItemDAO
    }

    public String toString() {
        String walletData = "\nbalance: " + this.getBalance()
                          + "\nearnedCoins: " + this.getEarnedCoins();

        return walletData;
    }
}
