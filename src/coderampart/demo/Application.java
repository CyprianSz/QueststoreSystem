package coderampart.demo;

public class Application {

    public static void main(String[] args) {
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

        System.out.println("\nGOOD BYE\n");
    }
}
