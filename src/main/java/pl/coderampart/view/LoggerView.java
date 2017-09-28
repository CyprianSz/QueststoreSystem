package pl.coderampart.view;

import java.util.ArrayList;
import java.util.Arrays;

public class LoggerView extends View {

    public void displayLoggerMenu(){
        ArrayList<String> options = new ArrayList<>( Arrays.asList(
                "Admin",
                "Mentor",
                "Codecooler"
        ));

        displayOptions(options);
        this.output("\n0. Exit");
    }
}