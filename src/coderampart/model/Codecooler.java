package coderampart.model;

import java.time.LocalDate;

public class Codecooler extends AbstractUser {

    private Wallet wallet;
    private Group group;
    private Level level;
    private Team team;
    private ArrayList<Achievement> achievementList;

    public Codecooler() {
        // CZY TWORZYĆ TAKIE PUSTE KONSTRUKTORY ???!!! (były w projekcie)
        this.ID = null;
        this.name = null;
        this.surname = null;
        this.email = null;
        this.password = null;
        this.dateOfBirth = null;
        this.group = null;
        this.wallet = null;
        this.level = null;
        this.team = null;
        this.achievementList = null;
    }

    public Codecooler(String name, String surname, String email, LocalTime dateOfBirth) {
        this.ID = null; // ID generatoro here
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = null; // some method to create password here
        this.dateOfBirth = dateOfBirth;
        this.wallet = new Wallet();
        this.level = LevelDAO.levelList.get(0); // tutaj uważać bo może być IndexOutOfBound exception jak nie będzie jeszcze żadnego levelu
        this.team = null;
        this.achievementList = new ArrayList<Achievement>();
    }

    public Wallet getWallet() { return this.wallet; }
    public Level getLevel() { return this.level; }
    public Group getGroup() { return this.group; }
    public Team getTeam() {return this.team; }
    public void setGroup(Group group) { this.group = group; }
    public void setTeam(Team team) { this.team = team; }

}
