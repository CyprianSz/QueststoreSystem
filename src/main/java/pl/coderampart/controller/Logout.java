package pl.coderampart.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import pl.coderampart.DAO.SessionDAO;
import pl.coderampart.controller.helpers.FlashNoteHelper;
import pl.coderampart.controller.helpers.HelperController;

import java.io.IOException;
import java.net.HttpCookie;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class Logout implements HttpHandler {

    private Connection connection;
    private SessionDAO sessionDAO;
    private HelperController helper;
    private FlashNoteHelper flashNoteHelper;


    public Logout(Connection connection) {
        this.connection = connection;
        this.sessionDAO = new SessionDAO( connection );
        this.helper = new HelperController(connection);
        this.flashNoteHelper = new FlashNoteHelper();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Map<String, String> cookiesMap = helper.createCookiesMap( httpExchange );
        String sessionID = cookiesMap.get("sessionID");

        try {
            sessionDAO.deleteByID( sessionID );
            String successFlashNote = "LOGOUT SUCCESSFULLY";
            flashNoteHelper.addSuccessFlashNoteToCookie(successFlashNote , httpExchange);
        } catch (SQLException | ClassNotFoundException e) {
            flashNoteHelper.addFailureFlashNoteToCookie(httpExchange);
            e.printStackTrace();
        }

        httpExchange.getResponseHeaders().set("Location", "/login");
        httpExchange.sendResponseHeaders(302, -1);
    }
}
