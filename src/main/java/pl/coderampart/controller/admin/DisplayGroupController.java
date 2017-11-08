package pl.coderampart.controller.admin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.GroupDAO;
import pl.coderampart.model.Group;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.List;

public class DisplayGroupController implements HttpHandler{

    private Connection connection;
    private GroupDAO groupDAO;

    public DisplayGroupController(Connection connection) {
        this.connection = connection;
        this.groupDAO = new GroupDAO(this.connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        List<Group> allGroups = readGroupsFromDB();
        String method = httpExchange.getRequestMethod();
        String response = "";
        response += render("header");
        response += render("admin/adminMenu");
        response += renderDisplayGroups(allGroups);
        response += render("footer");
        httpExchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private String render(String fileName) {
        String templatePath = "templates/" + fileName + ".twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        return template.render(model);
    }
}
