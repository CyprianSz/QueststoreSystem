package pl.coderampart.controller;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;


public class Logout {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {


        httpExchange.getResponseHeaders().set("Location", "/hello");
        httpExchange.sendResponseHeaders(302, -1);
    }
}
