package pl.coderampart.view;

import pl.coderampart.DAO.ArtifactDAO;
import pl.coderampart.model.Artifact;

import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class View {

    public static final String emailRegEx = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+"
            + "(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";

    public static final String dateRegEx = "^[12][09]\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";


    public void displayOptions(ArrayList<String> options) {
        Integer number = 1;
        this.output(" ");
        for (String option : options) {
            this.output(String.format("%d. %s", number, option));
            number++;
        }
    }

    public void displayArtifacts(ArtifactDAO artifactDAO) {
        ArrayList<Artifact> allArtifacts = artifactDAO.readAll();
        ArrayList<String> artifactStrings = new ArrayList<String>();

        for (Artifact artifact: allArtifacts){
            artifactStrings.add(artifact.toString());
        }

        this.outputTable(artifactStrings);
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
        String firstName = getInput("First name: ");
        String lastName = getInput("Last name: ");
        String dateOfBirth = getDateInput("Date of birth (yyyy-mm-dd): ");
        String email = getEmailInput("E-mail: ");
        String password = getInput("Password: ");

        return new String[] {firstName, lastName, dateOfBirth, email, password};
    }

    public String getRegExInput(String regEx, String label) {
        Scanner inputScan = new Scanner(System.in);
        String input;

        do {
            this.output(label);
            input = inputScan.next();
        } while (!input.matches(regEx));

        return input;
    }

    public String getInput(String label) {
        Scanner inputScan = new Scanner(System.in);
        this.output(label);
        String input = inputScan.nextLine();

        return input;
   }

   //TODO: change name where used to 'getRegExInput' and remove
   public String getDateInput(String label) {
       Scanner inputScan = new Scanner(System.in);
       String input;

       do {
           this.output(label);
           input = inputScan.next();
       } while (!input.matches(dateRegEx));

       return input;
   }

    //TODO: change name where used to 'getRegExInput' and remove
   public String getEmailInput(String label) {
        Scanner inputScan = new Scanner(System.in);
        String input;

        do {
            this.output(label);
            input = inputScan.next();
        } while (!input.matches(emailRegEx));

        return input;
   }

   public LocalDate stringToDate(String date) {
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);

       return LocalDate.parse(date, formatter);
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

    public int chooseEdit(ArrayList<String> options) {
        this.output("\nWhat do you want to change?: ");

        displayOptions(options);
        int userChoice = getUserChoice();

        return userChoice;
    }

    public void outputTable(ArrayList<String> table) {
        for (String record: table){
            this.output(record);
        }
    }

    public void sayGoodbye(){
        this.output("\nGOOD BYE\n");
    }

    public void output(String output) {
        System.out.println(output);
    }
}
