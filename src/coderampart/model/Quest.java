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
}
