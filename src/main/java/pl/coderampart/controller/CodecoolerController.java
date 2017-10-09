package pl.coderampart.controller;

import pl.coderampart.DAO.*;
import pl.coderampart.enums.CodecoolerMenuOption;
import pl.coderampart.model.*;
import pl.coderampart.services.Bootable;
import pl.coderampart.view.CodecoolerView;
import java.util.ArrayList;




public class CodecoolerController implements Bootable<Codecooler> {

    private CodecoolerView codecoolerView = new CodecoolerView();

    public boolean start(Codecooler codecooler) {

        codecoolerView.displayCodecoolerMenu();
        int userChoice = codecoolerView.getUserChoice();
        CodecoolerMenuOption codecoolerMenuOption = CodecoolerMenuOption.values()[userChoice];

        codecoolerView.clearTerminal();

        switch(codecoolerMenuOption) {

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
        codecoolerView.enterToContinue();
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

        codecoolerView.output(walletData);
        codecoolerView.displayUserItems(userItems);
    }

    public void buyArtifact() {
        // TODO:
        // DEMO:
        codecoolerView.output("Current balance: 500cc");
        codecoolerView.output("Choose an item:");
        codecoolerView.output("\n1. Combat training, 50cc"
                  + "\n2. Sanctuary, 300cc"
                  + "\n3. Time Travel, 500cc");

        Integer artifactChoice = codecoolerView.getUserChoice();
        if (artifactChoice >= 0) {
            codecoolerView.output("Item bought!");
        }
    }

    public void buyWithGroup(){
        // TODO: Demo:
        codecoolerView.output("Not enough codecoolers in your group. Recruit some noobs");
    }

    public void displayLevel(Codecooler codecooler) {
        codecoolerView.output(codecooler.getLevel().toString());
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