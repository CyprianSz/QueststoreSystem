package pl.coderampart.controller.admin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.LevelDAO;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Level;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DisplayLevelsController implements HttpHandler{

    private Connection connection;
    private LevelDAO levelDAO;
    private HelperController helperController;

    public DisplayLevelsController(Connection connection) {
        this.connection = connection;
        this.levelDAO = new LevelDAO(this.connection);
        this.helperController = new HelperController();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        List<Level> allLevels = readLevelsFromDB();
        String response = "";
        response += helperController.renderHeader(httpExchange);
        response += helperController.render("admin/adminMenu");
        response += renderDisplayLevels(allLevels);
        response += helperController.render("footer");
        httpExchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private List<Level> readLevelsFromDB(){
        List<Level> allLevels = null;

        try {
            allLevels = levelDAO.readAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allLevels;
    }

    private String renderDisplayLevels(List<Level> allLevels) {
        String templatePath = "templates/admin/displayLevels.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("allLevels", allLevels);

        return template.render(model);
    }
}
