package coderampart.Demo;

import coderampart.model.Codecooler;

public class CodecoolerControllerDemo implements Bootable {

    public final Integer DISPLAY_WALLET = 1;
    public final Integer BUY_ARTIFACT = 2;
    public final Integer BUY_WITH_GROUP = 3;
    public final Integer DISPLAY_LEVEL = 4;

    public void start() {
        View.displayCodecoolerMenu();
        Integer userChoice = View.getUserChoice();

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
}