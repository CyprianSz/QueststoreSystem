package pl.coderampart.model;

import pl.coderampart.controller.UUIDController;
import java.util.ArrayList;
// import coderampart.DAO.WalletDAO;  TODO: uncomment when ItemDAO created

public class Wallet {

    private String ID;
    private Integer balance;
    private Integer earnedCoins;

    public Wallet() {
        this.ID = UUIDController.createUUID();
    }

    public Wallet(String ID, Integer balance, Integer earnedCoins) {
        this.ID = ID;
        this.balance = balance;
        this.earnedCoins = earnedCoins;
    }

    public String getID() { return this.ID; }
    public Integer getBalance() { return this.balance; }
    public Integer getEarnedCoins() { return this.earnedCoins; }


    public String toString() {
        String walletData = "\nbalance: " + this.getBalance()
                          + "\nearnedCoins: " + this.getEarnedCoins();

        return walletData;
    }
}
