package coderampart.view;
import java.util.Scanner;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;

public class View {

    public void displayMentorMenu() {
        ArrayList<String> options = new ArrayList<>(Arrays.asList("Create new Codecooler", "Add quest", "Update quest",
                                                                  "Set quest category", "Mark quest", "Add artifact", "Update artifact",
                                                                  "Set artifact type", "Mark artifact", "Display wallet details"));

        displayOptions(options);
        System.out.println("\n0. Exit");
    }

    public void displayCodecoolerMenu() {
        ArrayList<String> options = new ArrayList<>(Arrays.asList("Display wallet", "Buy artifact", "Buy artifact with group",
                                                                  "Display level"));

        displayOptions(options);
        System.out.println("\n0. Exit");
    }

    public void displayOptions(ArrayList<String> options) {
        Integer number = 1;
        System.out.println(" ");
        for (String option : options) {
            System.out.println(String.format("%d. %s", number, option));
            number++;
        }
    }

    public int getUserChoice() {
        Scanner input = new Scanner(System.in);
        int number;

        System.out.println("Enter a number: ");
        while (!input.hasNextInt()) {
            printErrorMessage();
            input.next();
        }

        number = input.nextInt();
        return number;
    }

    public void printErrorMessage() {
        System.out.println("\nBad choice");
    }

    public String[] getUserData() {
        String name = takeInput("Name: ");
        String surname = takeInput("Surname: ");
        String email = takeInput("E-mail: ");
        String dateOfBirth = takeDateInput("Date of birth: ");

        String[] userData = new String[] {name, surname, email, dateOfBirth};

        return userData;
    }

    public String takeInput(String label) {
        Scanner inputScan = new Scanner(System.in);
        System.out.println(label);
        String input = inputScan.next();

        return input;
   }

   public String takeDateInput(String label) {
       Scanner inputScan = new Scanner(System.in);
       String input;

       do {
           System.out.println(label);
           input = inputScan.next();
       } while (!input.matches("^[2][0][1-9][0-9]-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$"));

       return input;
   }

   public String[] getQuestData() {
       String name = takeInput("Name: ");
       String reward = takeInput("Reward (coolcoins): ");

       String[] questData = new String[] {name, reward};

       return questData;
   }

   public String[] getArtifactData() {
       String name = takeInput("Name: ");
       String value = takeInput("Value (coolcoins): ");

       String[] artifactData = new String[] {name, value};

       return artifactData;
   }

   public String chooseQuestCategory() {
       // Demo:
       System.out.println("Category:\n1. Basic\n2. Extra\n");
       String category = "Basic";
       return category;
   }

   public String chooseArtifactType() {
       // Demo:
       System.out.println("Category:\n1. Basic\n2. Magic\n");
       String type = "Magic";
       return type;
   }

    public void displayLogingInfo() {
        Scanner inputScannet = new Scanner(System.in);
        this.clearTerminal();
        System.out.println("\nLOGING IN:\n");
        this.takeInput("\nType login: ");
        this.takeInput("\nType password: ");
    }

    public void displayProgressBar() {
        /* Prints imitation of progress bar, and at the same time
        percentage of work done. Uses 'format' to achieve stady format
        of displayed percentage value (completes value with zeros). */
        String progressBar = "";

        for (int i = 1; i <= 50; i++) {
            progressBar += "#";

            this.clearTerminal();
            System.out.println("\n-----> LOGING IN - PLEASE WAIT <-----\n");

            System.out.println(progressBar);
            System.out.println(progressBar);
            System.out.format("%n-----> " + "%03d" + "%s" + " <-----", i*2, "%");

            delayExecutionFor(70);
        }
        System.out.println("\n\n-----> LOGED IN SUCCESSFULLY <-----");
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
            System.out.print("\nPRESS ENTER TO CONTINUE");
            System.in.read();
        } catch (IOException e) {
            System.out.println("INPUT INTERRUPTED");
            e.printStackTrace();
        }
    }

    public int chooseEdit() {
        System.out.println("\nWhat do you want to change?: ");

        ArrayList<String> options = new ArrayList<>(Arrays.asList("Name", "Amount of coolcoins"));
        displayOptions(options);
        int userChoice = getUserChoice();

        return userChoice;
    }
}
