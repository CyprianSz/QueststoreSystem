package pl.coderampart.DAO;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class AchievementDAOTest {

    @Test
    void testReadAll() throws SQLException{
        ConnectionToDB cb = new ConnectionToDB();
        AchievementDAO dao = new AchievementDAO(cb.connectToDataBase());
        assertThrows(SQLException.class, () -> {
            dao.readAll();
        });
    }

}