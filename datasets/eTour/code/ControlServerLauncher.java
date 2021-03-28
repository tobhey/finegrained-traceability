package unisa.gps.etour.control;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import unisa.gps.etour.control.CulturalHeritageManager.CulturalHeritageCommonManager;
import unisa.gps.etour.control.CulturalHeritageManager.ICulturalHeritageCommonManager;

/**
 * This is the entry point of the control server. This class is responsible for
 * Make the deployment of services on the RMI registry, thus rendering the Some
 * services are available and usable.
 *
 */
public class ControlServerLauncher {
    /**
     * Entry point of ControlServer
     *
     * @Param args The command line parameters
     */
    public static void main(String[] args) {
//*** WARNING *** *** WARNING *** EXPERIMENTAL EXPERIMENTAL

        try {
// ManagerCulturalHeritageCommon is the class that implements the interface IManagerCulturalHeritageCommon,
// This interface on both the client, both the server
            CulturalHeritageCommonManager gBCC = new CulturalHeritageCommonManager();

// Here you create the stub for the registry, making it clear to the RMI system you are exporting the object on a gBCC
// Anonymous port
            ICulturalHeritageCommonManager stubGBCC = (ICulturalHeritageCommonManager) UnicastRemoteObject
                    .exportObject(gBCC, 0);

// There shall call the register (default is on localhost) and "bind" (alloy)
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("stubGBCC", null);
        } catch (Exception e) {
            System.out.println("Exception:" + e.getLocalizedMessage());
        }
    }
}
