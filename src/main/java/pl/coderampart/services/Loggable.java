package pl.coderampart.services;

import java.sql.SQLException;
import java.time.LocalDate;

public interface Loggable {
    public String getID();
    public String getFirstName();
    public String getLastName();
    public String getEmail();
    public LocalDate getDateOfBirth();
    public String getType();
    public String getPassword();
    public void setFirstName(String firstName);
    public void setLastName(String lastName);
    public void setEmail(String email);
    public void setDateOfBirth(LocalDate birthDate);
    public void setPassword(String password);
}
