package pl.coderampart;

import com.sun.net.httpserver.HttpServer;
import pl.coderampart.controller.admin.CreateMentorController;
import pl.coderampart.controller.helpers.Static;

import java.net.InetSocketAddress;
import java.sql.SQLException;

public class Application {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/create-mentor", new CreateMentorController());
        server.createContext("/static", new Static());
        server.setExecutor(null);

        server.start();
    }
//    public static void main(String[] args) {
//
//        try {
//            Logger logger = new Logger();
//        } catch (SQLException e){
//            e.printStackTrace();
//        }
//    }
}