package pl.coderampart.controller.admin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.LevelDAO;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Level;

import java.io.*;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EditLevelController implements HttpHandler{

    private Connection connection;
    private LevelDAO levelDAO;
    private HelperController helperController;

    public EditLevelController(Connection connection) {
        this.connection = connection;
        this.levelDAO = new LevelDAO(this.connection);
        this.helperController = new HelperController();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String response = "";

        String[] uri = httpExchange.getRequestURI().toString().split("=");
        String id = uri[uri.length-1];

        List<Level> allLevels = readLevelsFromDB();

        if(method.equals("GET")) {
            response += helperController.renderHeader(httpExchange);
            response += render("admin/adminMenu");
            String responseTemp = renderLevelsList(allLevels);
            if(id.length()==36) {
                responseTemp = renderEditLevel(getLevelById(id, allLevels), allLevels);
            }
            response += responseTemp;
            response += render("footer");
        }

        if(method.equals("POST")){
            InputStreamReader isr = new InputStreamReader( httpExchange.getRequestBody(), "utf-8" );
            BufferedReader br = new BufferedReader(isr);
            String formData = br.readLine();

            Map inputs = parseFormData(formData);

            editLevel(inputs, allLevels, id);

            response += render("header");
            response += render("admin/adminMenu");
            String responseTemp = renderLevelsList(allLevels);
            if(id.length()==36) {

                responseTemp = renderEditLevel(getLevelById(id, allLevels), allLevels);
            }
            response +=responseTemp;
            response += render("footer");
        }

        httpExchange.sendResponseHeaders( 200, response.getBytes().length );
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());

        os.close();
    }

    private void editLevel(Map inputs, List<Level>allLevels, String id) {
        Integer rank = Integer.valueOf(inputs.get("rank").toString());
        Integer requiredExperience = Integer.valueOf(inputs.get("required-experience").toString());
        String description = String.valueOf(inputs.get("description"));

        Level changedLevel = getLevelById(id, allLevels);

        if (!changedLevel.equals(null)) {
            changedLevel.setRank(rank);
            changedLevel.setRequiredExperience(requiredExperience);
            changedLevel.setDescription(description);

            try {
                levelDAO.update(changedLevel);
            } catch (SQLException se){
                se.printStackTrace();
            }
        }
    }

    private Level getLevelById(String id, List<Level> allLevels){
        Level changedLevel = null;

        for (Level level: allLevels){
            if(id.equals(level.getID())){
                changedLevel = level;
            }
        }
        return changedLevel;
    }

    private List<Level> readLevelsFromDB(){
        List<Level> allLevels = null;

        try {
            allLevels = levelDAO.readAll();
        } catch (SQLException se) {
            se.printStackTrace();
        }

        return allLevels;
    }

    private String render(String fileName) {
        String templatePath = "templates/" + fileName + ".twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        return template.render(model);
    }

    private String renderLevelsList(List<Level> allLevels) {
        String templatePath = "templates/admin/editLevel.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();
        model.with("allLevels", allLevels);

        return template.render(model);
    }

    private String renderEditLevel(Level level, List<Level> allLevels) {
        String templatePath = "templates/admin/editLevel.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("allLevels", allLevels);
        model.with("rank", level.getRank());
        model.with("requiredExperience", level.getRequiredExperience());
        model.with("description", level.getDescription());

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
