package pl.coderampart.model;

import java.time.LocalDate;

public class Mentor extends AbstractUser {

    private Group group;

    public Mentor(String firstName, String lastName, LocalDate dateOfBirth, String email, String password) {
        super(firstName, lastName, dateOfBirth, email, password);
        this.group = null;
    }

    public Mentor(String ID, String firstName, String lastName, LocalDate dateOfBirth,
                  String email, String password, Group group) {
        super(ID, firstName, lastName, dateOfBirth, email, password);
        this.group = group;
    }

    public String toString() {
        String userData = "\nID: " + this.getID()
                        + "\nname: " + this.getFirstName()
                        + "\ndate of birth: " + this.getDateOfBirth()
                        + "\nsurname: " + this.getLastName()
                        + "\nemail: " + this.getEmail();

        return userData;
    }

    public Group getGroup() { return this.group; }
    public void setGroup(Group group) { this.group = group; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setDateOfBirth(LocalDate birthDate) { this.dateOfBirth = birthDate; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
}
