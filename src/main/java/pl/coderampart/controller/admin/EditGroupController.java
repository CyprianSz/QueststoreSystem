package pl.coderampart.controller.admin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.GroupDAO;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Group;

import java.io.*;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditGroupController implements HttpHandler{

    private Connection connection;
    private GroupDAO groupDAO;
    private HelperController helperController;

    public EditGroupController(Connection connection){
        this.connection = connection;
        this.groupDAO = new GroupDAO(this.connection);
        this.helperController = new HelperController();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String response = "";

        String[] uri = httpExchange.getRequestURI().toString().split("=");
        String id = uri[uri.length-1];

        List<Group> allGroups = readGroupsFromDB();

        if(method.equals("GET")) {
            response += helperController.renderHeader(httpExchange);
            response += helperController.render("admin/adminMenu");
            String responseTemp = renderGroupsList(allGroups);
            if(id.length()==36){
                responseTemp = renderEditGroup(getGroupById(id, allGroups), allGroups);
            }
            response += responseTemp;
            response += helperController.render("footer");
        }

        if(method.equals("POST")) {
            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String formData = br.readLine();

            Map inputs = helperController.parseFormData(formData);

            editGroup(inputs, allGroups, id);

            response += helperController.render("header");
            response += helperController.render("admin/adminMenu");
            String responseTemp = renderGroupsList(allGroups);
            if (id.length() == 36) {
                responseTemp = renderEditGroup(getGroupById(id, allGroups), allGroups);
            }
            response += responseTemp;
            response += helperController.render("footer");
        }

        httpExchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void editGroup(Map inputs, List<Group>allGroups, String id){
        String name = String.valueOf(inputs.get("group-name"));

        Group changedGroup = getGroupById(id, allGroups);

        if (!changedGroup.equals(null)) {
            changedGroup.setName(name);
            try{
                groupDAO.update(changedGroup);
            } catch (SQLException se){
                se.printStackTrace();
            }
        }
    }

    private Group getGroupById(String id, List<Group> allGroups){
        Group changedGroup = null;

        for (Group group: allGroups) {
            if(id.equals(group.getID())) {
                changedGroup = group;
            }
        }
        return changedGroup;
    }

    private List<Group> readGroupsFromDB() {
        List<Group> allGroups = null;

        try {
            allGroups = groupDAO.readAll();
        } catch (SQLException se) {
            se.printStackTrace();
        }

        return allGroups;
    }

    private String renderGroupsList(List<Group> allGroups) {
        String templatePath = "templates/admin/editGroup.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();
        model.with("allGroups", allGroups);

        return template.render(model);
    }

    private String renderEditGroup(Group group, List<Group> allGroups) {
        String templatePath = "templates/admin/editGroup.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        model.with("allGroups", allGroups);
        model.with("groupName", group.getName());

        return template.render(model);
    }
}
