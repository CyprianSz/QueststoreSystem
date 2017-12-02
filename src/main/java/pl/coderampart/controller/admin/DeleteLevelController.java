package pl.coderampart.controller.admin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.LevelDAO;
import pl.coderampart.controller.helpers.AccessValidator;
import pl.coderampart.controller.helpers.FlashNoteHelper;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Level;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DeleteLevelController extends AccessValidator implements HttpHandler {

    private Connection connection;
    private LevelDAO levelDAO;
    private HelperController helper;
    private FlashNoteHelper flashNoteHelper;

    public DeleteLevelController(Connection connection) {
        this.connection = connection;
        this.levelDAO = new LevelDAO(this.connection);
        this.helper = new HelperController(connection);
        this.flashNoteHelper = new FlashNoteHelper();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        validateAccess( "Admin", httpExchange, connection);
        String method = httpExchange.getRequestMethod();
        List<Level> allLevels = helper.readLevelsFromDB();
        String levelID = helper.getIdFromURI(httpExchange);
        Level level = helper.getLevelById(levelID);

        if (method.equals( "GET" )) {
            String response = "";
            response += helper.renderHeader( httpExchange, connection );
            response += helper.render("admin/adminMenu");
            response += renderProperBodyResponse(levelID, allLevels);
            response += helper.render("footer");

            helper.sendResponse( response, httpExchange);
        }

        if (method.equals("POST")) {
            Map inputs = helper.getInputsMap(httpExchange);

            if(inputs.get("confirmation").equals("yes")) {
                deleteLevel(level, httpExchange);
            }
            helper.redirectTo( "/level/delete", httpExchange );
        }
    }

    private String renderProperBodyResponse(String levelID, List<Level> allLevels) {
        Integer idLength = 36;
        if(levelID.length() == idLength) {
            Level levelToDelete = helper.getLevelById(levelID);
            return renderConfirmation(levelToDelete, allLevels);
        } else {
            return renderLevelsList(allLevels);
        }
    }

    private String renderConfirmation(Level level, List<Level> allLevels) {
        String templatePath = "templates/admin/deleteChosenLevel.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        model.with("allLevels", allLevels);
        model.with("rank", level.getRank());

        return template.render(model);
    }

    public String renderLevelsList(List<Level> allLevels) {
        String templatePath = "templates/admin/deleteLevelStartPage.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        model.with("allLevels", allLevels);

        return template.render(model);
    }

    private void deleteLevel(Level level, HttpExchange httpExchange) {
        try {
            levelDAO.delete( level );

            String rank = level.getRank().toString();
            String flashNote = flashNoteHelper.createDeletionFlashNote( "Level", rank);
            flashNoteHelper.addSuccessFlashNoteToCookie(flashNote, httpExchange);
        } catch (SQLException e) {
            flashNoteHelper.addFailureFlashNoteToCookie(httpExchange);
            e.printStackTrace();
        }
    }
}
