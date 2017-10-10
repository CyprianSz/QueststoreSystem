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

        String codecoolerWalletID;
        codecoolerWalletID = codecooler.getWallet().getID();

        ArrayList<Item> userItems;
        try{
            userItems = itemDAO.getUserItems(codecoolerWalletID);

            String walletData;
            walletData = codecooler.getWallet().toString();

            codecoolerView.output(walletData);
            codecoolerView.displayUserItems(userItems);
        } catch (SQLException e){
          System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void buyWithGroup(){
        // TODO: Demo:
        codecoolerView.output("Not enough codecoolers in your group. Recruit some noobs");
    }

    public void displayLevel(Codecooler codecooler) {
        codecoolerView.output(codecooler.getLevel().toString());
    }

    public void buyArtifact(Codecooler codecooler) {

//        codecoolerView.displayArtifacts( artifactDAO );
        String name = codecoolerView.getInput( "\nEnter artifact name: " );

        try{

            Artifact artifact = artifactDAO.getByName( name );
            Integer balance = codecooler.getWallet().getBalance();

            if (artifact == null) {
                codecoolerView.output( "No such artifact" );
            } else if (balance > artifact.getValue()) {
                Item item = new Item( artifact, codecooler.getWallet() );
                try {
                    itemDAO.create( item );
                } catch (SQLException e){
                  System.err.println(e.getClass().getName() + ": " + e.getMessage());
                }
                codecoolerView.output( "Bought: \n" + item.toString() );
                walletController.changeBalance( codecooler, artifact.getValue() * (-1) );
            } else {
                codecoolerView.output("Too expensive");
            }
        } catch (SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

    }

    public void updateLevel(Codecooler codecooler) {

        LevelDAO levelDao = new LevelDAO(connection);

        try {

            ArrayList<Level> levelList = levelDao.readAll();
            Integer playerExperience = codecooler.getWallet().getEarnedCoins();

            for (Level level: levelList) {
                if (playerExperience >= level.getRequiredExperience()) {
                    codecooler.setLevel(level);
                }
            }
        } catch (SQLException e){
          System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
}