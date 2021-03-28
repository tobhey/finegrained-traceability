package unisa.gps.etour.control.RefreshmentPointManager;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import unisa.gps.etour.bean.BeanRefreshmentPoint;
import unisa.gps.etour.bean.BeanTag;
import unisa.gps.etour.bean.BeanVisitPR;

/**
 * Interface for common operations on the refreshment
 *
 */
public interface IRefreshmentPointCommonManager extends Remote {

    /**
     * Method to return a particular Refreshment
     *
     * @Param pRefreshmentPointID to identify the Refreshment from Return
     * @Return Bean contains the data of Refreshment concerned
     */
    public BeanRefreshmentPoint getRefreshmentPoint(int pRefreshmentPointID) throws RemoteException;

    /**
     * Method which returns the tags to some refreshment
     *
     * @Param pRefreshmentPointID point identification Refreshment
     * @Return structure containing all BeanTag associated with the point
     *         Refreshments passed as parameter
     */
    public ArrayList<BeanTag> getTagRefreshmentPoint(int pRefreshmentPointID) throws RemoteException;

    /**
     * Method which returns the last 10 comments made for a Refreshment
     *
     * @Param pRefreshmentPointID ID for the point of rest in Question
     * @Return Array of strings containing 10 items
     */
    public String[] getLastComments(int pRefreshmentPointID) throws RemoteException;

    /**
     * Returns for the Refreshment specified, an array where each Location contains
     * the number of ratings corresponding to the value Index of the array more
     * 'one. The calculation and 'made in the period 30 days and today.
     *
     * @Param pRefreshmentPointID unique identifier of Refreshment
     * @Return ArrayList containing the counters as explained above
     */
    public ArrayList<Integer> getStatisticRefreshmentPoint(int pRefreshmentPointID) throws RemoteException;

    /**
     * Method which allows you to change the comment issued for a Refreshment
     *
     * @Param pRefreshmentPointId unique identifier of Refreshment
     * @Param nuovaVisit Bean containing new comment
     * @Return Boolean value-true if the operation went successfully, False
     *         otherwise
     */
    public boolean modifyFeedbackRefreshmentPoint(int pRefreshmentPointId, BeanVisitPR newVisit)
            throws RemoteException;
}
