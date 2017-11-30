package pl.coderampart.controller.codecooler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.controller.helpers.AccessValidator;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Codecooler;
import pl.coderampart.model.Fundraising;
import pl.coderampart.model.Item;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class JoinFundraising extends AccessValidator implements HttpHandler {

    private Connection connection;
    private HelperController helper;

    public JoinFundraising(Connection connection) {
        this.connection = connection;
        this.helper = new HelperController(connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        validateAccess( "Codecooler", httpExchange, connection);
        String method = httpExchange.getRequestMethod();
        List<Fundraising> fundraisingsList = helper.readFundraisingsFromDB();
        String fundraisingID = helper.getIdFromURI( httpExchange );
        Fundraising fundraising = helper.getFundraisingByID(fundraisingID);

        if (method.equals("GET")) {
            String response = "";
            response += helper.renderHeader(httpExchange, connection);
            response += helper.render("codecooler/codecoolerMenu");
            response += renderProperBodyResponse(fundraisingID, fundraisingsList);
            response += helper.render("footer");

            helper.sendResponse(response, httpExchange);
        }

        if(method.equals("POST")) {
//            zapisanie do fundraisngu
//            Map<String, String> inputs = helper.getInputsMap(httpExchange);
//            editCodecooler(inputs, codecooler, httpExchange);
            helper.redirectTo( "/fundraising/join", httpExchange );
        }
    }

    private String renderProperBodyResponse(String fundraisingID, List<Fundraising> fundraisingsList) {
        Integer idLength = 36;
        if(fundraisingID.length() == idLength) {
            Fundraising fundraising = helper.getFundraisingByID(fundraisingID);
            return renderDisplayExactFundraising(fundraising); //i dodac wszystkich funraiserow do listy
        } else {
            return renderDisplayFundraisings(fundraisingsList);
        }
    }

    private String renderDisplayFundraisings(List<Fundraising> fundraisingsList) {
        String templatePath = "templates/codecooler/joinFundraising.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("fundraisingsList", fundraisingsList);

        return template.render(model);
    }

    private String renderDisplayExactFundraising(Fundraising fundraising) {
        String templatePath = "templates/codecooler/displayFundraising.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("fundraising", fundraising);

        return template.render(model);
    }
}
