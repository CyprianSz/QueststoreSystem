package pl.coderampart.controller.codecooler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import pl.coderampart.DAO.WalletDAO;
import pl.coderampart.controller.helpers.HelperController;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;

public class DisplayWalletController implements HttpHandler {

    private Connection connection;
    private WalletDAO walletDAO;
    private HelperController helperController;

    public DisplayWalletController(Connection connection) {
        this.connection = connection;
        this.walletDAO = new WalletDAO(this.connection);
        this.helperController = new HelperController();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "";
        response += helperController.renderHeader(httpExchange);
        response += helperController.render("codecooler/codecoolerMenu");
        response += helperController.render("codecooler/codecoolerWallet");
//        response += renderWalletItems(walletItems);
        response += helperController.render("footer");
        httpExchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}