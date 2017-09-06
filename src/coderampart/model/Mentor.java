package coderampart.model;

import java.time.LocalDate;

public class Mentor extends AbstractUser {

    private Group group;

    public Mentor() {
        this.ID = null;
        this.name = null;
        this.surname = null;
        this.email = null;
        this.password = null;
        this.dateOfBirth = null;
        this.group = null;
    }

    public Mentor(String name, String surname, String email, LocalTime dateOfBirth) {
        this.ID = null; // ID generatoro here
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = null; // some method to create password here
        this.dateOfBirth = dateOfBirth;
    }

    public Group getGroup() { return this.group; }
    public void setGroup(Group group) { this.group = group; }

}
