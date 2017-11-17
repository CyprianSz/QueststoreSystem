package pl.coderampart.model;

import pl.coderampart.services.Loggable;

import java.time.LocalDate;

public class Admin extends AbstractUser implements Loggable {

    public Admin(String firstName, String lastName, LocalDate dateOfBirth, String email, String password) {
        super(firstName, lastName, dateOfBirth, email, password);
    }

    public Admin(String ID, String firstName, String lastName, LocalDate dateOfBirth, String email, String password) {
        super(ID, firstName, lastName, dateOfBirth, email, password);
    }

    public String getType() { return this.getClass().getSimpleName(); }
}
