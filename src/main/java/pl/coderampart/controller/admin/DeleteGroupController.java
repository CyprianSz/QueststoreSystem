package pl.coderampart.controller.admin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.GroupDAO;
import pl.coderampart.controller.helpers.AccessValidator;
import pl.coderampart.controller.helpers.FlashNoteHelper;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Group;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DeleteGroupController extends AccessValidator implements HttpHandler{

    private Connection connection;
    private GroupDAO groupDAO;
    private HelperController helper;
    private FlashNoteHelper flashNoteHelper;


    public DeleteGroupController(Connection connection) {
        this.connection = connection;
        this.groupDAO = new GroupDAO(this.connection);
        this.helper = new HelperController(connection);
        this.flashNoteHelper = new FlashNoteHelper();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        validateAccess( "Admin", httpExchange, connection);
        String method = httpExchange.getRequestMethod();
        List<Group> allGroups = helper.readGroupsFromDB();
        String groupID = helper.getIdFromURI(httpExchange);
        Group group = helper.getGroupById(groupID);

        if (method.equals("GET")) {
            String response = "";
            response += helper.renderHeader( httpExchange, connection );
            response += helper.render( "admin/adminMenu" );
            response += renderProperBodyResponse( groupID, allGroups );
            response += helper.render( "footer" );

            helper.sendResponse( response, httpExchange );
        }

        if(method.equals("POST")){
            Map inputs = helper.getInputsMap(httpExchange);

            if(inputs.get("confirmation").equals("yes")){
                deleteGroup(group, httpExchange);
            }
            helper.redirectTo( "/group/delete", httpExchange );
        }
    }

    private String renderProperBodyResponse(String groupID, List<Group> allGroups) {
        Integer idLength = 36;
        if(groupID.length() == idLength) {
            Group groupToDelete = helper.getGroupById(groupID);
            return renderConfirmation(groupToDelete, allGroups);
        } else {
            return renderGroupsList(allGroups);
        }
    }

    private String renderConfirmation(Group group, List<Group> allGroups) {
        String templatePath = "templates/admin/deleteChosenGroup.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        model.with("allGroups", allGroups);
        model.with("name", group.getName());

        return template.render( model );
    }

    public String renderGroupsList(List<Group> allGroups) {
        String templatePath = "templates/admin/deleteGroupStartPage.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        model.with("allGroups", allGroups);

        return template.render(model);
    }

    private void deleteGroup(Group group, HttpExchange httpExchange) {
        try {
            groupDAO.delete( group );

            String name = group.getName();
            String flashNote = flashNoteHelper.createDeletionFlashNote( "Group", name);
            flashNoteHelper.addSuccessFlashNoteToCookie(flashNote, httpExchange);
        } catch (SQLException e) {
            flashNoteHelper.addFailureFlashNoteToCookie(httpExchange);
            e.printStackTrace();
        }
    }
}
