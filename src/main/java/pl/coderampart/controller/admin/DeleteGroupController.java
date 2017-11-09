package pl.coderampart.controller.admin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.GroupDAO;
import pl.coderampart.model.Group;

import java.io.*;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeleteGroupController implements HttpHandler{

    private Connection connection;
    private GroupDAO groupDAO;

    private DeleteGroupController(Connection connection) {
        this.connection = connection;
        this.groupDAO = new GroupDAO(this.connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String response = "";

        List<Group> allGroups = readGroupsFromDB();

        String[] uri = httpExchange.getRequestURI().toString().split("=%2F");
        String id = uri[uri.length-1];

        response += render("header");
        response += render("admin/adminMenu");
        response += renderGroupsList(allGroups);
        response += render("footer");

        if(method.equals("POST")){
            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String formData = br.readLine();
            Map inputs = parseFormData(formData);

            if(inputs.get("confirmation").equals("yes")){
                deleteGroup(allGroups, id);
            }
        }

        httpExchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private List<Group> readGroupsFromDB(){
        List<Group> allGroups = null;

        try {
            allGroups = groupDAO.readAll();
        } catch (SQLException se) {
            se.printStackTrace();
        }

        return allGroups;
    }

    private String render(String fileName){
        String templatePath = "templates/" + fileName + ".twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        return template.render(model);
    }

    private String renderGroupsList(List<Group> allGroups) {
        String templatePath = "templates/admin/deleteGroup.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("allGroups", allGroups);

        return template.render(model);
    }

    private static Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        String[] pairs = formData.split("&");
        for(String pair : pairs){
            String[] keyValue = pair.split("=");
            String value = URLDecoder.decode(keyValue[1], "UTF-8");
            map.put(keyValue[0], value);
        }
        return map;
    }

    private void deleteGroup(List<Group> allGroups, String id) {
        Group deletedGroup = null;
        for (Group group: allGroups) {
            if (id.equals(group.getID())) {
                deletedGroup = group;
                try {
                    groupDAO.delete(deletedGroup);
                } catch (SQLException se) {
                    se.printStackTrace();
                }
                break;
            }
        }
    }
}
