package pl.coderampart.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Codecooler extends AbstractUser {

    private Wallet wallet;
    private Group group;
    private Level level;
    private Team team;
    private ArrayList<Achievement> achievementList;

    public Codecooler(String name, String surname, String email, String password, LocalDate dateOfBirth) {
        super(name, surname, email, password, dateOfBirth);
        this.wallet = new Wallet();
        // TODO: uncomment when levelDAO created
        // be careful on IndexOutOfBound. Handle it.
        // this.level = LevelDAO.levelList.get(0);
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