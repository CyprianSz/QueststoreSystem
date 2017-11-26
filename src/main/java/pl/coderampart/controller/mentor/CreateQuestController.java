package pl.coderampart.controller.mentor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import pl.coderampart.DAO.QuestDAO;
import pl.coderampart.controller.helpers.AccessValidator;
import pl.coderampart.controller.helpers.FlashNoteHelper;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Quest;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class CreateQuestController extends AccessValidator implements HttpHandler {

    private Connection connection;
    private HelperController helper;
    private FlashNoteHelper flashNoteHelper;
    private QuestDAO questDAO;

    public CreateQuestController(Connection connection) {
        this.connection = connection;
        this.questDAO = new QuestDAO( connection );
        this.helper = new HelperController(connection);
        this.flashNoteHelper = new FlashNoteHelper();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        validateAccess( "Mentor", httpExchange, connection);
        String response = "";
        String method = httpExchange.getRequestMethod();

        if (method.equals( "GET" )) {
            response += helper.renderHeader( httpExchange, connection );
            response += helper.render( "mentor/mentorMenu" );
            response += helper.render( "mentor/createQuest" );
            response += helper.render( "footer" );

            helper.sendResponse( response, httpExchange );
        }

        if (method.equals( "POST" )) {
            Map<String, String> inputs = helper.getInputsMap( httpExchange );
            createQuest( inputs, httpExchange );
            helper.redirectTo( "/quest/create", httpExchange );
        }
    }

    private void createQuest(Map<String, String> inputs, HttpExchange httpExchange) {
        String name = inputs.get("name");
        String description = inputs.get("description");
        Integer reward = Integer.valueOf(inputs.get("reward"));

        try {
            Quest newQuest = new Quest( name, description, reward );
            questDAO.create(newQuest);

            String flashNote = flashNoteHelper.createCreationFlashNote( "Quest", name );
            flashNoteHelper.addSuccessFlashNoteToCookie(flashNote, httpExchange);
        } catch (SQLException e) {
            flashNoteHelper.addFailureFlashNoteToCookie(httpExchange);
            e.printStackTrace();
        }
    }
}