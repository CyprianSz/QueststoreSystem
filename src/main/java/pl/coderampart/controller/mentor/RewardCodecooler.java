package pl.coderampart.controller.mentor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.*;
import pl.coderampart.controller.helpers.AccessValidator;
import pl.coderampart.controller.helpers.FlashNoteHelper;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class RewardCodecooler extends AccessValidator implements HttpHandler {

    private Connection connection;
    private CodecoolerDAO codecoolerDAO;
    private QuestDAO questDAO;
    private AchievementDAO achievementDAO;
    private WalletDAO walletDAO;
    private LevelDAO levelDAO;
    private HelperController helper;
    private FlashNoteHelper flashNoteHelper;

    public RewardCodecooler(Connection connection) {
        this.connection = connection;
        this.codecoolerDAO = new CodecoolerDAO(connection);
        this.questDAO = new QuestDAO( connection );
        this.achievementDAO = new AchievementDAO(connection);
        this.walletDAO = new WalletDAO( connection );
        this.levelDAO = new LevelDAO( connection );
        this.helper = new HelperController( connection );
        this.flashNoteHelper = new FlashNoteHelper();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        validateAccess("Mentor", httpExchange, connection);
        String method = httpExchange.getRequestMethod();
        List<Quest> allQuests = helper.readQuestsFromDB();
        List<Codecooler> allCodecoolers = helper.readCodecoolersFromDB();

        if (method.equals("GET")) {
            String response = "";
            response += helper.renderHeader( httpExchange, connection );
            response += helper.render( "mentor/mentorMenu" );
            response += renderRewardQuest( allQuests, allCodecoolers );
            response += helper.render( "footer" );

            helper.sendResponse( response, httpExchange );
        }

        if (method.equals("POST")) {
            Map<String, String> inputs = helper.getInputsMap(httpExchange);
            String questName = inputs.get("quest-name");
            String codecoolerName = inputs.get("codecooler-name");

            rewardCodecooler(questName, codecoolerName, httpExchange);

            helper.redirectTo( "/codecooler/reward", httpExchange );
        }
    }

    private String renderRewardQuest(List<Quest> allQuests, List<Codecooler> allCodecoolers) {
        String templatePath = "templates/mentor/rewardCodecooler.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        model.with("allQuests", allQuests);
        model.with("allCodecoolers", allCodecoolers);

        return template.render( model );
    }

    private void rewardCodecooler(String questName, String rewardedName, HttpExchange httpExchange) {
        String[] splitedCodecoolersFullname = rewardedName.split(" ");
        String codecoolerFirstName = splitedCodecoolersFullname[0];
        String codecoolerLastName = splitedCodecoolersFullname[1];

        try {
            Quest quest = questDAO.getByName(questName);
            Codecooler codecooler = codecoolerDAO.getByName(codecoolerFirstName, codecoolerLastName);
            Integer reward = quest.getReward();

            createNewAchievement(codecooler, quest);
            addCoolcoinsToCodecoolerAccount(codecooler, reward);

            String flashNote = rewardedName + " rewarded with: " + reward + " coolcoins";
            flashNoteHelper.addSuccessFlashNoteToCookie(flashNote, httpExchange);
        } catch (SQLException e) {
            flashNoteHelper.addFailureFlashNoteToCookie(httpExchange);
            e.printStackTrace();
        }
    }

    private void createNewAchievement(Codecooler codecooler, Quest quest) throws SQLException {
        Achievement newAchievement = new Achievement( quest, codecooler );
        achievementDAO.create(newAchievement);
    }

    private void addCoolcoinsToCodecoolerAccount(Codecooler codecooler, Integer reward) throws SQLException {
        Wallet codecoolerWallet = codecooler.getWallet();
        Integer actualCodecoolerBalance = codecooler.getWallet().getBalance();
        Integer actualEarnedCoins = codecooler.getWallet().getEarnedCoins();
        Integer updatedCodecoolerBalance = actualCodecoolerBalance + reward;
        Integer updatedEarnedCoins = actualEarnedCoins + reward;
        codecoolerWallet.setBalance( updatedCodecoolerBalance );
        codecoolerWallet.setEarnedCoins( updatedEarnedCoins );
        updateCodecoolerLevel( codecooler );

        walletDAO.update(codecoolerWallet);
    }

    private void updateCodecoolerLevel(Codecooler codecooler) throws SQLException {
        Wallet codecoolerWallet = codecooler.getWallet();
        Integer codecoolerEarnedCoins = codecoolerWallet.getEarnedCoins();
        Level updatedLevel = checkLevelByEarnedCoins(codecoolerEarnedCoins);

        if (updatedLevel != codecooler.getLevel()){
            codecooler.setLevel(updatedLevel);
            codecoolerDAO.update(codecooler);
        }
    }

    private Level checkLevelByEarnedCoins(Integer earnedCoins) throws SQLException {
        List<Level> allLevels = levelDAO.readAll();
        Level currentLevel = null;
        Level lowerLevel = null;
        Level higherLevel = null;

        for (int i = 0; i < allLevels.size(); i++){
            if (i < allLevels.size() - 1){
                lowerLevel = allLevels.get(i);
                higherLevel = allLevels.get(i + 1);
                if (earnedCoins < higherLevel.getRequiredExperience() && earnedCoins > lowerLevel.getRequiredExperience()){
                    currentLevel = lowerLevel;
                    break;
                }
            } else if (i == allLevels.size() - 1) {
                if (earnedCoins > allLevels.get(i).getRequiredExperience()){
                    currentLevel = allLevels.get(i);
                }
            }
        }

        return currentLevel;
    }
}
