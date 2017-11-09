package pl.coderampart.controller.helpers;

import com.sun.net.httpserver.HttpExchange;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class HelperController {

    public Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        String[] pairs = formData.split("&");
        for(String pair : pairs){
            String[] keyValue = pair.split("=");
            String value = new URLDecoder().decode(keyValue[1], "UTF-8");
            map.put(keyValue[0], value);
        }
        return map;
    }

    public Map<String, String> createCookiesMap(HttpExchange httpExchange) {
        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");
        String[] cookiesValues = cookieStr.split("; ");

        Map<String, String> cookiesMap = new HashMap<>();

        for (String cookie : cookiesValues) {
            String[] nameValuePairCookie = cookie.split("=\"");
            String name = nameValuePairCookie[0];
            String value = nameValuePairCookie[1].replace("\"", "");

            cookiesMap.put(name, value);
        }
        return cookiesMap;
}