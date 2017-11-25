package pl.coderampart.controller.admin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.MentorDAO;
import pl.coderampart.controller.helpers.FlashNoteHelper;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Mentor;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class DeleteMentorController implements HttpHandler {

    private Connection connection;
    private MentorDAO mentorDAO;
    private HelperController helper;
    private FlashNoteHelper flashNoteHelper;


    public DeleteMentorController(Connection connection) {
        this.connection = connection;
        this.mentorDAO = new MentorDAO(this.connection);
        this.helper = new HelperController(connection);
        this.flashNoteHelper = new FlashNoteHelper();
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
            Map inputs = helper.getInputsMap(httpExchange);

            if(inputs.get("confirmation").equals("yes")) {
                deleteMentor(mentor, httpExchange);
            }
            helper.redirectTo( "/mentor/delete", httpExchange );
        }
    }

    private String renderProperBodyResponse(String mentorID, List<Mentor> allMentors) {
        Integer idLength = 36;
        if(mentorID.length() == idLength) {
            Mentor mentorToDelete = helper.getMentorById(mentorID);
            return renderConfirmation(mentorToDelete, allMentors);
        } else {
            return renderMentorsList(allMentors);
        }
    }

    private String renderConfirmation(Mentor mentor, List<Mentor> allMentors) {
        String templatePath = "templates/admin/deleteChosenMentor.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("allMentors", allMentors);
        model.with("firstName", mentor.getFirstName());
        model.with("lastName", mentor.getLastName());

        return template.render(model);
    }

    public String renderMentorsList(List<Mentor> allMentors) {
        String templatePath = "templates/admin/deleteMentorStartPage.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        model.with("allMentors", allMentors);

        return template.render(model);
    }

    private void deleteMentor(Mentor mentor, HttpExchange httpExchange) {
        try {
            mentorDAO.delete( mentor );

            String deletedMentorFullName = mentor.getFirstName() + mentor.getLastName();
            String flashNote = flashNoteHelper.createDeletionFlashNote( "Mentor",
                                                                        deletedMentorFullName);
            flashNoteHelper.addSuccessFlashNoteToCookie(flashNote, httpExchange);
        } catch (SQLException e) {
            flashNoteHelper.addFailureFlashNoteToCookie(httpExchange);
            e.printStackTrace();
        }
    }
}
