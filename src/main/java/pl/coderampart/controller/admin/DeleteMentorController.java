package pl.coderampart.controller.admin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.MentorDAO;
import pl.coderampart.model.Mentor;

import java.io.*;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class DeleteMentorController implements HttpHandler {

    private Connection connection;
    private MentorDAO mentorDAO;

    public DeleteMentorController(Connection connection) {
        this.connection = connection;
        this.mentorDAO = new MentorDAO(this.connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String response = "";

        List<Mentor> allMentors = readMentorsFromDB();

        String[] uri = httpExchange.getRequestURI().toString().split("=");
        String id = uri[uri.length-1];

        if(method.equals("GET")) {
            response += render("header");
            response += render("admin/adminMenu");
            String responseTemp = renderMentorsList(allMentors);
            if(id.length()==36) {
                responseTemp = renderDeleteQuestion(getMentorById(id, allMentors), allMentors);
            }
            response += responseTemp;
            response += render("footer");
        }

        if(method.equals("POST")){

            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String formData = br.readLine();
            Map inputs = parseFormData(formData);
            if(inputs.get("confirmation").equals("yes")) {
                deleteMentor(allMentors, id);
            }
            httpExchange.getResponseHeaders().set("Location", "/delete-mentor");
            httpExchange.sendResponseHeaders(302, -1);
            response += render("header");
            response += render("admin/adminMenu");
            String responseTemp = renderMentorsList(allMentors);
            response += responseTemp;
            response += render("footer");
        }

        httpExchange.sendResponseHeaders( 200, response.getBytes().length );
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());

        os.close();
    }

    private List<Mentor> readMentorsFromDB() {
        List<Mentor> allMentors = null;

        try {
            allMentors = mentorDAO.readAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allMentors;
    }

    private Mentor getMentorById(String id, List<Mentor> allMentors) {
        Mentor changedMentor = null;

        for (Mentor mentor: allMentors) {
            if (id.equals(mentor.getID())) {
                changedMentor = mentor;
            }
        }
        return changedMentor;
    }

    private String render(String fileName) {
            String templatePath = "templates/" + fileName + ".twig";
            JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
            JtwigModel model = JtwigModel.newModel();

            return template.render(model);
        }

    private String renderDeleteQuestion(Mentor mentor, List<Mentor> allMentors) {

        String templatePath = "templates/admin/deleteChosenMentor.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("allMentors", allMentors);
        model.with("firstName", mentor.getFirstName());
        model.with("lastName", mentor.getLastName());
        return template.render(model);
    }


    private String renderMentorsList(List<Mentor> allMentors) {
        String templatePath = "templates/admin/deleteMentorStartPage.twig";
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

    private void deleteMentor(List<Mentor> allMentors,String id) {
        Mentor changedMentor = null;
        for (Mentor mentor: allMentors) {
            if (id.equals(mentor.getID())) {
                changedMentor = mentor;
                try{

                    mentorDAO.delete(changedMentor);
                }catch (SQLException se){}
                break;
            }
        }
    }
}
