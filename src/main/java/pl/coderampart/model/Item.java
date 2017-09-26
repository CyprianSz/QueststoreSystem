package pl.coderampart.model;

import java.time.LocalDate;

public class Item {

    private Artifact artifact;
    private LocalDate creationDate;
    private boolean isMarked;

    public Item() {
        this.artifact = null;
        this.creationDate = null;
        this.isMarked = false;
    }

    public Item(Artifact artifact) {
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
