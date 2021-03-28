
package unisa.gps.etour.control.SearchManager;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class for managing Standard Search
 *
 */
public class StandardSearch extends Search {

    /**
     * Constructor of the class
     *
     */
    public StandardSearch() throws RemoteException {
    }

    protected int getSearchResultNumberSpecialized() throws SQLException {
// Check the type of site and gets the number of results
        switch (pSiteType) {
        case BENE_CULTURALE:
            return CulturalHeritage.getSearchResultNumber(this.pKeyword, this.pTags, this.pUserPosition,
                    this.pMaxRadius);
        case PUNTO_DI_RISTORO:
            return RefreshmentPoint.getSearchResultNumber(this.pKeyword, this.pTags, this.pUserPosition,
                    this.pMaxRadius);
        default:
            return -1;
        }
    }

    protected ArrayList<?> searchSpecializedPerPage(int pNumberPage) throws SQLException {
// Check the type of site and search
        switch (pSiteType) {
        case BENE_CULTURALE:
            return CulturalHeritage.search(pKeyword, pTags, pNumberPage, numberPageResult, pUserPosition,
                    pMaxRadius);
        case PUNTO_DI_RISTORO:
            return RefreshmentPoint.search(pKeyword, pTags, pNumberPage, numberPageResult, pUserPosition,
                    pMaxRadius);
        default:
            return null;

        }
    }
}