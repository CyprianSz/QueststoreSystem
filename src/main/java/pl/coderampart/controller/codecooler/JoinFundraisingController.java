package pl.coderampart.controller.codecooler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.FundraisersDAO;
import pl.coderampart.controller.helpers.AccessValidator;
import pl.coderampart.controller.helpers.FlashNoteHelper;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Codecooler;
import pl.coderampart.model.Fundraising;
import pl.coderampart.model.Item;
import pl.coderampart.model.Session;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class JoinFundraisingController extends AccessValidator implements HttpHandler {

    private Connection connection;
    private HelperController helper;
    private FundraisersDAO fundraisersDAO;
    private FlashNoteHelper flashNoteHelper;


    public JoinFundraisingController(Connection connection) {
        this.connection = connection;
        this.helper = new HelperController(connection);
        this.fundraisersDAO = new FundraisersDAO(connection);
        this.flashNoteHelper = new FlashNoteHelper();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        validateAccess( "Codecooler", httpExchange, connection);
        String method = httpExchange.getRequestMethod();
        List<Fundraising> fundraisingsList = helper.readFundraisingsFromDB();

        if (method.equals("GET")) {
            String response = "";
            response += helper.renderHeader(httpExchange, connection);
            response += helper.render("codecooler/codecoolerMenu");
            response += renderProperBodyResponse(fundraisingsList, httpExchange);
            response += helper.render("footer");

            helper.sendResponse(response, httpExchange);
        }

        if (method.equals("POST")) {
            joinToFundraising(httpExchange, connection);
            helper.redirectTo( "/fundraising/join", httpExchange );
        }
    }

    private void joinToFundraising(HttpExchange httpExchange, Connection connection) {
        Session currentSession = helper.getCurrentSession(httpExchange, connection);
        String userID = currentSession.getUserID();
        String fundraisingID = helper.getIdFromURI( httpExchange );

        try {
            fundraisersDAO.create(userID, fundraisingID);
            String flashNote = "JOIN SUCCESSFULL";
            flashNoteHelper.addSuccessFlashNoteToCookie(flashNote, httpExchange);
        } catch (SQLException e) {
            flashNoteHelper.addFailureFlashNoteToCookie(httpExchange);
            e.printStackTrace();
        }
    }

    private String renderProperBodyResponse(List<Fundraising> fundraisingsList,
                                            HttpExchange httpExchange) {
        Integer idLength = 36;
        String fundraisingID = helper.getIdFromURI( httpExchange );

        if (fundraisingID.length() == idLength) {
            Fundraising fundraising = helper.getFundraisingByID(fundraisingID);
            List<Codecooler> fundraisersList = helper.getFundraisersList(fundraisingID);
            return renderDisplayExactFundraising(fundraising, fundraisersList);
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

    private String renderDisplayExactFundraising(Fundraising fundraising,
                                                 List<Codecooler> fundraisersList) {
        String templatePath = "templates/codecooler/displayFundraising.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("fundraising", fundraising);
        model.with("fundraisersList", fundraisersList);

        return template.render(model);
    }
}
