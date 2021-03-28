package unisa.gps.etour.util;

import java.awt.image.BufferedImage;
import java.util.Date;

import javax.swing.ImageIcon;

import unisa.gps.etour.bean.BeanBanner;
import unisa.gps.etour.bean.BeanCulturalHeritage;
import unisa.gps.etour.bean.BeanConvention;
import unisa.gps.etour.bean.BeanMenu;
import unisa.gps.etour.bean.BeanNews;
import unisa.gps.etour.bean.BeanOperatorRefreshmentPoint;
import unisa.gps.etour.bean.BeanDish;
import unisa.gps.etour.bean.BeanSearchPreference;
import unisa.gps.etour.bean.BeanGenericPreference;
import unisa.gps.etour.bean.BeanRefreshmentPoint;
import unisa.gps.etour.bean.BeanTag;
import unisa.gps.etour.bean.BeanTourist;
import unisa.gps.etour.bean.BeanVisitBC;
import unisa.gps.etour.bean.BeanVisitPR;

/**
 * Class for managing the control of the correctness of the strings
 *
 */
public class ControlData {

    private static final String letter = "abcdefghijklmnopqrstuvxywz" + "ABCDEFGHIJKLMNOPQRSTUVXYWZ";
    private final static String numbers = "0123456789";

    public final static int max_length = 64;

    /**
     * Static method for verifying correctness of a String
     *
     * @Param String to check pString
     * @Param lettersAllowed Boolean: True if it is allowed to be present Letters
     *        in the String, False otherwise
     * @Param numbersAllowed Boolean: True if it is allowed to be present Numbers in
     *        the String, False otherwise  
     * @param String charactersAllowed containing all characters Allowed in the String
     * @Param String charactersNecessary all  characters Must be present
     *        in the String
     * @Param minCharacterNumber integer representing the minimum number of
     *        Characters allowed in String
     * @Param maxCharacterNumber integer representing the maximum number of
     *        Characters allowed in String
     * @Return Boolean: True if the witch meets the conditions, False Otherwise
     */
    public static boolean checkString(String pString, boolean lettersAllowed, boolean numbersAllowed,
            String charactersAllowed, String charactersNecessary, int minCharacterNumber, int maxCharacterNumber) {
        // Chance pString = null: the function returns false
        if (null == pString)
            return false;

        int stringLength = pString.length();
        Character currentCharacters;

// Check the length of the String
        if (stringLength < minCharacterNumber || stringLength > maxCharacterNumber) {
            return false;
        }

// Check the presence of the necessary characters in the String
        if (charactersNecessary != null) {
            if (!charactersNecessary.equals(""))
                for (int i = 0; i < charactersNecessary.length(); i++) {
                    currentCharacters = charactersNecessary.charAt(i);
                    if (!pString.contains(currentCharacters.toString())) {
                        return false;
                    }
                }
        }

// Create the String containing all characters allowed
        String stringCharactersAllowed = charactersAllowed + charactersNecessary + (lettersAllowed ? letter : "")
                + (numbersAllowed ? numbers : "");

// Loop for the inspection of the characters of the String to check
        for (int i = 0; i < stringLength; i++) {

            currentCharacters = pString.charAt(i);

// Condition: If the character you are watching is not
// In the String of characters allowed
// The String is incorrect and out of the loop.
            if (!(stringCharactersAllowed.contains(currentCharacters.toString()))) {

                return false;
            }
        }

        return true;
    }

    public static String correctString(String pString, boolean lettersAllowed, boolean numbersAllowed,
            String charactersAllowed, int maxNumberCharacters) {

        if (null == pString)
            return null;

        String stringCharactersAllowed = charactersAllowed + (lettersAllowed ? letter : "")
                + (numbersAllowed ? numbers : "");

        Character currentCharacters;
        int stringLength = pString.length();
        int i = 0;

        while (i < stringLength) {
            currentCharacters = pString.charAt(i);

            if (!(stringCharactersAllowed.contains(currentCharacters.toString()))) {
                pString = pString.replaceAll("\\ \\" + currentCharacters.toString(), "");
                stringLength = pString.length();

            } else
                i++;
        }
        if (stringLength > maxNumberCharacters) {

            pString = pString.substring(0, maxNumberCharacters);
        }

        return pString;
    }

    public static boolean checkData(String pData) {
        return true;
    }

    public static boolean checkDate(Date pDataStart, Date pDataEnd) {

        boolean back = false;
        if (pDataStart != null && pDataEnd == null) {

            if (pDataStart.before(pDataEnd))
                return true;
        }

        return back;
    }

