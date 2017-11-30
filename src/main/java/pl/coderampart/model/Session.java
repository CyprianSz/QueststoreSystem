package pl.coderampart.model;

import pl.coderampart.controller.helpers.UUIDController;

public class Session {

    protected String ID;
    protected String userID;
    protected String userFirstName;
    protected String userLastName;
    protected String userEmail;
    protected String userType;

    public Session(String userID, String userFirstName, String userLastName,
                   String userEmail, String userType) {

        this.ID = UUIDController.createUUID();
        this.userID = userID;
        this.userFirstName = userFirstName;
        this.userLastName= userLastName;
        this.userEmail = userEmail;
        this.userType = userType;
    }

    public Session(String ID, String userID, String userFirstName,
                   String userLastName, String userEmail, String userType) {

        this.ID = ID;
        this.userID = userID;
        this.userFirstName = userFirstName;
        this.userLastName= userLastName;
        this.userEmail = userEmail;
        this.userType = userType;
    }

    public String getID() {
        return ID;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserType() { return userType; }
}

