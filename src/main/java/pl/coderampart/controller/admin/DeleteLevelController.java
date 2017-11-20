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

public class DeleteLevelController implements HttpHandler {

    private Connection connection;
    private LevelDAO levelDAO;
    private HelperController helperController;


    public DeleteLevelController(Connection connection) {
        this.connection = connection;
        this.levelDAO = new LevelDAO(this.connection);
        this.helperController = new HelperController();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String response = "";

        List<Level> allLevels = readLevelsFromDB();

        String[] uri = httpExchange.getRequestURI().toString().split("=%2F");
        String id = uri[uri.length-1];

        response += helperController.render("header");
        response += helperController.render("admin/adminMenu");
        response += renderLevelsList(allLevels);
        response += helperController.render("footer");

        if (method.equals("POST")) {
            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String formData = br.readLine();
            Map inputs = helperController.parseFormData(formData);

            if(inputs.get("confirmation").equals("yes")) {
                deleteLevel(allLevels, id);
            }
        }

        httpExchange.sendResponseHeaders( 200, response.getBytes().length );
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private List<Level> readLevelsFromDB() {
        List<Level> allLevels = null;

        try {
            allLevels = levelDAO.readAll();
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return allLevels;
    }

    private String renderLevelsList(List<Level> allLevels) {
        String templatePath = "templates/admin/deleteLevel.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        model.with("allLevels", allLevels);

        return template.render(model);
    }

    private void deleteLevel(List<Level> allLevels, String id) {
        Level deletedLevel = null;
        for (Level level: allLevels) {
            if (id.equals(level.getID())) {
                deletedLevel = level;

                try {
                    levelDAO.delete(deletedLevel);
                } catch (SQLException se) {
                    se.printStackTrace();
                }
                break;
            }
        }
    }
}
