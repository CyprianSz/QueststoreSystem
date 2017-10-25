package pl.coderampart.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

class QuestTest {

    private Quest quest;

    @BeforeEach
    void setup(){
        this.quest = new Quest("test", "test", "test", 100);
    }

    @Test
    void test1QuestConstructor() {
        Quest quest = new Quest("Nowy", "Quest", 100);
        assertNotNull(quest);
    }
    @Test
    void test2QuestConstructor() {
        assertNotNull(quest);
    }

    @Test
    void testGetId(){
        assertNotNull(quest.getID());
    }

    @Test
    void testGetName(){
        assertNotNull(quest.getName());
    }

    @Test
    void testGetDescription(){
        assertNotNull(quest.getDescription());
    }

    @Test
    void testGetReward(){
        assertNotNull(quest.getReward());
    }

    @Test
    void testSetQuestName(){
        quest.setName("new");
        assertEquals("new", quest.getName());
    }

    @Test
    void testSetQuestDescription(){
        quest.setDescription("new");
        assertEquals("new", quest.getDescription());
    }

    @Test
    void testSetQuestReward(){
        quest.setReward(50);
        assertEquals(Integer.valueOf(50), quest.getReward());

    }



}