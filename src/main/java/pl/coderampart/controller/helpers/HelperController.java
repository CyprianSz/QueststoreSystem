package pl.coderampart.controller.helpers;

import com.sun.net.httpserver.HttpExchange;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.*;
import pl.coderampart.model.*;

import java.io.*;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelperController {

    private Connection connection;
    private MentorDAO mentorDAO;
    private LevelDAO levelDAO;
    private GroupDAO groupDAO;
    private TeamDAO teamDAO;
    private CodecoolerDAO codecoolerDAO;
    private ItemDAO itemDAO;

    public HelperController(Connection connection) {
        this.connection = connection;
        this.mentorDAO = new MentorDAO(connection);
        this.levelDAO = new LevelDAO(connection);
        this.groupDAO = new GroupDAO(connection);
        this.teamDAO = new TeamDAO(connection);
        this.codecoolerDAO = new CodecoolerDAO(connection);
        this.itemDAO = new ItemDAO(connection);
    }

    public Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        String[] pairs = formData.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            String value = URLDecoder.decode(keyValue[1], "UTF-8");
            map.put(keyValue[0], value);
        }
        return map;
    }

    public Map<String, String> createCookiesMap(HttpExchange httpExchange) {
        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");
        Map<String, String> cookiesMap = new HashMap<>();

        if (cookieStr != null) {
            String[] cookiesValues = cookieStr.split("; ");

            for (String cookie : cookiesValues) {
                String[] nameValuePairCookie = cookie.split("=\"");
                String name = nameValuePairCookie[0];
                String value = nameValuePairCookie[1].replace("\"", "");

                cookiesMap.put(name, value);
            }
        }
        return cookiesMap;
    }

    public String renderHeader(HttpExchange httpExchange) {
        Map<String, String> cookiesMap = createCookiesMap(httpExchange);

        String templatePath = "templates/header.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("firstName", cookiesMap.get("firstName"));
        model.with("lastName", cookiesMap.get("lastName"));
        model.with("userType", cookiesMap.get("typeOfUser"));

        return template.render(model);
    }

    public String renderHeader(HttpExchange httpExchange, Connection connection) {
        Session currentSession = getCurrentSession(httpExchange, connection);

        String templatePath = "templates/header.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("firstName", currentSession.getUserFirstName());
        model.with("lastName", currentSession.getUserLastName());
        model.with("userType", currentSession.getUserType());

        return template.render(model);
    }

    public Session getCurrentSession(HttpExchange httpExchange, Connection connection) {
        Map<String, String> cookiesMap = createCookiesMap(httpExchange);
        String sessionID = cookiesMap.get("sessionID");

        try {
            SessionDAO sessionDAO = new SessionDAO(connection);
            return sessionDAO.getByID(sessionID);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String render(String fileName) {
        String templatePath = "templates/" + fileName + ".twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        return template.render(model);
    }

    public void sendResponse(String response, HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public Map<String, String> getInputsMap(HttpExchange httpExchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String formData = br.readLine();

        return parseFormData(formData);
    }

    public String getIdFromURI(HttpExchange httpExchange) {
        String[] uri = httpExchange.getRequestURI().toString().split("=");
        return uri[uri.length - 1];
    }

    public void redirectTo(String path, HttpExchange httpExchange) throws IOException {
        httpExchange.getResponseHeaders().set("Location", path);
        httpExchange.sendResponseHeaders(302, -1);
    }

    public List<Mentor> readMentorsFromDB() {
        try {
            return mentorDAO.readAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Mentor getMentorById(String id) {
        try {
            return mentorDAO.getByID(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Level> readLevelsFromDB() {
        try {
            return levelDAO.readAll();
        } catch (SQLException se) {
            se.printStackTrace();
            return null;
        }
    }

    public Level getLevelById(String id) {
        try {
            return levelDAO.getByID(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Group> readGroupsFromDB() {
        try {
            return groupDAO.readAll();
        } catch (SQLException se) {
            se.printStackTrace();
            return null;
        }
    }

    public Group getGroupById(String id) {
        try {
            return groupDAO.getByID(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Team> readTeamsFromDB() {
        try {
            return teamDAO.readAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Team getTeamById(String id) {
        try {
            return teamDAO.getByID(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Codecooler getCodecoolerByID(String ID) {
        try {
            return codecoolerDAO.getByID(ID);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Item> readUserItemsFromDB(HttpExchange httpExchange, Connection connection) {
        Session currentSession = getCurrentSession(httpExchange, connection);
        String userID = currentSession.getUserID();
        Codecooler loggedCodecooler = getCodecoolerByID(userID);
        String walletID = loggedCodecooler.getWallet().getID();

        try {
            return itemDAO.getUserItems(walletID);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Level readUserLevelFromDB(HttpExchange httpExchange, Connection connection) {
        Session currentSession = getCurrentSession(httpExchange, connection);
        String userID = currentSession.getUserID();
        Codecooler loggedCodecooler = getCodecoolerByID(userID);
        String levelID = loggedCodecooler.getLevel().getID();

        try {
            return levelDAO.getByID(levelID);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}