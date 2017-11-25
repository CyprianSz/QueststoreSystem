package pl.coderampart;

import com.sun.net.httpserver.HttpServer;
import pl.coderampart.controller.*;
import pl.coderampart.controller.admin.*;
import pl.coderampart.controller.codecooler.*;

import java.net.InetSocketAddress;
import pl.coderampart.DAO.ConnectionToDB;
import pl.coderampart.controller.helpers.CookiesConfirmator;
import pl.coderampart.controller.mentor.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class Application {

    public static void main(String[] args) throws ClassNotFoundException, IOException, SQLException {
        ConnectionToDB connectionToDB = ConnectionToDB.getInstance();
        Connection connection = connectionToDB.connectToDataBase();

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/logout", new Logout(connection));
        server.createContext("/login", new Login(connection));

        server.createContext("/mentor/create", new CreateMentorController(connection));
        server.createContext("/mentor/display", new DisplayMentorsController(connection));
        server.createContext("/mentor/edit", new EditMentorController(connection));
        server.createContext("/mentor/delete", new DeleteMentorController(connection));

        server.createContext("/group/create", new CreateGroupController(connection));
        server.createContext("/group/display", new DisplayGroupsController(connection));
        server.createContext("/group/edit", new EditGroupController(connection));
        server.createContext("/group/delete", new DeleteGroupController(connection));

        server.createContext("/level/create", new CreateLevelController(connection));
        server.createContext("/level/display", new DisplayLevelsController(connection));
        server.createContext("/level/edit", new EditLevelController(connection));
        server.createContext("/level/delete", new DeleteLevelController(connection));

        server.createContext("/team/create", new CreateTeamController(connection));
        server.createContext("/team/display", new DisplayTeamsController(connection));
        server.createContext("/team/edit", new EditTeamController(connection));
        server.createContext("/team/delete", new DeleteTeamController(connection));

        server.createContext("/artifact/create", new CreateArtifactController(connection));
        server.createContext("/artifact/display", new DisplayArtifactsController(connection));
        server.createContext("/artifact/edit", new EditArtifactController(connection));
        server.createContext("/artifact/delete", new DeleteArtifactController(connection));

        server.createContext("/wallet/display", new DisplayWalletController(connection));
        server.createContext("/codecooler/create", new CreateCodecoolerController(connection));
        server.createContext("/codecooler/display", new DisplayCodecoolersController(connection));
        server.createContext("/codecooler/edit", new EditCodecoolerController(connection));
        server.createContext("/codecooler/delete", new DeleteCodecoolerController(connection));

        server.createContext("/quest/create", new CreateQuestController(connection));
        server.createContext("/quest/display", new DisplayQuestsController(connection));
        server.createContext("/quest/edit", new EditQuestController(connection));
        server.createContext("/quest/delete", new DeleteQuestController(connection));

        server.createContext("/confirm-cookie-info", new CookiesConfirmator(connection));
        server.createContext("/change-password", new PasswordChanger(connection));
        server.createContext("/account", new LoggedUserData(connection));
        server.createContext("/wallet/level-info", new DisplayUserLevel(connection));
        server.createContext("/static", new Static());

//        server.createContext("/buy-artifact", new ?(connection));
//        server.createContext("/display-fundraisings", new ?(connection));
//        server.createContext("/create-fundraising", new ?(connection));

        server.setExecutor(null);
        server.start();
    }
}
