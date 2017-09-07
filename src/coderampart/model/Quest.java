package coderampart.model;

public class Quest{

    private String ID;
    private String name;
    private String category;
    private Integer reward;

    public Quest(){

    }

    public Quest(String name, Integer reward){
        this.name = name;
        this.reward = reward;
    }

    public String getID() { return this.ID; }
    public String getName() { return this.name; }
    public String getCategory() { return this.category; }
    public Integer getReward()  {return this.reward; }
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setReward(Integer reward) { this.reward = reward; }


    public String toString() {
        String questData = "\nID: " + this.getID()
                         + "\nname: " + this.getName()
                         + "\ncategory: " + this.getCategory()
                         + "\nreward: " + this.getReward();

        return questData;
    }
}
