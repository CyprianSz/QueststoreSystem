package pl.coderampart.controller.helpers;

import com.sun.net.httpserver.HttpExchange;
import org.jtwig.JtwigModel;

import java.net.HttpCookie;
import java.util.Map;

public class FlashNoteHelper {

    public void modelFlashNote(Map<String, String> cookiesMap, JtwigModel model, HttpExchange httpExchange) {
        String controllerName = getControllerNameFromURI(httpExchange);
        String flashNoteCookieNameWithPath = controllerName + "flashNote";
        String divIDCookieNameWithPath = controllerName + "divID";

        String flashNote = cookiesMap.get( flashNoteCookieNameWithPath );
        String divID = cookiesMap.get( divIDCookieNameWithPath );
        String flashNoteInHTML = createFlashNoteHTMLMessage( flashNote, divID );
        model.with("flashNote", flashNoteInHTML);
    }

    public void clearUsedFlashNoteCookie(HttpExchange httpExchange) {
        String controllerName = getControllerNameFromURI(httpExchange);
        String flashNoteCookieNameWithPath = controllerName + "flashNote";
        String divIDCookieNameWithPath = controllerName + "divID";

        HttpCookie flashNoteCookie = new HttpCookie(flashNoteCookieNameWithPath, "");
        httpExchange.getResponseHeaders().add("Set-Cookie", flashNoteCookie.toString());

        HttpCookie divID = new HttpCookie(divIDCookieNameWithPath, "");
        httpExchange.getResponseHeaders().add("Set-Cookie", divID.toString());
    }

    public void addSuccessFlashNoteToCookie(String flashNote, HttpExchange httpExchange) {
        String controllerName = getControllerNameFromURI(httpExchange);
        String flashNoteCookieNameWithPath = controllerName + "flashNote";
        String divIDCookieNameWithPath = controllerName + "divID";
        String successFlashNoteIDName = "flashNote";

        HttpCookie flashNoteCookie = new HttpCookie(flashNoteCookieNameWithPath, flashNote);
        httpExchange.getResponseHeaders().add("Set-Cookie", flashNoteCookie.toString());

        HttpCookie divID = new HttpCookie(divIDCookieNameWithPath, successFlashNoteIDName);
        httpExchange.getResponseHeaders().add("Set-Cookie", divID.toString());
    }

    public void addFailureFlashNoteToCookie(HttpExchange httpExchange) {
        String controllerName = getControllerNameFromURI(httpExchange);
        String failureNote = "OPERATION UNSUCCESSFUL";
        String flashNoteCookieNameWithPath = controllerName + "flashNote";
        String divIDCookieNameWithPath = controllerName + "divID";
        String negativeFlashNoteIDName = "negativeFlashNote";

        HttpCookie flashNoteCookie = new HttpCookie(flashNoteCookieNameWithPath, failureNote);
        httpExchange.getResponseHeaders().add("Set-Cookie", flashNoteCookie.toString());

        HttpCookie divID = new HttpCookie(divIDCookieNameWithPath, negativeFlashNoteIDName);
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
