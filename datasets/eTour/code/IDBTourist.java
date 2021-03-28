package unisa.gps.etour.repository;

import java.sql.SQLException;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanCulturalHeritage;
import unisa.gps.etour.bean.BeanTourist;

/**
 * Interface for the management of tourists in the database
 *
 */
public interface IDBTourist {
    /**
     * Add a tourist
     *
     * @Param to add pTourist Tourist
     */
    public boolean insertTourist(BeanTourist pTourist) throws SQLException;

    /**
     * Modify a tourist
     *
     * @Param to change pTourist Tourist
     * @Return True if and 'been changed otherwise false
     */
    public boolean modifyTourist(BeanTourist pTourist) throws SQLException;

    /**
     * Delete a tourist from the database
     *
     * @Param pIdTourist Identificatie Tourist delete
     * @Return True if and 'been changed otherwise false
     */
    public boolean delete(int pIdTourist) throws SQLException;

    /**
     * Returns the data of the Tourist
     *
     * @Param pUsername Username tourists
     * @Return Information about tourist
     */
    public BeanTourist getTourist(String pUsername) throws SQLException;

    /**
     * Attach a cultural tourists preferred
     *
     * @Param ID pIdTourist tourists
     * @Param pIdCulturalHeritage ID of the cultural
     */
    public boolean insertCulturalHeritagePreference(int pIdTourist, int pIdCulturalHeritage) throws SQLException;

    /**
     * Attach a point of catering to the tourist favorite
     *
     * @Param ID pIdTourist tourists
     * @Param pIdRefreshmentPoint ID of the cultural
     */
    public boolean insertRefreshmentPointPreference(int pIdTourist, int pIdRefreshmentPoint) throws SQLException;

    /**
     * Delete a cultural favorite
     *
     * @Param ID pIdTourist tourists
     * @Param pIdCulturalHeritage ID of the cultural
     * @Return True if and 'been changed otherwise false
     */
    public boolean clearCulturalHeritagePreference(int pIdTourist, int pIdCulturalHeritage) throws SQLException;

    /**
     * Delete a favorite resting spot
     *
     * @Param ID pIdTourist tourists
     * @Param pIdRefreshmentPoint ID of the cultural
     * @Return True if and 'was deleted false otherwise
     */
    public boolean clearRefreshmentPointPreference(int pIdTourist, int pIdRefreshmentPoint) throws SQLException;

    /**
     * Returns an ArrayList of tourists who have a username like that Given as
     * argument
     *
     * @Param pUsernameTourist Usrername tourists to search
     * @Return data for Tourists
     */
    public ArrayList<BeanTourist> getTourists(String pUsernameTourist) throws SQLException;

    /**
     * Returns the list of tourists turned on or off
     *
     * @Param select pact True False those tourists turned off
     * @Return data for Tourists
     */
    public ArrayList<BeanTourist> getTourist(boolean condition) throws SQLException;

    /**
     * Returns the data of the tourist with ID equal to that given in Input
     *
     * @Param ID pIdTourist tourists to find
     * @Return Tourists with id equal to the input, null if there is
     */
    public BeanTourist getTourist(int pIdTourist) throws SQLException;

    /**
     * Returns the list of cultural favorites from a particular Tourist
     *
     * @Param ID pIdTourist tourists to find
     * @Return List of Cultural Heritage Favorites
     */
    public ArrayList<Integer> getCulturalHeritagePreference(int pIdTourist) throws SQLException;

    /**
     * Returns a list of favorite resting spot by a particular Tourist
     *
     * @Param ID pIdTourist tourists to find
     * @Return List of Refreshment Favorites
     */
    public ArrayList<Integer> getRefreshmentPointPreference(int pIdTourist) throws SQLException;

}