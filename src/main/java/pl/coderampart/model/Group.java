package pl.coderampart.model;

import pl.coderampart.controller.UUIDController;

public class Group{

    protected String ID;
    protected String name;


    public Group(String name){
        this.ID = UUIDController.createUUID(name);
        this.name = name;
    }

    public String toString(){
        String groupData = "\nID: " + this.getID()
                         + "\nname: " + this.getName();

        return groupData;
    }

    public String getID(){ return this.ID; }
    public String getName(){ return this.name; }
}
