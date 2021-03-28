package unisa.gps.etour.control.CulturalHeritageManager;

import unisa.gps.etour.bean.BeanVisitBC;

/**
 * This class has the task of monitoring data from a cultural visit. Various
 * consistency checks are performed, such as length of strings, Null reference,
 * dynamic types incorrect.
 *
 */
public class CulturalHeritageVisitChecker {
    /**
     *
     * Please consistency check by calling the appropriate methods
     *
     * @Param bean The pVBC cultural visit to check
     * @Return boolean The result of the check: true if OK, false otherwise
     */
    public static boolean checkDataVisitCulturalHeritage(BeanVisitBC pVBC) {
// If the bean is null
        if (pVBC == null || !(pVBC instanceof BeanVisitBC))
            return false;

// Check the ID of a cultural visit
// And the id of its tourist
        if (!(pVBC.getIdCulturalHeritage() > 0) || !(pVBC.getIdTourist() > 0))
            return false;

// Check the vote (of course ratings are accepted only between 1 and 5
        if (!(pVBC.getRating() >= 1 && pVBC.getRating() <= 5))
            return false;

// Check for null references in the bean
        if (!checkDataNull(pVBC))
            return false;

// Check the correct length of String
        if (!(pVBC.getComment().length() > 0))
            return false;

        return true;
    }

    /**
     *
     * Check for null data in a bean cultural visit
     *
     * @Param bean The PBC cultural visit to check
     * @Return boolean The result of the check: true if there are no null
     *         references, false otherwise
     */
    public static boolean checkDataNull(BeanVisitBC pBC) {
// Check the nullity of the fields of feedback
        if (pBC.getComment() == null || pBC.getDataVisit() == null)
            return false;

        return true;
    }
}