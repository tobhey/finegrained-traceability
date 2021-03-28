package unisa.gps.etour.control.RegisteredUserManager;

import java.rmi.RemoteException;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanCulturalHeritage;
import unisa.gps.etour.bean.BeanSearchPreference;
import unisa.gps.etour.bean.BeanGenericPreference;
import unisa.gps.etour.bean.BeanRefreshmentPoint;
import unisa.gps.etour.bean.BeanTourist;
import unisa.gps.etour.bean.BeanVisitBC;
import unisa.gps.etour.bean.BeanVisitPR;

/**
 * Interface on the Management of Tourist Information
 *
 */
public interface ITouristClientManager extends ITouristCommonManager {

    /**
     * Method for the insertion of a Tourist
     *
     * @Param pTourist container for all data relating to tourism by Insert
     * @Return Boolean: True if the insertion is successful, False otherwise
     */
    public boolean insertTourist(BeanTourist pTourist) throws RemoteException;

    /**
     * Method for including the General Preferences Tourist
     *
     * @Param pIdTourist Identifier Tourist which involve General Preferences
     * @Param pGenericPreference General Preferences for inclusion
     * @Return Boolean: True if the insertion is successful, False otherwise
     */
    public boolean insertGenericPreference(BeanGenericPreference pGenericPreference) throws RemoteException;

    /**
     * Method for the extraction Preferences generously given Tourists
     *
     * @Param pIdTourist Identifier Tourist which you want Get the General
     *        Preferences
     * @Return Preferences General information relating to tourism
     */
    public BeanGenericPreference getGenericPreference(int pIdTourist) throws RemoteException;

    /**
     * Method for changing the Preferences generously given Tourists
     *
     * @Param ID pIdTourist of tourists for whom you want Change the General
     *        Preferences
     * @Param pGenericPreferenceNew The Prefereferze General for inclusion
     * @Param pGenericPreferenceVecchie Preferences generous to replace
     * @Return Boolean: True if the modify successful, False otherwise
     */
    public boolean modifyGenericPreference(BeanGenericPreference pGenericPreferencNew)
            throws RemoteException;

    /**
     * Method for the removal of preferences associated with the General Tourist
     *
     * @Param ID pIdTourist of tourists for whom you want Delete the General
     *        Preferences
     * @Return Preferences General erased
     */
    public BeanGenericPreference clearGenericPreference(int pIdTourist) throws RemoteException;

    /**
     * Method to insert a Search Preferences
     *
     * @Param ID pIdTourist of tourists for which you intend Insert a Search
     *        Preferences
     * @Param pSearchPreference Search Preferences be included
     * @Return Boolean: True if the insertion is successful, False otherwise
     */
    public boolean insertSearchPreference(int pIdTourist, BeanSearchPreference pSearchPreference)
            throws RemoteException;

    /**
     * Method for extracting the set of Search Preferences given Tourist
     *
     * @Param ID pIdTourist of tourists for whom you want Extract search preferences
     *        Together 
     * @return the search preferences associated with the Tourist
     *        information
     */
    public BeanSearchPreference[] getSearchPreference(int pIdTourist) throws RemoteException;

    /**
     * Method for deleting a Search Preference given its ID and Tourists
     *
     * @Param ID pIdTourist of tourists for whom you want Delete a Search
     *        Preferences
     * @Param ID pIdSearchPreference Search Preferences To cancel
     * @Return The preference of search Delete
     */
    public BeanSearchPreference clearSearchPreference(int pIdTourist, int pIdSearchPreference)
            throws RemoteException;

    /**
     * Method to extract the list references to the Cultural Heritage Visited by a
     * tourist
     *
     * @Param pIdTourist Identifier Tourist
     * @Return list of references to the Cultural Heritage Visited
     */
    public BeanVisitBC[] getCulturalHeritageVisit(int pIdTourist) throws RemoteException;

    /**
     * Method for the extraction of the list when making reference to Refreshments
     * Visited by a tourist
     *
     * @Param pIdTourist Identifier Tourist
     * @Return list of references to Refreshments Visited
     */
    public BeanVisitPR[] getRefreshmentPointVisit(int pIdTourist) throws RemoteException;

    /**
     * Method for the insertion of a cultural Visited
     *
     * @Param pVisitBC package containing all information relating to Visit
     * @Return true if the item is added successfully, false otherwise
     */
    public boolean insertCulturalHeritageVisit(BeanVisitBC pVisitBC) throws RemoteException;

    /**
     * Method for inserting a refreshment Visited
     *
     * @Param pVisitPR package containing all information relating to Visit
     * @Return true if the item is added successfully, false otherwise
     */
    public boolean insertRefreshmentPointVisit(BeanVisitPR pVisitPR) throws RemoteException;

    /**
     * Method for the insertion of a cultural object in the list of Favorites
     *
     * @Param pIdTourist Identifier Tourist
     * @Param ID pIdCulturalHeritage of Cultural Heritage
     * @Return true if the insertion is successful, false otherwise
     */
    public boolean insertCulturalHeritagePreference(int pIdTourist, int pIdCulturalHeritage) throws RemoteException;

    /**
     * Method for inserting a refreshment to my Favorites
     *
     * @Param pIdTourist Identifier Tourist
     * @Param pIdRefreshmentPoint point identification Refreshment
     * @Return true if the insertion is successful, false otherwise
     */
    public boolean insertRefreshmentPointPreference(int pIdTourist, int pIdRefreshmentPoint) throws RemoteException;

    /**
     * Method for the clearing of a cultural object from the list of Favorites
     *
     * @Param pIdTourist Identifier Tourist
     * @Param ID pIdCulturalHeritage of Cultural Heritage
     * @Return true if the clearing is successful, false otherwise
     * @Throws RemoteException Exception Remote
     */
    public boolean clearCulturalHeritagePreference(int pIdTourist, int pIdCulturalHeritage) throws RemoteException;

    /**
     * Method for deleting a refreshment from the list of Favorites
     *
     * @Param pIdTourist Identifier Tourist
     * @Param pIdRefreshmentPoint point identification Refreshment
     * @Return true if the clearing is successful, false otherwise
     */
    public boolean clearRefreshmentPointPreference(int pIdTourist, int pIdRefreshmentPoint) throws RemoteException;

    /**
     * Method to extract the list of Cultural Heritage Favorites
     *
     * @Param pIdTourist Identifier Tourist
     * @Return List of Cultural Heritage Favorites
     */
    public BeanCulturalHeritage[] getCulturalHeritagePreference(int pIdTourist) throws RemoteException;

    /**
     * Method to extract the list of Refreshments
     *
     * @Param pIdTourist Identifier Tourist
     * @Return list of eateries Favorites
     */
    public BeanRefreshmentPoint[] getRefreshmentPointPreference(int pIdTourist) throws RemoteException;

}
