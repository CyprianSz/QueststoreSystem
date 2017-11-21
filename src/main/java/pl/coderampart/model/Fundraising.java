package pl.coderampart.model;

import pl.coderampart.controller.UUIDController;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Fundraising {

    private String ID;
    private Artifact artifact;
    private String name;
    private LocalDate creationDate;
    private Codecooler creator;
    private boolean isOpen;

    public Fundraising(Artifact artifact, String name, Codecooler creator) {
        this.ID = UUIDController.createUUID();
        this.artifact = artifact;
        this.name = name;
        this.creationDate = LocalDate.now();
        this.creator = creator;
        this.isOpen = true;
    }

    public Fundraising(String ID, Artifact artifact, String name, LocalDate date, Codecooler creator, boolean isOpen) {
        this.ID = ID;
        this.artifact = artifact;
        this.name = name;
        this.creationDate = date;
        this.creator = creator;
        this.isOpen = isOpen;
    }

    public String getID() {
        return ID;
    }

    public Artifact getArtifact() { return artifact; }

    public String getName() {
        return name;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Codecooler getCreator() {
        return creator;
    }

    public boolean getIsOpen() {
        return isOpen;
    }

    public void closeFundraising(boolean open) {
        isOpen = false;
    }

    public String dateToString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return this.creationDate.format(formatter);
    }

    public String getArtifactID() {
        return artifact.getID();
    }
}