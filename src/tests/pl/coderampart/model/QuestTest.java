package pl.coderampart.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

class QuestTest {

    @Test
    void test1QuestConstructor() {
        Quest quest = new Quest("Nowy", "Quest", 100);
        assertNotNull(quest);
    }
    @Test
    void test2QuestConstructor() {
        Quest quest = new Quest("id","Nowy", "Quest", 100);
        assertNotNull(quest);
    }



}