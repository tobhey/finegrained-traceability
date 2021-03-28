package unisa.gps.etour.control.SearchManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

import unisa.gps.etour.bean.BeanCulturalHeritage;
import unisa.gps.etour.bean.BeanRefreshmentPoint;
import unisa.gps.etour.bean.BeanTag;
import unisa.gps.etour.repository.DBCulturalHeritage;
import unisa.gps.etour.repository.DBRefreshmentPoint;
import unisa.gps.etour.repository.DBTag;
import unisa.gps.etour.repository.IDBCulturalHeritage;
import unisa.gps.etour.repository.IDBRefreshmentPoint;
import unisa.gps.etour.repository.IDBTag;
import unisa.gps.etour.util.ControlData;
import unisa.gps.etour.util.ErrorMessage;
import unisa.gps.etour.util.Point3D;

public class Search extends UnicastRemoteObject implements ISearch {

    private static final long serialVersionUID = -6009809097302884655L;

    public Search() throws RemoteException {
        super();
    }

// Search Parameters
    private int pIdTourist = -1;
    protected String pKeyword;
    protected double pMaxRadius;
    protected Point3D pUserPosition;
    private int pElementsPerPage = -1;
    protected byte pSiteType = -1;

// Objects for the database connection
    protected IDBCulturalHeritage CulturalHeritage = new DBCulturalHeritage();
    protected IDBRefreshmentPoint RefreshmentPoint = new DBRefreshmentPoint();
    private IDBTag Tag = new DBTag();

// List of tags obtained
    protected ArrayList<BeanTag> pTags;

// Search Results pages for partitioned
    private Hashtable<Integer, ArrayList<BeanCulturalHeritage>> resultSearchCulturalHeritage = new Hashtable<Integer, ArrayList<BeanCulturalHeritage>>();
    private Hashtable<Integer, ArrayList<BeanRefreshmentPoint>> resultSearchRefreshmentPoint = new Hashtable<Integer, ArrayList<BeanRefreshmentPoint>>();

// Quantity of items and pages results in the research phase
    protected int numberPageResult = -1;
    private int numberElementsSearch = -1;

    /*
     * (Non-Javadoc) Unisa.gps.etour.control.ManagerRicerche.ISearch * @see #
     * search (int, java.lang.String, int [], double, int,
     * unisa.gps.etour.util.Punto3D, byte)
     */
    public int search(int pIdTourist, String pKeyword, int[] pTagsId, double pMaxRadius, int pElementsPerPage,
            Point3D pUserPosition, byte pSiteType) throws RemoteException {
// Reset the parameters of the previous search
        clearParameters();

// Start checking the correctness of the search parameters
        try {
            this.pIdTourist = pIdTourist;
            this.pTags = arrayToArrayListTag(pTagsId);
            this.pKeyword = ControlData.correctString(pKeyword, true, true, "", ControlData.max_length);
            this.pMaxRadius = pMaxRadius;
            this.pUserPosition = pUserPosition;
            this.pSiteType = pSiteType;

// If the parameters are valid, I get the number of results
            if (pElementsPerPage > 0 && ((pSiteType == 0) || (pSiteType == 1)) && pMaxRadius > 0
                    && pUserPosition != null
                    && (this.numberElementsSearch = getSearchResultNumberSpecialized()) >= 0) {
                this.pElementsPerPage = pElementsPerPage;
                this.numberPageResult = calculateSearchPageNumber();

                return this.numberElementsSearch;
            }

// Otherwise returns -1
            else
                return -1;
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        } catch (Exception e) {
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }

    }

    /*
     * (Non-Javadoc)
     * 
     * @See unisa.gps.etour.control.ManagerRicerche.ISearch #
     * getPageResultCulturalHeritage (int)
     */
    public BeanCulturalHeritage[] getPageResultCulturalHeritage(int pPage) throws RemoteException {
// Check that the type of site is valid and that the page is
// In the range
        if ((this.pSiteType == BENE_CULTURALE) && checkPage(pPage)) {
// Transform the list obtained in array
            BeanCulturalHeritage[] results = new BeanCulturalHeritage[calculateNumberElementsPage(pPage)];
// Results = searchCulturalHeritagePerPage (pPage). ToArray (
// Results);

            results = searchPerPage(pPage).toArray(results);

            return results;
        } else
            return null;
    }

