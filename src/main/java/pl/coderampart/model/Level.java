package pl.coderampart.model;

import pl.coderampart.controller.UUIDController;

public class Level {

    private String ID;
    private Integer rank;
    private Integer requiredExperience;
    private String description;

    public Level(Integer rank, Integer requiredExperience, String description) {
        this.ID = UUIDController.createUUID();
        this.rank = rank;
        this.requiredExperience = requiredExperience;
        this.description = description;
    }

    public String toString() {
        String levelInfo = "\nLevel number: " + this.rank
                         + "\nRequired experience: " + this.requiredExperience
                         + "\nDescription: " + this.description;
        return levelInfo;
    }
}
