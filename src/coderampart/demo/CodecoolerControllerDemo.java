package coderampart.demo;

import coderampart.model.Codecooler;
import coderampart.services.Bootable;
import coderampart.view.View;

public class CodecoolerControllerDemo implements Bootable {

    private View view = new View();

    public static final int DISPLAY_WALLET = 1;
    public static final int BUY_ARTIFACT = 2;
    public static final int BUY_WITH_GROUP = 3;
    public static final int DISPLAY_LEVEL = 4;
    public static final int EXIT = 0;

    public boolean start() {
        view.displayCodecoolerMenu();
        int userChoice = view.getUserChoice();

        view.clearTerminal();

        switch(userChoice) {
            case DISPLAY_WALLET: displayWallet();
                break;
            case BUY_ARTIFACT: buyArtifact();
                break;
            case BUY_WITH_GROUP: buyWithGroup();
                break;
            case DISPLAY_LEVEL: displayLevel();
                break;
            case EXIT:
                return false;
        }

        view.enterToContinue();

        return true;
    }

    public void displayWallet(){
        System.out.println("Your wallet:");
        System.out.println("Balance: 500 coolcoins");
        System.out.println("Bought items: Private mentoring");
    }

    public void buyArtifact(){
        System.out.println("Current balance: 500cc");
        System.out.println("Choose an item:");
        System.out.println("\n1. Combat training, 50cc"
                         + "\n2. Sanctuary, 300cc"
                         + "\n3. Time Travel, 500cc");

        Integer artifactChoice = view.getUserChoice();
        if (artifactChoice >= 0) {
            System.out.println("Item bought!");
        }
    }

    public void buyWithGroup(){
        // Demo:
        System.out.println("Not enough codecoolers in your group. Recruit some noobs");
    }

    public void displayLevel(){
        // Demo:
        System.out.println("Your level: D.N.O.");
    }
}
