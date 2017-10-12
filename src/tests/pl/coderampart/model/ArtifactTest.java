package pl.coderampart.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArtifactTest {

    private Artifact artifact;

    @BeforeEach
    public void setup() {
        artifact = new Artifact("1", "Artifact", "Very important", "Magic", 100);
    }

    @Test
    public void testArtifactConstructorIsNotNull() {

        assertNotNull(artifact);
    }

    @Test
    public void testToString() {

        String actual = artifact.toString();
        String expected = "\n" +
                "name: Artifact\n" +
                "type: Magic\n" +
                "value: 100";
        assertEquals(expected, actual);
    }

    @Test
    public void testGetId() {

        String actual = artifact.getID();
        String expected = "1";
        assertEquals(expected, actual);
    }

    @Test
    public void testGetName() {

        String actual = artifact.getName();
        String expected = "Artifact";
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDescription() {

        String actual = artifact.getDescription();
        String expected = "Very important";
        assertEquals(expected, actual);
    }

    @Test
    public void testGetType() {

        String actual = artifact.getType();
        String expected = "Magic";
        assertEquals(expected, actual);
    }

    @Test
    public void testSetName() {

        artifact.setName("New name");
        String expected = "New name";
        String actual = artifact.getName();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetValue() {

        artifact.setValue(200);
        Integer expected = 200;
        Integer actual = artifact.getValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetDescription() {

        artifact.setDescription("Testing");
        String expected = "Testing";
        String actual = artifact.getDescription();
        assertEquals(expected, actual);
    }
}