package pl.coderampart.controller.mentor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.ArtifactDAO;
import pl.coderampart.controller.helpers.FlashNoteHelper;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Artifact;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class EditArtifactController implements HttpHandler {

    private Connection connection;
    private ArtifactDAO artifactDAO;
    private HelperController helper;
    private FlashNoteHelper flashNoteHelper;

    public EditArtifactController(Connection connection) {
        this.connection = connection;
        this.artifactDAO = new ArtifactDAO(connection);
        this.helper = new HelperController(connection);
        this.flashNoteHelper = new FlashNoteHelper();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        List<Artifact> allArtifacts = helper.readArtifactsFromDB();
        String artifactID = helper.getIdFromURI( httpExchange );
        Artifact artifact = helper.getArtifactById( artifactID );

        if(method.equals("GET")) {
            String response = "";
            response += helper.renderHeader(httpExchange, connection);
            response += helper.render("mentor/mentorMenu");
            response += renderProperBodyResponse(artifactID, allArtifacts);
            response += helper.render("footer");

            helper.sendResponse( response, httpExchange );
        }

        if(method.equals("POST")) {
            Map inputs = helper.getInputsMap(httpExchange);
            editArtifact(inputs, artifact, httpExchange);
            helper.redirectTo( "/artifact/edit", httpExchange );
        }
    }

    private String renderProperBodyResponse(String artifactID, List<Artifact> allArtifacts) {
        Integer idLength = 36;
        if(artifactID.length() == idLength) {
            Artifact artifactToEdit = helper.getArtifactById(artifactID);
            return renderEditArtifact(artifactToEdit, allArtifacts);
        } else {
            return renderArtifactEmptyForm(allArtifacts);
        }
    }

    private String renderEditArtifact(Artifact artifact, List<Artifact> allArtifacts) {
        String templatePath = "templates/mentor/editArtifact.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        model.with("allArtifacts", allArtifacts);
        model.with("name", artifact.getName());
        model.with("description", artifact.getDescription());
        model.with("value", artifact.getValue());
        initiallyCheckProperType( model, artifact );

        return template.render(model);
    }

    private void initiallyCheckProperType(JtwigModel model, Artifact artifact) {
        if (artifact.getType().equals( "single" )) {
            model.with("checkedSingle", "checked");
        } else {
            model.with("checkedGroup", "checked");
        }
    }

    private String renderArtifactEmptyForm(List<Artifact> allArtifacts) {
        String templatePath = "templates/mentor/editArtifact.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        model.with("allArtifacts", allArtifacts);

        return template.render(model);
    }

    private void editArtifact(Map<String, String> inputs, Artifact artifact, HttpExchange httpExchange) {
        String name = inputs.get("name");
        String description = inputs.get("description");
        Integer value = Integer.valueOf(inputs.get("value"));
        String type = inputs.get("type");

        try {
            artifact.setName(name);
            artifact.setDescription( description );
            artifact.setValue( value );
            artifact.setType( type );
            artifactDAO.update( artifact );

            String flashNote = flashNoteHelper.createEditionFlashNote( "Artifact", name );
            flashNoteHelper.addSuccessFlashNoteToCookie(flashNote, httpExchange);
        } catch (SQLException e) {
            flashNoteHelper.addFailureFlashNoteToCookie(httpExchange);
            e.printStackTrace();
        }
    }
}
