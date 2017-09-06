package coderampart.model;

import java.time.LocalDate;

public abstract class AbstractUser {

    private String ID;
    private String name;
    private String surname;
    private String email;
    private String password;
    private LocalDate dateOfBirth;

    public String getID() { return self.ID; }
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
