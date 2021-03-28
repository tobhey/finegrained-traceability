package unisa.gps.etour.control.SearchManager;

import java.rmi.Remote;
import java.rmi.RemoteException;

import unisa.gps.etour.bean.BeanCulturalHeritage;
import unisa.gps.etour.bean.BeanRefreshmentPoint;
import unisa.gps.etour.util.Point3D;

/**
 * Interface for management of research
 *
 */
public interface ISearch extends Remote {

// Constants identify the types of site
    public final static byte BENE_CULTURALE = 0;
    public final static byte PUNTO_DI_RISTORO = 1;

    /**
     * Initialization method for research
     *
     * @Param pIdTourist ID of the tourist. Pass -1 in case of a Guest
     * @Param pKeyword Together keyword search Together * @param pTagsId Search
     *        Tags
     * @Param pMaxRadius Maximum distance between the user and the site
     * @Param numberElementsPerPage number of items to look for in a Search
     *        session
     * @Param pUserPosition detected by the GPS user position
     * @Param type pSiteType site search
     * @Return number of elements emerged from the research. On error Returns -1
     */
    public int search(int pIdTourist, String pKeyword, int[] pTagsId, double pMaxRadius, int pElementsPerPage,
            Point3D pUserPosition, byte pSiteType) throws RemoteException;

    /**
     * Method for returning the list of emerging from the Cultural Heritage
     * Research, in a given interval
     *
     * @Param pPage range of items to be included in the results
     * @Return entirety of cultural property related to the range of results
     *         Selected search. In case of error returns null
     */
    public BeanCulturalHeritage[] getPageResultCulturalHeritage(int pPage) throws RemoteException;

    /**
     * Method to return the list of eateries have emerged from Research related to a
     * specific inteval
     *
     * @Param pPage range of items to be included in the results
     * @Return set of points relating to the range of refreshment Selected search
     *         results. In case of error returns null
     * @Throws RemoteException Exception Remote
     */
    public BeanRefreshmentPoint[] getPageResultRefreshmentPoint(int pPage) throws RemoteException;

    /**
     * Method for returning the number of elements results from Search
     *
     * @Return number of elements emerged in the research phase. Where no Is
     *         initialized the search returns -1
     */
    public int getSearchResultNumber() throws RemoteException;

    /**
     * Method for returning the number of pages appear in results
     *
     * @Return number of pages that have emerged in the research phase. Where no Is
     *         initialized the search returns -1
     */
    public int getSearchPageNumber() throws RemoteException;

}
