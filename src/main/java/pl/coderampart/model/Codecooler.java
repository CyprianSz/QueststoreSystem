package pl.coderampart.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import pl.coderampart.DAO.LevelDAO;

public class Codecooler extends AbstractUser {

    private Wallet wallet;
    private Group group;
    private Level level;
    private Team team;
    private ArrayList<Achievement> achievementList;
    private Connection connection;

    public Codecooler(String firstName, String lastName, LocalDate dateOfBirth, String email, String password, Connection connectionToDB) {
        super(firstName, lastName, dateOfBirth, email, password);
        this.connection = connectionToDB;
        this.wallet = new Wallet();
        this.group = null;
        LevelDAO levelDAO = new LevelDAO(this.connection);

        try{
            this.level = levelDAO.getFirstLevel();
        } catch (SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        this.team = null;
        this.achievementList = new ArrayList<Achievement>();
    }

    public Codecooler(String ID, String name, String surname, LocalDate dateOfBirth,
                      String email, String password, Wallet wallet, Group group,
                      Level level, Team team) {
        super(ID, name, surname, dateOfBirth, email, password);
        this.wallet = wallet;
        this.group = group;
        this.level = level;
        this.team = team;
        this.achievementList = new ArrayList<Achievement>();
    }

    public Wallet getWallet() { return this.wallet; }
    public void setLevel(Level level) {
        this.level = level;
    }
    public Level getLevel() { return this.level; }
    public Group getGroup() { return this.group; }
    public Team getTeam() {return this.team; }
    public void setGroup(Group group) { this.group = group; }
    public void setTeam(Team team) { this.team = team; }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
