package pl.coderampart.controller.helpers;

import com.sun.net.httpserver.HttpExchange;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.ConnectionToDB;
import pl.coderampart.DAO.SessionDAO;
import pl.coderampart.model.Session;

import java.io.*;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class HelperController {

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
            String[] cookiesValues = cookieStr.split( "; " );

            for (String cookie : cookiesValues) {
                String[] nameValuePairCookie = cookie.split( "=\"" );
                String name = nameValuePairCookie[0];
                String value = nameValuePairCookie[1].replace( "\"", "" );

                cookiesMap.put( name, value );
            }
        }
        return cookiesMap;
    }

    public String renderHeader(HttpExchange httpExchange) {
        Session currentSession = getCurrentSession( httpExchange );

        String templatePath = "templates/header.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        String firstName = currentSession.getUserFirstName();
        String lastName = currentSession.getUserLastName();
        String typeOfUser = currentSession.getUserType();

        model.with("firstName", firstName );
        model.with("lastName", lastName );
        model.with("userType", typeOfUser );

        return template.render(model);
    }

    public Session getCurrentSession(HttpExchange httpExchange) {
        Map<String, String> cookiesMap = createCookiesMap( httpExchange );
        String sessionID = cookiesMap.get("sessionID");

        try {
            ConnectionToDB connectionToDB = ConnectionToDB.getInstance();
            Connection connection = connectionToDB.connectToDataBase();
            SessionDAO sessionDAO = new SessionDAO(connection);
            Session currentSession = sessionDAO.getByID( sessionID );
            connection.close();

            return currentSession;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String render(String fileName) {
        String templatePath = "templates/" + fileName + ".twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        return template.render(model);
    }

    public void sendResponse(String response, HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = httpExchange.getResponseBody();
        os.write( response.getBytes() );
        os.close();
    }

    public Map<String,String> getInputsMap(HttpExchange httpExchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String formData = br.readLine();

        return parseFormData(formData);
    }
}