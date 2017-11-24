package pl.coderampart.controller.mentor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import pl.coderampart.DAO.CodecoolerDAO;
import pl.coderampart.DAO.GroupDAO;
import pl.coderampart.DAO.TeamDAO;
import pl.coderampart.controller.PasswordHasher;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Codecooler;
import pl.coderampart.model.Group;
import pl.coderampart.model.Team;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class CreateCodecoolerController implements HttpHandler {

    private Connection connection;
    private HelperController helper;
    private CodecoolerDAO codecoolerDAO;
    private GroupDAO groupDAO;
    private TeamDAO teamDAO;
    private PasswordHasher hasher;

    public CreateCodecoolerController(Connection connection) {
        this.connection = connection;
        this.codecoolerDAO = new CodecoolerDAO( connection );
        this.helper = new HelperController(connection);
        this.groupDAO = new GroupDAO(connection);
        this.teamDAO = new TeamDAO(connection);
        this.hasher = new PasswordHasher();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String response = "";
        String method = httpExchange.getRequestMethod();

        if (method.equals( "GET" )) {
            response += helper.renderHeader( httpExchange, connection );
            response += helper.render( "mentor/mentorMenu" );
            response += helper.renderWithDropdowns( "mentor/createCodecooler");
            response += helper.render( "footer" );

            helper.sendResponse( response, httpExchange );
        }

        if (method.equals( "POST" )) {
            Map<String, String> inputs = helper.getInputsMap( httpExchange );
            createCodecooler( inputs );
            helper.redirectTo( "/codecooler/create", httpExchange );
        }
    }

    private void createCodecooler(Map<String, String> inputs) {
        String firstName = inputs.get("first-name");
        String lastName = inputs.get("last-name");
        String dateOfBirth = inputs.get("date-of-birth");
        String email = inputs.get("email");
        String password = inputs.get("password");
        String groupName = inputs.get("group");
        String teamName = inputs.get("team");
        LocalDate dateOfBirthObject = LocalDate.parse(dateOfBirth);

        try {
            String hashedPassword = hasher.generateStrongPasswordHash( password );
            Group group = groupDAO.getByName( groupName );
            Team team = teamDAO.getByName( teamName );
            Codecooler newCodecooler= new Codecooler( firstName, lastName, dateOfBirthObject,
                                                      email, hashedPassword, connection );
            newCodecooler.setGroup( group );
            newCodecooler.setTeam( team );
            codecoolerDAO.create( newCodecooler );
        } catch (NoSuchAlgorithmException | SQLException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }
}
