package unisa.gps.etour.control.CulturalHeritageManager;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import unisa.gps.etour.bean.BeanCulturalHeritage;
import unisa.gps.etour.bean.BeanTag;
import unisa.gps.etour.bean.BeanVisitBC;

/**
 * Interface for operations common to users and operators on Agency
 * 
 *
 */
public interface ICulturalHeritageCommonManager extends Remote {
    /**
     *
     * Method to return a particular Cultural Heritage
     *
     * @Param pCulturalHeritageID The identifier of the cultural property to be
     *        returned
     * @Return BeanCulturalHeritage Contains data required of Cultural Heritage
     */
    public BeanCulturalHeritage getCulturalHeritage(int pCulturalHeritageID) throws RemoteException;

    /**
     * Returns the list of tags of a cultural
     *
     * @Param ID pCulturalHeritageID of Cultural Heritage
     * @Return ArrayList of the cultural <BeanTag> tags specified
     */
    public ArrayList<BeanTag> getTagCulturalHeritage(int pCulturalHeritageID) throws RemoteException;

    /**
     *
     * Returns a list of feedback to the cultural specified
     *
     * @Param ID pCulturalHeritageID of Cultural Heritage
     * @Return HashMap <BeanVisitBC, String> The feedback of Cultural Heritage
     */
    public HashMap<BeanVisitBC, String> getFeedbackCulturalHeritage(int pCulturalHeritageID) throws RemoteException;

    /**
     *
     * Returns for the cultural property specified, an array where each position
     * contains the number of Feedback corresponding to the value of the array more
     * than 'one. The calculation and 'made in the period between 30 days and today.
     *
     * @Param ID pCulturalHeritageID of Cultural Heritage
     * @Return ArrayList <Integer> The statistics of last thirty days
     */
    public ArrayList<Integer> getStatisticCulturalHeritage(int pCulturalHeritageID) throws RemoteException;

    /**
     * Method for updatang (or modifytion) of a feedback for a certain good
     * Cultural. The method has the burden of
     *
     * @Param pCulturalHeritageID The identifier of the cultural change which the
     *        feedback
     * @Param pBeanVisitBC The new feedback to the cultural indicated
     * @Return boolean The result of the operation; true if was successful, false
     *         otherwise
     */
    public boolean modifyFeedbackCulturalHeritage(int pCulturalHeritageID, BeanVisitBC pBeanVisitBC)
            throws RemoteException;
}