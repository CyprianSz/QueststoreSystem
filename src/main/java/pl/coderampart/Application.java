package pl.coderampart;

import com.sun.net.httpserver.HttpServer;
import pl.coderampart.controller.admin.CreateMentorController;
import java.net.InetSocketAddress;
import pl.coderampart.DAO.ConnectionToDB;
import pl.coderampart.controller.Static;
import pl.coderampart.controller.admin.DeleteMentorController;
import pl.coderampart.controller.admin.DisplayMentorsController;
import pl.coderampart.controller.admin.EditMentorController;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.SQLException;

public class Application {

    public static void main(String[] args) throws ClassNotFoundException, IOException, SQLException {
        ConnectionToDB connectionToDB = ConnectionToDB.getInstance();
        Connection connection = connectionToDB.connectToDataBase();

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/create-mentor", new CreateMentorController());
        server.createContext("/update-admin", new EditMentorController(connection));
        server.createContext("/display-mentors", new DisplayMentorsController(connection));
        server.createContext("/delete-mentor", new DeleteMentorController(connection));
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