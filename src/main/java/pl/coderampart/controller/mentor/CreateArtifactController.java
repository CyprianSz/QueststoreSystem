package pl.coderampart.controller.mentor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import pl.coderampart.DAO.ArtifactDAO;
import pl.coderampart.controller.helpers.FlashNoteHelper;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Artifact;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class CreateArtifactController implements HttpHandler {

    private Connection connection;
    private HelperController helper;
    private FlashNoteHelper flashNoteHelper;
    private ArtifactDAO artifactDAO;

    public CreateArtifactController(Connection connection) {
        this.connection = connection;
        this.artifactDAO = new ArtifactDAO( connection );
        this.helper = new HelperController(connection);
        this.flashNoteHelper = new FlashNoteHelper();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "";
        String method = httpExchange.getRequestMethod();

        if (method.equals( "GET" )) {
            response += helper.renderHeader( httpExchange, connection );
            response += helper.render( "mentor/mentorMenu" );
            response += helper.render( "mentor/createArtifact" );
            response += helper.render( "footer" );

            helper.sendResponse( response, httpExchange );
        }

        if (method.equals( "POST" )) {
            Map<String, String> inputs = helper.getInputsMap( httpExchange );
            createArtifact( inputs, httpExchange );
            helper.redirectTo( "/artifact/create", httpExchange );
        }
    }

    private void createArtifact(Map<String, String> inputs, HttpExchange httpExchange) {
        String name = inputs.get("name");
        String description = inputs.get("description");
        String type = inputs.get("type");
        Integer value = Integer.valueOf(inputs.get("value"));

        try {
            Artifact newArtifact = new Artifact( name, description, type, value );
            artifactDAO.create(newArtifact);

            String flashNote = flashNoteHelper.createCreationFlashNote( "Artifact", name );
            flashNoteHelper.addSuccessFlashNoteToCookie(flashNote, httpExchange);
        } catch (SQLException e) {
            flashNoteHelper.addFailureFlashNoteToCookie(httpExchange);
            e.printStackTrace();
        }
    }
}
