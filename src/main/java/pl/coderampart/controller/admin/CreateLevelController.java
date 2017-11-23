package pl.coderampart.controller.admin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.LevelDAO;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.controller.helpers.Validator;
import pl.coderampart.model.Level;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CreateLevelController implements HttpHandler{

    private Connection connection;
    private LevelDAO levelDAO;
    private HelperController helper;
    private Validator validator;

    private static Map<String, String> inputs = new HashMap<>();

    public CreateLevelController(Connection connection) {
        this.connection = connection;
        this.levelDAO = new LevelDAO( connection );
        this.helper = new HelperController(connection);
        this.validator = new Validator(connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String response = "";

        if (method.equals("GET")) {
            response += helper.renderHeader(httpExchange, connection);
            response += helper.render("admin/adminMenu");
            response += renderCreateLevel(inputs);
            response += helper.render("footer");

            helper.sendResponse( response, httpExchange );
        }

        if (method.equals("POST")) {
            inputs = helper.getInputsMap(httpExchange);
            createLevel(inputs);
            helper.redirectTo( "/level/create", httpExchange );
        }
    }

    private void createLevel(Map<String, String> inputs) {
        Integer rank = Integer.valueOf(inputs.get("rank"));
        Integer requiredExperience = Integer.valueOf(inputs.get("required-experience"));
        String description = inputs.get("description");
        Level newLevel = new Level(rank, requiredExperience, description);

        try {
            levelDAO.create(newLevel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String renderCreateLevel(Map<String, String> inputs) throws IOException {
        if (inputs.isEmpty()) {
            return helper.render("admin/createLevel");
        }
        return checkIfCreateLevel(inputs);
    }


    private String checkIfCreateLevel(Map<String, String> inputs) throws IOException {
        if (this.validator.validateData(inputs) == true) {
            createLevel(inputs);
            return helper.render("admin/createLevel");
        }
        return renderCreateWithMessages(inputs);
    }

    private String renderCreateWithMessages(Map<String, String> inputs) throws IOException {
        String templatePath = "templates/admin/templatesWithValidations/createLevelWithExceptions.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        model.with("rank", this.validator.checkIfIsDigit(inputs.get("rank")));
        model.with("requiredExperience", this.validator.checkIfIsDigit(inputs.get("required-experience")));
        model.with("description", inputs.get("description"));
        return template.render(model);
    }
}
