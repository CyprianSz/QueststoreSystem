package pl.coderampart.model;

import java.time.LocalDate;
import pl.coderampart.controller.UUIDController;

public class Achievement {

    private String ID;
    private Quest quest;
    private LocalDate creationDate;


    public Achievement(Quest quest) {
        this.ID = UUIDController.createUUID();
        this.quest = quest;
        this.creationDate = LocalDate.now();
    }
}
