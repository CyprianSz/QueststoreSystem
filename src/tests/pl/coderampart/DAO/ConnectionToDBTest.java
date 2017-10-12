package pl.coderampart.DAO;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@RunWith(MockitoJUnitRunner.class)
class ConnectionToDBTest {

    private ConnectionToDB connectionToDB = new ConnectionToDB();

    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement stmt;
    @Mock
    private ResultSet rs;

    @Before
    public void setUp() throws SQLException {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testDatabaseConnection() throws SQLException{
        when(connection.createStatement()).thenReturn(stmt);
        when(connection.createStatement().executeUpdate(any(String.class))).thenReturn(1);
        when(stmt.executeQuery()).thenReturn(rs);
        int value = connection.createStatement().executeUpdate("");
        assertEquals(value, 1);
        verify(connection.createStatement(), times(1));
    }
}

