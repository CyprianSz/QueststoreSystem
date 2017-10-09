package pl.coderampart.view;

import pl.coderampart.model.Item;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

public class CodecoolerView extends View {

    public void displayCodecoolerMenu() {
        ArrayList<String> options = new ArrayList<>(Arrays.asList("Display wallet", "Buy artifact", "Buy artifact with group",
                "Display level"));

        displayOptions(options);
        this.output("\n0. Exit");
    }

    public String chooseQuestCategory() {
        // Demo:
        this.output("Category:\n1. Basic\n2. Extra\n");
        String category = "Basic";
        return category;
    }

    public String chooseArtifactType() {
        // Demo:
        this.output("Category:\n1. Basic\n2. Magic\n");
        String type = "Magic";
        return type;
    }

    public void displayUserItems(ArrayList<Item> userItems) {

        this.output("\nYour items:");
        for (Item item: userItems) {
            this.output(item.getArtifact().toString());
            this.output("Date: " + item.dateToString() );
        }
    }
}

