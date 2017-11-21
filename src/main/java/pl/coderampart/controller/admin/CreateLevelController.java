package pl.coderampart.controller.admin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import pl.coderampart.DAO.LevelDAO;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Level;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class CreateLevelController implements HttpHandler{

    private Connection connection;
    private LevelDAO levelDAO;
    private HelperController helper;

    public CreateLevelController(Connection connection) {
        this.connection = connection;
        this.levelDAO = new LevelDAO( connection );
        this.helper = new HelperController();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String response = "";

        if (method.equals("GET")) {
            response += helper.renderHeader(httpExchange);
            response += helper.render("admin/adminMenu");
            response += helper.render("admin/createLevel");
            response += helper.render("footer");

            helper.sendResponse( response, httpExchange );
        }

        if (method.equals("POST")) {
            Map<String, String> inputs = helper.getInputsMap(httpExchange);

            createLevel(inputs);
            helper.redirectTo( "/level/create", httpExchange );
        }
    }

    private void createLevel(Map<String, String> inputs) {
        Integer rank = Integer.valueOf(inputs.get("rank"));
        Integer requiredExperience = Integer.valueOf(inputs.get("required-experience"));
        String desctiption = inputs.get("description");
        Level newLevel = new Level(rank, requiredExperience, desctiption);

        try {
            levelDAO.create(newLevel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
