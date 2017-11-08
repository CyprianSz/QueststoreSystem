package pl.coderampart.controller.admin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.MentorDAO;
import pl.coderampart.model.Mentor;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DisplayMentorsController implements HttpHandler{

    private Connection connection;
    private MentorDAO mentorDAO;

    public DisplayMentorsController(Connection connection) {
        this.connection = connection;
        this.mentorDAO = new MentorDAO(this.connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        List<Mentor> allMentors = readMentorsFromDB();
        String method = httpExchange.getRequestMethod();
        String response = "";
        response += render("header");
        response += render("admin/adminMenu");
        response += renderDisplayMentors(allMentors);
        response += render("footer");
        httpExchange.sendResponseHeaders( 200, response.getBytes().length );
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

    private List<Mentor> readMentorsFromDB() {
        List<Mentor> allMentors = null;

        try {
            allMentors = mentorDAO.readAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allMentors;
    }

    private String renderDisplayMentors(List<Mentor> allMentors) {
        String templatePath = "templates/admin/displayMentors.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        model.with("allMentors", allMentors);

        return template.render(model);
    }
}
