package coderampart.view;

import java.util.Scanner;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class View {

    public static void displayMentorMenu() {
        ArrayList<String> options = new ArrayList<>(Arrays.asList("Create new Codecooler", "Add quest", "Update quest",
                                                             "Set quest category", "Mark quest", "Add artifact", "Update artifact", 
                                                             "Set artifact type", "Mark artifact", "Display wallet details"));

        displayOptions(options);
        System.out.println("\n0. Exit");
    }

    public static void displayOptions(ArrayList<String> options) {
        Integer number = 1;
        System.out.println(" ");
        for (String option : options) {
            System.out.println(String.format("%d. %s", number, option));
            number++;
        }
    }

    public static void printExitMessage() {
        System.out.println("\nGood bye;)");
    }

    public static int getUserChoice() {
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

    public static void printErrorMessage() {
        System.out.println("\nBad choice");
    }

    public static String[] getUserData() {
        String name = takeInput("Name: ");
        String surname = takeInput("Surname: ");
        String email = takeInput("E-mail: ");
        String dateOfBirth = takeDateInput("Date of birth: ");

        String[] userData = new String[] {name, surname, email, dateOfBirth};

        return userData;
    }

    public static String takeInput(String label) {
        Scanner inputScan = new Scanner(System.in);
        System.out.println(label);
        String input = inputScan.next();

        return input;
   }

   public static String takeDateInput(String label) {
       Scanner inputScan = new Scanner(System.in);
       String input;

       do {
           System.out.println(label);
           input = inputScan.next();
       } while (!input.matches("^[2][0][1-9][0-9]-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$"));

       return input;
   }

   public static String[] getQuestData() {
       String name = takeInput("Name: ");
       String reward = takeInput("Reward (coolcoins): ");
    
       String[] questData = new String[] {name, reward};
       
       return questData;
   }

   public static String[] getArtifactData() {
       String name = takeInput("Name: ");
       String value = takeInput("Value (coolcoins): ");
    
       String[] artifactData = new String[] {name, value};
       
       return artifactData;
   }

   public static String chooseQuestCategory() {
       // Demo:
       System.out.println("Category:\n1. Basic\n2. Extra\n");
       String category = "Basic";
       return category;
   }

   public static String chooseArtifactType() {
       // Demo:
       System.out.println("Category:\n1. Basic\n2. Magic\n");
       String type = "Magic";
       return type;
   }

}