package pl.coderampart.controller.admin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.MentorDAO;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Mentor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DisplayMentorsController implements HttpHandler{

    private Connection connection;
    private MentorDAO mentorDAO;
    private HelperController helper;

    public DisplayMentorsController(Connection connection) {
        this.connection = connection;
        this.mentorDAO = new MentorDAO(this.connection);
        this.helper = new HelperController(connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        List<Mentor> allMentors = readMentorsFromDB();
        String response = "";

        response += helper.renderHeader(httpExchange);
        response += helper.render("admin/adminMenu");
        response += renderDisplayMentors(allMentors);
        response += helper.render("footer");

        helper.sendResponse( response, httpExchange );
    }

    private List<Mentor> readMentorsFromDB() {
        try {
            return mentorDAO.readAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String renderDisplayMentors(List<Mentor> allMentors) {
        String templatePath = "templates/admin/displayMentors.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        model.with("allMentors", allMentors);

        return template.render(model);
    }
}
