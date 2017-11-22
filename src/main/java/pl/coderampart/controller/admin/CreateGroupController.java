package pl.coderampart.controller.admin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import pl.coderampart.DAO.GroupDAO;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Group;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class CreateGroupController implements HttpHandler {

    private Connection connection;
    private HelperController helper;
    private GroupDAO groupDAO;

    public CreateGroupController(Connection connection) {
        this.connection = connection;
        this.groupDAO = new GroupDAO( connection );
        this.helper = new HelperController(connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "";
        String method = httpExchange.getRequestMethod();

        if (method.equals("GET")) {
            response += helper.renderHeader(httpExchange, connection);
            response += helper.render("admin/adminMenu");
            response += helper.render("admin/createGroup");
            response += helper.render("footer");

            helper.sendResponse( response, httpExchange );
        }

        if (method.equals("POST")) {
            Map<String, String> inputs = helper.getInputsMap( httpExchange );

            createGroup( inputs );
            helper.redirectTo( "/group/create", httpExchange );
        }
    }

    public void createGroup(Map<String, String> inputs) {
        String groupName = inputs.get("group-name");
        Group newGroup = new Group(groupName);

        try {
            groupDAO.create(newGroup);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
