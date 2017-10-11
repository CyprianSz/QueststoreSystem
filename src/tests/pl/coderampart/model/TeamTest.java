package pl.coderampart.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeamTest {

    @Test
    void test1TeamCOnstructor(){
        Group group = new Group("name");
        Team team = new Team("name", group);
        assertNotNull(team);
    }

    @Test
    void test2TeamCOnstructor(){
        Group group = new Group("name");
        Team team = new Team("id", "name", group);
        assertNotNull(team);
    }

}