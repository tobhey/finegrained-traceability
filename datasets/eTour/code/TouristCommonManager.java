package unisa.gps.etour.control.RegisteredUserManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

import unisa.gps.etour.bean.BeanTourist;
import unisa.gps.etour.repository.DBTourist;
import unisa.gps.etour.repository.IDBTourist;
import unisa.gps.etour.util.ErrorMessage;

/**
 * Class that implements the common tasks for Operators and Tourist Agency Ie
 * modifyTourist and getTourist
 */
public class TouristCommonManager extends UnicastRemoteObject implements ITouristCommonManager {

    protected IDBTourist tourist;

// Constructor that  turn the class constructor
// UnicastRemoteObject to connect via RMI
// Instantiate and connect to the database
    public TouristCommonManager() throws RemoteException {
        super();
// Connect to the Database
        try {
            tourist = new DBTourist();
        }
// Exception in the database connection
        catch (Exception e) {
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
    }

// Method that allows you to change the data of a tourist through its
// Data
    public boolean modifyTourist(BeanTourist pProfileTourist) throws RemoteException {
// Check the validity of past data
        if ((pProfileTourist == null) || (!(pProfileTourist instanceof BeanTourist)))
            throw new RemoteException(ErrorMessage.ERROR_FORMAT_BEAN);
// Execution of the amendment
        try {
// If the changes were made returns true
            if (tourist.modifyTourist(pProfileTourist))
                return true;
        }
// Exception in operations on database
        catch (SQLException e) {
// If the data layer sends an exception is throws the exception remote
            System.out.println("Error in method modifyTourist" + e.toString());
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }
// Unexpected exception caused by other factors
        catch (Exception ee) {
            System.out.println("Error in method modifyTourist" + ee.toString());
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
// If there were no exceptions but the changes are not
// Returns false were made
        return false;
    }

// Method to obtain the bean with data from the Tourist
// Identified by
// The parameter passed
    public BeanTourist getTourist(int pIdTourist) throws RemoteException {
// Check the validity identifier
        if (pIdTourist < 0)
            throw new RemoteException(ErrorMessage.ERROR_DATA);
        BeanTourist toReturn = null; // variable return
// Retrieve data
        try {
// Are requested to return the bean layer on the tourist
// With id equal to pIdTourist
            toReturn = tourist.getTourist(pIdTourist);
            if (null == toReturn)
                throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }
// Exception in database operations
        catch (SQLException e) {
// If the data layer sends an exception is throws the exception remote
            System.out.println("Error in method getTourist" + e.toString());
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }
// Unexpected exceptions caused by other factors
        catch (Exception ee) {
            System.out.println("Error in method getTourist" + ee.toString());
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
// Return the result
        return toReturn;
    }
}