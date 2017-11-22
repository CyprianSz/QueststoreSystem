package pl.coderampart.controller.codecooler;

import com.sun.net.httpserver.HttpHandler;
import pl.coderampart.DAO.FundraisersDAO;
import pl.coderampart.DAO.SessionDAO;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Session;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class JoinFundraising implements HttpHandler {

    private Connection connection;
    private SessionDAO sessionDAO;
    private FundraisersDAO fundraisersDAO;
    private HelperController helper;

    public JoinFundraising(Connection connection) {
        this.connection = connection;
        this.sessionDAO = new SessionDAO(connection);
        this.fundraisersDAO = new FundraisersDAO( connection );
        this.helper = new HelperController(connection);
    }

    @Override
    public void handle(com.sun.net.httpserver.HttpExchange httpExchange) throws IOException {
//        sprawdzić czy to w dobrym miejscu tutaj dzieli i bierze to z URL bo spora szansa, że nie
        String[] uri = httpExchange.getRequestURI().toString().split("=");
        String fundraisingID = uri[uri.length - 1];

        Map<String, String> cookiesMap = helper.createCookiesMap( httpExchange );
        String sessionID = cookiesMap.get("sessionID");

        try {
            Session currentSession = sessionDAO.getByID( sessionID );
            String fundraiserID = currentSession.getUserID();
            fundraisersDAO.create( fundraiserID, fundraisingID );
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

//        Uncomment if won't redirect when click on JOIN
//        httpExchange.getResponseHeaders().set("Location", "/fundraising/display");
//        httpExchange.sendResponseHeaders(302, -1);

    }
}
