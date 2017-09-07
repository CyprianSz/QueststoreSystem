package coderampart.model;

public class Team extends Group{

    private Group group;

    public Team(){
        this.ID = null;
        this.name = null;
        this.group = null;
    }

    public Team(String name){
        this.ID = null;
        this.name = name;
        this.group = group;
    }

    public Group getgroup() {return this.group;}
    public void setGroup(Group group) {this.group = group;}

}
