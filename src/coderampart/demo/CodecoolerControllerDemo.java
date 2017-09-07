package coderampart.demo;

import coderampart.model.Codecooler;
import coderampart.services.Bootable;
import coderampart.view.View;

public class CodecoolerControllerDemo implements Bootable {

    public static final int DISPLAY_WALLET = 1;
    public static final int BUY_ARTIFACT = 2;
    public static final int BUY_WITH_GROUP = 3;
    public static final int DISPLAY_LEVEL = 4;

    public void start() {
        View.displayCodecoolerMenu();
        int userChoice = View.getUserChoice();

        switch(userChoice) {
            case DISPLAY_WALLET: displayWallet();
                break;
            case BUY_ARTIFACT: buyArtifact();
                break;
            case BUY_WITH_GROUP: buyWithGroup();
                break;
            case DISPLAY_LEVEL: displayLevel();
                break;
        }
    }

    public static void displayWallet(){
        System.out.println("Your wallet:");
        System.out.println("Balance: 500 coolcoins");
        System.out.println("Bought items: Private mentoring");
    }

    public static void buyArtifact(){
        System.out.println("Current balance: 500cc");
        System.out.println("Choose an item:");
        System.out.println("\n1. Combat training, 50cc"
                         + "\n2. Sanctuary, 300cc"
                         + "\n3. Time Travel, 500cc");

        Integer artifactChoice = View.getUserChoice();
        if (artifactChoice >= 0) {
            System.out.println("Item bought!");
        }
    }

    public static void buyWithGroup(){
    }

    public static void displayLevel(){
        System.out.println("");
    }
}
