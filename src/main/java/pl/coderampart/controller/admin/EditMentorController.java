package pl.coderampart.controller.admin;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.MentorDAO;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Mentor;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class EditMentorController implements HttpHandler {

    private Connection connection;
    private MentorDAO mentorDAO;
    private HelperController helper;

    public EditMentorController(Connection connection) {
        this.connection = connection;
        this.mentorDAO = new MentorDAO(connection);
        this.helper = new HelperController(connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        List<Mentor> allMentors = helper.readMentorsFromDB();
        String mentorID = helper.getIdFromURI(httpExchange);
        Mentor mentor = helper.getMentorById( mentorID );

        if (method.equals("GET")) {
            String response = "";
            response += helper.renderHeader(httpExchange, connection);
            response += helper.render("admin/adminMenu");
            response += renderProperBodyResponse(mentorID, allMentors);
            response += helper.render("footer");

            helper.sendResponse( response, httpExchange );
        }

        if (method.equals("POST")) {
            Map<String, String> inputs = helper.getInputsMap(httpExchange);
            editMentor(inputs, mentor);
            helper.redirectTo( "/mentor/edit", httpExchange );
        }
    }

    private String renderProperBodyResponse(String mentorID, List<Mentor> allMentors) {
        Integer idLength = 36;
        if (mentorID.length() == idLength) {
            Mentor mentorToEdit = helper.getMentorById( mentorID );
            return renderEditMentor(mentorToEdit, allMentors);
        } else {
            return renderMentorEmptyForm(allMentors);
        }
    }

    private String renderEditMentor(Mentor mentor, List<Mentor> allMentors) {
        String templatePath = "templates/admin/editMentor.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        model.with("allMentors", allMentors);
        model.with("firstName", mentor.getFirstName());
        model.with("lastName", mentor.getLastName());
        model.with("email", mentor.getEmail());
        model.with("dateOfBirth", mentor.getDateOfBirth());

        return template.render(model);
    }

    private String renderMentorEmptyForm(List<Mentor> allMentors) {
        String templatePath = "templates/admin/editMentor.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();
        model.with("allMentors", allMentors);

        return template.render(model);
    }

    private void editMentor(Map<String, String> inputs, Mentor mentor) {
        String firstName = inputs.get("first-name");
        String lastName= inputs.get("last-name");
        String dateOfBirth = inputs.get("date-of-birth");
        String email = inputs.get("email");
        LocalDate dateOfBirthObject = LocalDate.parse(dateOfBirth);

        try {
            mentor.setFirstName( firstName );
            mentor.setLastName( lastName );
            mentor.setEmail( email );
            mentor.setDateOfBirth( dateOfBirthObject );

            mentorDAO.update( mentor );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}