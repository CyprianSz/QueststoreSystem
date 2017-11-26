package pl.coderampart.controller.admin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import pl.coderampart.DAO.LevelDAO;
import pl.coderampart.controller.helpers.AccessValidator;
import pl.coderampart.controller.helpers.FlashNoteHelper;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Level;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CreateLevelController extends AccessValidator implements HttpHandler{

    private Connection connection;
    private LevelDAO levelDAO;
    private HelperController helper;
    private FlashNoteHelper flashNoteHelper;

    public CreateLevelController(Connection connection) {
        this.connection = connection;
        this.levelDAO = new LevelDAO( connection );
        this.helper = new HelperController(connection);
        this.flashNoteHelper = new FlashNoteHelper();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        validateAccess( "Admin", httpExchange, connection);
        String method = httpExchange.getRequestMethod();
        String response = "";

        if (method.equals("GET")) {
            response += helper.renderHeader(httpExchange, connection);
            response += helper.render("admin/adminMenu");
            response += helper.render("admin/createLevel");
            response += helper.render("footer");

            helper.sendResponse( response, httpExchange );
        }

        if (method.equals("POST")) {
            Map<String, String> inputs = helper.getInputsMap(httpExchange);
            createLevel(inputs, httpExchange);
            helper.redirectTo( "/level/create", httpExchange );
        }
    }

    private void createLevel(Map<String, String> inputs, HttpExchange httpExchange) {
        Integer rank = Integer.valueOf(inputs.get("rank"));
        Integer requiredExperience = Integer.valueOf(inputs.get("required-experience"));
        String description = inputs.get("description");
        Level newLevel = new Level(rank, requiredExperience, description);

        try {
            levelDAO.create(newLevel);

            String flashNote = flashNoteHelper.createCreationFlashNote( "Level",
                                                                        inputs.get("rank") );
            flashNoteHelper.addSuccessFlashNoteToCookie(flashNote, httpExchange);
        } catch (SQLException e) {
            flashNoteHelper.addFailureFlashNoteToCookie(httpExchange);
            e.printStackTrace();
        }
    }
}
