package pl.coderampart.model;

import java.time.LocalDate;

public class Admin extends AbstractUser {

    public Admin(String first_name, String last_name, String password, String email, LocalDate dateOfBirth) {
        super(first_name, last_name, password, email, dateOfBirth);
    }

    public Admin(String ID, String first_name, String last_name, String password, String email, LocalDate dateOfBirth) {
        super(ID, first_name, last_name, password, email, dateOfBirth);
    }
}
