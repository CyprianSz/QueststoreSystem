package coderampart.model;

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

}
