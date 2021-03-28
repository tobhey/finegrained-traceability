package unisa.gps.etour.repository;

import java.sql.SQLException;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanSearchPreference;

/**
 * Interface for managing search preferences in database
 *
 */
public interface IDBSearchPreference {
    /**
     * Add a preference of Search
     *
     * @Param pPreference Search Preferences
     */
    public boolean insertSearchPreference(BeanSearchPreference pPreference) throws SQLException;

    /**
     * Delete a preference for research
     *
     * @Param pPreference preference to eliminate
     * @Return True if and 'have been deleted false otherwise
     */
    public boolean clearSearchPreference(int pIdPreference) throws SQLException;

    /**
     * Returns the list of preferences to find a tourist
     *
     * @Param Id pIdTourist tourists
     * @Return List Search Preferences
     */
    public ArrayList<BeanSearchPreference> getSearchPreferenceDelTourist(int pIdTourist) throws SQLException;

    /**
     * Returns the list of preferences for research of a cultural
     *
     * @Param pIdCulturalHeritage ID of the cultural
     * @Return list search preferences.
     */
    public ArrayList<BeanSearchPreference> getSearchPreferenceDelBC(int pIdCulturalHeritage) throws SQLException;

    /**
     * Returns the list of preferences to find a resting spot
     *
     * @Param identifier pIdRefreshmentPoint a refreshment
     * @Return list search preferences.
     */
    public ArrayList<BeanSearchPreference> getSearchPreferenceDelPR(int pIdRefreshmentPoint)
            throws SQLException;

    /**
     * Add a preference for a cultural
     *
     * @Param pIdCulturalHeritage ID of the cultural
     * @Param pIdSearchPreference ID SearchPreference
     * @Param pPreference Search Preferences
     */
    public boolean insertSearchPreferenceDelBC(int pIdCulturalHeritage, int pIdSearchPreference)
            throws SQLException;

    /**
     * Add a search preference to a tourist
     *
     * @Param Id pIdTourist tourists
     * @Param pIdSearchPreference ID SearchPreference
     * @Param pPreference Search Preferences
     */
    public boolean insertSearchPreferenceDelTourist(int pIdTourist, int pIdSearchPreference)
            throws SQLException;

    /**
     * Add a preference research to a refreshment
     *
     * @Param pIdRefreshmentPoint point identification Refreshments
     * @Param pIdSearchPreference ID SearchPreference
     * @Param pPreference Search Preferences
     */
    public boolean insertSearchPreferenceDelPR(int pIdRefreshmentPoint, int pIdSearchPreference)
            throws SQLException;

    /**
     * Deletes a preference to find a Tourist
     *
     * @Param Id pIdTourist tourists
     * @Param pIdPreference Search Preferences
     * @Return True if and 'have been deleted false otherwise
     */
    public boolean clearSearchPreferenceTourist(int pIdTourist, int pIdPreference) throws SQLException;

    /**
     * Deletes a preference for research of a cultural
     *
     * @Param pIdSearchPreference Search Preferences
     * @Param pIdCulturalHeritage ID of the cultural
     * @Return True if and 'have been deleted false otherwise
     */
    public boolean clearSearchPreferenceBC(int pIdCulturalHeritage, int pIdSearchPreference) throws SQLException;

    /**
     * Deletes a preference to find a resting spot
     *
     * @Param pIdPreference Search Preferences
     * @Param pIdRefreshmentPoint point identification Refreshments
     * @Throws SQLException
     * @Return True if and 'have been deleted false otherwise
     */
    public boolean clearSearchPreferencePR(int pIdRefreshmentPoint, int pIdPreference) throws SQLException;

    /**
     * Returns a list of all search preferences in the DB
     *
     * @Throws SQLException
     * @Return List of search preferences in the DB
     */
    public ArrayList<BeanSearchPreference> getSearchPreference() throws SQLException;

}
