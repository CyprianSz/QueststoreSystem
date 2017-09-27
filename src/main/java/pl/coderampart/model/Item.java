package pl.coderampart.model;

import pl.coderampart.controller.UUIDController;
import java.time.LocalDate;

public class Item {

    private String ID;
    private Artifact artifact;
    private LocalDate creationDate;
    private boolean isMarked;

    public Item(Artifact artifact) {
        this.ID = UUIDController.createUUID();
        this.artifact = artifact;
        this.creationDate = LocalDate.now();
        this.isMarked = false;
    }

    public void setMark() {
        this.isMarked = true;
    }

    public boolean getMark() {
        return this.isMarked;
    }
}
