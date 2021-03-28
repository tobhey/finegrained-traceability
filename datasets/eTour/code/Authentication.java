package unisa.gps.etour.control.RegisteredUserManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

import unisa.gps.etour.bean.BeanTourist;
import unisa.gps.etour.repository.DBTourist;
import unisa.gps.etour.util.ControlData;
import unisa.gps.etour.util.ErrorMessage;

public class Authentication extends UnicastRemoteObject implements IAuthentication {

    private static final long serialVersionUID = 0L;

    public Authentication() throws RemoteException {
        super();
    }

    // Objects to manipulate data Tourist
    private DBTourist tourist = new DBTourist();
    private BeanTourist bTourist;

    public int login(String pUsername, String pPassword, byte pUsertype) throws RemoteException {
        // Check if the String username and password
        if(ControlData.checkString(pUsername, true, true, "_-", null, 6, 12) 
                && ControlData.checkString(pPassword, true, true, "_-", null, 5, 12)) {
            try {
                switch (pUsertype) {
                // If the type is Tourist
                case VISITORS:
                    // Invoke the method to obtain the Bean del Tourist
                    // Given the username
                    bTourist = tourist.getTourist(pUsername);
                    // Check that the Bean is not null and
                    // Passwords match
                    if (bTourist != null && bTourist.getPassword().equals(pPassword)) {
                        return bTourist.getId();
                    }
                // If the type and eateries
                case OP_PUNTO_DI_RISTORO:
                    // Not implemented was the operational point of
                    // Refreshment
                    return -1;
                // If not match any known type
                default:
                    return -1;
                }
            } catch(SQLException e) {
                throw new RemoteException(ErrorMessage.ERROR_DBMS);
            } catch (Exception e) {
                throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
            }
         
        }
     // If the data are incorrect returns -1
        return -1;
    }
}