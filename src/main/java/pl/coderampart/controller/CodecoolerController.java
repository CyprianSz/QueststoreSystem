package pl.coderampart.controller;

import pl.coderampart.DAO.*;
import pl.coderampart.model.*;
import pl.coderampart.services.Bootable;
import pl.coderampart.view.CodecoolerView;
import java.util.ArrayList;

public class CodecoolerController implements Bootable<Codecooler> {

    private CodecoolerView codecoolerView = new CodecoolerView();
    private ArtifactDAO artifactDAO = new ArtifactDAO();
    private ItemDAO itemDAO = new ItemDAO();
    private WalletController walletController = new WalletController();

    private static final int DISPLAY_WALLET = 1;
    private static final int BUY_ARTIFACT = 2;
    private static final int BUY_WITH_GROUP = 3;
    private static final int DISPLAY_LEVEL = 4;
    private static final int EXIT = 0;

    public boolean start(Codecooler codecooler) {
        codecoolerView.displayCodecoolerMenu();
        int userChoice = codecoolerView.getUserChoice();

        codecoolerView.clearTerminal();

        switch(userChoice) {
            case DISPLAY_WALLET:
                displayWallet(codecooler);
                break;
            case BUY_ARTIFACT:
                buyArtifact(codecooler);
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

    public void buyWithGroup(){
        // TODO: Demo:
        codecoolerView.output("Not enough codecoolers in your group. Recruit some noobs");
    }

    public void displayLevel(Codecooler codecooler) {
        codecoolerView.output(codecooler.getLevel().toString());
    }

    public void buyArtifact(Codecooler codecooler) {
        codecoolerView.displayArtifacts( artifactDAO );
        String name = codecoolerView.getInput( "\nEnter artifact name: " );
        Artifact artifact = artifactDAO.getByName( name );
        Integer balance = codecooler.getWallet().getBalance();

        if (artifact == null) {
            codecoolerView.output( "No such artifact" );
        } else if (balance > artifact.getValue()) {
            Item item = new Item( artifact, codecooler.getWallet() );
            itemDAO.create( item );
            codecoolerView.output( "Bought: \n" + item.toString() );
            walletController.changeBalance( codecooler, artifact.getValue() * (-1) );
        } else {
            codecoolerView.output( "Too expensive" );
        }
    }

    public void updateLevel(Codecooler codecooler) {
        LevelDAO levelDao = new LevelDAO();
        ArrayList<Level> levelList = levelDao.readAll();
        Integer playerExperience = codecooler.getWallet().getEarnedCoins();

        for (Level level: levelList) {
            if (playerExperience >= level.getRequiredExperience()) {
                codecooler.setLevel(level);
            }
        }
    }
}