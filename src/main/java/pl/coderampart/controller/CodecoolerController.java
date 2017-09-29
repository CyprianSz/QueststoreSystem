package pl.coderampart.controller;

import pl.coderampart.DAO.*;
import pl.coderampart.model.*;
import pl.coderampart.services.Bootable;
import pl.coderampart.view.View;
import java.util.ArrayList;



public class CodecoolerController implements Bootable<Codecooler> {

    private View view = new View();

    public static final int DISPLAY_WALLET = 1;
    public static final int BUY_ARTIFACT = 2;
    public static final int BUY_WITH_GROUP = 3;
    public static final int DISPLAY_LEVEL = 4;
    public static final int EXIT = 0;

    public boolean start(Codecooler codecooler) {
        view.displayCodecoolerMenu();
        int userChoice = view.getUserChoice();

        view.clearTerminal();

        switch(userChoice) {
            case DISPLAY_WALLET:
                displayWallet(codecooler);
                break;
            case BUY_ARTIFACT:
                buyArtifact();
                break;
            case BUY_WITH_GROUP:
                buyWithGroup();
                break;
            case DISPLAY_LEVEL:
                displayLevel(codecooler);
                break;
            case EXIT:
                return false;
        }
        view.enterToContinue();
        return true;
    }

    public void displayWallet(Codecooler codecooler) {
        ItemDAO itemDao = new ItemDAO();

        String codecoolerWalletID;
        codecoolerWalletID = codecooler.getWallet().getID();

        ArrayList<Item> userItems;
        userItems = itemDao.getUserItems(codecoolerWalletID);

        String walletData;
        walletData = codecooler.getWallet().toString();

        view.output(walletData);
        view.displayUserItems(userItems);
    }

    public void buyArtifact() {
        // TODO:
        // DEMO:
        view.output("Current balance: 500cc");
        view.output("Choose an item:");
        view.output("\n1. Combat training, 50cc"
                  + "\n2. Sanctuary, 300cc"
                  + "\n3. Time Travel, 500cc");

        Integer artifactChoice = view.getUserChoice();
        if (artifactChoice >= 0) {
            view.output("Item bought!");
        }
    }

    public void buyWithGroup(){
        // TODO: Demo:
        view.output("Not enough codecoolers in your group. Recruit some noobs");
    }

    public void displayLevel(Codecooler codecooler) {
        view.output(codecooler.getLevel().toString());
    }

    public void updateLevel(Codecooler codecooler) {
        LevelDAO levelDao = new LevelDAO();

        ArrayList<Level> levelList= levelDao.readAll();

        Integer playerExperience;
        playerExperience = codecooler.getWallet().getEarnedCoins();

        for (Level level: levelList) {
            if (playerExperience >= level.getRequiredExperience()) {
                codecooler.setLevel(level);
            }
        }
    }
}