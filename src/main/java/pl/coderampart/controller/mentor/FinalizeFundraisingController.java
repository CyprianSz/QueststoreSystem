package pl.coderampart.controller.mentor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.*;
import pl.coderampart.controller.helpers.AccessValidator;
import pl.coderampart.controller.helpers.FlashNoteHelper;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FinalizeFundraisingController extends AccessValidator implements HttpHandler {
    private Connection connection;
    private FundraisersDAO fundraisersDAO;
    private FundraisingDAO fundraisingDAO;;
    private CodecoolerDAO codecoolerDAO;
    private ItemDAO itemDAO;
    private WalletDAO walletDAO;
    private HelperController helper;
    private FlashNoteHelper flashNoteHelper;

    public FinalizeFundraisingController(Connection connection) {
        this.connection = connection;
        this.fundraisersDAO = new FundraisersDAO( connection );
        this.fundraisingDAO = new FundraisingDAO( connection );
        this.codecoolerDAO = new CodecoolerDAO( connection );
        this.itemDAO = new ItemDAO( connection );
        this.walletDAO = new WalletDAO ( connection );
        this.helper = new HelperController( connection );
        this.flashNoteHelper = new FlashNoteHelper();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        validateAccess( "Mentor", httpExchange, connection);
        String method = httpExchange.getRequestMethod();
        List<Fundraising> allFundraising = helper.readOpenFundraisingsFromDB();
        String fundraisingID = helper.getIdFromURI( httpExchange );
        Fundraising fundraising = helper.getFundraisingById(fundraisingID);

        if (method.equals("GET")) {
            String response = "";
            response += helper.renderHeader( httpExchange, connection );
            response += helper.render( "mentor/mentorMenu" );
            response += renderProperBodyResponse( fundraisingID, allFundraising );
            response += helper.render( "footer" );

            helper.sendResponse( response, httpExchange );
        }

        if (method.equals("POST")) {
            Map inputs = helper.getInputsMap(httpExchange);

            if (inputs.get("confirmation").equals("yes")) {
                finalizeFundraising(fundraising, httpExchange);
            }
            helper.redirectTo( "/codecooler/finalize-fundraising", httpExchange );
        }
    }

    private String renderProperBodyResponse(String fundraisingID, List<Fundraising> allFundraisings) {
        Integer idLength = 36;
        if(fundraisingID.length() == idLength) {
            Fundraising fundraisingToFinalize = helper.getFundraisingById(fundraisingID);
            return renderConfirmation(fundraisingToFinalize);
        } else {
            return renderDisplayFundraisings(allFundraisings);
        }
    }

    private String renderConfirmation(Fundraising fundraising) {
        String templatePath = "templates/mentor/finalizeChosenFundraising.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        model.with("name", fundraising.getName());

        return template.render( model );
    }

    private String renderDisplayFundraisings(List<Fundraising> allFundraising) {
        String templatePath = "templates/mentor/finalizeFundraisingStartPage.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("allFundraisings", allFundraising);

        return template.render(model);
    }

    private void finalizeFundraising(Fundraising fundraising, HttpExchange httpExchange) {
        try {
            String fundraisingID = fundraising.getID();
            List<String> fundraisersIDs = fundraisersDAO.getFundraisersIDs( fundraisingID );
            List<Codecooler> confirmedFundraisers = new ArrayList<>();

            Artifact artifact = fundraising.getArtifact();
            Integer artifactPrice = artifact.getValue();
            Integer amountOfPotentialFundraisers = fundraisersIDs.size();
            Integer priceRequired = artifactPrice / amountOfPotentialFundraisers;

            boolean haveFundraisersEnoughFunds = checkIfFundraisersHaveEnoughFunds(fundraisersIDs, confirmedFundraisers, priceRequired);
            boolean isFundraisingOpen = fundraising.getIsOpen();

            if (haveFundraisersEnoughFunds && isFundraisingOpen) {
                getFoundsFromFundraisersAccounts(confirmedFundraisers, priceRequired);
                createNewGroupItem(fundraising);
                closeFundraising(fundraising);

                String flashNote = "FUNDRAISING FINALIZED SUCCESSFULLY";
                flashNoteHelper.addSuccessFlashNoteToCookie(flashNote, httpExchange);
            } else {
                flashNoteHelper.addFailureFlashNoteToCookie(httpExchange);
            }
        } catch (SQLException e) {
            flashNoteHelper.addFailureFlashNoteToCookie(httpExchange);
            e.printStackTrace();
        }
    }

    private void closeFundraising(Fundraising fundraising) throws SQLException {
        fundraising.closeFundraising();
        fundraisingDAO.update(fundraising);
    }

    private void createNewGroupItem(Fundraising fundraising) throws SQLException {
        Artifact artifact = fundraising.getArtifact();
        Wallet wallet = fundraising.getCreator().getWallet();

        Item newItem = new Item(artifact, wallet);
        itemDAO.create(newItem);
    }

    private boolean checkIfFundraisersHaveEnoughFunds(List<String> fundraisersIDs,
                                                      List<Codecooler> confirmedFundraisers,
                                                      Integer priceRequired) throws SQLException {
        for (String fundraiserID : fundraisersIDs) {
            Codecooler fundraiser = codecoolerDAO.getByID( fundraiserID );
            Integer fundraiserBalance = fundraiser.getWallet().getBalance();

            if (fundraiserBalance >= priceRequired) {
                confirmedFundraisers.add( fundraiser );
            } else {
                return false;
            }
        }
        return true;
    }

    private void getFoundsFromFundraisersAccounts(List<Codecooler> fundraisers, Integer priceRequired) throws SQLException {
        for (Codecooler fundraiser : fundraisers) {
            Integer actualFundraiserBalance = fundraiser.getWallet().getBalance();
            Integer updatedFundraiserBalance = actualFundraiserBalance - priceRequired;
            Wallet fundraiserWallet = fundraiser.getWallet();
            fundraiserWallet.setBalance( updatedFundraiserBalance );

            walletDAO.update(fundraiserWallet);
        }
    }

}
