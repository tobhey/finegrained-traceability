package unisa.gps.etour.control.TagManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import unisa.gps.etour.bean.BeanTag;
import unisa.gps.etour.repository.DBTag;
import unisa.gps.etour.repository.IDBTag;
import unisa.gps.etour.util.ErrorMessage;

/**
 * Class that implements the common tasks for the use of tags
 *
 */
public class TagCommonManager extends UnicastRemoteObject implements ITagCommonManager {

    private static final long serialVersionUID = 1L;
// Object for the database connection
    protected IDBTag tags;

    public TagCommonManager() throws RemoteException {
        super();
// Connect to the Database
        try {
            tags = new DBTag();
        } catch (Exception e) {
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
    }

// Method that returns all tags
    public ArrayList<BeanTag> getTags() throws RemoteException {
// ArrayList to fill with the tags to return
        ArrayList<BeanTag> toReturn;
// Retrieve data from Database
        try {
// Get the information from the Database
            toReturn = tags.getListTag();
        }
// Exception in the execution of database operations
        catch (SQLException e) {
            System.out.println("Error in method getTags:" + e.toString());
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }
// Unexpected exceptions caused by other factors
        catch (Exception ee) {
            System.out.println("Error in method getTags" + ee.toString());
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
// Check the data back in order not to return null values
// Caller
        if (null == toReturn)
            throw new RemoteException(ErrorMessage.ERROR_FORMAT_BEAN);
        return toReturn;
    }
}
