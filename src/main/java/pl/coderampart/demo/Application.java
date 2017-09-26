package pl.coderampart.demo;

import pl.coderampart.view.View;


public class Application {

    public static void main(String[] args) {
        MentorControllerDemo mentorController = new MentorControllerDemo();
        CodecoolerControllerDemo codecoolerController = new CodecoolerControllerDemo();
        Login login = new Login();
        View view = new View();
        boolean proceed = true;

        login.start();

        while (proceed) {
            proceed = mentorController.start();
        }

        login.start();
        proceed = true;

        while (proceed) {
            proceed = codecoolerController.start();
        }

        view.output("\nSEE YOU SOON\n");
    }
}
