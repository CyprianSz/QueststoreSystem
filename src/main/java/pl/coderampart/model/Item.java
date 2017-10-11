package pl.coderampart.model;

import pl.coderampart.controller.UUIDController;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class Item {

    private String ID;
    private Artifact artifact;
    private Wallet wallet;
    private LocalDate creationDate;
    private boolean isSpent;

    public Item(Artifact artifact, Wallet wallet) {
        this.ID = UUIDController.createUUID();
        this.artifact = artifact;
        this.wallet = wallet;
        this.creationDate = LocalDate.now();
        this.isSpent = false;
    }

    public Item(String ID, Artifact artifact, Wallet wallet, LocalDate date, boolean isSpent) {
        this.ID = ID;
        this.artifact = artifact;
        this.wallet = wallet;
        this.creationDate = date;
        this.isSpent = isSpent;
    }

    public String getID() { return this.ID; }
    public Artifact getArtifact() { return this.artifact; }
    public Wallet getWallet() { return this.wallet; }
    public LocalDate getCreationDate() { return this.creationDate; }
    public boolean getMark() { return this.isSpent; }
    public void setMark() {
        this.isSpent = true;
    }

    public String dateToString() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return this.creationDate.format(formatter);
    }

    @Override
    public String toString() {

        return this.artifact.getName();
    }

}
