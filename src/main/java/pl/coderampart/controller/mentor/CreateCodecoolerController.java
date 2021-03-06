package pl.coderampart.controller.mentor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import pl.coderampart.DAO.CodecoolerDAO;
import pl.coderampart.DAO.GroupDAO;
import pl.coderampart.DAO.TeamDAO;
import pl.coderampart.controller.helpers.*;
import pl.coderampart.model.Codecooler;
import pl.coderampart.model.Group;
import pl.coderampart.model.Team;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;

public class CreateCodecoolerController extends AccessValidator implements HttpHandler {

    private Connection connection;
    private HelperController helper;
    private FlashNoteHelper flashNoteHelper;
    private CodecoolerDAO codecoolerDAO;
    private GroupDAO groupDAO;
    private TeamDAO teamDAO;
    private PasswordHasher hasher;
    private MailSender mailSender;

    public CreateCodecoolerController(Connection connection) {
        this.connection = connection;
        this.codecoolerDAO = new CodecoolerDAO( connection );
        this.groupDAO = new GroupDAO(connection);
        this.teamDAO = new TeamDAO(connection);
        this.helper = new HelperController(connection);
        this.flashNoteHelper = new FlashNoteHelper();
        this.hasher = new PasswordHasher();
        this.mailSender = new MailSender();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        validateAccess( "Mentor", httpExchange, connection);
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
            createCodecooler( inputs, httpExchange );
            helper.redirectTo( "/codecooler/create", httpExchange );
        }
    }

    private void createCodecooler(Map<String, String> inputs, HttpExchange httpExchange) {
        String firstName = inputs.get("first-name");
        String lastName = inputs.get("last-name");
        String dateOfBirth = inputs.get("date-of-birth");
        String email = inputs.get("email");
        String groupName = inputs.get("group");
        String teamName = inputs.get("team");

        LocalDate dateOfBirthObject = LocalDate.parse(dateOfBirth);

        try {
            String generatedPassword = helper.generateRandomPassword();
            String hashedPassword = hasher.generateStrongPasswordHash( generatedPassword );
            Group group = groupDAO.getByName( groupName );
            Team team = teamDAO.getByName( teamName );

            Codecooler newCodecooler= new Codecooler( firstName, lastName, dateOfBirthObject,
                                                      email, hashedPassword, connection );
            newCodecooler.setGroup( group );
            newCodecooler.setTeam( team );
            codecoolerDAO.create( newCodecooler );

            String initialMessage = mailSender.prepareMessage( firstName, generatedPassword );
            mailSender.send( email, initialMessage );

            String codecoolerFullName = firstName + " " + lastName;
            String flashNote = flashNoteHelper.createCreationFlashNote( "Codecooler", codecoolerFullName );
            flashNoteHelper.addSuccessFlashNoteToCookie(flashNote, httpExchange);
        } catch (NoSuchAlgorithmException | SQLException | InvalidKeySpecException e) {
            flashNoteHelper.addFailureFlashNoteToCookie(httpExchange);
            e.printStackTrace();
        }
    }


}
