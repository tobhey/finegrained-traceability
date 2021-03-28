package unisa.gps.etour.repository;

import java.sql.SQLException;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanRefreshmentPoint;
import unisa.gps.etour.bean.BeanTag;
import unisa.gps.etour.util.Point3D;

/**
 * Interface for management of eateries in the database
 *
 */
public interface IDBRefreshmentPoint {
    /**
     * Add a refreshment
     *
     * @Param pRefreshmentPoint Refreshment to add
     */
    public boolean insertRefreshmentPoint(BeanRefreshmentPoint pRefreshmentPoint) throws SQLException;

    /**
     * Modify a refreshment
     *
     * @Param pRefreshmentPoint Refreshment to edit
     * @Return True if and 'been changed otherwise false
     */
    public boolean modifyRefreshmentPoint(BeanRefreshmentPoint pRefreshmentPoint) throws SQLException;

    /**
     * Delete a refreshment
     *
     * @Param ID pIdRefreshmentPoint Refreshment to eliminate
     * @Return True if and 'have been deleted false otherwise
     */
    public boolean clearRefreshmentPoint(int pIdRefreshmentPoint) throws SQLException;

    /**
     * Returns data from a point of comfort with the ID given as argument
     *
     * @Param pId point identification Refreshments
     * @Return Refreshment
     */
    public BeanRefreshmentPoint getRefreshmentPoint(int pId) throws SQLException;

    /**
     * Advanced Search. Returns the list of eateries that have in Name or
     * description given String as input, sorted according to Preferences of
     * tourists, the tags and filtered according to the distance Max. The list
     * returned contains only the number of catering outlets input data. To scroll
     * the real list, which may contain multiple 'items, you Use paramtro numPage.
     *
     * @Param Id pIdTourist tourists who carried out the research PKeyword * @param
     *        String that contains the keyword to search the Name or description of
     *        refreshment
     * @Param pTags list of tags used to filter the search. the Maximum number of
     *        tags to be included should not exceed five Units'. If you exceed this
     *        number the other tags Excess will be ignored.
     * @Param pNumberPage The page number you want to view. O The 1 page (the
     *        first 10 results), 1 for 2 page (s Results from 11 to 20) etc ... *
     * @Param pPosition position of the person who carried out the research
     * @Param int Number of elements to return pNumberElementsPerPage
     * @Param pMaxDistance Maximum distance from the user to refreshment To seek
     * @Return list containing ten points Refreshments
     */
    public ArrayList<BeanRefreshmentPoint> searchAdvanced(int pIdTourist, String PKeyword, ArrayList<BeanTag> pTags,
            int pNumberPage, int pNumberElementsPerPage, Point3D pPosition, double pMaxDistance)
            throws SQLException;

    /**
     * Method to get the number of elements to search.
     *
     * @See searchAdvanced ()
     * @Param Id pIdTourist tourists who carried out the research PKeyword 
     * @param
     *        String that contains the keyword to search the Name or description of
     *        refreshment
     * @Param pTags list of tags used to filter the search. the Maximum number of
     *        tags to be included should not exceed five Units'. If you exceed this
     *        number the other tags Excess will be ignored. *
     * @Param pPosition position of the person who carried out the research
     * @Param pMaxDistance Maximum distance from the user to refreshment To seek
     * @Return number of pages.
     */
    public int getSearchResultNumberAdvanced(int pIdTourist, String PKeyword, ArrayList<BeanTag> pTags,
            Point3D pPosition, double pMaxDistance) throws SQLException;

    /**
     * Research. Returns the list of eateries that have the name or Description
     * given String as input, filtered and tags According to the maximum distance.
     * The returned list contains the number of Points Refreshments input data. To
     * scroll the real list, which May contain more 'items, you use the paramtro
     * NumPage.
     *
     * PKeyword 
     * @param String that contains the keyword to search the Name or
     * description of refreshment
     * 
     * @Param pTags list of tags used to filter the search. the Maximum number of
     *        tags to be included should not exceed five Units'. If you exceed this
     *        number the other tags Excess will be ignored.
     * @Param pNumberPage The page number you want to view. O The 1 page (the
     *        first 10 results), 1 for 2 page (s Results from 11 to 20) etc ... *
     * @Param pPosition position of the person who carried out the research
     * @Param pMaxDistance Maximum distance from the user to refreshment
     * @Param int Number of elements to return pNumberElementsPerPage
     * @Return list containing ten points Refreshments
     */
    public ArrayList<BeanRefreshmentPoint> search(String pKeyword, ArrayList<BeanTag> pTags, int pNumberPage,
            int pNumberElementsPerPage, Point3D pPosition, double pMaxDistance) throws SQLException;

    /**
     * Method to get you the elements for an advanced search.
     *
     * @See search ()
     * @Param username pUsernameTourist tourists who carried out the research
     *        PKeyword * @param String that contains the keyword to search the Name
     *        or description of refreshment
     * @Param pTags list of tags used to filter the search. The Maximum number of
     *        tags to be included should not exceed five Units'. If you exceed this
     *        number the other tags Excess will be ignored.
     * @Param pPosition position of the person who carried out the research
     * @Param pMaxDistance Maximum distance from the user to refreshment To seek
     * @Throws SQLException
     * @Return number of pages.
     */
    public int getSearchResultNumber(String pKeyword, ArrayList<BeanTag> pTags, Point3D pPosition,
            double pMaxDistance) throws SQLException;

    /**
     * Returns a list of all the refreshment
     *
     * @Throws SQLException
     * @Return list of all the refreshment
     */
    public ArrayList<BeanRefreshmentPoint> getListPR() throws SQLException;
}