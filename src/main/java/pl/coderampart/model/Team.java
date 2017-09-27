package pl.coderampart.model;

import pl.coderampart.controller.UUIDController;

public class Team extends Group{

    private Group group;

    public Team(String name, Group group){
        super(name);
        this.group = group;
    }

    public Group getgroup() { return this.group; }
    public void setGroup(Group group) { this.group = group; }
}
