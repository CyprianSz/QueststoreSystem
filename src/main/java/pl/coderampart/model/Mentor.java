package pl.coderampart.model;

import java.time.LocalDate;

public class Mentor extends AbstractUser {

    private Group group;

    // TODO: remove after testing
    public Mentor() {
        super();
        this.group = null;
    }

    public Mentor(String name, String surname, String email, LocalDate dateOfBirth) {
        super(name, surname, email, dateOfBirth);
    }

    public Group getGroup() { return this.group; }
    public void setGroup(Group group) { this.group = group; }

}
