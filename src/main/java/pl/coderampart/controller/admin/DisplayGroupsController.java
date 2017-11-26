package pl.coderampart.controller.admin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.CodecoolerDAO;
import pl.coderampart.controller.helpers.AccessValidator;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Codecooler;
import pl.coderampart.model.Group;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class DisplayGroupsController extends AccessValidator implements HttpHandler {

    private Connection connection;
    private CodecoolerDAO codecoolerDAO;
    private HelperController helper;

    public DisplayGroupsController(Connection connection) {
        this.connection = connection;
        this.codecoolerDAO = new CodecoolerDAO(connection);
        this.helper = new HelperController(connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        validateAccess( "Admin", httpExchange, connection);
        List<Group> allGroups = helper.readGroupsFromDB();
        String response = "";

        response += helper.renderHeader(httpExchange, connection);
        response += helper.render("admin/adminMenu");
        response += renderDisplayGroups(allGroups);
        response += helper.render("footer");

        helper.sendResponse( response, httpExchange );
    }

    private String renderDisplayGroups(List<Group> allGroups) {
        String templatePath = "templates/admin/displayGroups.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        try {
            List<List<Codecooler>> groupedCodecoolers = groupCodecoolers( allGroups );
            model.with("groupedCodecoolers", groupedCodecoolers);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return template.render(model);
    }

    private List<List<Codecooler>> groupCodecoolers (List<Group> allGroups) throws SQLException {
        List<List<Codecooler>> groupedCodecoolers = new ArrayList<>();

        for (Group group : allGroups) {
            List<Codecooler> groupMembers = codecoolerDAO.getByGroupID( group.getID() );
            groupedCodecoolers.add(groupMembers);
        }
        return groupedCodecoolers;
    }
}
