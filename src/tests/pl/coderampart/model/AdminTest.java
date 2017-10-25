package pl.coderampart.model;
import pl.coderampart.view.View;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;


class AdminTest {

    @Test
    void testAdmin() {
        View view = new View();
        LocalDate birthday = view.stringToDate("1972-05-16");
        Admin admin  = new Admin("Andrzej", "Duda" ,birthday, "aduda@prezydent.pl", "qwerty");
        assertNotNull(admin);
    }

}