package unisa.gps.etour.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Bean containing information relating to the feedback of a cultural
 *
 */
public class BeanVisitBC implements Serializable {
    private static final long serialVersionUID = 3331567128449243852L;
    private int rating;
    private int idCulturalHeritage;
    private String comment;
    private int IdTourist;
    private Date dataVisit;

/**
* Parameterized constructor
*
* @Param pRating
* @Param pIdCulturalHeritage
* @Param pComment
* @Param pIdTourist
* @Param pDataVisit
*/
public BeanVisitBC (int pRating , int pIdCulturalHeritage,
        String pComment , int pIdTourist, Date pDataVisit)
{
setRating (pRating);
setIdCulturalHeritage (pIdCulturalHeritage);
setComment (pComment);
setIdTourist (pIdTourist);
setDataVisit (pDataVisit);
}

    /**
     * Empty Constructor
     */
    public BeanVisitBC() {

    }

    /**
     * Returns the value of comment
     *
     * @Return value of comment.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the new value of comment
     *
     * @Param value New pComment comment.
     */
    public void setComment(String pComment) {
        comment = pComment;
    }

/**
* Returns the value of dataVisit
*
* @Return value dataVisit.
*/
public Date getDataVisit ()
{
    return dataVisit ;
}

    /**
     * Sets the new value of dataVisit
     *
     * @Param value pDataVisit New dataVisit.
     */
    public void setDataVisit(Date pDataVisit) {
        dataVisit = pDataVisit;
    }

/**
* Returns the value of idCulturalHeritage
*
* @Return value idCulturalHeritage.
*/
public int getIdCulturalHeritage ()
{
    return idCulturalHeritage ;
}

    /**
     * Sets the new value of idCulturalHeritage
     *
     * @Param value pIdCulturalHeritage New idCulturalHeritage.
     */
    public void setIdCulturalHeritage(int pIdCulturalHeritage) {
        idCulturalHeritage = pIdCulturalHeritage;
    }

/**
* Returns the value of IdTourist
*
* @Return value IdTourist.
*/
public int getIdTourist ()
{
    return IdTourist ;
}

    /**
     * Sets the new value of IdTourist
     *
     * @Param value pIdTourist New IdTourist.
     */
    public void setIdTourist(int pIdTourist) {
        IdTourist = pIdTourist;
    }

    /**
     * Returns the value of voting
     *
     * @Return value of vote.
     */
    public int getRating() {
        return rating;
    }

    /**
     * Sets the new value of voting
     *
     * New value * @param pRating to vote.
     */
    public void setRating(int pRating) {
        rating = pRating;
    }

}
