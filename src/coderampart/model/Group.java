package coderampart.model;

public class Group{

    private String ID;
    private String name;

    public Group(){
        this.ID = null;
        this.name = null;
    }

    public Group(String name){
        this.ID = null;
    }

    public String toString(){
        String groupData = "\nID: " + this.getID()
                         + "\nname: " + this.getName();

        return groupData;
    }

    public String getID(){ return this.ID; }
    public String getName(){ return this.name; }
}
