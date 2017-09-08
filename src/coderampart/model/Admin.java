package coderampart.model;

import java.time.LocalDate;

public class Admin extends AbstractUser {

    // TODO: remove after testing
    public Admin() {
        super();
    }

    public Admin(String name, String surname, String email, LocalDate dateOfBirth) {
        super(name, surname, email, dateOfBirth);
    }

}
