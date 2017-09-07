package coderampart.view;

import java.util.Scanner;

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

    public static void getUserData() {
        
    }

    public String takeInput(String label) {
        Scanner inputScan = new Scanner();
        System.out.println(label);
        String input = inputScan.next();
        
        return input;
   }




}