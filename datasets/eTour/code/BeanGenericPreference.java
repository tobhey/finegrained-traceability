package unisa.gps.etour.bean;

/**
  * Bean containing information relating to the General Preferences
  *
*/

import java.io.Serializable;

public class BeanGenericPreference implements Serializable {
    private static final long serialVersionUID = 6805656922951334071L;
    private int id;
    private int dimensioneFont;
    private String font;
    private String subject;
    private int idTourist;

    /**
     * Parameterized constructor
     *
     * @Param pId
     * @Param pDimensioneFont
     * @Param pFont
     * @Param pTheme
     * @Param pIdTourist
     */
    public BeanGenericPreference(int pId, int pDimensioneFont, String pFont, String pTheme, int pIdTourist) {
        setId(pId);
        setDimensioneFont(pDimensioneFont);
        setFont(pFont);
        setTheme(pTheme);
        setIdTourist(pIdTourist);
    }

    /**
     * Empty Constructor
     */
    public BeanGenericPreference() {

    }

    /**
     * Returns the value of dimensioneFont
     *
     * @Return value dimensioneFont.
     */
    public int getDimensioneFont() {
        return dimensioneFont;
    }

    /**
     * Sets the new value of dimensioneFont
     *
     * @Param value pDimensioneFont New dimensioneFont.
     */
    public void setDimensioneFont(int pDimensioneFont) {
        dimensioneFont = pDimensioneFont;
    }

    /**
     * Returns the value of font
     *
     * @Return Value of fonts.
     */
    public String getFont() {
        return font;
    }

    /**
     * Sets the new value of font
     *
     * New value * @param pFont font.
     */
    public void setFont(String pFont) {
        font = pFont;
    }

    /**
     * Returns the value of the subject
     *
     * @Return value issue.
     */
    public String getTheme() {
        return subject;
    }

    /**
     * Sets the new value of the subject
     *
     * @Param value New pTheme theme.
     */
    public void setTheme(String pTheme) {
        subject = pTheme;
    }

    /**
     * Returns the value of id
     *
     * @Return value id.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the value of usernameTourist
     *
     * @Return value usernameTourist.
     */
    public int getIdTourist() {
        return idTourist;
    }

    /**
     * Sets the new value of usernameTourist
     *
     * @Param value pIdTourist New usernameTourist.
     */
    public void setIdTourist(int pIdTourist) {
        idTourist = pIdTourist;
    }

    /**
     * Sets the new value of id
     *
     * @Param pId New value for id.
     */
    public void setId(int pId) {
        id = pId;
    }

}