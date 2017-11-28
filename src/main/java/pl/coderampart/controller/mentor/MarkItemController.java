package pl.coderampart.controller.mentor;

import com.sun.net.httpserver.*;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.CodecoolerDAO;
import pl.coderampart.DAO.ItemDAO;
import pl.coderampart.controller.helpers.AccessValidator;
import pl.coderampart.controller.helpers.FlashNoteHelper;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Codecooler;
import pl.coderampart.model.DTOCodecoolerItem;
import pl.coderampart.model.Item;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class MarkItemController extends AccessValidator implements HttpHandler {

    private Connection connection;
    private CodecoolerDAO codecoolerDAO;
    private ItemDAO itemDAO;
    private HelperController helper;
    private FlashNoteHelper flashNoteHelper;

    public MarkItemController(Connection connection) {
        this.connection = connection;
        this.codecoolerDAO = new CodecoolerDAO(connection);
        this.itemDAO = new ItemDAO(connection);
        this.helper = new HelperController(connection);
        this.flashNoteHelper = new FlashNoteHelper();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        validateAccess("Mentor", httpExchange, connection);
        String method = httpExchange.getRequestMethod();
        List<Item> allItems = helper.readItemsFromDB();
        String itemID = helper.getIdFromURI( httpExchange );
        Item item = helper.getItemById(itemID);

        if (method.equals("GET")) {
            String response = "";
            response += helper.renderHeader( httpExchange, connection );
            response += helper.render( "mentor/mentorMenu" );
            response += renderProperBodyResponse( itemID, allItems );
            response += helper.render( "footer" );

            helper.sendResponse( response, httpExchange );
        }

        if (method.equals("POST")) {
            Map inputs = helper.getInputsMap(httpExchange);

            if (inputs.get("confirmation").equals("yes")) {
                markItemAsSpent(item, httpExchange);
            }
            helper.redirectTo( "/codecooler/mark-item", httpExchange );
        }
    }

    private String renderProperBodyResponse(String itemID, List<Item> allItems) {
        Integer idLength = 36;
        if(itemID.length() == idLength) {
            Item itemToMark = helper.getItemById(itemID);
            return renderConfirmation(itemToMark);
        } else {
            return renderDisplayItems(allItems);
        }
    }

    private String renderConfirmation(Item item) {
        String templatePath = "templates/mentor/markChoosenItem.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        model.with("name", item.getArtifact().getName());

        return template.render( model );
    }

    private String renderDisplayItems(List<Item> allItems) {
        String templatePath = "templates/mentor/markItem.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        try {
            List<DTOCodecoolerItem> codecoolerItemPairs = createCodecoolerItemPairs(allItems);
            model.with("codecoolersItemsPairs", codecoolerItemPairs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return template.render(model);
    }

    private List<DTOCodecoolerItem> createCodecoolerItemPairs(List<Item> allItems) throws SQLException {
        List<DTOCodecoolerItem> codecoolerItemPairs = new ArrayList<>();

        for (Item item : allItems) {
            boolean itemIsSpent = item.getMark();

            if (!itemIsSpent) {
                String walletID = item.getWallet().getID();
                Codecooler codecooler = codecoolerDAO.getCodecoolerByWalletID( walletID );
                DTOCodecoolerItem codecoolerItem = new DTOCodecoolerItem( codecooler, item );
                codecoolerItemPairs.add( codecoolerItem );
            }
        }
        return codecoolerItemPairs;
    }

    private void markItemAsSpent(Item item, HttpExchange httpExchange) {
        try {
            item.setMark();
            itemDAO.update( item );

            String flashNote = item.getArtifact().getName() + " spent";
            flashNoteHelper.addSuccessFlashNoteToCookie(flashNote, httpExchange);
        } catch (SQLException e) {
            flashNoteHelper.addFailureFlashNoteToCookie(httpExchange);
            e.printStackTrace();
        }
    }
}
