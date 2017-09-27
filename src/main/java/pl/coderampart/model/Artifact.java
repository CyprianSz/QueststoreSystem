package pl.coderampart.model;

import pl.coderampart.controller.UUIDController;

public class Artifact {

    private String ID;
    private String name;
    private String type;
    private Integer value;

    public Artifact(String name, String type, Integer value) {
        this.ID = UUIDController.createUUID();
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public String getID() { return this.ID; }
    public String getType() { return this.type; }
    public String getName() { return this.name; }
    public Integer getValue() { return this.value; }
    public void setType(String type) { this.type = type; }
    public void setName(String name) { this.name = name; }
    public void setValue(Integer value) { this.value = value; }

    public String toString() {
        String userData = "\nID: " + this.getID()
                        + "\nname: " + this.getName()
                        + "\ntype: " + this.getType()
                        + "\nvalue: " + this.getValue();

        return userData;
    }
}
