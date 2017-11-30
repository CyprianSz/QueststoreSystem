package pl.coderampart.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import pl.coderampart.DAO.LevelDAO;
import pl.coderampart.DAO.WalletDAO;
import pl.coderampart.services.Loggable;

public class Codecooler extends AbstractUser implements Loggable {

    private Wallet wallet;
    private Group group;
    private Level level;
    private Team team;
    private Connection connection;

    public Codecooler(String firstName, String lastName, LocalDate dateOfBirth, String email, String password, Connection connectionToDB) {
        super(firstName, lastName, dateOfBirth, email, password);
        this.connection = connectionToDB;
        this.wallet = new Wallet();
        this.group = null;
        this.team = null;

        try {
            WalletDAO walletDAO = new WalletDAO(this.connection);
            LevelDAO levelDAO = new LevelDAO(this.connection);
            this.level = levelDAO.getFirstLevel();
            walletDAO.create(this.wallet);
        } catch (SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public Codecooler(String ID, String name, String surname, LocalDate dateOfBirth,
                      String email, String password, Wallet wallet, Group group,
                      Level level, Team team) {
        super(ID, name, surname, dateOfBirth, email, password);
        this.wallet = wallet;
        this.group = group;
        this.level = level;
        this.team = team;
    }

    public Wallet getWallet() { return this.wallet; }

    public Level getLevel() { return this.level; }

    public Group getGroup() { return this.group; }

    public Team getTeam() { return this.team; }

    public String getType() { return this.getClass().getSimpleName(); }

    public void setLevel(Level level) {
        this.level = level;
    }

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
