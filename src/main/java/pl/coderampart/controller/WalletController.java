package pl.coderampart.controller;

import pl.coderampart.DAO.WalletDAO;
import pl.coderampart.model.Codecooler;
import pl.coderampart.model.Wallet;
import static java.lang.Math.abs;

public class WalletController {

    private WalletDAO walletDAO = new WalletDAO();

    public void changeBalance(Codecooler codecooler, Integer coins) {
        Integer balance = codecooler.getWallet().getBalance();
        Integer earnedCoins = codecooler.getWallet().getEarnedCoins();
        balance += coins;
        earnedCoins += abs(coins);

        Wallet wallet = codecooler.getWallet();
        wallet.setBalance(balance);
        wallet.setEarnedCoins(earnedCoins);

        walletDAO.update(wallet);
    }
}