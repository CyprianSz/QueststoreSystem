package pl.coderampart.DAO;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.sql.DataSource;


class AchievementDAOTest {


    @Mock
    private DataSource ds;
    @Mock
    private Connection c;
    @Mock
    private PreparedStatement stmt;
    @Mock
    private ResultSet rs;

    private AchievementDAO dao;

    @Before
    public void setUp() throws Exception {
        assertNotNull(ds);
        when(c.prepareStatement(any(String.class))).thenReturn(stmt);
        when(ds.getConnection()).thenReturn(c);
        dao = new AchievementDAO(c);
        when(rs.first()).thenReturn(true);

        when(stmt.executeQuery()).thenReturn(rs);
    }

    @Test
    void testReadAll() throws SQLException{
        ConnectionToDB cb = mock(ConnectionToDB.class);
        AchievementDAO dao = new AchievementDAO(cb.connectToDataBase());
        assertThrows(SQLException.class, () -> {
            dao.readAll();
        });
    }

}