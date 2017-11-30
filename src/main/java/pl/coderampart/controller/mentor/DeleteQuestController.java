package pl.coderampart.controller.mentor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.QuestDAO;
import pl.coderampart.controller.helpers.AccessValidator;
import pl.coderampart.controller.helpers.FlashNoteHelper;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Quest;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DeleteQuestController extends AccessValidator implements HttpHandler {

    private Connection connection;
    private QuestDAO questDAO;
    private HelperController helper;
    private FlashNoteHelper flashNoteHelper;


    public DeleteQuestController(Connection connection) {
        this.connection = connection;
        this.questDAO = new QuestDAO(connection);
        this.helper = new HelperController(connection);
        this.flashNoteHelper = new FlashNoteHelper();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        validateAccess( "Mentor", httpExchange, connection);
        String method = httpExchange.getRequestMethod();
        List<Quest> allQuests = helper.readQuestsFromDB();
        String questID = helper.getIdFromURI( httpExchange );
        Quest quest = helper.getQuestById(questID);

        if (method.equals("GET")) {
            String response = "";
            response += helper.renderHeader(httpExchange, connection);
            response += helper.render("mentor/mentorMenu");
            response += renderProperBodyResponse(questID, allQuests);
            response += helper.render("footer");

            helper.sendResponse(response, httpExchange);
        }

        if(method.equals("POST")){
            Map inputs = helper.getInputsMap(httpExchange);

            if (inputs.get("confirmation").equals("yes")) {
                deleteQuest(quest, httpExchange);
            }
            helper.redirectTo( "/quest/delete", httpExchange );
        }
    }

    private String renderProperBodyResponse(String questID, List<Quest> allQuests) {
        Integer idLength = 36;
        if(questID.length() == idLength) {
            Quest questToDelete = helper.getQuestById(questID);
            return renderConfirmation(questToDelete, allQuests);
        } else {
            return renderQuestsList(allQuests);
        }
    }

    private String renderConfirmation(Quest quest, List<Quest> allQuests) {
        String templatePath = "templates/mentor/deleteChosenQuest.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        model.with("allQuests", allQuests);
        model.with("name", quest.getName());

        return template.render( model );
    }

    private String renderQuestsList(List<Quest> allQuests) {
        String templatePath = "templates/mentor/deleteQuestStartPage.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("allQuests", allQuests);

        return template.render(model);
    }

    private void deleteQuest(Quest quest, HttpExchange httpExchange) {
        try {
            questDAO.delete( quest );

            String name = quest.getName();
            String flashNote = flashNoteHelper.createDeletionFlashNote( "Quest", name);
            flashNoteHelper.addSuccessFlashNoteToCookie(flashNote, httpExchange);
        } catch (SQLException e) {
            flashNoteHelper.addFailureFlashNoteToCookie(httpExchange);
            e.printStackTrace();
        }
    }
}
