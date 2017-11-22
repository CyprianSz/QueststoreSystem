package pl.coderampart.controller.admin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.LevelDAO;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Level;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DisplayLevelsController implements HttpHandler{

    private Connection connection;
    private LevelDAO levelDAO;
    private HelperController helper;

    public DisplayLevelsController(Connection connection) {
        this.connection = connection;
        this.levelDAO = new LevelDAO(connection);
        this.helper = new HelperController(connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        List<Level> allLevels = readLevelsFromDB();
        String response = "";

        response += helper.renderHeader(httpExchange, connection);
        response += helper.render("admin/adminMenu");
        response += renderDisplayLevels(allLevels);
        response += helper.render("footer");

        helper.sendResponse( response, httpExchange );
    }

    private List<Level> readLevelsFromDB(){
        try {
            return levelDAO.readAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String renderDisplayLevels(List<Level> allLevels) {
        String templatePath = "templates/admin/displayLevels.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("allLevels", allLevels);

        return template.render(model);
    }
}
