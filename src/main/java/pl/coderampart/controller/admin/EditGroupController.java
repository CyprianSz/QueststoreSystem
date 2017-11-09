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

public class EditGroupController implements HttpHandler{

    private Connection connection;
    private GroupDAO groupDAO;

    public EditGroupController(Connection connection){
        this.connection = connection;
        this.groupDAO = new GroupDAO(this.connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String response = "";

        String[] uri = httpExchange.getRequestURI().toString().split("=");
        String id = uri[uri.length-1];

        List<Group> allGroups = readGroupsFromDB();

        if(method.equals("GET")) {
            response += render("header");
            response += render("admin/adminMenu");
            response += renderEditGroup(allGroups);
            response += render("footer");
        }

        if(method.equals("POST")){
            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String formData = br.readLine();

            Map inputs = parseFormData(formData);

            editGroup(inputs, allGroups, id);
        }

        httpExchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void editGroup(Map inputs, List<Group> allGroups, String id){
        String name = String.valueOf(inputs.get("group-name"));

        Group changedGroup = null;
        for (Group group: allGroups) {
            if (id.equals(group.getID())) {
                changedGroup = group;
                changedGroup.setName(name);

                try{
                    groupDAO.update(changedGroup);
                } catch (SQLException se){
                    se.printStackTrace();
                }
                break;
            }
        }
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

    private String renderEditGroup(List<Group> allGroups){
        String templatePath = "templates/admin/editGroup.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
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
}
