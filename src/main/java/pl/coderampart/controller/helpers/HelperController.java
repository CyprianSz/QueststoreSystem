package pl.coderampart.controller.helpers;

import com.sun.net.httpserver.HttpExchange;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.*;
import pl.coderampart.model.*;
import pl.coderampart.services.Loggable;

import java.io.*;
import java.net.HttpCookie;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelperController {

    private Connection connection;
    private MentorDAO mentorDAO;
    private AdminDAO adminDAO;
    private LevelDAO levelDAO;
    private GroupDAO groupDAO;
    private TeamDAO teamDAO;
    private ArtifactDAO artifactDAO;
    private QuestDAO questDAO;
    private CodecoolerDAO codecoolerDAO;
    private ItemDAO itemDAO;

    public HelperController(Connection connection) {
        this.connection = connection;
        this.mentorDAO = new MentorDAO(connection);
        this.adminDAO = new AdminDAO(connection);
        this.levelDAO = new LevelDAO(connection);
        this.groupDAO = new GroupDAO(connection);
        this.teamDAO = new TeamDAO(connection);
        this.artifactDAO = new ArtifactDAO(connection);
        this.questDAO = new QuestDAO(connection);
        this.codecoolerDAO = new CodecoolerDAO(connection);
        this.itemDAO = new ItemDAO(connection);
    }

    private Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {
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

    public String renderHeader(HttpExchange httpExchange, Connection connection) {
        Session currentSession = getCurrentSession(httpExchange, connection);
        Map<String, String> cookiesMap = createCookiesMap(httpExchange);

        String templatePath = "templates/header.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        if (cookiesMap.containsKey( "flashNote" )) {
            String flashNote = cookiesMap.get( "flashNote" );
            String divID = cookiesMap.get( "divID" );
            model.with("flashNote", createFlashNoteHTMLMessage( flashNote, divID ));
            clearUsedFlashNoteCookie(httpExchange);
        }

        model.with("firstName", currentSession.getUserFirstName());
        model.with("lastName", currentSession.getUserLastName());
        model.with("userType", currentSession.getUserType());

        return template.render(model);
    }

    private void clearUsedFlashNoteCookie(HttpExchange httpExchange) {
        HttpCookie cookie = new HttpCookie("flashNote", "");
        httpExchange.getResponseHeaders().add("Set-Cookie", cookie.toString());
    }

    public void addSuccessFlashNoteDataToCookie(String flashNote, HttpExchange httpExchange) {
        HttpCookie divID = new HttpCookie("divID", "flashNote");
        httpExchange.getResponseHeaders().add("Set-Cookie", divID.toString());

        HttpCookie cookie = new HttpCookie("flashNote", flashNote);
        httpExchange.getResponseHeaders().add("Set-Cookie", cookie.toString());
    }

    public void addFailureFlashNoteDataToCookie(HttpExchange httpExchange) {
        String failureNote = "Ups... Something gone really wrong ! OPERATION UNSUCCESSFUL";

        HttpCookie divID = new HttpCookie("divID", "negativeFlashNote");
        httpExchange.getResponseHeaders().add("Set-Cookie", divID.toString());

        HttpCookie cookie = new HttpCookie("flashNote", failureNote);
        httpExchange.getResponseHeaders().add("Set-Cookie", cookie.toString());
    }

    private String createFlashNoteHTMLMessage(String flashNote, String divID) {
        return "<div id=\"" + divID + "\">" + flashNote + "</div>";
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

    public Artifact getArtifactById(String id) {
        try {
            return artifactDAO.getByID( id );
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

    public Codecooler getCodecoolerById(String id) {
        try {
            return codecoolerDAO.getByID( id );
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Quest getQuestById(String id) {
        try {
            return questDAO.getByID( id );
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Item> readUserItemsFromDB (HttpExchange httpExchange, Connection connection) {
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

    public List<Artifact> readArtifactsFromDB() {
        try {
            return artifactDAO.readAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Codecooler> readCodecoolersFromDB() {
        try {
            return codecoolerDAO.readAll();
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

    public List<Quest> readQuestsFromDB() {
        try {
            return questDAO.readAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Loggable getLoggedUser(Session currentSession) {
        String loggedUserID = currentSession.getUserID();
        String loggedUserType = currentSession.getUserType();
        Loggable loggedUser = null;

        try {
            switch (loggedUserType) {
                case "Admin":
                    loggedUser = adminDAO.getByID( loggedUserID );
                    break;
                case "Mentor":
                    loggedUser = mentorDAO.getByID( loggedUserID );
                    break;
                case "Codecooler":
                    loggedUser = codecoolerDAO.getByID( loggedUserID );
                    break;
            }
            return loggedUser;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}