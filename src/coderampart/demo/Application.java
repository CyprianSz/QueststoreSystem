package coderampart.demo;

import coderampart.view.View;

public class Application {

    public static void main(String[] args) {

        View myView = new View();

        MentorControllerDemo mentorController = new MentorControllerDemo();
        CodecoolerControllerDemo codecoolerController = new CodecoolerControllerDemo();
        Login login = new Login();
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

        myView.sayGoodbye();
    }
}
