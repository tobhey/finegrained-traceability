package unisa.gps.etour.control.AdvertisementManager;

import java.rmi.RemoteException;

/**
 * Class that implements management services of the advertisement For the operator
 * eating place.
 *
 */
public class AdvertisementRefreshmentPointManager extends AdvertisementManager
        implements IAdvertisementRefreshmentPointManager {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor, call the constructor of the superclass.
     * 
     * @Throws RemoteException
     */
    public AdvertisementRefreshmentPointManager() throws RemoteException {
        super();
    }

}
