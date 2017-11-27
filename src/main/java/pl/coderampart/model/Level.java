package pl.coderampart.model;

import pl.coderampart.controller.helpers.UUIDController;

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

    public Level(String ID, Integer rank, Integer requiredExperience, String description) {
        this.ID = ID;
        this.rank = rank;
        this.requiredExperience = requiredExperience;
        this.description = description;
    }

    public String getID() { return this.ID; }
    public Integer getRank() { return this.rank; }
    public Integer getRequiredExperience() { return this.requiredExperience; }
    public String getDescription() { return this.description; }
    public void setRank(Integer rank) { this.rank = rank; }
    public void setRequiredExperience(Integer reqExp) { this.requiredExperience = reqExp; }
    public void setDescription(String description) { this.description = description; }

    public String toString() {
        String levelInfo = "\nLevel number: " + this.rank
                         + "\nRequired experience: " + this.requiredExperience
                         + "\nDescription: " + this.description;
        return levelInfo;
    }


}
