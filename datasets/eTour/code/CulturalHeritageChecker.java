package unisa.gps.etour.control.CulturalHeritageManager;

import java.util.Date;
import unisa.gps.etour.bean.BeanCulturalHeritage;

/**
 * This class has the task of checking the data of a cultural object. Various
 * consistency checks are performed, such as length of strings, Null reference,
 * dynamic types incorrect.
 *
 */
public class CulturalHeritageChecker {
    /**
     *
     * Please consistency check by calling the appropriate methods. This method
     * wrappa "all other methods of control with a single call. At the first false
     * value the flow is interrupted.
     *
     * @Param bean The PBC of the cultural object to be inspected @Return boolean
     * The result of the check: true if OK, false otherwise
     */
    public static boolean checkDataCulturalHeritage(BeanCulturalHeritage pBC) {
// This method checks the input parameter in the cases
// Null reference or dynamic Wrong
        if (pBC == null || !(pBC instanceof BeanCulturalHeritage))
            return false;

// This method checks if the ID passed as a parameter BeanCulturalHeritage
// Is valid or not
        if (!checkIdCulturalHeritage(pBC.getId()))
            return false;

// This method checks the objects contained in BeanCulturalHeritage Date
// As such, a check is made null and reference checks
// On the dynamic.
        if (!checkDateCulturalHeritage(pBC))
            return false;

// This method checks all the fields in BeanCulturalHeritage, research
// Any null references
        if (!checkDataNull(pBC))
            return false;

// Check the correct length of string, in this case the CAP must
// To force of circumstances than five digits, while the province of two.
        if (pBC.getCap().length() != 5 || pBC.getProvince().length() != 2)
            return false;

        return true;
    }

    /**
     *
     * Check for null data in a bean cultural property. The check is performed on
     * all fields of the bean.
     *
     * @Param bean The PBC cultural property to be checked 
     * @Return boolean The result of the check: true if there are no null references, false otherwise
     */
    public static boolean checkDataNull(BeanCulturalHeritage pBC) {
        if (pBC.getCap() == null || pBC.getCity() == null || pBC.getDescription() == null
                || pBC.getClosingDay() == null || pBC.getLocation() == null || pBC.getName() == null
                || pBC.getOpeningTime() == null || pBC.getClosingTime() == null || pBC.getProvince() == null
                || pBC.getPhone() == null || pBC.getStreet() == null)
            return false;

        return true;
    }

    /**
     *
     * Check the consistency of dates within this BeanCulturalHeritage. The check is
     * performed only on objects, while not carried out No validity check 'on a date
     * as a cultural object may also have Dates later than today (see for example on
     * open exhibitions).
     *
     * @Param The PBC BeanCulturalHeritage which check the dates 
     * @Return boolean The result of the check: true if the dates have consistency; false otherwise
     */
    public static boolean checkDateCulturalHeritage(BeanCulturalHeritage pBC) {
        if (pBC.getOpeningTime() == null || !(pBC.getOpeningTime() instanceof Date)
                || pBC.getClosingTime() == null || !(pBC.getClosingTime() instanceof Date))
            return false;

        return true;
    }

    /**
     *
     * Check the ID of BeanCulturalHeritage
     *
     * @Param pId Id BeanCulturalHeritage be checked 
     * @Return boolean The result of the check: true if the ID is correct, false otherwise
     */
    public static boolean checkIdCulturalHeritage(int pid) {
        return (pid > 0);
    }
}