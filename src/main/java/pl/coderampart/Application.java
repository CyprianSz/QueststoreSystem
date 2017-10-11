package pl.coderampart;

import java.sql.SQLException;

public class Application {

    public static void main(String[] args) {

        try {
            Logger logger = new Logger();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}