package pl.coderampart.controller.admin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.GroupDAO;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Group;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DisplayGroupsController implements HttpHandler {

    private Connection connection;
    private GroupDAO groupDAO;
    private HelperController helper;

    public DisplayGroupsController(Connection connection) {
        this.connection = connection;
        this.groupDAO = new GroupDAO(this.connection);
        this.helper = new HelperController(connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        List<Group> allGroups = readGroupsFromDB();
        String response = "";

        response += helper.renderHeader(httpExchange);
        response += helper.render("admin/adminMenu");
        response += renderDisplayGroups(allGroups);
        response += helper.render("footer");

        helper.sendResponse( response, httpExchange );
    }

    private List<Group> readGroupsFromDB(){
        try {
            return groupDAO.readAll();
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    private String renderDisplayGroups(List<Group> allGroups) {
        String templatePath = "templates/admin/displayGroups.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("allGroups", allGroups);

        return template.render(model);
    }
}