    /*
     * (Non-Javadoc)
     * 
     * @See unisa.gps.etour.control.ManagerRicerche.ISearch #
     * getPageResultRefreshmentPoint (int)
     */
    public BeanRefreshmentPoint[] getPageResultRefreshmentPoint(int pPage) throws RemoteException {
// Check that the type of site is valid and that the page is
// In the range
        if ((this.pSiteType == PUNTO_DI_RISTORO) && checkPage(pPage)) {
// Transform the list obtained in array
            BeanRefreshmentPoint[] results = new BeanRefreshmentPoint[calculateNumberElementsPage(pPage)];
// Results = searchRefreshmentPointPerPage (pPage). ToArray (
// Results);

            results = searchPerPage(pPage).toArray(results);

            return results;
        } else
            return null;
    }

    /*
     * (Non-Javadoc)
     * 
     * @See unisa.gps.etour.control.ManagerRicerche.ISearch #
     * getSearchResultNumber ()
     */
    public int getSearchResultNumber() throws RemoteException {
        return numberElementsSearch;
    }

    /*
     * (Non-Javadoc)
     * 
     * @See unisa.gps.etour.control.ManagerRicerche.ISearch #
     * getSearchPageNumber ()
     */
    public int getSearchPageNumber() throws RemoteException {
        return numberPageResult;
    }

    /**
     * Method for returning the number of findings from research
     *
     * @Return number of findings from research. On error Returns -1
     * @Throws SQLException Exception Connecting to Database
     */
    private int getSearchResultNumberSpecialized() throws SQLException {
// Check the type of site and gets the number of results
        if (checkIdTourist()) {
            switch (pSiteType) {
            case BENE_CULTURALE:
                return pIdTourist > 0
                        ? CulturalHeritage.getSearchResultNumberAdvanced(this.pIdTourist, this.pKeyword,
                                this.pTags, this.pUserPosition, this.pMaxRadius)
                        : CulturalHeritage.getSearchResultNumber(this.pKeyword, this.pTags,
                                this.pUserPosition, this.pMaxRadius);
            case PUNTO_DI_RISTORO:
                return pIdTourist > 0
                        ? RefreshmentPoint.getSearchResultNumberAdvanced(this.pIdTourist, this.pKeyword,
                                this.pTags, this.pUserPosition, this.pMaxRadius)
                        : RefreshmentPoint.getSearchResultNumber(this.pKeyword, this.pTags,
                                this.pUserPosition, this.pMaxRadius);
            }
        }
        return -1;
    }

    /**
     * Method for returning the list of results
     *
     * @Param pNumberPage range of results to return
     * @Return Container sites emerged in the research phase as the interval
     */
    private ArrayList<?> searchPerPage(int pNumberPage) throws RemoteException {

// Declare the list of sites related to the page input
        ArrayList<?> resultPageCurrent = null;
        try {
            if (checkIdTourist()) {
                // Check the type of site
                switch (pSiteType) {
                case BENE_CULTURALE:
                    // If the list has not already been obtained previously
                    // Search
                    if ((resultPageCurrent = resultSearchCulturalHeritage.get(pNumberPage)) == null) {
                        resultPageCurrent = (ArrayList<?>) (pIdTourist > 0
                                ? CulturalHeritage.searchAdvanced(pIdTourist, pKeyword, pTags, pNumberPage,
                                        pElementsPerPage, pUserPosition, pMaxRadius)
                                : CulturalHeritage.search(pKeyword, pTags, pNumberPage, pElementsPerPage,
                                        pUserPosition, pMaxRadius));

                        resultSearchCulturalHeritage.put(pNumberPage,
                                (ArrayList<BeanCulturalHeritage>) resultPageCurrent);
                    }
                    break;

                case PUNTO_DI_RISTORO:
// If the list has not already been obtained previously
// Search
                    if ((resultPageCurrent = resultSearchRefreshmentPoint.get(pNumberPage)) == null) {
                        resultPageCurrent = (ArrayList<?>) (pIdTourist > 0
                                ? RefreshmentPoint.searchAdvanced(pIdTourist, pKeyword, pTags, pNumberPage,
                                        pElementsPerPage, pUserPosition, pMaxRadius)
                                : RefreshmentPoint.search(pKeyword, pTags, pNumberPage, pElementsPerPage,
                                        pUserPosition, pMaxRadius));
                        resultSearchRefreshmentPoint.put(pNumberPage,
                                (ArrayList<BeanRefreshmentPoint>) resultPageCurrent);
                    }
                    break;
                }
            }
// Return the results
            return resultPageCurrent;
        }

        catch (SQLException e) {
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        } catch (Exception e) {
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }

    }

