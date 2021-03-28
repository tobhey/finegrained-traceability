package unisa.gps.etour.control.TagManager;

import java.rmi.RemoteException;
import java.sql.SQLException;
import unisa.gps.etour.bean.BeanTag;
import unisa.gps.etour.util.ErrorMessage;

/**
 * Class that implements the methods for the functionality of the Operator
 * Agency Extending the class of common Tag Management
 */
public class TagAgencyOperatorManager extends TagCommonManager implements ITagAgencyOperatorManager {

    private static final long serialVersionUID = 1L;

    public TagAgencyOperatorManager() throws RemoteException {
// Invoke the constructor of the superclass for communication with
// Database
        super();
    }

// Method to delete from database the tag whose ID is passed
// As parameter
    public boolean clearTag(int pTagID) throws RemoteException {
// Check the valise of past data
        if ((pTagID <= 0))
            throw new RemoteException(ErrorMessage.ERROR_DATA);
        try {
// Make the database operation
            tags.clearTag(pTagID);
            return true;
        }
// Exception in the execution of database operations
        catch (SQLException e) {
            System.out.println("Error in method clearTag:" + e.toString());
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }
// Unexpected exceptions caused by other factors
        catch (Exception ee) {
            System.out.println("Error in method clearTag" + ee.toString());
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
    }

// Method that allows the insertion of a new tag as a parameter
    public boolean insertTag(BeanTag pTagNew) throws RemoteException {
// Check the validity of the Bean and the data contained within
        if (null == pTagNew)
            throw new RemoteException(ErrorMessage.ERROR_FORMAT_BEAN);
        if ((pTagNew = checkTag(pTagNew)) == null)
            throw new RemoteException(ErrorMessage.ERROR_FORMAT_BEAN);
        try {
// Execute the operation on the Database
            tags.insertTag(pTagNew);
            return true;
        }
// Exception running the operation on Database
        catch (SQLException e) {
            System.out.println("Error in method insertTag" + e.toString());
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }
// Unexpected exceptions caused by other factors
        catch (Exception ee) {
            System.out.println("Error in method insertTag" + ee.toString());
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
    }

// Method that enables the modifytion of a tag that is passed as
// Parameter
    public boolean modifyTag(BeanTag pTagChanged) throws RemoteException {
// Check the validity of data
        if ((pTagChanged = checkTag(pTagChanged)) == null)
            throw new RemoteException(ErrorMessage.ERROR_FORMAT_BEAN);
        try {
// Execute the operation on the Database
            tags.modifyTag(pTagChanged);
            return true;
        }
// Exception running the operation on Database
        catch (SQLException e) {
            System.out.println("Error in method modifyTag:" + e.toString());
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }
// Unexpected exceptions caused by other factors
        catch (Exception ee) {
            System.out.println("Error in method modifyTag" + ee.toString());
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
    }

// Method to obtain the tags whose identifier is passed
// As parameter
    public BeanTag getTag(int pTagID) throws RemoteException {
// Check the validity of data
        if (pTagID <= 0)
            throw new RemoteException(ErrorMessage.ERROR_DATA);
// Bean to return
        BeanTag toReturn;
        try {
// Execute the operation on the Database
            toReturn = tags.getTag(pTagID);
        }
// Exception running the operation on Database
        catch (SQLException e) {
            System.out.println("Error in method getTag:" + e.toString());
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }
// Unexpected exceptions caused by other factors
        catch (Exception ee) {
            System.out.println("Error in method getTag:" + ee.toString());
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
        if (null == toReturn)
            throw new RemoteException(ErrorMessage.ERROR_FORMAT_BEAN);
        return toReturn;
    }

// Method that controls all the attributes of a BeanTag
    private BeanTag checkTag(BeanTag toControl) {
// Check the ID
        if (toControl.getId() <= 0)
            return null;
// Check the description
        if (toControl.getDescription().equals(""))
            toControl.setDescription("***");
// Check the name
        if (toControl.getName().equals(""))
            return null;
// Check that the name does not contain a space
        if (toControl.getName().contains(""))
            return null;
        return toControl;
    }
}
