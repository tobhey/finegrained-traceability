package unisa.gps.etour.control.RegisteredUserManager;

import java.rmi.RemoteException;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanTourist;
import unisa.gps.etour.bean.BeanVisitBC;
import unisa.gps.etour.bean.BeanVisitPR;

/**
 * Interface for handling tourists from the side of the transaction Agency
 *
 */
public interface ITouristAgencyManager extends ITouristCommonManager {

    /**
     * Method for the clearing of a tourist from the Database
     *
     * @Param pIdTourist Identifier Tourist delete
     * @Return Boolean: true if the operation is successful, false otherwise
     */
    public boolean delete(int pIdTourist) throws RemoteException;

    /**
     * Method to activate a registered tourists
     *
     * @Param pIdTourist ID to activate the Tourist
     * @Return Boolean: true if the operation is successful, false otherwise
     */
    public boolean activeTourist(int pIdTourist) throws RemoteException;

    /**
     * Method to disable an active tourists
     *
     * @Param to disable pIdTourist Identifier Tourist
     * @Return Boolean: true if the operation is successful, false otherwise
     */
    public boolean disactiveTourist(int pIdTourist) throws RemoteException;

    /**
     * Method to obtain a collection of Tourists
     *
     * @Return ArrayList of BeanTourist
     */
    public ArrayList<BeanTourist> getTourist() throws RemoteException;

    /**
     * Method to obtain a collection of active tourists or not
     *
     * @Param boolean statusAccount Tourists can choose On whether
     * @Return ArrayList of BeanTourist
     */
    public ArrayList<BeanTourist> getTourist(boolean statusAccount) throws RemoteException;

    /**
     * Method to get all the feedback issued by a tourist for the points
     * Refreshments
     *
     * @Param pIdTourist ID to pick up the tourists in Feedback
     * @Return ArrayList containing all the beans Feedback released
     * @Throws RemoteException Exception Remote
     */
    public ArrayList<BeanVisitPR> getFeedbackPR(int pIdTourist) throws RemoteException;

    /**
     * Method to get all the feedback issued by a tourist for Heritage Cultural
     *
     * @Param pIdTourist ID to pick up the tourists in Feedback
     * @Return ArrayList containing all the beans Feedback released
     * @Throws RemoteException Exception Remote
     */
    public ArrayList<BeanVisitBC> getFeedbackBC(int pIdTourist) throws RemoteException;

}
