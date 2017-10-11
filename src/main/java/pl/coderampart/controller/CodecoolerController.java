package pl.coderampart.controller;

import pl.coderampart.DAO.*;
import pl.coderampart.enums.CodecoolerMenuOption;
import pl.coderampart.model.*;
import pl.coderampart.services.Bootable;
import pl.coderampart.view.CodecoolerView;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class CodecoolerController implements Bootable<Codecooler> {

    private CodecoolerView codecoolerView = new CodecoolerView();
    private Connection connection;
    private ArtifactDAO artifactDAO;
    private ItemDAO itemDAO;
    private WalletController walletController;

    public CodecoolerController(Connection connectionToDB) {
        connection = connectionToDB;
        artifactDAO = new ArtifactDAO(connection);
        itemDAO = new ItemDAO(connection);
        walletController = new WalletController(connection);
    }

    public boolean start(Codecooler codecooler) throws SQLException {

        codecoolerView.displayCodecoolerMenu();
        int userChoice = codecoolerView.getUserChoice();
        CodecoolerMenuOption codecoolerMenuOption = CodecoolerMenuOption.values()[userChoice];
        codecoolerView.clearTerminal();

        switch(codecoolerMenuOption) {
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

    public void displayWallet(Codecooler codecooler) throws SQLException {

        String codecoolerWalletID;
        codecoolerWalletID = codecooler.getWallet().getID();
        ArrayList<Item> userItems;

        userItems = itemDAO.getUserItems(codecoolerWalletID);

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

    public void buyArtifact(Codecooler codecooler) throws SQLException {
        //TODO: add displayArtifacts method to CodecoolerView
        //codecoolerView.displayArtifacts( artifactDAO );
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
            codecoolerView.output("Too expensive");
        }

    }

    public void updateLevel(Codecooler codecooler) throws SQLException {
        LevelDAO levelDao = new LevelDAO(connection);

        ArrayList<Level> levelList = levelDao.readAll();
        Integer playerExperience = codecooler.getWallet().getEarnedCoins();

        for (Level level: levelList) {
            if (playerExperience >= level.getRequiredExperience()) {
                codecooler.setLevel(level);
            }
        }
    }
}