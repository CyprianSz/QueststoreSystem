package pl.coderampart;

import com.sun.net.httpserver.HttpServer;
import pl.coderampart.controller.admin.*;

import java.net.InetSocketAddress;
import pl.coderampart.DAO.ConnectionToDB;
import pl.coderampart.controller.Static;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.SQLException;

public class Application {

    public static void main(String[] args) throws ClassNotFoundException, IOException, SQLException {
        ConnectionToDB connectionToDB = ConnectionToDB.getInstance();
        Connection connection = connectionToDB.connectToDataBase();

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/create-mentor", new CreateMentorController(connection));
        server.createContext("/edit-mentor", new EditMentorController(connection));
        server.createContext("/display-mentors", new DisplayMentorsController(connection));
        server.createContext("/delete-mentor", new DeleteMentorController(connection));
        server.createContext("/create-group", new CreateGroupController());
        server.createContext("/display-groups", new DisplayGroupsController(connection));
        server.createContext("/create-level", new CreateLevelController());
        server.createContext("/display-levels", new DisplayLevelsController(connection));
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