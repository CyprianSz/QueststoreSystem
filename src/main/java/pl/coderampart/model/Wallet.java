package pl.coderampart.model;

import pl.coderampart.controller.helpers.UUIDController;

public class Wallet {

    private String ID;
    private Integer balance;
    private Integer earnedCoins;

    public Wallet() {
        this.ID = UUIDController.createUUID();
        this.balance = 0;
        this.earnedCoins = 0;
    }

    public Wallet(String ID, Integer balance, Integer earnedCoins) {
        this.ID = ID;
        this.balance = balance;
        this.earnedCoins = earnedCoins;
    }

    public String getID() { return this.ID; }
    public Integer getBalance() { return this.balance; }
    public Integer getEarnedCoins() { return this.earnedCoins; }
    public void setBalance(Integer balance) { this.balance = balance; }
    public void setEarnedCoins(Integer earnedCoins) { this.earnedCoins = earnedCoins; }

    public String toString() {
        String walletData = "\nBalance: " + this.getBalance()
                          + "\nTotal coins earned: " + this.getEarnedCoins();

        return walletData;
    }
}
