package unisa.gps.etour.repository;

import java.sql.SQLException;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanCulturalHeritage;
import unisa.gps.etour.bean.BeanTag;
import unisa.gps.etour.util.Point3D;

/**
 * Interface for the management of cultural heritage database
 *
 */

public interface IDBCulturalHeritage {
    /**
     * Add a cultural heritage, given input
     *
     * @Param pBene Cultural Heritage for inclusion in database
     */
    public boolean insertCulturalHeritage(BeanCulturalHeritage pBene) throws SQLException;

    /**
     * Modify the information in the cultural
     *
     * @Param pBene contains the information to modify the database
     * @Return True if there 'was a modified false otherwise
     */
    public boolean modifyCulturalHeritage(BeanCulturalHeritage pBene) throws SQLException;

    /**
     * Delete a cultural object from the database
     *
     * @Param ID pIdBene cultural property to delete
     * @Return True if and 'was deleted false otherwise
     */
    public boolean clearCulturalHeritage(int pIdBene) throws SQLException;

    /**
     * Returns the cultural object with id as input
     *
     * @Param pId cultural property to be extracted from the database
     * @Return cultural property obtained from the database
     */
    public BeanCulturalHeritage getCulturalHeritage(int pId) throws SQLException;

    /**
     * Research. Returns the list of cultural property in their name or Description
     * given String as input, filtered according to tags and Maximum distance. The
     * returned list contains the number of goods given as input. To browse the real
     * list, which may contain more 'of Ten elements, you use the paramtro
     * numPage.
     *
     * PKeyword 
     * @param String that contains the keyword to search the Name or description of the cultural
     * 
     * @Param pTags list of tags used to filter the search. the Maximum number of
     *        tags to be included should not exceed five Units'. If you exceed this
     *        number the other tags Excess will be ignored.
     * @Param pNumPage The page number you want to view. O for 1 page (the first
     *        10 results), 1 for 2 page (s Results from 11 to 20) etc ...
     * @Param pPosition position of the person who carried out the research
     * @Param pMaxDistance Maximum distance from the user to search for good
     * @Param pNumberElementsPerPage number of items to return per page
     * @Throws SQLException
     * @Return list contained ten cultural
     */
    public ArrayList<BeanCulturalHeritage> search(String pKeyword, ArrayList<BeanTag> pTags, int pNumPage,
            int pNumberElementsPerPage, Point3D pPosition, double pMaxDistance) throws SQLException;

    /**
     * Advanced Search. Returns the list of cultural goods which have in Name or
     * description given String as input, sorted according to Preferences of
     * tourists and filtered according to the tag and the maximum distance. The
     * Returned list contains the number of goods given as input. To scroll The
     * actual list, which may contain multiple 'items, you Use paramtro numPage.
     *
     * @Param ID pIdTourist tourists who carried out the research PKeyword * @param
     *        String that contains the keyword to search the Name or description of
     *        the cultural
     * @Param pTags list of tags used to filter the search. the Maximum number of
     *        tags to be included should not exceed five Units'. If you exceed this
     *        number the other tags Excess will be ignored.
     * @Param pNumPage The page number you want to view. O for 1 page (the first
     *        10 results), 1 for 2 page (s Results from 11 to 20) etc ...
     * @Param pPosition position of the person who carried out the research
     * @Param pMaxDistance Maximum distance from the user to search for good
     * @Param pNumberElementsPerPage number of items to return per page
     * @Throws SQLException
     * @Return list contained ten cultural
     */
    public ArrayList<BeanCulturalHeritage> searchAdvanced(int pIdTourist, String PKeyword, ArrayList<BeanTag> pTags,
            int pNumPage, int pNumberElementsPerPage, Point3D pPosition, double pMaxDistance)
            throws SQLException;

    /**
     * Method to get the number of elements to search.
     *
     * PKeyword * @param String that contains the keyword to search the Name or
     * description of the cultural
     * 
     * @Param pTags list of tags used to filter the search. the Maximum number of
     *        tags to be included should not exceed five Units'. If you exceed this
     *        number the other tags Excess will be ignored.
     * @Param pPosition position of the person who carried out the research
     * @Param pMaxDistance Maximum distance from the user to search for good
     * @Throws SQLException
     * @Return number of pages.
     */
    public int getSearchResultNumber(String pKeyword, ArrayList<BeanTag> pTags, Point3D pPosition,
            double pMaxDistance) throws SQLException;

    /**
     * Method to get the number of elements to search.
     *
     * @Param identifier pIdTourist tourists who carried out the research PKeyword
     *        * @param String that contains the keyword to search the Name or
     *        description of the cultural
     * @Param pTags list of tags used to filter the search. the Maximum number of
     *        tags to be included should not exceed five Units'. If you exceed this
     *        number the other tags Excess will be ignored.
     * @Param pPosition position of the person who carried out the research
     * @Param pMaxDistance Maximum distance from the user to search for good
     * @Throws SQLException
     * @Return number of pages.
     */
    public int getSearchResultNumberAdvanced(int pIdTourist, String PKeyword, ArrayList<BeanTag> pTags,
            Point3D pPosition, double pMaxDistance) throws SQLException;

    /**
     * Returns a list of all cultural
     *
     * @Throws SQLException
     * @Return List of all cultural
     */
    public ArrayList<BeanCulturalHeritage> getListBC() throws SQLException;
}