package pl.coderampart.controller.admin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.LevelDAO;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Level;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class EditLevelController implements HttpHandler{

    private Connection connection;
    private LevelDAO levelDAO;
    private HelperController helper;

    public EditLevelController(Connection connection) {
        this.connection = connection;
        this.levelDAO = new LevelDAO(this.connection);
        this.helper = new HelperController(connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        List<Level> allLevels = helper.readLevelsFromDB();
        String levelID = helper.getIdFromURI( httpExchange );
        Level level = helper.getLevelById( levelID );

        if(method.equals("GET")) {
            String response = "";
            response += helper.renderHeader(httpExchange);
            response += helper.render("admin/adminMenu");
            response += renderProperBodyResponse(levelID, allLevels);
            response += helper.render("footer");

            helper.sendResponse( response, httpExchange );
        }

        if(method.equals("POST")){
            Map<String, String> inputs = helper.getInputsMap(httpExchange);
            editLevel(inputs, level);
            helper.redirectTo( "/level/edit", httpExchange );
        }
    }

    private String renderProperBodyResponse(String levelID, List<Level> allLevels) {
        Integer idLength = 36;
        if (levelID.length() == idLength) {
            Level levelToEdit = helper.getLevelById( levelID );
            return renderEditLevel(levelToEdit, allLevels);
        } else {
            return helper.renderLevelsList(allLevels);
        }
    }

    private String renderEditLevel(Level level, List<Level> allLevels) {
        String templatePath = "templates/admin/editLevel.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("allLevels", allLevels);
        model.with("rank", level.getRank());
        model.with("requiredExperience", level.getRequiredExperience());
        model.with("description", level.getDescription());

        return template.render(model);
    }

    private void editLevel(Map<String, String> inputs, Level level) {
        Integer rank = Integer.valueOf(inputs.get("rank"));
        Integer requiredExperience = Integer.valueOf(inputs.get("required-experience"));
        String description = inputs.get("description");

        try {
            level.setRank(rank);
            level.setRequiredExperience( requiredExperience );
            level.setDescription( description );

            levelDAO.update(level);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
