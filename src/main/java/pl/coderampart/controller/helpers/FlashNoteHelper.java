package pl.coderampart.controller.helpers;

import com.sun.net.httpserver.HttpExchange;
import org.jtwig.JtwigModel;

import java.net.HttpCookie;
import java.util.Map;

public class FlashNoteHelper {
    public void modelFlashNote(Map<String, String> cookiesMap, JtwigModel model, HttpExchange httpExchange) {
        String controllerName = getControllerNameFromURI(httpExchange);

        String flashNote = cookiesMap.get( controllerName + "flashNote" );
        String divID = cookiesMap.get( controllerName + "divID" );
        String flashNoteInHTML = createFlashNoteHTMLMessage( flashNote, divID );
        model.with("flashNote", flashNoteInHTML);
    }

    public void clearUsedFlashNoteCookie(HttpExchange httpExchange) {
        String controllerName = getControllerNameFromURI(httpExchange);

        HttpCookie flashNoteCookie = new HttpCookie(controllerName + "flashNote", "");
        httpExchange.getResponseHeaders().add("Set-Cookie", flashNoteCookie.toString());

        HttpCookie divID = new HttpCookie(controllerName + "divID", "");
        httpExchange.getResponseHeaders().add("Set-Cookie", divID.toString());
    }

    public void addSuccessFlashNoteToCookie(String flashNote, HttpExchange httpExchange) {
        String controllerName = getControllerNameFromURI(httpExchange);

        HttpCookie flashNoteCookie = new HttpCookie(controllerName + "flashNote", flashNote);
        httpExchange.getResponseHeaders().add("Set-Cookie", flashNoteCookie.toString());

        HttpCookie divID = new HttpCookie(controllerName + "divID", "flashNote");
        httpExchange.getResponseHeaders().add("Set-Cookie", divID.toString());
    }

    public void addFailureFlashNoteToCookie(HttpExchange httpExchange) {
        String controllerName = getControllerNameFromURI(httpExchange);
        String failureNote = "OPERATION UNSUCCESSFUL";

        HttpCookie flashNoteCookie = new HttpCookie(controllerName + "flashNote", failureNote);
        httpExchange.getResponseHeaders().add("Set-Cookie", flashNoteCookie.toString());

        HttpCookie divID = new HttpCookie(controllerName + "divID", "negativeFlashNote");
        httpExchange.getResponseHeaders().add("Set-Cookie", divID.toString());

    }

    public String getControllerNameFromURI(HttpExchange httpExchange) {
        String[] uri = httpExchange.getRequestURI().toString().split("/");
        System.out.println(uri[uri.length - 2]);
        return uri[uri.length - 2];
    }

    private String createFlashNoteHTMLMessage(String flashNote, String divID) {
        return "<div id=\"" + divID + "\">" + flashNote + "</div>";
    }
}
