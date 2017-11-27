package pl.coderampart.controller.mentor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.ArtifactDAO;
import pl.coderampart.controller.helpers.AccessValidator;
import pl.coderampart.controller.helpers.FlashNoteHelper;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Artifact;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DeleteArtifactController extends AccessValidator implements HttpHandler {

    private Connection connection;
    private ArtifactDAO artifactDAO;
    private HelperController helper;
    private FlashNoteHelper flashNoteHelper;

    public DeleteArtifactController(Connection connection) {
        this.connection = connection;
        this.artifactDAO = new ArtifactDAO(connection);
        this.helper = new HelperController(connection);
        this.flashNoteHelper = new FlashNoteHelper();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        validateAccess( "Mentor", httpExchange, connection);
        String method = httpExchange.getRequestMethod();
        List<Artifact> allArtifacts = helper.readArtifactsFromDB();
        String artifactID = helper.getIdFromURI( httpExchange );
        Artifact artifact = helper.getArtifactById(artifactID);

        if (method.equals("GET")) {
            String response = "";
            response += helper.renderHeader(httpExchange, connection);
            response += helper.render("mentor/mentorMenu");
            response += renderProperBodyResponse(artifactID, allArtifacts);
            response += helper.render("footer");

            helper.sendResponse(response, httpExchange);
        }

        if(method.equals("POST")){
            Map inputs = helper.getInputsMap(httpExchange);

            if(inputs.get("confirmation").equals("yes")){
                deleteArtifact(artifact, httpExchange);
            }
            helper.redirectTo( "/artifact/delete", httpExchange );
        }
    }

    private String renderProperBodyResponse(String artifactID, List<Artifact> allArtifacts) {
        Integer idLength = 36;
        if(artifactID.length() == idLength) {
            Artifact artifactToDelete = helper.getArtifactById(artifactID);
            return renderConfirmation(artifactToDelete, allArtifacts);
        } else {
            return renderTeamsList(allArtifacts);
        }
    }

    private String renderConfirmation(Artifact artifact, List<Artifact> allArtifacts) {
        String templatePath = "templates/mentor/deleteChosenArtifact.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        model.with("allArtifacts", allArtifacts);
        model.with("name", artifact.getName());

        return template.render( model );
    }

    private String renderTeamsList(List<Artifact> allArtifacts) {
        String templatePath = "templates/mentor/deleteArtifactStartPage.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("allArtifacts", allArtifacts);

        return template.render(model);
    }

    private void deleteArtifact(Artifact artifact, HttpExchange httpExchange) {
        try {
            artifactDAO.delete( artifact );

            String name = artifact.getName();
            String flashNote = flashNoteHelper.createDeletionFlashNote( "Artifact", name );
            flashNoteHelper.addSuccessFlashNoteToCookie(flashNote, httpExchange);
        } catch (SQLException e) {
            flashNoteHelper.addFailureFlashNoteToCookie(httpExchange);
            e.printStackTrace();
        }
    }
}
