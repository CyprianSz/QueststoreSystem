package pl.coderampart.controller.helpers;

import com.sun.net.httpserver.HttpExchange;
import pl.coderampart.DAO.SessionDAO;
import pl.coderampart.model.Session;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

public abstract class AccessValidator {

    protected void validateAccess(String validUserType, HttpExchange httpExchange, Connection connection) {
        try {
            SessionDAO sessionDAO = new SessionDAO( connection );
            HelperController helper = new HelperController( connection );
            Map<String, String> cookiesMap = helper.createCookiesMap( httpExchange );

            if (cookiesMap.containsKey( "sessionID" )) {
                String loggedUserType = getLoggedUserType( cookiesMap, sessionDAO );

                if (!Objects.equals( loggedUserType, validUserType )) {
                    httpExchange.sendResponseHeaders( 403, -1 );
                }
            } else {
                httpExchange.sendResponseHeaders( 403, -1 );
            }
        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String getLoggedUserType( Map<String, String> cookiesMap, SessionDAO sessionDAO )
            throws SQLException, ClassNotFoundException {
        String sessionID = cookiesMap.get( "sessionID" );
        Session currentSession = sessionDAO.getByID( sessionID );
        return currentSession.getUserType();}
}