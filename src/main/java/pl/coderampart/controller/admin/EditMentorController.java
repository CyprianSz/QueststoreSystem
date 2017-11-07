package pl.coderampart.controller.admin;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.MentorDAO;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import pl.coderampart.model.Mentor;

import java.io.*;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditMentorController implements HttpHandler {

    private Connection connection;
    private MentorDAO mentorDAO;

    public EditMentorController(Connection connection) {
        this.connection = connection;
        this.mentorDAO = new MentorDAO(this.connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String response = "";

        if(method.equals("GET")) {
            List<Mentor> allMentors = readMentorsFromDB();

            response += render("header");
            response += render("admin/adminMenu");
            response += renderEditMentor(allMentors);
            response += render("footer");
        }

        if(method.equals("POST")) {
            InputStreamReader isr = new InputStreamReader( httpExchange.getRequestBody(), "utf-8" );
            BufferedReader br = new BufferedReader(isr);
            String formData = br.readLine();

            Map inputs = parseFormData(formData);
//            Mentor editedMentor = createEditedMentorFromInput(inputs);

            response = "<HTML><BODY>DUPA</BODY></HTML>";
        }

        httpExchange.sendResponseHeaders( 200, response.getBytes().length );
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());


        os.close();
    }

//    private Mentor createEditedMentorFromInput(Map<String, String> inputs) {
//        String firstName = inputs.get("first-name");
//        String lastName= inputs.get("last-name");
//        String dateOfBirth = inputs.get("date-of-birth");
//        String email= inputs.get("email");
//
//        Mentor editedMentor = new Mentor();
//
//        try {
//            mentorDAO.update(editedMentor);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return editedMentor;
//    }

    private List<Mentor> readMentorsFromDB() {
        List<Mentor> allMentors = null;

        try {
            allMentors = mentorDAO.readAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allMentors;
    }

    private String render(String fileName) {
        String templatePath = "templates/" + fileName + ".twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        return template.render(model);
    }

    private String renderEditMentor(List<Mentor> allMentors) {
        String templatePath = "templates/admin/editMentor.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        model.with("allMentors", allMentors);

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
