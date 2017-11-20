package pl.coderampart.controller.admin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.ConnectionToDB;
import pl.coderampart.DAO.GroupDAO;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Group;

import java.io.*;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CreateGroupController implements HttpHandler {

    private HelperController helperController = new HelperController();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "";
        String method = httpExchange.getRequestMethod();

        if (method.equals("GET")) {
            response += helperController.renderHeader(httpExchange);
            response += helperController.render("admin/adminMenu");
            JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/admin/createGroup.twig");
            JtwigModel model = JtwigModel.newModel();
            response += template.render(model);
            response += helperController.render("footer");
        }

        if (method.equals("POST")) {
            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String formData = br.readLine();

            Map inputs = helperController.parseFormData(formData);

            String[] data = new String[]{String.valueOf(inputs.get("group-name"))};

            try {
                createGroup(data);
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        String[] pairs = formData.split("&");
        for(String pair : pairs){
            String[] keyValue = pair.split("=");
            String value = new URLDecoder().decode(keyValue[1], "UTF-8");
            map.put(keyValue[0], value);
        }
        return map;
    }

    public void createGroup(String[] groupData) throws SQLException {
        ConnectionToDB connectionToDB = ConnectionToDB.getInstance();
        Connection connection = connectionToDB.connectToDataBase();
        GroupDAO groupDAO = new GroupDAO(connection);

        Group newGroup = new Group(groupData[0]);

        groupDAO.create(newGroup);
    }
}
