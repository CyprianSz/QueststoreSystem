package pl.coderampart.model;

import pl.coderampart.controller.UUIDController;

public class Quest{

    private String ID;
    private String name;
    private String description;
    private Integer reward;

    public Quest(String name, String description, Integer reward){
        this.ID = UUIDController.createUUID(name);
        this.name = name;
        this.description = description;
        this.reward = reward;
    }

    public Quest(String ID, String name, String description, Integer reward) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.reward = reward;
    }

    public String getID() { return this.ID; }
    public String getName() { return this.name; }
    public String getDescription() { return this.description; }
    public Integer getReward()  {return this.reward; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String category) { this.description = category; }
    public void setReward(Integer reward) { this.reward = reward; }


    public String toString() {
        String questData = "\nID: " + this.getID()
                         + "\nname: " + this.getName()
                         + "\ndescription: " + this.getDescription()
                         + "\nreward: " + this.getReward();

        return questData;
    }
}
