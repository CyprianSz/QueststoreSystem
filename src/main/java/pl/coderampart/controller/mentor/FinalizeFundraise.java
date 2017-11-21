package pl.coderampart.controller.mentor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import pl.coderampart.DAO.ArtifactDAO;
import pl.coderampart.DAO.CodecoolerDAO;
import pl.coderampart.DAO.FundraisersDAO;
import pl.coderampart.DAO.FundraisingsDAO;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Artifact;
import pl.coderampart.model.Codecooler;
import pl.coderampart.model.Fundraising;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FinalizeFundraise implements HttpHandler {
    private Connection connection;
    private FundraisersDAO fundraisersDAO;
    private FundraisingsDAO fundraisingsDAO;
    private CodecoolerDAO codecoolerDAO;
    private ArtifactDAO artifactDAO;
    private HelperController helper;
    private boolean isFundraisingValid;

    public FinalizeFundraise(Connection connection) {
        this.connection = connection;
        this.fundraisersDAO = new FundraisersDAO( connection );
        this.fundraisingsDAO = new FundraisingsDAO( connection );
        this.codecoolerDAO = new CodecoolerDAO( connection );
        this.artifactDAO = new ArtifactDAO( connection );
        this.helper = new HelperController(connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String[] uri = httpExchange.getRequestURI().toString().split("=");
        String fundraisingID = uri[uri.length - 1];

        try {
            Fundraising fundraising = fundraisingsDAO.getByID(fundraisingID);

            List<String> fundraisersIDs = fundraisersDAO.getFundraisersIDs( fundraisingID );
            List<Codecooler> fundraisers = new ArrayList<>();

            Artifact artifact = fundraising.getArtifact();
            Integer artifactPrice = artifact.getValue();
            Integer priceRequired = artifactPrice / fundraisers.size();

            isFundraisingValid = true;
            for (String fundraiserID : fundraisersIDs) {
                Codecooler fundraiser = codecoolerDAO.getByID( fundraiserID );
                Integer fundraiserBalance = fundraiser.getWallet().getBalance();

                if (fundraiserBalance >= priceRequired) {
                    fundraisers.add( fundraiser );
                } else {
                    isFundraisingValid = false;
                    break;
                }
            }

            if (isFundraisingValid) {
                for (Codecooler fundraiser : fundraisers) {
                    Integer actualFundraiserBalance = fundraiser.getWallet().getBalance();
                    Integer updatedFundraiserBalance = actualFundraiserBalance - priceRequired;
                    fundraiser.getWallet().setBalance( updatedFundraiserBalance );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
