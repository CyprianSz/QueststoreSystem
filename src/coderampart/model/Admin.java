package coderampart.model;

import java.time.LocalDate;

public class Admin extends AbstractUser {

    public Admin() {
        this.ID = null;
        this.name = null;
        this.surname = null;
        this.email = null;
        this.password = null;
        this.dateOfBirth = null;
    }

    public Admin(String name, String surname, String email, LocalDate dateOfBirth) {
        this.ID = null; // ID generatoro here
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = null; // some method to create password here
        this.dateOfBirth = dateOfBirth;
    }

}
