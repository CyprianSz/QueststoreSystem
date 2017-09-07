package coderampart.model;

public class Artifact {

    private String ID;
    private String type;
    private String name;
    private Integer value;

    public Artifact() {
        this.ID = null;
        this.type = null;
        this.name = null;
        this.value = null;
    }

    public Artifact(String name, Integer value) {
        this.ID = null; // ID generatoro here
        this.name = name;
        this.value = value;
    }

    parseTo

    public String getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public Integer getValue() {
        return this.value;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
