package pl.coderampart.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GroupTest {

    private Group group;

    @BeforeEach
    public void setup() {
         this.group = new Group("1", "Team");
    }

    @Test
    public void testToString() {

        String actual = group.toString();
        String expected = "\n" +
                "ID: 1\n" +
                "name: Team";

        assertEquals(expected, actual);
    }

    @Test
    public void testGroupConstructor() {

        String actual = group.getID() + group.getName();
        String expected = "1" + "Team";
        assertEquals(expected, actual);
    }

    @Test
    public void testGroupConstructorNotNull() {

        assertNotNull(group);
    }

    @Test
    public void testGetId() {

        String expected = "1";
        String actual = group.getID();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetName() {

        String expected = "Team";
        String actual = group.getName();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetName() {

        group.setName("Testing");
        String expected = "Testing";
        String actual = group.getName();
        assertEquals(expected, actual);
    }
}