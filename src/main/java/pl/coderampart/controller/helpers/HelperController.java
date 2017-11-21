package pl.coderampart.controller.helpers;

import com.sun.net.httpserver.HttpExchange;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.ConnectionToDB;
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
        Map<String, String> cookiesMap = createCookiesMap( httpExchange );

        String templatePath = "templates/header.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        model.with("firstName", cookiesMap.get("firstName") );
        model.with("lastName", cookiesMap.get("lastName") );
        model.with("userType", cookiesMap.get("typeOfUser") );

        return template.render(model);
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