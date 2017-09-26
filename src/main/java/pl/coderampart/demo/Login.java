package pl.coderampart.demo;


import pl.coderampart.view.View;

public class Login {

    private View view = new View();

    public void start() {
        view.displayLogingInfo();
        view.displayProgressBar();
    }
}
