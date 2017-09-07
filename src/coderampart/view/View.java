package coderampart.view;

import java.io.IOException;

public class View {

    public void displayLogingInfo() {
        Scanner inputScannet = new Scanner(System.in);
        this.clearTerminal();
        System.out.println("\nLOGING IN:\n");
        this.takeInput("\nType login: ");
        this.takeInput("\nType password: ");
    }

    private void displayProgressBar() {
        /* Prints imitation of progress bar, and at the same time
        percentage of work done. Uses 'format' to achieve stady format
        of displayed percentage value (completes value with zeros). */
        String progressBar = "";

        for (int i = 1; i <= 50; i++) {
            progressBar += "#";

            view.clearTerminal();
            System.out.println("\n-----> LOGING IN - PLEASE WAIT <-----\n");

            System.out.println(progressBar);
            System.out.println(progressBar);
            System.out.format("%n-----> " + "%03d" + "%s" + " <-----", i*2, "%");

            delayExecutionFor(70);
        }
        System.out.println("\n\n-----> LOGED IN SUCCESSFULLY <-----");
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

}
