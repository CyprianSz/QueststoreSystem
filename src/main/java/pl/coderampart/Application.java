package pl.coderampart;

import com.sun.net.httpserver.HttpServer;
import pl.coderampart.DAO.ConnectionToDB;
import pl.coderampart.controller.Static;
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

        server.createContext("/update-admin", new EditMentorController(connection));
        server.createContext("/static", new Static());

        server.setExecutor(null);
        server.start();

/*        try {
            Logger logger = new Logger();
        } catch (SQLException e){
            e.printStackTrace();
        }*/
    }
}