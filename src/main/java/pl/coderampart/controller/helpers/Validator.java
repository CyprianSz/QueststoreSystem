package pl.coderampart.controller.helpers;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.sql.Connection;
import java.util.Map;

public class Validator {

    private HelperController helper;
    private Connection connection;

    public Validator(Connection connection) {
        this.helper = new HelperController(connection);
    }

    public String checkDateRegex(String date) throws IOException {

        String response = date;
        String dateRegEx = "^[12][09]\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";

        if(!date.matches(dateRegEx)){
            response = "Wrong format(YYYY-MM-DD)";
        }
        return response;
    }

    public String checkEmailRegex( String email) throws  IOException {
        String response = email;
        String emailRegEx = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+"
                + "(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
        if(!email.matches(emailRegEx)){
            response = "Wrong format";
        }
        return response;
    }


    public boolean validateData(Map<String, String> inputs) throws IOException {
        String dateOfBirth = inputs.get("date-of-birth");
        String email = inputs.get("email");
        String groupName = inputs.get("group");

        return checkDateRegex(dateOfBirth).equals(dateOfBirth)
                && checkEmailRegex(email).equals(email)
                && checkGroup(groupName).equals(groupName);
    }


    public String checkGroup(String groupName) {
        if( helper.getGroupByName(groupName) == null ) {
            return "Group doesn't exist";
        }
        return helper.getGroupByName(groupName).getName();
    }

    public String checkIfIsDigit(String input) {
        String regex = "[0-9]+";
        if(input.matches(regex)) {
            return input;
        }return "It's not a digit";
    }

}
