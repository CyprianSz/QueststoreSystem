package pl.coderampart.controller.mentor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import pl.coderampart.DAO.QuestDAO;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Quest;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class CreateQuestController implements HttpHandler {

    private Connection connection;
    private HelperController helper;
    private QuestDAO questDAO;

    public CreateQuestController(Connection connection) {
        this.connection = connection;
        this.questDAO = new QuestDAO( connection );
        this.helper = new HelperController(connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
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
            createQuest( inputs );
            helper.redirectTo( "/quest/create", httpExchange );
        }
    }

    private void createQuest(Map<String, String> inputs) {
        String name = inputs.get("name");
        String description = inputs.get("description");
        Integer reward = Integer.valueOf(inputs.get("reward"));

        try {
            Quest newQuest = new Quest( name, description, reward );
            questDAO.create(newQuest);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
