package pl.coderampart.controller.codecooler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.CodecoolerDAO;
import pl.coderampart.DAO.ItemDAO;
import pl.coderampart.controller.helpers.AccessValidator;
import pl.coderampart.controller.helpers.FlashNoteHelper;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class BuyArtifactController extends AccessValidator implements HttpHandler {

    private Connection connection;
    private HelperController helper;
    private FlashNoteHelper flashNoteHelper;
    private ItemDAO itemDAO;
    private CodecoolerDAO codecoolerDAO;

    public BuyArtifactController(Connection connection) {
        this.connection = connection;
        this.helper = new HelperController(connection);
        this.flashNoteHelper = new FlashNoteHelper();
        this.itemDAO = new ItemDAO(connection);
        this.codecoolerDAO = new CodecoolerDAO(connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        validateAccess("Codecooler", httpExchange, connection);
        String method = httpExchange.getRequestMethod();
        List<Artifact> allArtifacts = helper.readArtifactsFromDB();
        String artifactID = helper.getIdFromURI( httpExchange );
        Artifact artifact = helper.getArtifactById(artifactID);

        if (method.equals("GET")) {
            String response = "";
            response += helper.renderHeader( httpExchange, connection );
            response += helper.render("codecooler/codecoolerMenu");
            response += renderProperBodyResponse( artifactID, allArtifacts );
            response += helper.render( "footer" );

            helper.sendResponse( response, httpExchange );
        }

        if (method.equals("POST")) {
            System.out.println("dupa");
            Map inputs = helper.getInputsMap(httpExchange);

            if (inputs.get("confirmation").equals("yes")) {
                buyArtifact(artifact, httpExchange);
            }
            helper.redirectTo( "/artifact/buy", httpExchange );
        }
    }

    private String renderProperBodyResponse(String artifactID, List<Artifact> allArtifacts) {
        Integer idLength = 36;
        if(artifactID.length() == idLength) {
            Artifact artifactToBuy = helper.getArtifactById(artifactID);
            return renderConfirmation(artifactToBuy);
        } else {
            return renderDisplayArtifacts(allArtifacts);
        }
    }

    private String renderConfirmation(Artifact artifact) {
        String templatePath = "templates/codecooler/buyChosenArtifact.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        model.with("name", artifact.getName());

        return template.render( model );
    }

    private String renderDisplayArtifacts(List<Artifact> allArtifacts) {
        String templatePath = "templates/codecooler/buyArtifact.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("allArtifacts", allArtifacts);

        return template.render(model);
    }

    private void buyArtifact(Artifact artifact, HttpExchange httpExchange) {
        Session currentSession = helper.getCurrentSession(httpExchange, connection);
        String userID = currentSession.getUserID();

        try {
            Codecooler codecooler = codecoolerDAO.getByID(userID);
            Item item = new Item(artifact, codecooler.getWallet());
            itemDAO.create(item);

            String flashNote = artifact.getName() + " BOUGHT";
            flashNoteHelper.addSuccessFlashNoteToCookie(flashNote, httpExchange);
        } catch (SQLException e) {
            flashNoteHelper.addFailureFlashNoteToCookie(httpExchange);
            e.printStackTrace();
        }
    }
}