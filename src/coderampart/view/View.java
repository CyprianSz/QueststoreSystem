package coderampart.view;

public class View {

    public static void displayMentorMenu() {
        ArrayList<String> options = new ArrayList<>(Arrays.asList("Create new Codecooler", "Add quest", "Update quest",
                                                             "Set quest category", "Mark quest", "Add artifact", "Update artifact", 
                                                             "Set artifact type", "Mark artifact", "Display wallet details"));

        displayOptions(options);
        System.out.println("\n0. Exit");
    }

}