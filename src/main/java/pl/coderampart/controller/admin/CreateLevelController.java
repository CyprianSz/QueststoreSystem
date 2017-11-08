package pl.coderampart.controller.admin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.ConnectionToDB;
import pl.coderampart.DAO.GroupDAO;
import pl.coderampart.DAO.LevelDAO;
import pl.coderampart.DAO.MentorDAO;
import pl.coderampart.model.Group;
import pl.coderampart.model.Level;
import pl.coderampart.model.Mentor;

import java.io.*;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CreateLevelController implements HttpHandler{
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "";
        String method = httpExchange.getRequestMethod();

        if(method.equals("GET")) {
            response += render("header");
            response += render("admin/adminMenu");
            JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/admin/createLevel.twig");
            JtwigModel model = JtwigModel.newModel();
            response += template.render(model);
            response += render("footer");
        }

        if(method.equals("POST")) {
            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String formData = br.readLine();

            Map inputs = parseFormData(formData);

            String[] data = new String[]{String.valueOf(inputs.get("rank")),
                                        String.valueOf(inputs.get("required-experience")),
                                        String.valueOf(inputs.get("description"))};

            try{
                createLevel(data);
            } catch (SQLException se){
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

    public void createLevel(String[] levelData) throws SQLException {
        ConnectionToDB connectionToDB = ConnectionToDB.getInstance();
        Connection connection = connectionToDB.connectToDataBase();
        LevelDAO levelDAO = new LevelDAO(connection);



        Level newLevel = new Level(Integer.valueOf(levelData[0]), Integer.valueOf(levelData[1]), levelData[2]);

        levelDAO.create(newLevel);
    }


}
