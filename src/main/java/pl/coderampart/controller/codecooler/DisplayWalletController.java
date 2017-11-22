package pl.coderampart.controller.codecooler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.WalletDAO;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Item;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DisplayWalletController implements HttpHandler {

    private Connection connection;
    private WalletDAO walletDAO;
    private HelperController helperController;

    public DisplayWalletController(Connection connection) {
        this.connection = connection;
        this.walletDAO = new WalletDAO(this.connection);
        this.helperController = new HelperController(connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        List<Item> itemsList = readItemsFromDB(); 
        String response = "";
        response += helperController.renderHeader(httpExchange);
        response += helperController.render("codecooler/codecoolerMenu");
        response += renderDisplayItems(itemsList);
        response += helperController.render("footer");
        httpExchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private List<Item> readItemsFromDB() {
        List<Item> itemsList = null;

        try {
            itemsList = walletDAO.???READITEMS(String ID)???;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return itemsList;
    }

    private String renderDisplayItems(List<Item> itemsList) {
        String templatePath = "templates/codecooler/walletTable.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("itemsList", itemsList);

        return template.render(model);
    }
}