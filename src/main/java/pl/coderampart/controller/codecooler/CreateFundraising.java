package pl.coderampart.controller.codecooler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.ArtifactDAO;
import pl.coderampart.DAO.CodecoolerDAO;
import pl.coderampart.DAO.FundraisingDAO;
import pl.coderampart.controller.helpers.AccessValidator;
import pl.coderampart.controller.helpers.FlashNoteHelper;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Artifact;
import pl.coderampart.model.Codecooler;
import pl.coderampart.model.Fundraising;
import pl.coderampart.model.Session;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class CreateFundraising extends AccessValidator implements HttpHandler {

    private Connection connection;
    private FundraisingDAO fundraisingDAO;
    private ArtifactDAO artifactDAO;
    private CodecoolerDAO codecoolerDAO;
    private HelperController helper;
    private FlashNoteHelper flashNoteHelper;

    public CreateFundraising(Connection connection) {
        this.connection = connection;
        this.fundraisingDAO = new FundraisingDAO(connection);
        this.artifactDAO = new ArtifactDAO( connection );
        this.codecoolerDAO = new CodecoolerDAO( connection );
        this.helper = new HelperController(connection);
        this.flashNoteHelper = new FlashNoteHelper();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        validateAccess( "Codecooler", httpExchange, connection);
        String response = "";
        String method = httpExchange.getRequestMethod();

        if (method.equals("GET")) {
            response += helper.renderHeader(httpExchange, connection);
            response += helper.render("codecooler/codecoolerMenu");
            response += renderCreateFundraising("codecooler/createFundraising");
            response += helper.render("footer");

            helper.sendResponse(response, httpExchange);
        }

        if (method.equals("POST")) {
            Map<String, String> inputs = helper.getInputsMap(httpExchange);
            createFundraising(inputs, httpExchange);
            helper.redirectTo( "/fundraising/create", httpExchange );
        }
    }

    private String renderCreateFundraising(String fileName) {
        List<Artifact> allArtifacts = helper.readArtifactsFromDB();

        String templatePath = "templates/" + fileName + ".twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        model.with("allArtifacts", allArtifacts);

        return template.render(model);
    }

    private void createFundraising(Map<String, String> inputs, HttpExchange httpExchange) {
        String fundraisingName = inputs.get("fundraising-name");
        String artifactName = inputs.get("artifact-name");

        Session currentSession = helper.getCurrentSession(httpExchange, connection);
        String userID = currentSession.getUserID();

        try {
            Codecooler loggedUser = codecoolerDAO.getByID(userID);
            Artifact choosenArtifact = artifactDAO.getByName(artifactName);
            Fundraising newFundraising = new Fundraising( choosenArtifact, fundraisingName, loggedUser);
            fundraisingDAO.create( newFundraising );

            String flashNote = flashNoteHelper.createCreationFlashNote( "Fundraising",
                                                                        fundraisingName);
            flashNoteHelper.addSuccessFlashNoteToCookie(flashNote, httpExchange);
        } catch (SQLException e) {
            flashNoteHelper.addFailureFlashNoteToCookie(httpExchange);
            e.printStackTrace();
        }
    }
}
