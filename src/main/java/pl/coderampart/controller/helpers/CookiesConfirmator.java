package pl.coderampart.controller.helpers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpCookie;
import java.sql.Connection;

public class CookiesConfirmator implements HttpHandler {

    private Connection connection;
    private HelperController helper;

    public CookiesConfirmator(Connection connection) {
        this.connection = connection;
        this.helper = new HelperController(connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        HttpCookie cookieInfoConfirmation = new HttpCookie("cookieInfoConfirmation", "ACCEPTED");
        httpExchange.getResponseHeaders().add("Set-Cookie", cookieInfoConfirmation.toString());

        helper.redirectTo( "/login", httpExchange );
    }
}
