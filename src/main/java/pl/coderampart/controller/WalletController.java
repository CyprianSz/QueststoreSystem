package pl.coderampart.controller;

import pl.coderampart.DAO.WalletDAO;
import pl.coderampart.model.Codecooler;
import pl.coderampart.model.Wallet;

public class WalletController {

    private WalletDAO walletDAO = new WalletDAO();

    public void changeBalance(Codecooler codecooler, Integer coins) {
        Integer balance = codecooler.getWallet().getBalance();
        balance += coins;

        Wallet wallet = codecooler.getWallet();
        wallet.setBalance(balance);

        walletDAO.update(wallet);
    }

    public void addExperience(Codecooler codecooler, Integer coins) {
        Integer experience = codecooler.getWallet().getEarnedCoins();
        experience += coins;

        Wallet wallet = codecooler.getWallet();
        wallet.setEarnedCoins(experience);

        walletDAO.update(wallet);
    }
}