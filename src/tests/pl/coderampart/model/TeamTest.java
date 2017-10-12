package pl.coderampart.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TeamTest {

    private Group group;
    private Team team;

    @BeforeEach
    void setup(){
        this.group = mock(Group.class);
        this.team = new Team("test", group);
    }

    @Test
    void test1TeamConstructor(){
        Team team = new Team("test", group);
        assertNotNull(team);
    }

    @Test
    void test2TeamConstructor(){
        assertNotNull(team);
    }

    @Test
    void testTeamGetGroup(){
        assertNotNull(team.getGroup());
    }
    @Test
    void testTeamGetName(){
        assertEquals("test", team.getName());
    }

    @Test
    void testSetTeamGroup(){
        Group group = mock(Group.class);
        team.setGroup(group);
        assertEquals(group, team.getGroup());
    }

    @Test
    void testSetTeamName(){
        team.setName("new");
        assertEquals("new", team.getName());
    }

}