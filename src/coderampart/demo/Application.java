package coderampart.demo;

public class Application {

    public static void main(String[] args) {
        MentorControllerDemo mentorController = new MentorControllerDemo();
        Login login = new Login();
        boolean proceed = true;

        login.start();

        while (proceed) {
            proceed = mentorController.start();
        }
    }
}
