package unisa.gps.etour.control.RegisteredUserManager;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import unisa.gps.etour.bean.BeanTourist;
import unisa.gps.etour.bean.BeanVisitBC;
import unisa.gps.etour.bean.BeanVisitPR;
import unisa.gps.etour.repository.DBVisitBC;
import unisa.gps.etour.repository.DBVisitPR;
import unisa.gps.etour.repository.IDBVisitBC;
import unisa.gps.etour.repository.IDBVisitPR;
import unisa.gps.etour.util.ErrorMessage;

/**
 * Class that implements the interface for managing the side Tourists Agency and
 * extends UnicastRemoteObject for communication in remote Provides basic
 * methods for handling and additional methods for returning Tourists with
 * special characteristics of
 *
 */
public class TouristAgencyManager extends TouristCommonManager implements ITouristAgencyManager {

    private IDBVisitBC feedbackBC;
    private IDBVisitPR feedbackPR;

    public TouristAgencyManager() throws RemoteException {
        super();
        try {
// Instantiate objects for database connections
            feedbackBC = new DBVisitBC();
            feedbackPR = new DBVisitPR();
        }
// Exception on the database connection
        catch (Exception e) {
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
    }

// Method for obtaining all tourists alike present
// In the database
    public ArrayList<BeanTourist> getTourist() throws RemoteException {
// Object that will contain the return value
        ArrayList<BeanTourist> toReturn;
// Retrieve data
        try {
// Invoke the method with empty String to get all Tourists
            toReturn = tourist.getTourists("");
            if (null == toReturn)
                throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }
// Exception in database operations
        catch (SQLException e) {
// If the data layer is thrown an exception SQLException
// It throws RemoteException the remote exception
            System.out.println("Error in method getTourist" + e.toString());
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }
// Unexpected exception caused by other factors
        catch (Exception ee) {
            System.out.println("Error in method getTourist" + ee.toString());
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
// Return the result from the data layer ottenuro
        if (null == toReturn)
            throw new RemoteException(ErrorMessage.ERROR_FORMAT_BEAN);
        return toReturn;
    }

// Method that allows to obtain all the tourists who have an account
// Active or not
    public ArrayList<BeanTourist> getTourist(boolean statusAccount) throws RemoteException {
// ArrayList containing the results
        ArrayList<BeanTourist> toReturn;
// Retrieve data
        try {
// All tourists are taken to the state as a parameter
            toReturn = tourist.getTourist(statusAccount);
        }
// Exception in implementing the operation on the database
        catch (SQLException e) {
// If the data layer sends an exception is throws the exception remote
            System.out.println("Error in method getTourist (boolean)" + e.toString());
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }
// Unexpected exceptions caused by other factors
        catch (Exception ee) {
            System.out.println("Error in method getTourist (boolean)" + ee.toString());
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
// Check the data to return, so you do not return null values
        if (null == toReturn)
            throw new RemoteException(ErrorMessage.ERROR_FORMAT_BEAN);
        return toReturn;
    }

// Method that allows the activation of a tourist is not yet activated
    public boolean activeTourist(int pIdTourist) throws RemoteException {
// Check the validity of input data
        if (pIdTourist < 0)
            throw new RemoteException(ErrorMessage.ERROR_DATA);
// Bean that contains the data of tourists to activate
        BeanTourist toChange;
// Retrieve and edit data required
        try {
// Get the data from the Database
            toChange = tourist.getTourist(pIdTourist);
// Check that the tourist is not already activated
            if (toChange.isActive())
                throw new RemoteException(ErrorMessage.ERROR_DATA);
// It sets the value indicating the activation status to true
            toChange.setActive(true);
// You try to change the database
            if (tourist.modifyTourist(toChange))
// If the edit operation is successful returns
// True
                return true;
        }
// Exception in the execution of database operations
        catch (SQLException e) {
// If the data layer sends an exception is throws the exception remote
            System.out.println("Error in method activeTourist" + e.toString());
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }
// Unexpected exceptions caused by other factors
        catch (Exception ee) {
            System.out.println("Error in method activeTourist" + ee.toString());
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
// If no exceptions were thrown but the changes
// Not have been successful returns false
        return false;
    }

// Method that allows you to disable a tourist
    public boolean disactiveTourist(int pIdTourist) throws RemoteException {
// Check the validity of data
        if (pIdTourist < 0)
            throw new RemoteException(ErrorMessage.ERROR_DATA);
        BeanTourist toChange; // bean that contains the data from the tourist
// Disable
// Retrieve and edit data required
        try {
// Get the data from the database
            toChange = tourist.getTourist(pIdTourist);
// Check that the tourist is active
            if (!toChange.isActive())
                throw new RemoteException(ErrorMessage.ERROR_DATA);
// It sets the value indicating the activation status to false
            toChange.setActive(false);
// You try to change the database
            if (tourist.modifyTourist(toChange))
// If the edit operation is successful returns
// True
                return true;
        }
// Exception in the execution of database operations
        catch (SQLException e) {
// If the data layer sends an exception is throws the exception remote
            System.out.println("Error in method disactiveTourist" + e.toString());
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }
// Unexpected exceptions caused by other factors
        catch (Exception ee) {
            System.out.println("Error in method disactiveTourist" + ee.toString());
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
// If no exceptions were thrown but the changes
// Not have been successful returns false
        return false;
    }

// Method that erases a tourist from the Database
    public boolean delete(int pIdTourist) throws RemoteException {
// Check the validity of data
        if (pIdTourist < 0)
            throw new RemoteException(ErrorMessage.ERROR_DATA);
        try {
// If the clearing is successful it returns true
            if (tourist.delete(pIdTourist))
                return true;
        } catch (SQLException e) {
// If the data layer sends an exception is throws the exception remote
            System.out.println("Error in delete method" + e.toString());
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }
// Unexpected exception caused by other factors
        catch (Exception ee) {
            System.out.println("Error in delete method" + ee.toString());
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
// Otherwise if you have not thrown exceptions, but the clearing
// Not have been successful returns false
        return false;
    }

// Method that returns an ArrayList containing the feedback issued by
// Some for the Cultural Tourist
    public ArrayList<BeanVisitBC> getFeedbackBC(int pIdTourist) throws RemoteException {
// Check the validity of data
        if (pIdTourist < 0)
            throw new RemoteException(ErrorMessage.ERROR_DATA);
// ArrayList return
        ArrayList<BeanVisitBC> toReturn;
        try {
// Retrieve data from Database
            toReturn = feedbackBC.getListVisitBCTourist(pIdTourist);
        } catch (SQLException e) {
// If the data layer sends an exception is throws the exception remote
            System.out.println("Error in method getFeedbackBC" + e.toString());
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }
// Unexpected exception caused by other factors
        catch (Exception ee) {
            System.out.println("Error in method getFeedbackBC" + ee.toString());
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
// Check the data back in order not to return null values
        if (null == toReturn)
            throw new RemoteException(ErrorMessage.ERROR_FORMAT_BEAN);
        return toReturn;
    }

// Method that returns an ArrayList containing the feedback issued by
// Some for the Cultural Tourist
    public ArrayList<BeanVisitPR> getFeedbackPR(int pIdTourist) throws RemoteException {
// Check the validity of data
        if (pIdTourist < 0)
            throw new RemoteException(ErrorMessage.ERROR_DATA);
        ArrayList<BeanVisitPR> toReturn; // variable return
        try {
// Insert the result in toReturn
// Returned from the Data Layer
            toReturn = feedbackPR.getListVisitPRTourist(pIdTourist);
        } catch (SQLException e) {
// If the data layer sends an exception is throws the exception remote
            System.out.println("Error in method getFeedbackPR" + e.toString());
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }
// Unexpected exceptions caused by other factors
        catch (Exception ee) {
            System.out.println("Error in method getFeedbackPR" + ee.toString());
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
// Check the array of return, so you do not return null values
        if (null == toReturn)
            throw new RemoteException(ErrorMessage.ERROR_FORMAT_BEAN);
        return toReturn;
    }
}
