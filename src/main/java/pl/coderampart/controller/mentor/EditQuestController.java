package pl.coderampart.controller.mentor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.ArtifactDAO;
import pl.coderampart.DAO.QuestDAO;
import pl.coderampart.controller.helpers.FlashNoteHelper;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Quest;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public class EditQuestController implements HttpHandler {

    private Connection connection;
    private QuestDAO questDAO;
    private HelperController helper;
    private FlashNoteHelper flashNoteHelper;


    public EditQuestController(Connection connection) {
        this.connection = connection;
        this.questDAO = new QuestDAO(connection);
        this.helper = new HelperController(connection);
        this.flashNoteHelper = new FlashNoteHelper();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        List<Quest> allQuests= helper.readQuestsFromDB();
        String questID = helper.getIdFromURI( httpExchange );
        Quest quest = helper.getQuestById( questID );


        if(method.equals("GET")) {
            String response = "";
            response += helper.renderHeader(httpExchange, connection);
            response += helper.render("mentor/mentorMenu");
            response += renderProperBodyResponse(questID, allQuests);
            response += helper.render("footer");

            helper.sendResponse( response, httpExchange );
        }

        if(method.equals("POST")) {
            Map inputs = helper.getInputsMap(httpExchange);
            editQuest(inputs, quest, httpExchange);
            helper.redirectTo( "/quest/edit", httpExchange );
        }
    }

    private String renderProperBodyResponse(String questID, List<Quest> allQuests) {
        Integer idLength = 36;
        if(questID.length() == idLength) {
            Quest questToEdit = helper.getQuestById(questID);
            return renderEditQuest(questToEdit, allQuests);
        } else {
            return renderQuestEmptyForm(allQuests);
        }
    }

    private String renderEditQuest(Quest quest, List<Quest> allQuest) {
        String templatePath = "templates/mentor/editQuest.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        model.with("allQuests", allQuest);
        model.with("name", quest.getName());
        model.with("description", quest.getDescription());
        model.with("reward", quest.getReward());

        return template.render(model);
    }

    private String renderQuestEmptyForm(List<Quest> allQuests) {
        String templatePath = "templates/mentor/editQuest.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        model.with("allQuests", allQuests);

        return template.render(model);
    }

    private void editQuest(Map<String, String> inputs, Quest quest, HttpExchange httpExchange) {
        String name = inputs.get("name");
        String description = inputs.get("description");
        Integer reward = Integer.valueOf(inputs.get("reward"));

        try {
            quest.setName( name );
            quest.setDescription( description );
            quest.setReward( reward );
            questDAO.update( quest );

            String flashNote = flashNoteHelper.createEditionFlashNote( "Quest", name );
            flashNoteHelper.addSuccessFlashNoteToCookie(flashNote, httpExchange);
        } catch (SQLException e) {
            flashNoteHelper.addFailureFlashNoteToCookie(httpExchange);
            e.printStackTrace();
        }
    }
}
