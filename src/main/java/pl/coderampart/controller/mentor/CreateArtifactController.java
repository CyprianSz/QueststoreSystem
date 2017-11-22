package pl.coderampart.controller.mentor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import pl.coderampart.DAO.ArtifactDAO;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Artifact;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class CreateArtifactController implements HttpHandler {
    private Connection connection;
    private HelperController helper;
    private ArtifactDAO artifactDAO;

    public CreateArtifactController(Connection connection) {
        this.connection = connection;
        this.artifactDAO = new ArtifactDAO( connection );
        this.helper = new HelperController(connection);
    }


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "";
        String method = httpExchange.getRequestMethod();

        if (method.equals("GET")) {
            response += helper.renderHeader(httpExchange, connection);
            response += helper.render("mentor/mentorMenu");
            response += helper.render("mentor/createArtifact");
            response += helper.render("footer");

            helper.sendResponse(response, httpExchange);
        }

        if (method.equals("POST")) {
            Map<String, String> inputs = helper.getInputsMap(httpExchange);
            createArtifact(inputs);
            helper.redirectTo( "/team/create", httpExchange );
        }

        public void createArtifact(Map<String, String> inputs) {
            String name = inputs.get("name");
            String description = inputs.get("description");
            String type = inputs.get("type");
            Integer value = Integer.valueOf(inputs.get("value"));

            try {
                Artifact newArtifact = new Artifact( name, description, type, value )
                artifactDAO.create(newArtifact);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
