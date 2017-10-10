package pl.coderampart.controller;

import pl.coderampart.DAO.WalletDAO;
import pl.coderampart.model.Codecooler;
import pl.coderampart.model.Wallet;

import java.sql.Connection;
import java.sql.SQLException;

import static java.lang.Math.abs;

public class WalletController {

    private WalletDAO walletDAO;
    private Connection connection;

    public WalletController(Connection connectionToDB) {

        connection = connectionToDB;
        walletDAO  = new WalletDAO(connection);
    }

    public void changeBalance(Codecooler codecooler, Integer coins) {
        Integer balance = codecooler.getWallet().getBalance();
        Integer earnedCoins = codecooler.getWallet().getEarnedCoins();
        balance += coins;
        earnedCoins += abs(coins);

        Wallet wallet = codecooler.getWallet();
        wallet.setBalance(balance);
        wallet.setEarnedCoins(earnedCoins);

        try {
            walletDAO.update(wallet);
        } catch (SQLException e){
          System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
}