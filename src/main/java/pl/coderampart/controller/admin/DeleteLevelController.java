package pl.coderampart.controller.admin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import pl.coderampart.DAO.LevelDAO;
import pl.coderampart.model.Level;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DeleteLevelController implements HttpHandler{

    private Connection connection;
    private LevelDAO levelDAO;

    public DeleteLevelController(Connection connection){
        this.connection = connection;
        this.levelDAO = new LevelDAO(this.connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String response = "";

        List<Level> allLevels = readLevelsFromDB();

        String[] uri = httpExchange.getRequestURI().toString().split("=%2F");
        String id = uri[uri.length-1];

        response += render("header");
        response += render("admin/adminMenu");
        response += renderLevelsList(allLevels);
        response += render("footer");

        if(method.equals("POST")) {
            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String formData = br.readLine();
            Map inputs = parseFormData(formData);

            if(inputs.get("confirmation").equals("yes")) {
                deleteLevel(allLevels, id);
            }
        }

        httpExchange.sendResponseHeaders( 200, response.getBytes().length );
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
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
}
