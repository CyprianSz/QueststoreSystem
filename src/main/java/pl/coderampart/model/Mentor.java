package pl.coderampart.model;

import java.time.LocalDate;

public class Mentor extends AbstractUser {

    private Group group;

    public Mentor(String name, String surname, String email, String password, LocalDate dateOfBirth) {
        super(name, surname, email, password, dateOfBirth);
        this.group = null;
    }

    public Group getGroup() { return this.group; }
    public void setGroup(Group group) { this.group = group; }
}