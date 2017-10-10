package pl.coderampart.controller;

import pl.coderampart.DAO.*;
import pl.coderampart.enums.*;
import pl.coderampart.model.*;
import pl.coderampart.services.Bootable;
import pl.coderampart.view.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class MentorController implements Bootable<Mentor> {

    private Mentor selfMentor;
    private MentorView mentorView = new MentorView();
    private CodecoolerDAO codecoolerDAO;
    private GroupDAO groupDAO;
    private TeamDAO teamDAO;
    private ArtifactDAO artifactDAO;
    private AchievementDAO achievementDAO;
    private ItemDAO itemDAO;
    private WalletDAO walletDAO;
    private QuestDAO questDAO;
    private Connection connection;

    public MentorController(Connection connectionToDB) {

        connection = connectionToDB;
        codecoolerDAO = new CodecoolerDAO(connection);
        groupDAO = new GroupDAO(connection);
        teamDAO = new TeamDAO(connection);
        artifactDAO = new ArtifactDAO(connection);
        achievementDAO = new AchievementDAO(connection);
        itemDAO = new ItemDAO(connection);
        walletDAO = new WalletDAO(connection);
        questDAO = new QuestDAO(connection);
    }


    public boolean start(Mentor mentor) {
        mentorView.displayMentorManagementMenu();
        selfMentor = mentor;

        int userChoice = mentorView.getUserChoice();

        MentorSubmenuOption mentorSubmenuOption = MentorSubmenuOption.values()[userChoice];

        mentorView.clearTerminal();

        switch(mentorSubmenuOption) {

            case DISPLAY_CODECOOLER_MANAGEMENT_MENU:
                startCodecoolerMM();
                break;
            case DISPLAY_QUEST_MANAGEMENT_MENU:
                startQuestMM();
                break;
            case DISPLAY_ARTIFACT_MANAGEMENT_MENU:
                startArtifactMM();
                break;
            case DISPLAY_TEAM_MANAGEMENT_MENU:
                startTeamMM();
                break;
            case EXIT:
                return false;
        }
        mentorView.enterToContinue();
        return true;
    }

    public boolean startCodecoolerMM(){

        mentorView.displayCodecoolerMM();
        int userChoice = mentorView.getUserChoice();

        CodecoolerMMOptions codecoolerMMOptions = CodecoolerMMOptions.values()[userChoice];
        mentorView.clearTerminal();
        switch (codecoolerMMOptions){
            case CREATE_CODECOOLER: createCodecooler();
                break;
            case EDIT_CODECOOLER: editCodecooler();
                break;
            case DISPLAY_CODECOOLERS: displayCodecoolers();
                break;
            case CREATE_ACHIEVEMENT: createAchievement();
                break;
            case MARK_ITEM: markItem();
                break;
            case DISPLAY_WALLET: displayWallet();
                break;
            case BACK_TO_MAIN_MENU:
                return false;
        }
        return true;
    }

    public boolean startQuestMM(){

        mentorView.displayQuestMM();
        int userChoice = mentorView.getUserChoice();

        QuestMMOptions questMMOptions = QuestMMOptions.values()[userChoice];
        mentorView.clearTerminal();
        switch (questMMOptions){
            case CREATE_QUEST: createQuest();
                break;
            case EDIT_QUEST: editQuest();
                break;
            case DISPLAY_QUESTS: displayQuests();
                break;
            case DELETE_QUEST: deleteQuest();
                break;
            case BACK_TO_MAIN_MENU:
                return false;
        }
        return true;
    }

    public boolean startArtifactMM(){

        mentorView.displayArtifactMM();
        int userChoice = mentorView.getUserChoice();

        ArtifactMMOptions artifactMMOptions = ArtifactMMOptions.values()[userChoice];
        mentorView.clearTerminal();
        switch (artifactMMOptions){
            case CREATE_ARTIFACT: createArtifact();
                break;
            case EDIT_ARTIFACT: editArtifact();
                break;
            case DISPLAY_ARTIFACTS: displayArtifacts();
                break;
            case DELETE_ARTIFACT: deleteArtifact();
                break;
            case BACK_TO_MAIN_MENU:
                return false;
        }
        return true;
    }

    public boolean startTeamMM(){

        mentorView.displayTeamMM();
        int userChoice = mentorView.getUserChoice();

        TeamMMOptions teamMMOptions = TeamMMOptions.values()[userChoice];
        mentorView.clearTerminal();
        switch (teamMMOptions){
            case CREATE_TEAM: createTeam();
                break;
            case EDIT_TEAM: editTeam();
                break;
            case DISPLAY_TEAMS: displayTeams();
                break;
            case DELETE_TEAM: deleteTeam();
                return false;
        }
        return true;
    }

    public void createCodecooler(){
        this.displayCodecoolers();

        String[] codecoolerData = mentorView.getUserData();

        Codecooler newCodecooler = new Codecooler(codecoolerData[0], codecoolerData[1], mentorView.stringToDate(codecoolerData[2]),
                                                  codecoolerData[3], codecoolerData[4], connection);

        this.displayTeams();
        try{
            ArrayList<Team> allTeams = teamDAO.readAll();
            String chosenTeamName = mentorView.getInput("Enter name of a team you wish to assign this Codecooler to, " +
                                                  "\nAdditionally, Codecooler will be assigned to the group of this mentor");
            for (Team team: allTeams){
                String teamName = team.getName();

                if (teamName.equals(chosenTeamName)){
                    newCodecooler.setTeam(team);
                }
            }
            newCodecooler.setGroup(selfMentor.getGroup());

            codecoolerDAO.create(newCodecooler);
        } catch (SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void createQuest(){
        this.displayQuests();

        String[] questData = mentorView.getQuestData();

        Quest newQuest = new Quest(questData[0], questData[1], Integer.valueOf(questData[2]));

        try {
            questDAO.create(newQuest);
        } catch (SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void createArtifact(){
        this.displayArtifacts();

        String[] artifactData = mentorView.getArtifactData();

        Artifact newArtifact = new Artifact(artifactData[0], artifactData[1], artifactData[2], Integer.valueOf(artifactData[3]));

        try {
            artifactDAO.create(newArtifact);
        } catch (SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void createTeam(){
        this.displayTeams();

        String teamName = mentorView.getInput("Enter name of a new team: ");

        Team newTeam = new Team(teamName, selfMentor.getGroup());

        try {
            teamDAO.create(newTeam);
        } catch (SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void editCodecooler(){

        Codecooler changedCodecooler = null;

        try {
            ArrayList<Codecooler> allCodecoolers = codecoolerDAO.readAll();
            String chosenCodecoolerEmail = mentorView.getInput("Enter email of a codecooler you wish to edit: ");
            for (Codecooler codecooler: allCodecoolers){
                if (chosenCodecoolerEmail.equals(codecooler.getEmail())){
                    changedCodecooler = codecooler;
                    break;
                }
            }

        } catch (SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }


        if (changedCodecooler != null) {
            final int EDIT_FIRST = 1;
            final int EDIT_LAST = 2;
            final int EDIT_EMAIL = 3;
            final int EDIT_PASSWORD = 4;
            final int EDIT_BIRTHDATE = 5;

            ArrayList<String> editCodecoolerOptions = new ArrayList<>(Arrays.asList("Edit first name", "Edit last name,",
                    "Edit email", "Edit password",
                    "Edit birthdate"));
            mentorView.displayOptions(editCodecoolerOptions);
            int userChoice = mentorView.getUserChoice();
            mentorView.clearTerminal();

            switch(userChoice){
                case EDIT_FIRST:
                    changedCodecooler.setFirstName(mentorView.getInput("Enter new name: "));
                    break;
                case EDIT_LAST:
                    changedCodecooler.setLastName(mentorView.getInput("Enter new name: "));
                    break;
                case EDIT_EMAIL:
                    changedCodecooler.setEmail(mentorView.getRegExInput(mentorView.emailRegEx, "Enter new email: "));
                    break;
                case EDIT_PASSWORD:
                    changedCodecooler.setPassword(mentorView.getInput("Enter new password: "));
                    break;
                case EDIT_BIRTHDATE:
                    changedCodecooler.setDateOfBirth(mentorView.stringToDate(mentorView.getRegExInput(mentorView.dateRegEx,
                            "Enter new date")));
                    break;
            }
            try{
                codecoolerDAO.update(changedCodecooler);
            } catch (SQLException e){
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }
    }

    public void editQuest(){

        Quest changedQuest = null;

        try {
            ArrayList<Quest> allQuests = questDAO.readAll();
            String chosenQuestName = mentorView.getInput("Enter name of a quest you wish to edit: ");
            for (Quest quest: allQuests){
                if(chosenQuestName.equals(quest.getName())){
                    changedQuest = quest;
                    break;
                }
            }
        } catch (SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        if (changedQuest != null){

            final int EDIT_NAME = 1;
            final int EDIT_DESCR = 2;
            final int EDIT_REWARD = 3;

            ArrayList<String> editQuestOptions = new ArrayList<>(Arrays.asList("Edit name", "Edit description",
                                                                               "Edit reward"));
            mentorView.displayOptions(editQuestOptions);
            int userChoice = mentorView.getUserChoice();
            mentorView.clearTerminal();

            switch(userChoice){
                case EDIT_NAME:
                    changedQuest.setName(mentorView.getInput("Enter new name: "));
                    break;
                case EDIT_DESCR:
                    changedQuest.setDescription(mentorView.getInput("Enter new description: "));
                    break;
                case EDIT_REWARD:
                    changedQuest.setReward(Integer.valueOf(mentorView.getInput("Enter new reward: ")));
                    break;
            }
            try {
                questDAO.update(changedQuest);
            } catch (SQLException e){
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }
    }

    public void editArtifact(){

        Artifact changedArtifact = null;

        try {
            ArrayList<Artifact> allArtifacts = artifactDAO.readAll();
            String chosenArtifactName = mentorView.getInput("Enter name of an artifact you wish to edit: ");

            for (Artifact artifact: allArtifacts){
                if(chosenArtifactName.equals(artifact.getName())){
                    changedArtifact = artifact;
                    break;
                }
            }
        } catch (SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        if (changedArtifact != null){

            final int EDIT_NAME = 1;
            final int EDIT_DESCRIPTION = 2;
            final int EDIT_TYPE = 3;
            final int EDIT_VALUE = 4;

            ArrayList<String> editArtifactOptions = new ArrayList<>(Arrays.asList("Edit name", "Edit description",
                                                                                  "Edit type", "Edit value"));
            mentorView.displayOptions(editArtifactOptions);
            int userChoice = mentorView.getUserChoice();
            mentorView.clearTerminal();

            switch(userChoice){

                case EDIT_NAME:
                    changedArtifact.setName(mentorView.getInput("Enter new name: "));
                    break;
                case EDIT_DESCRIPTION:
                    changedArtifact.setDescription(mentorView.getInput("Enter new description: "));
                    break;
                case EDIT_TYPE:
                    changedArtifact.setType(mentorView.getInput("Enter new type: "));
                    break;
                case EDIT_VALUE:
                    changedArtifact.setValue(Integer.valueOf(mentorView.getInput("Enter new value: ")));
                    break;
            }
            try{
                artifactDAO.update(changedArtifact);
            } catch (SQLException e){
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }
    }

    public void editTeam(){

        Team changedTeam = null;

        try {
            ArrayList<Team> allTeams = teamDAO.readAll();
            String chosenTeamName = mentorView.getInput("Enter name of a group you wish to edit: ");

            for (Team team: allTeams){
                if (chosenTeamName.equals(team.getName())){
                    changedTeam = team;
                }
            }

            if (changedTeam != null){
                changedTeam.setName(mentorView.getInput("Enter new name: "));
            }

            teamDAO.update(changedTeam);
        } catch (SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void displayCodecoolers(){

        try {
            ArrayList<Codecooler> allCodecoolers = codecoolerDAO.readAll();
            ArrayList<String> codecoolerStrings = new ArrayList<String>();

            for (Codecooler codecooler: allCodecoolers){
                codecoolerStrings.add(codecooler.toString());
            }

            mentorView.outputTable(codecoolerStrings);
        } catch (SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void displayQuests(){

        try {
            ArrayList<Quest> allQuests = questDAO.readAll();
            ArrayList<String> questStrings = new ArrayList<String>();

            for (Quest quest: allQuests){
                questStrings.add(quest.toString());
            }

            mentorView.outputTable(questStrings);
        } catch (SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void displayArtifacts(){

        try {
            ArrayList<Artifact> allArtifacts = artifactDAO.readAll();
            ArrayList<String> artifactStrings = new ArrayList<String>();

            for (Artifact artifact: allArtifacts){
                artifactStrings.add(artifact.toString());
            }

            mentorView.outputTable(artifactStrings);
        } catch (SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void displayTeams(){

        try {
            ArrayList<Team> allTeams = teamDAO.readAll();
            ArrayList<String> teamStrings = new ArrayList<String>();

            for (Team team: allTeams){
                teamStrings.add(team.toString());
            }

            mentorView.outputTable(teamStrings);
        } catch (SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void deleteQuest(){
        try {
            ArrayList<Quest> allQuests = questDAO.readAll();
            String chosenQuestName = mentorView.getInput("Enter name of a quest you wish to delete: ");

            for (Quest quest: allQuests){
                if (chosenQuestName.equals(quest.getName())){
                    questDAO.delete(quest);
                }
            }
        } catch (SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void deleteArtifact(){

        try {
            ArrayList<Artifact> allArtifacts = artifactDAO.readAll();
            String chosenArtifactName = mentorView.getInput("Enter name of an artifact you wish to delete: ");

            for (Artifact artifact: allArtifacts){
                if (chosenArtifactName.equals(artifact.getName())){
                    artifactDAO.delete(artifact);
                }
            }
        } catch (SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void deleteTeam() {

        try {
            ArrayList<Team> allTeams = teamDAO.readAll();
            String chosenTeamName = mentorView.getInput("Enter name of a team you wish to delete: ");

            for (Team team: allTeams){
                if (chosenTeamName.equals(team.getName())){
                    teamDAO.delete(team);
                }
            }
        } catch (SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void markItem(){

        try {
            ArrayList<Codecooler> allCodecoolers = codecoolerDAO.readAll();
            String chosenCodecoolerEmail = mentorView.getRegExInput(mentorView.emailRegEx, "Enter email of a codecooler:");
            Codecooler chosenCodecooler = null;

            for (Codecooler codecooler: allCodecoolers){
                if (chosenCodecoolerEmail.equals(codecooler.getEmail())){
                    chosenCodecooler = codecooler;
                }
            }
            ArrayList<Item> codecoolerItems = itemDAO.getUserItems(chosenCodecooler.getWallet().getID());
            mentorView.displayUserItems(codecoolerItems);

            String chosenItemIndex = mentorView.getInput("Enter index of item: ");
            Item chosenItem = codecoolerItems.get(Integer.valueOf(chosenItemIndex));

            if (chosenItem.getMark()){
                mentorView.output("Item is already marked.");
            } else {
                chosenItem.setMark();
            }
        } catch (SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

    }

    public void displayWallet(){

        try {
            ArrayList<Codecooler> allCodecoolers = codecoolerDAO.readAll();
            String chosenCodecoolerEmail = mentorView.getRegExInput(mentorView.emailRegEx, "Enter email of a codecooler:");
            Codecooler chosenCodecooler = null;

            for (Codecooler codecooler: allCodecoolers){
                if (chosenCodecoolerEmail.equals(codecooler.getEmail())){
                    chosenCodecooler = codecooler;
                }
            }

            mentorView.output(chosenCodecooler.getWallet().toString());
        } catch (SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void createAchievement() {

        try {
            ArrayList<Codecooler> allCodecoolers = codecoolerDAO.readAll();
            String chosenCodecoolerEmail = mentorView.getRegExInput(mentorView.emailRegEx, "Enter email of a codecooler:");
            Codecooler chosenCodecooler = null;

            for (Codecooler codecooler : allCodecoolers) {
                if (chosenCodecoolerEmail.equals(codecooler.getEmail())) {
                    chosenCodecooler = codecooler;
                }
            }

            ArrayList<Quest> allQuests = questDAO.readAll();
            String chosenQuestName = mentorView.getInput("Enter name of a quest:");
            Quest chosenQuest = null;

            for (Quest quest : allQuests) {
                if (chosenQuestName.equals(quest.getName())) {
                    chosenQuest = quest;
                }
            }

            Achievement newAchievement = new Achievement(chosenQuest, chosenCodecooler);
            achievementDAO.create(newAchievement);
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
}