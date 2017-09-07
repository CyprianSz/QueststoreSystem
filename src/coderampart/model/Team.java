package coderampart.model;

public class Team extends Group{

    private Group group;

    public Team(){
    }

    public Team(String name){
        this.name = name;
        this.group = group;
    }

    public Group getgroup() {return this.group;}
    public void setGroup(Group group) {this.group = group;}

}
