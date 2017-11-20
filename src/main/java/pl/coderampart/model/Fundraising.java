package pl.coderampart.model;

import pl.coderampart.controller.UUIDController;
import java.time.LocalDate;

public class Fundraising {

    private String ID;
    private String name;
    private LocalDate creationDate;
    private Codecooler codecooler;
    private boolean isOpen;

    public Fundraising(String name, Codecooler codecooler) {
        this.ID = UUIDController.createUUID();
        this.name = name;
        this.creationDate = LocalDate.now();
        this.codecooler = codecooler;
        this.isOpen = true;
    }
}