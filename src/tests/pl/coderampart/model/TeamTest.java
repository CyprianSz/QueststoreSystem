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
        this.group = new Group("test");
        this.team = new Team("test", group);
    }

    @Test
    void test1TeamCOnstructor(){
        assertNotNull(team);
    }

    @Test
    void test2TeamCOnstructor(){
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
    void setTeamGroup(){
        Group group = mock(Group.class);
        team.setGroup(group);
        assertEquals(group, team.getGroup());
    }

    @Test
    void setTeamName(){
        team.setName("new");
        assertEquals("new", team.getName());
    }

}