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

    public void createCodecooler(){
        this.displayCodecoolers();

        String[] codecoolerData = view.getUserData();

        Codecooler newCodecooler = new Codecooler(codecoolerData[0], codecoolerData[1], view.stringToDate(codecoolerData[2]),
                                                  codecoolerData[3], codecoolerData[4]);

        this.displayTeams();
        ArrayList<Team> allTeams = teamDAO.readAll();
        String chosenTeamName = view.getInput("Enter name of a team you wish to assign this Codecooler to, " +
                                              "\nAdditionally, Codecooler will be assigned to the group that chosen team is in");
        for (Team team: allTeams){
            String teamName = team.getName();

            if (teamName.equals(chosenTeamName)){
                newCodecooler.setTeam(team);
                newCodecooler.setGroup(team.getGroup());
            }
        }

        codecoolerDAO.create(newCodecooler);
    }

    public void createQuest(){

    }

    public void createArtifact(){

    }

    public void createTeam(){

    }

    public void editCodecooler(){

    }

    public void editQuest(){

    }

    public void editArtifact(){

    }

    public void editTeam(){

    }

    public void displayCodecoolers(){
        ArrayList<Codecooler> allCodecoolers = codecoolerDAO.readAll();
        ArrayList<String> codecoolerStrings = new ArrayList<String>();

        for (Codecooler codecooler: allCodecoolers){
            codecoolerStrings.add(codecooler.toString());
        }

        view.outputTable(codecoolerStrings);
    }

    public void displayQuests(){
        ArrayList<Quest> allQuests = questDAO.readAll();
        ArrayList<String> questStrings = new ArrayList<String>();

        for (Quest quest: allQuests){
            questStrings.add(quest.toString());
        }

        view.outputTable(questStrings);
    }

    public void displayArtifacts(){
        ArrayList<Artifact> allArtifacts = artifactDAO.readAll();
        ArrayList<String> artifactStrings = new ArrayList<String>();

        for (Artifact artifact: allArtifacts){
            artifactStrings.add(artifact.toString());
        }

        view.outputTable(artifactStrings);
    }

    public void displayTeams(){
        ArrayList<Team> allTeams = teamDAO.readAll();
        ArrayList<String> teamStrings = new ArrayList<String>();

        for (Team team: allTeams){
            teamStrings.add(team.toString());
        }

        view.outputTable(teamStrings);
    }

    public void deleteQuest(){
        this.displayQuests();

        ArrayList<Quest> allQuests = questDAO.readAll();
        String chosenQuestName = view.getInput("Enter name of a quest you wish to delete: ");

        for (Quest quest: allQuests){
            if (chosenQuestName.equals(quest.getName())){
                questDAO.delete(quest);
            }
        }
    }

    public void deleteArtifact(){
        this.displayArtifacts();

        ArrayList<Artifact> allArtifacts = artifactDAO.readAll();
        String chosenArtifactName = view.getInput("Enter name of an artifact you wish to delete: ");

        for (Artifact artifact: allArtifacts){
            if (chosenArtifactName.equals(artifact.getName())){
                artifactDAO.delete(artifact);
            }
        }
    }

    public void deleteTeam() {
        this.displayTeams();

        ArrayList<Team> allTeams = teamDAO.readAll();
        String chosenTeamName = view.getInput("Enter name of a team you wish to delete: ");

        for (Team team: allTeams){
            if (chosenTeamName.equals(team.getName())){
                teamDAO.delete(team);
            }
        }
    }
}