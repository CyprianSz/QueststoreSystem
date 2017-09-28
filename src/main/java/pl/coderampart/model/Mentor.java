package pl.coderampart.model;

import java.time.LocalDate;

public class Mentor extends AbstractUser {

    private Group group;

    public Mentor(String firstName, String lastName, LocalDate dateOfBirth, String email, String password) {
        super(firstName, lastName, dateOfBirth, email, password);
        this.group = null;
    }

    public Mentor(String ID, String firstName, String lastName, LocalDate dateOfBirth,
                  String email, String password, Group group) {
        super(ID, firstName, lastName, dateOfBirth, email, password);
        this.group = group;
    }

    public Group getGroup() { return this.group; }
    public void setGroup(Group group) { this.group = group; }
}
