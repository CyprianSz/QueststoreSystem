package pl.coderampart.model;

import java.time.LocalDate;
import pl.coderampart.controller.UUIDController;

public abstract class AbstractUser {

    protected String ID;
    protected String first_name;
    protected String last_name;
    protected String email;
    protected String password;
    protected LocalDate dateOfBirth;

    public AbstractUser(String first_name, String last_name, String email, String password, LocalDate dateOfBirth) {
        this.ID = UUIDController.createUUID(first_name);
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
    }

    public AbstractUser(String ID, String first_name, String last_name,
                        String email, String password, LocalDate dateOfBirth) {
        this.ID = ID;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
    }

    public String getID() { return this.ID; }
    public String getName() { return this.first_name; }
    public String getSurname() { return this.last_name; }
    public String getEmail() { return this.email; }
    public String getPassword() { return this.password; }
    public LocalDate getDateOfBirth() { return this.dateOfBirth; }

    public String toString() {
        String userData = "\nID: " + this.getID()
                        + "\nname: " + this.getName()
                        + "\nsurname: " + this.getSurname()
                        + "\nemail: " + this.getEmail()
                        + "\ndate of birth: " + this.getDateOfBirth();

        return userData;
    }
}
