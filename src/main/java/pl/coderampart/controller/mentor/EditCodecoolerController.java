package pl.coderampart.controller.mentor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.CodecoolerDAO;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Codecooler;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class EditCodecoolerController implements HttpHandler {

    private Connection connection;
    private CodecoolerDAO codecoolerDAO;
    private HelperController helper;

    public EditCodecoolerController(Connection connection) {
        this.connection = connection;
        this.codecoolerDAO = new CodecoolerDAO(connection);
        this.helper = new HelperController(connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        List<Codecooler> allCodecoolers = helper.readCodecoolersFromDB();
        String codecoolerID = helper.getIdFromURI( httpExchange );
        Codecooler codecooler = helper.getCodecoolerById( codecoolerID);

        if(method.equals("GET")) {
            String response = "";
            response += helper.renderHeader(httpExchange, connection);
            response += helper.render("mentor/mentorMenu");
            response += renderProperBodyResponse(codecoolerID, allCodecoolers);
            response += helper.render("footer");

            helper.sendResponse( response, httpExchange );
        }

        if(method.equals("POST")) {
            Map inputs = helper.getInputsMap(httpExchange);
            editCodecooler(inputs, codecooler);
            helper.redirectTo( "/codecooler/edit", httpExchange );
        }
    }

    private String renderProperBodyResponse(String codecoolerID, List<Codecooler> allCodecoolers) {
        Integer idLength = 36;
        if(codecoolerID.length() == idLength) {
            Codecooler codecoolerToEdit = helper.getCodecoolerById(codecoolerID);
            return renderEditCodecooler(codecoolerToEdit, allCodecoolers);
        } else {
            return renderCodecoolerEmptyForm(allCodecoolers);
        }
    }

    private String renderEditCodecooler(Codecooler codecooler, List<Codecooler> allCodecoolers) {
        String templatePath = "templates/mentor/editCodecooler.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        model.with("allCodecoolers", allCodecoolers);
        model.with("firstName", codecooler.getFirstName());
        model.with("lastName", codecooler.getLastName());
        model.with("email", codecooler.getEmail());
        model.with("dateOfBirth", codecooler.getDateOfBirth());

        return template.render(model);
    }

    private String renderCodecoolerEmptyForm(List<Codecooler> allCodecoolers) {
        String templatePath = "templates/mentor/editCodecooler.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        model.with("allCodecoolers", allCodecoolers);

        return template.render(model);
    }

    private void editCodecooler(Map<String, String> inputs, Codecooler codecooler) {
        String firstName = inputs.get("first-name");
        String lastName  = inputs.get("last-name");
        String email = inputs.get("email");
        String birthdate = inputs.get("birthdate");
        LocalDate dateOfBirthObject = LocalDate.parse( birthdate );

        try {
            codecooler.setFirstName( firstName );
            codecooler.setLastName( lastName );
            codecooler.setEmail( email );
            codecooler.setDateOfBirth( dateOfBirthObject );

            codecoolerDAO.update( codecooler );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
