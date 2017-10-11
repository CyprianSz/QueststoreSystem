package pl.coderampart.view;

import pl.coderampart.model.Item;

import java.util.ArrayList;
import java.util.Arrays;

public class CodecoolerView extends View {

    public void displayCodecoolerMenu() {
        ArrayList<String> options = new ArrayList<>(Arrays.asList("Display wallet", "Buy artifact", "Buy artifact with group",
                "Display level"));

        displayOptions(options);
        this.output("\n0. Exit");
    }

    public void displayUserItems(ArrayList<Item> userItems) {

        this.output("\nYour items:");
        for (Item item: userItems) {
            this.output(item.getArtifact().toString());
            this.output("Date: " + item.dateToString() );
        }
    }


}

