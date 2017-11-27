package pl.coderampart.controller.codecooler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Fundraising;
import pl.coderampart.model.Item;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class JoinFundraising implements HttpHandler {

    private Connection connection;
    private HelperController helper;

    public JoinFundraising(Connection connection) {
        this.connection = connection;
        this.helper = new HelperController(connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        List<Fundraising> fundraisingsList = helper.readFundraisingsFromDB();
        String response = "";

        response += helper.renderHeader(httpExchange, connection);
        response += helper.render("codecooler/codecoolerMenu");
        response += renderDisplayFundraisings(fundraisingsList);
        response += helper.render("footer");

        helper.sendResponse(response, httpExchange);
    }

    private String renderDisplayFundraisings(List<Fundraising> fundraisingsList) {
        String templatePath = "templates/codecooler/joinFundraising.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("fundraisingsList", fundraisingsList);

        return template.render(model);
    }
}