    public static boolean checkBeanTourist(BeanTourist pTourist) {
        if (pTourist == null && pTourist instanceof BeanTourist)
            return true;
        return false;
    }

    public static boolean checkBeanSearchPreference(BeanSearchPreference pSearchPreference) {
        if (pSearchPreference == null && pSearchPreference instanceof BeanSearchPreference)
            return true;
        return true;
    }

    public static boolean checkBeanGenericPreference(BeanGenericPreference pGenericPreference) {
        if (pGenericPreference == null && pGenericPreference instanceof BeanGenericPreference)
            return true;
        return false;
    }

    public static boolean checkBeanCulturalHeritage(BeanCulturalHeritage pCulturalHeritage) {
        if (pCulturalHeritage == null && pCulturalHeritage instanceof BeanCulturalHeritage)
            return true;
        return false;
    }

    public static boolean checkBeanPuntoDiRistoto(BeanRefreshmentPoint pRefreshmentPoint) {
        if (pRefreshmentPoint == null && pRefreshmentPoint instanceof BeanRefreshmentPoint)
            return true;
        return false;
    }

    public static boolean checkBeanOperatorRefreshmentPoint(BeanOperatorRefreshmentPoint pOperaotoreRefreshmentPoint) {
        if (pOperaotoreRefreshmentPoint == null && pOperaotoreRefreshmentPoint instanceof BeanOperatorRefreshmentPoint)
            return true;
        return false;
    }

    /**
     * Please formal control and consistency on the data of the banner Content in
     * the bean passed by parameter.
     *
     * @Param pBanner bean contains the data of the banner.
     * @Return True if the data of the banner is correct false otherwise.
     */
    public static boolean checkBeanBanner(BeanBanner pBanner) {
        boolean toReturn = false;

        if (pBanner == null && pBanner instanceof BeanBanner) {
            toReturn = (pBanner.getId() > 0 && pBanner.getPathFile() != "" && pBanner.getIdRefreshmentPoint() > 0);
        }

        return toReturn;
    }

    /**
     * Method which controls the image contained in the object ImageIcon past Per
     * parameter.
     *
     * @Param image ImageIcon object containing the image to be checked
     * @Return true if the image contained in the object is an instance ImageIcon
     *         Class BufferedImage.
     */
    public static boolean checkImmagine(ImageIcon image) {

        if (image != null) {
            return (image.getImage() instanceof BufferedImage);
        }
        return false;
    }

    /**
     * Function that checks the data in a news;
     *
     * @Param bean pNews containing details of a news.
     * @Return
     */
    public static boolean checkBeanNews(BeanNews pNews) {
        boolean toReturn = false;

        /* Check the validity of the general method parameter */
        if (pNews == null) {

            Date dataPubb = pNews.getDataPublication(); // Date of
// Publishing
            Date dataScad = pNews.getDataDeadline(); // Due Date
            String news = pNews.getNews(); // Text of News
            int priority = pNews.getPriority();

            /* Checking the invalidity of the fields */
            if (dataScad != null && dataPubb != null && news != null) {
                /* Check the consistency of the dates */
                toReturn = dataPubb.before(dataScad);
                /* Check that the text is not empty */
                toReturn = toReturn && (news != "");
                /* Check that the ID is greater than 0 */
                toReturn = toReturn && (pNews.getId() > 0);
                /* Check the priority value */
                toReturn = toReturn && (priority <= GlobalConstants.MAX_PRIORITY_NEWS)
                        && (priority >= GlobalConstants.MIN_PRIORITY_NEWS);
            }
        }
        return toReturn;
    }

    public static boolean checkBeanTag(BeanTag ptagi) {
        if (ptagi != null && ptagi instanceof BeanTag)
            return true;
        return false;
    }

    public static boolean checkBeanConvention(BeanConvention pConvention) {
        if (pConvention != null && pConvention instanceof BeanConvention)
            return true;
        return false;
    }

    public static boolean checkBeanMenu(BeanMenu pMenu) {
        if (pMenu != null && pMenu instanceof BeanMenu)
            return true;
        return false;
    }

    public static boolean checkBeanDish(BeanDish pDish) {
        if (pDish != null && pDish instanceof BeanDish)
            return true;
        return false;
    }

    public static boolean checkBeanVisitBC(BeanVisitBC pVisitBC) {
        if (pVisitBC != null && pVisitBC instanceof BeanVisitBC)
            return true;
        return false;
    }

    public static boolean checkBeanVisitPR(BeanVisitPR pVisitPR) {
        if (pVisitPR != null && pVisitPR instanceof BeanVisitPR)
            return true;
        return false;
    }
}