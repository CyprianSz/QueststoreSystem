package coderampart.model;

import java.time.LocalDate;

public class Archievement {

    private Quest quest;
    private LocalDate creationDate;

    public Achievement() {
        this.quest = null;
        this.creationDate = null;
    }

    public Achievement(Quest quest) {
        this.quest = quest;
        this.creationDate = LocalDate.now();
    }
}
