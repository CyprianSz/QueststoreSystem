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

    public void changeBalance(Codecooler codecooler, Integer coins) throws SQLException {
        Integer balance = codecooler.getWallet().getBalance();
        balance += coins;

        Wallet wallet = codecooler.getWallet();
        wallet.setBalance(balance);

        walletDAO.update(wallet);

    }

    public void addExperience(Codecooler codecooler, Integer coins) throws SQLException {
        Integer experience = codecooler.getWallet().getEarnedCoins();
        experience += coins;

        Wallet wallet = codecooler.getWallet();
        wallet.setEarnedCoins(experience);

        walletDAO.update(wallet);
    }
}