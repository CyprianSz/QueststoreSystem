package pl.coderampart.controller;

import pl.coderampart.DAO.*;
import pl.coderampart.model.*;
import pl.coderampart.services.Bootable;
import pl.coderampart.view.View;
import java.util.ArrayList;
import java.util.Arrays;

public class MentorController implements Bootable<Mentor> {

    private View view = new View();
    private CodecoolerDAO codecoolerDAO = new CodecoolerDAO();
    private GroupDAO groupDAO = new GroupDAO();
    private TeamDAO teamDAO = new TeamDAO();
    private ArtifactDAO artifactDAO = new ArtifactDAO();
    private ItemDAO itemDAO = new ItemDAO();
    private WalletDAO walletDAO = new WalletDAO();
    private QuestDAO questDAO = new QuestDAO();

    private static final int CREATE_CODECOOLER = 1;
    private static final int CREATE_QUEST = 2;
    private static final int CREATE_ARTIFACT = 3;
    private static final int CREATE_TEAM = 4;
    private static final int EDIT_CODECOOLER = 5;
    private static final int EDIT_QUEST = 6;
    private static final int EDIT_ARTIFACT = 7;
    private static final int EDIT_TEAM = 8;
    private static final int DISPLAY_CODECOOLERS = 9;
    private static final int DISPLAY_QUESTS = 10;
    private static final int DISPLAY_ARTIFACTS = 11;
    private static final int DISPLAY_TEAMS = 12;
    private static final int DELETE_QUEST = 13;
    private static final int DELETE_ARTIFACT = 14;
    private static final int DELETE_TEAM = 15;
    private static final int EXIT = 0;

    public boolean start(Mentor mentor) {
        view.displayMentorMenu();
        int userChoice = view.getUserChoice();

        view.clearTerminal();
        // TODO: USE ENUM, DECLARE SUBMENUS FOR EACH CLASS
        switch(userChoice) {

            case CREATE_CODECOOLER: createCodecooler();
                break;
            case CREATE_QUEST: createQuest();
                break;
            case CREATE_ARTIFACT: createArtifact();
                break;
            case CREATE_TEAM: createTeam();
                break;
            case EDIT_CODECOOLER: editCodecooler();
                break;
            case EDIT_QUEST: editQuest();
                break;
            case EDIT_ARTIFACT: editArtifact();
                break;
            case EDIT_TEAM: editTeam();
                break;
            case DISPLAY_CODECOOLERS: displayCodecoolers();
                break;
            case DISPLAY_QUESTS: displayQuests();
                break;
            case DISPLAY_ARTIFACTS: displayArtifacts();
                break;
            case DISPLAY_TEAMS: displayTeams();
                break;
            case DELETE_QUEST: deleteQuest();
                break;
            case DELETE_ARTIFACT: deleteArtifact();
                break;
            case DELETE_TEAM: deleteTeam();
                break;
            case EXIT:
                return false;
        }

        view.enterToContinue();
        return true;
    }
}