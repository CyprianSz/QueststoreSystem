package pl.coderampart.view;

import java.util.Locale;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;

public class View {

    private static final String emailRegEx = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+"
            + "(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";

    private static final String dateRegEx = "^[12][09]\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";

    public void displayAdminMenu(){
        ArrayList<String> options = new ArrayList<>(Arrays.asList("Create new mentor", "Create new group", "Update mentor data",
                                                                  "Display mentor data", "Create experience level"));

        displayOptions(options);
        this.output("\n0. Exit");
    }

    public void displayMentorMenu() {
        ArrayList<String> options = new ArrayList<>(Arrays.asList("Create new Codecooler", "Add quest", "Update quest",
                                                                  "Set quest category", "Mark quest", "Add artifact", "Update artifact",
                                                                  "Set artifact type", "Mark artifact", "Display wallet details"));

        displayOptions(options);
        this.output("\n0. Exit");
    }

    public void displayCodecoolerMenu() {
        ArrayList<String> options = new ArrayList<>(Arrays.asList("Display wallet", "Buy artifact", "Buy artifact with group",
                                                                  "Display level"));

        displayOptions(options);
        this.output("\n0. Exit");
    }

    public void displayOptions(ArrayList<String> options) {
        Integer number = 1;
        this.output(" ");
        for (String option : options) {
            this.output(String.format("%d. %s", number, option));
            number++;
        }
    }

    public int getUserChoice() {
        Scanner input = new Scanner(System.in);
        int number;

        this.output("Enter a number: ");
        while (!input.hasNextInt()) {
            printErrorMessage();
            input.next();
        }

        number = input.nextInt();
        return number;
    }

    public void printErrorMessage() {
        this.output("\nBad choice");
    }

    public String[] getUserData() {
        String name = getInput("Name: ");
        String surname = getInput("Surname: ");
        String email = getInput("E-mail: ");
        String dateOfBirth = getDateInput("Date of birth (yyyy-mm-dd): ");

        String[] userData = new String[] {name, surname, email, dateOfBirth};

        return userData;
    }

    public String getInput(String label) {
        Scanner inputScan = new Scanner(System.in);
        this.output(label);
        String input = inputScan.next();

        return input;
   }

   public String getDateInput(String label) {
       Scanner inputScan = new Scanner(System.in);
       String input;

       do {
           this.output(label);
           input = inputScan.next();
       } while (!input.matches(dateRegEx));

       return input;
   }

   public LocalDate stringToDate(String date) {
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);

       return LocalDate.parse(date, formatter);
   }

   public String[] getQuestData() {
       String name = getInput("Name: ");
       String reward = getInput("Reward (coolcoins): ");

       String[] questData = new String[] {name, reward};

       return questData;
   }

   public String[] getArtifactData() {
       String name = getInput("Name: ");
       String value = getInput("Value (coolcoins): ");

       String[] artifactData = new String[] {name, value};

       return artifactData;
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

    public void displayLogingInfo() {
        Scanner inputScanner = new Scanner(System.in);
        this.clearTerminal();
        this.output("\nLOGGING IN:\n");
        this.getInput("\nType login: ");
        this.getInput("\nType password: ");
    }

    public void displayProgressBar() {
        /* Prints imitation of progress bar, and at the same time
        percentage of work done. Uses 'format' to achieve stady format
        of displayed percentage value (completes value with zeros). */
        String progressBar = "";

        for (int i = 1; i <= 50; i++) {
            progressBar += "#";

            this.clearTerminal();
            this.output("\n-----> LOGGING IN - PLEASE WAIT <-----\n");

            this.output(progressBar);
            this.output(progressBar);
            System.out.format("%n-----> " + "%03d" + "%s" + " <-----", i*2, "%");

            delayExecutionFor(70);
        }
        this.output("\n\n-----> LOGGED IN SUCCESSFULLY <-----");
        delayExecutionFor(1000);
    }

    private void delayExecutionFor(Integer miliSeconds) {
        try {
            Thread.sleep(miliSeconds);
        }
        catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public void clearTerminal() {
        System.out.print("\033[H\033[2J");
    }

    public void enterToContinue() {
        try {
            this.output("\nPRESS ENTER TO CONTINUE");
            System.in.read();
        } catch (IOException e) {
            this.output("INPUT INTERRUPTED");
            e.printStackTrace();
        }
    }

    public int chooseEdit() {
        this.output("\nWhat do you want to change?: ");

        ArrayList<String> options = new ArrayList<>(Arrays.asList("Name", "Amount of coolcoins"));
        displayOptions(options);
        int userChoice = getUserChoice();

        return userChoice;
    }

    public void sayGoodbye(){
        this.output("\nGOOD BYE\n");
    }

    public void output(String output) {
        System.out.println(output);
    }
}
