package pl.coderampart.controller;

import pl.coderampart.DAO.WalletDAO;
import pl.coderampart.model.Codecooler;
import pl.coderampart.model.Wallet;

import java.sql.Connection;
import java.sql.SQLException;


public class WalletController {

    private WalletDAO walletDAO;
    private Connection connection;

    public WalletController(Connection connectionToDB) {

        connection = connectionToDB;
        walletDAO  = new WalletDAO(connection);
    }

    public void changeBalance(Codecooler codecooler, Integer coins) {
        Integer balance = codecooler.getWallet().getBalance();
        balance += coins;

        Wallet wallet = codecooler.getWallet();
        wallet.setBalance(balance);
        try{
            walletDAO.update(wallet);
        } catch (SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void addExperience(Codecooler codecooler, Integer coins) {
        Integer experience = codecooler.getWallet().getEarnedCoins();
        experience += coins;

        Wallet wallet = codecooler.getWallet();
        wallet.setEarnedCoins(experience);

        try {
            walletDAO.update(wallet);
        } catch (SQLException e){
          System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
}