package pl.coderampart.model;

import java.time.LocalDate;

public class Admin extends AbstractUser {

    public Admin(String firstName, String lastName, LocalDate dateOfBirth, String password, String email) {
        super(firstName, lastName, dateOfBirth, password, email);
    }

    public Admin(String ID, String firstName, String lastName, LocalDate dateOfBirth, String password, String email) {
        super(ID, firstName, lastName, dateOfBirth, password, email);
    }

    public String getType() { return this.getClass().getSimpleName(); }
}
