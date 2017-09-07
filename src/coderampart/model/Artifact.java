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

    public Artifact(String type, String name, Integer value) {
        this.ID = null; // ID generatoro here
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public String getType() {
        return this.type;
    }
}
