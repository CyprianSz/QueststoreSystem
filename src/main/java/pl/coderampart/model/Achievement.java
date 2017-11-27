package pl.coderampart.model;

import java.time.LocalDate;
import pl.coderampart.controller.helpers.UUIDController;

public class Achievement {

    private String ID;
    private Quest quest;
    private LocalDate creationDate;
    private Codecooler codecooler;

    public Achievement(Quest quest, Codecooler codecooler) {
        this.ID = UUIDController.createUUID();
        this.quest = quest;
        this.creationDate = LocalDate.now();
        this.codecooler = codecooler;

    }

    public Achievement(String ID, Quest quest, LocalDate date, Codecooler codecooler) {
        this.ID = ID;
        this.quest = quest;
        this.creationDate = date;
        this.codecooler = codecooler;
    }

    public String getID() {
        return ID;
    }

    public Quest getQuest() {
        return quest;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Codecooler getCodecooler() {
        return codecooler;
    }

}
