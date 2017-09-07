package coderampart.model;

public class Level {

    private Integer rank;
    private Integer requiredExperience;
    private String description;

    public Level() {
        this.rank = null;
        this.requiredExperience = null;
        this.description = null;
    }

    public Level(Integer rank, Integer requiredExperience, String description) {
        this.rank = rank;
        this.requiredExperience = requiredExperience;
        this.description = description;
    }
}
