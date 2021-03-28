package unisa.gps.etour.control.RefreshmentPointManager;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import unisa.gps.etour.bean.BeanConvention;
import unisa.gps.etour.bean.BeanRefreshmentPoint;
import unisa.gps.etour.bean.BeanVisitPR;

/**
 * Interface for refreshments on the side of the agency
 *
 */
public interface IRefreshmentPointAgencyManager extends IRefreshmentPointCommonManager {

    /**
     * Method for inserting a new Refreshment
     *
     * @Param pRefreshmentPoint containing all the data from the Refreshment Add
     */
    public boolean insertRefreshmentPoint(BeanRefreshmentPoint pRefreshmentPoint) throws RemoteException;

    /**
     * Method for deleting a refreshment bar with ID
     *
     * @Param pIDRefreshmentPoint for the unique identification of point Refreshments
     */
    public boolean clearRefreshmentPoint(int pRefreshmentPointID) throws RemoteException;

    /**
     * Method to return all the refreshment of the DataBase
     *
     * @Return ArrayList containing all the beans of the present Refreshments In the
     *         DataBase
     */
    public ArrayList<BeanRefreshmentPoint> getRefreshmentPoint() throws RemoteException;

    /**
     * Method to return all the refreshment with convention Active or not
     *
     * @Param Boolean statusConvention for the type of eateries by Get (contracted
     *        or not)
     * @Return ArrayList containing all the beans of the present Refreshments In the
     *         database depending on the status of the Convention
     */
    public ArrayList<BeanRefreshmentPoint> getRefreshmentPoint(boolean statusConvention) throws RemoteException;

    /**
     * Method for inserting a new convention for a certain point Refreshments
     *
     * @Param pRefreshmentPointID integer that uniquely identifies the point
     *        Refreshments
     * @Param pConv Convention that will enable (Parameter ID Refreshment create
     *        redundancy but is useful for security Data)
     * @Return boolean for confirmation of operation
     */
    public boolean activeConvention(int pRefreshmentPointID, BeanConvention pConv) throws RemoteException;

    /**
     * Method to get all the feedback associated to a certain point Refreshments
     *
     * @Param pRefreshmentPointID unique identifier of the Refreshment To get feedback
     * @Return HashMap containing the bean as the key value of feedback and how The
     *         tourist who issued the feedback
     */
    public HashMap<BeanVisitPR, String> getFeedbackRefreshmentPoint(int pRefreshmentPointID) throws RemoteException;

    /**
     * Method for updatang (or change) the data of a Refreshment
     *
     * @Param pRefreshmentPointID for the unique identification of point Refreshments
     *        to be amended
     * @Param pRefreshmentPointupdated containing the new data to be saved
     * @Return Boolean value-true if the operation went successfully, False
     *         otherwise
     */
    public boolean modifyRefreshmentPoint(BeanRefreshmentPoint pRefreshmentPointupdated) throws RemoteException;

    /**
     * Method which allows you to insert a tag to search for a useful point
     * Refreshments
     *
     * @Param pRefreshmentPointId unique identifier of Refreshment
     * @Param pTagId unique ID tags to be inserted
     * @Return Boolean value-true if the operation went successfully, False
     *         otherwise
     */
    public boolean insertTagRefreshmentPoint(int pRefreshmentPointId, int pTagId) throws RemoteException;

    /**
     * Method which allows you to delete a tag to search for a useful point
     * Refreshments
     *
     * @Param pRefreshmentPointId unique identifier of Refreshment
     * @Param pTagId unique ID tags to be inserted
     * @Return Boolean value-true if the operation went successfully, False
     *         otherwise
     */
    public boolean clearTagRefreshmentPoint(int pRefreshmentPointId, int pTagId) throws RemoteException;

}