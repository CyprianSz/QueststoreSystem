package pl.coderampart.controller.codecooler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.CodecoolerDAO;
import pl.coderampart.controller.helpers.AccessValidator;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Codecooler;
import pl.coderampart.model.Level;
import pl.coderampart.model.Session;

import java.io.IOException;
import java.sql.Connection;

public class DisplayUserLevelController extends AccessValidator implements HttpHandler {

    private Connection connection;
    private HelperController helper;
    private CodecoolerDAO codecoolerDAO;

    public DisplayUserLevelController(Connection connection) {
        this.connection = connection;
        this.helper = new HelperController(connection);
        this.codecoolerDAO = new CodecoolerDAO(connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        validateAccess( "Codecooler", httpExchange, connection);
        Level userLevel = helper.readUserLevelFromDB(httpExchange, connection);
        String response = "";
        Session currentSession = helper.getCurrentSession(httpExchange, connection);
        String userID = currentSession.getUserID();
        Codecooler codecooler = helper.getCodecoolerByID(userID);

        response += helper.renderHeader(httpExchange, connection);
        response += helper.render("codecooler/codecoolerMenu");
        response += renderUserLevel(userLevel, codecooler);
        response += helper.render("footer");

        helper.sendResponse(response, httpExchange);
    }

    private String renderUserLevel(Level userLevel, Codecooler codecooler) {
        String templatePath = "templates/codecooler/codecoolerLevel.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("userLevel", userLevel);
        model.with("wallet", codecooler.getWallet());

        return template.render(model);
    }
}