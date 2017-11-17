package pl.coderampart.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpCookie;

public class Logout implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        HttpCookie firstName = new HttpCookie("firstName", null);
        httpExchange.getResponseHeaders().add( "Set-Cookie", firstName.toString() );
        HttpCookie lastName = new HttpCookie("lastName", null);
        httpExchange.getResponseHeaders().add( "Set-Cookie", lastName.toString() );
        HttpCookie sessionId = new HttpCookie("sessionId",  UUIDController.createUUID());
        httpExchange.getResponseHeaders().add( "Set-Cookie", sessionId.toString() );
        HttpCookie userId = new HttpCookie("userId", null);
        httpExchange.getResponseHeaders().add( "Set-Cookie", userId.toString() );
        HttpCookie typeOfUser = new HttpCookie("typeOfUser", null);
        httpExchange.getResponseHeaders().add( "Set-Cookie", typeOfUser.toString() );

        httpExchange.getResponseHeaders().set("Location", "/login");
        httpExchange.sendResponseHeaders(302, -1);
    }
}
