package pl.coderampart.controller.mentor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.CodecoolerDAO;
import pl.coderampart.DAO.TeamDAO;
import pl.coderampart.controller.helpers.FlashNoteHelper;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Codecooler;
import pl.coderampart.model.Team;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public class DeleteCodecoolerController implements HttpHandler {

    private Connection connection;
    private CodecoolerDAO codecoolerDAO;
    private HelperController helper;
    private FlashNoteHelper flashNoteHelper;

    public DeleteCodecoolerController (Connection connection) {
        this.connection = connection;
        this.codecoolerDAO = new CodecoolerDAO(this.connection);
        this.helper = new HelperController(connection);
        this.flashNoteHelper = new FlashNoteHelper();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        List<Codecooler> allCodecoolers = helper.readCodecoolersFromDB();
        String codecoolerID  = helper.getIdFromURI( httpExchange );
        Codecooler codecooler = helper.getCodecoolerByID(codecoolerID);

        if (method.equals("GET")) {
            String response = "";
            response += helper.renderHeader(httpExchange, connection);
            response += helper.render("mentor/mentorMenu");
            response += renderProperBodyResponse(codecoolerID, allCodecoolers);
            response += helper.render("footer");

            helper.sendResponse(response, httpExchange);
        }

        if(method.equals("POST")){
            Map inputs = helper.getInputsMap(httpExchange);

            if(inputs.get("confirmation").equals("yes")){
                deleteCodecooler(codecooler, httpExchange);
            }
            helper.redirectTo( "/codecooler/delete", httpExchange );
        }
    }

    private String renderProperBodyResponse(String codecoolerID, List<Codecooler> allCodecoolers) {
        Integer idLength = 36;
        if(codecoolerID.length() == idLength) {
            Codecooler codecoolerToDelete = helper.getCodecoolerByID(codecoolerID);
            return renderConfirmation(codecoolerToDelete, allCodecoolers);
        } else {
            return renderTeamsList(allCodecoolers);
        }
    }

    private String renderConfirmation(Codecooler codecooler, List<Codecooler> allCodecoolers) {
        String templatePath = "templates/mentor/deleteChosenCodecooler.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        model.with("allCodecoolers", allCodecoolers);
        model.with("firstName", codecooler.getFirstName());
        model.with("lastName", codecooler.getLastName());

        return template.render( model );
    }

    private String renderTeamsList(List<Codecooler> allCodecoolers) {
        String templatePath = "templates/mentor/deleteCodecoolerStartPage.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("allCodecoolers", allCodecoolers);

        return template.render(model);
    }

    private void deleteCodecooler(Codecooler codecooler, HttpExchange httpExchange) {
        try {
            codecoolerDAO.delete( codecooler );

            String name = codecooler.getFirstName() + " " + codecooler.getLastName();
            String flashNote = flashNoteHelper.createDeletionFlashNote( "Codecooler", name);
            flashNoteHelper.addSuccessFlashNoteToCookie(flashNote, httpExchange);
        } catch (SQLException e) {
            flashNoteHelper.addFailureFlashNoteToCookie(httpExchange);
            e.printStackTrace();
        }
    }
}
