package pl.coderampart.model;

import java.time.LocalDate;
import java.util.ArrayList;
import pl.coderampart.DAO.LevelDAO;

public class Codecooler extends AbstractUser {

    private LevelDAO levelDAO = new LevelDAO();
    private Wallet wallet;
    private Group group;
    private Level level;
    private Team team;
    private ArrayList<Achievement> achievementList;

    public Codecooler(String name, String surname, String email, String password, LocalDate dateOfBirth) {
        super(name, surname, email, password, dateOfBirth);
        this.wallet = new Wallet();
        this.level = levelDAO.getFirstLevel();
        this.team = null;
        this.achievementList = new ArrayList<Achievement>();
    }

    public Codecooler(String ID, String name, String surname, String email, String password,
                      LocalDate dateOfBirth, Wallet wallet, Level level, Team team) {
        super(ID, name, surname, email, password, dateOfBirth);
        this.wallet = wallet;
        this.level = level;
        this.team = team;
        this.achievementList = new ArrayList<Achievement>();
    }


    public Wallet getWallet() { return this.wallet; }
    public Level getLevel() { return this.level; }
    public Group getGroup() { return this.group; }
    public Team getTeam() {return this.team; }
    public void setGroup(Group group) { this.group = group; }
    public void setTeam(Team team) { this.team = team; }

}
