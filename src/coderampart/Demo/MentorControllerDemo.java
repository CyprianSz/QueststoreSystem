package coderampart.Demo;

import coderampart.model.Mentor;

import java.util.Date;

public class MentorControllerDemo implements Bootable {

    public void start() {
        View.displayMentorMenu();
        Integer userChoice = View.getUserChoice();

        switch(userChoice) {
            case 1: createMentor();
                    break;
            case 2: addQuest();
                    break;
            case 3: updateQuest();
                    break;
            case 4: setQuestCategory();
                    break;
            case 5: markQuest();
                    break;
            case 6: addArtifact();
                    break;
            case 7: updateArtifact();
                    break;
            case 8: setArtifactCategory();
                    break;
            case 9: markArtifact();
                    break;
            case 10: displayWalletDetails();
                    break;
        }
    }

    public static void createMentor() {
        String[] userData = View.getUserData();

        String name = userData[0];
        String surname = userData[1];
        String email = userData[2];
        Date dateOfBirth = userData[3];
        Mentor mentor = new Codecooler(name, surname, email, dateOfBirth);

        // Demo:
        System.out.println("New Mentor: " + mentor);

    } 
}