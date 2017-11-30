package pl.coderampart.controller.codecooler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.CodecoolerDAO;
import pl.coderampart.DAO.ItemDAO;
import pl.coderampart.DAO.WalletDAO;
import pl.coderampart.controller.helpers.AccessValidator;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Item;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class DisplayWalletController extends AccessValidator implements HttpHandler {

    private Connection connection;
    private HelperController helper;

    public DisplayWalletController(Connection connection) {
        this.connection = connection;
        this.helper = new HelperController(connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        validateAccess( "Codecooler", httpExchange, connection);
        List<Item> userItemsList = helper.readUserItemsFromDB(httpExchange, connection);
        String response = "";

        response += helper.renderHeader(httpExchange, connection);
        response += helper.render("codecooler/codecoolerMenu");
        response += renderDisplayItems(userItemsList);
        response += helper.render("footer");

        helper.sendResponse(response, httpExchange);
    }

    private String renderDisplayItems(List<Item> userItemsList) {
        String templatePath = "templates/codecooler/walletTable.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("userItemsList", userItemsList);

        return template.render(model);
    }
}