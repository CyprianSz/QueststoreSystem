package pl.coderampart.model;

import java.time.LocalDate;
import pl.coderampart.controller.helpers.UUIDController;

public abstract class AbstractUser {

    protected String ID;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String password;
    protected LocalDate dateOfBirth;

    public AbstractUser(String firstName, String lastName, LocalDate dateOfBirth, String email, String password) {
        this.ID = UUIDController.createUUID(firstName);
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.password = password;
    }

    public AbstractUser(String ID, String firstName, String lastName,
                        LocalDate dateOfBirth, String email, String password) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
    }

    public String getID() { return this.ID; }
    public String getFirstName() { return this.firstName; }
    public String getLastName() { return this.lastName; }
    public String getEmail() { return this.email; }
    public String getPassword() { return this.password; }
    public LocalDate getDateOfBirth() { return this.dateOfBirth; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String toString() {
        String userData = "\nID: " + this.getID()
                        + "\nname: " + this.getFirstName()
                        + "\ndate of birth: " + this.getDateOfBirth()
                        + "\nsurname: " + this.getLastName()
                        + "\nemail: " + this.getEmail();

        return userData;
    }
}
