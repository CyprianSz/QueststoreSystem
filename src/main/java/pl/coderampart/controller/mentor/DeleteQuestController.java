//package pl.coderampart.controller.mentor;
//
//import com.sun.net.httpserver.HttpExchange;
//import com.sun.net.httpserver.HttpHandler;
//import org.jtwig.JtwigModel;
//import org.jtwig.JtwigTemplate;
//import pl.coderampart.DAO.QuestDAO;
//import pl.coderampart.DAO.TeamDAO;
//import pl.coderampart.controller.helpers.HelperController;
//import pl.coderampart.model.Quest;
//import pl.coderampart.model.Team;
//
//import java.io.*;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.List;
//import java.util.Map;
//
//public class DeleteQuestController  implements HttpHandler {
//
//    private Connection connection;
//    private QuestDAO questDAO;
//    private HelperController helper;
//
//    public DeleteQuestController(Connection connection) {
//        this.connection = connection;
//        this.questDAO = new QuestDAO(connection);
//        this.helper = new HelperController(connection);
//    }
//
//    @Override
//    public void handle(HttpExchange httpExchange) throws IOException {
//        String method = httpExchange.getRequestMethod();
//        List<Quest> allQuests = helper.readQuestsFromDB();
//        String questID = helper.getIdFromURI( httpExchange );
//        Quest quest = helper.getQuestById(questID);
//
//        if (method.equals("GET")) {
//            String response = "";
//            response += helper.renderHeader(httpExchange, connection);
//            response += helper.render("mentor/mentorMenu");
//            response += renderProperBodyResponse(questID, allQuests);
//            response += helper.render("footer");
//
//            helper.sendResponse(response, httpExchange);
//        }
//
//        if(method.equals("POST")){
//            Map inputs = helper.getInputsMap(httpExchange);
//
//            if(inputs.get("confirmation").equals("yes")){
//                deleteQuest(quest);
//            }
//            helper.redirectTo( "/quest/delete", httpExchange );
//        }
//    }
//
//    private String renderProperBodyResponse(String questID, List<Quest> allQuests) {
//        Integer idLength = 36;
//        if(teamID.length() == idLength) {
//            Team teamToDelete = helper.getTeamById(teamID);
//            return renderConfirmation(teamToDelete, allTeams);
//        } else {
//            return renderTeamsList(allTeams);
//        }
//    }
//
//}