    /**
     * Method for calculating the number of pages found at Search
     *
     * @Return many pages in the research phase
     */
    private int calculateSearchPageNumber() {
// If the number of pages is greater than or equal to 0 returns the number of
// Pages
        if (numberPageResult >= 0)
            return numberPageResult;
// If the number of elements is a multiple of the number of items per page
// Return their relationship
        if ((numberElementsSearch % pElementsPerPage == 0))
            return (numberElementsSearch / pElementsPerPage);
// Otherwise returns their relationship + 1
        return (numberElementsSearch / pElementsPerPage) + 1;
    }

    /**
     * Method for calculating the number of elements of nell'intevallo A given page
     *
     * @Param interval nPage results
     * @Return number of elements in a page
     */
    private int calculateNumberElementsPage(int nPage) {
// If page number is the last return their form
        if (nPage == (numberPageResult - 1))
            return (numberElementsSearch % pElementsPerPage);
// Otherwise returns the number of items per page
        else
            return (pElementsPerPage);
    }

    /**
     * Method for checking the validity of a page
     *
     * @Param pPage page to check
     * @Return true if the page is valid
     */
    private boolean checkPage(int pPage) {
// If page number is in the range returns true
        return (pPage >= 0 && pPage < numberPageResult);
    }

    /**
     * Method for checking the correctness of the identifier Tourist
     *
     * @Return true if the identifier is valid, false otherwise
     */
    private boolean checkIdTourist() {
        return ((pIdTourist > 0) || (pIdTourist == -1));
    }

    /**
     * Method to reset all the variables related to a search
     *
     * @Return A constant that indicates the correct zero
     */
    private int clearParameters() {

// Clears all the search parameters
        pIdTourist = -1;
        pSiteType = -1;
        pElementsPerPage = -1;
        numberElementsSearch = -1;
        numberPageResult = -1;

        resultSearchCulturalHeritage.clear();
        resultSearchRefreshmentPoint.clear();

        return -1;
    }

    /**
     * Method for the detection and conversion of data tags in struttara List
     *
     * @Param list of identifiers pTagsId Search Tags
     * @Return List Search Tags
     * @Throws SQLException
     */
    private ArrayList<BeanTag> arrayToArrayListTag(int[] pTagsId) throws SQLException {
// Initialize the list of Tags
        ArrayList<BeanTag> PTags = new ArrayList<BeanTag>();
// If the list of identifiers of the tag is empty I get the tags
        if (pTagsId == null) {
            BeanTag currentTag = null;
            try {

// Loop for the insertion of tags found in the list
                for (int i = 0; i < pTagsId.length; i++) {
// If the identifier is greater than 0
                    if (pTagsId[i] > 0) {
                        currentTag = Tag.getTag(pTagsId[i]);
// Check correctness on tags
                        if (ControlData.checkBeanTag(currentTag))
                            pTags.add(currentTag);
                    }
                }

            } catch (SQLException e) {
                throw new SQLException(ErrorMessage.ERROR_DBMS);
            }
        }
// Return the list of tags
        return pTags;
    }
}
