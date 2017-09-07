package coderampart.model;

import java.time.LocalDate;

public abstract class AbstractUser {

    protected String ID;
    protected String name;
    protected String surname;
    protected String email;
    protected String password;
    protected LocalDate dateOfBirth;

    public String getID() { return this.ID; }
    public String getName() { return this.name; }
    public String getSurname() { return this.surname; }
    public String getEmail() { return this.email; }
    public String getPassword() { return this.password; }
    public LocalDate getDateOfBirth() { return this.dateOfBirth; }
    public void setEmail(String password) { this.password = password; }

    public String toString() {
        String userData = "\nID: " + this.getID()
                        + "\nname: " + this.getName()
                        + "\nsurname: " + this.getSurname()
                        + "\nemail: " + this.getEmail()
                        + "\ndate of birth: " + this.getDateOfBirth();

        return userData;
    }
}
