package unisa.gps.etour.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Bean that contains the data for feedback to a refreshment
 *
 */
public class BeanVisitPR implements Serializable {

    private static final long serialVersionUID = -4065240072283418782L;
    private int rating;
    private int idRefreshmentPoint;
    private String comment;
    private int IdTourist;
    private Date dataVisit;

    /**
     * Parameterized constructor
     *
     * @Param pRating
     * @Param pIdRefreshmentPoint
     * @Param pComment
     * @Param pIdTourist
     * @Param pDataVisit
     */
    public BeanVisitPR(int pRating, int pIdRefreshmentPoint, String pComment, int pIdTourist, Date pDataVisit) {
        setRating(pRating);
        setIdRefreshmentPoint(pIdRefreshmentPoint);
        setComment(pComment);
        setIdTourist(pIdTourist);
        setDataVisit(pDataVisit);
    }

    /**
     * Empty Constructor
     */
    public BeanVisitPR() {

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
    public Date getDataVisit() {
        return dataVisit;
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
     * Returns the value of idRefreshmentPoint
     *
     * @Return value idRefreshmentPoint.
     */
    public int getIdRefreshmentPoint() {
        return idRefreshmentPoint;
    }

    /**
     * Sets the new value of idRefreshmentPoint
     *
     * @Param value pIdRefreshmentPoint New idRefreshmentPoint.
     */
    public void setIdRefreshmentPoint(int pIdRefreshmentPoint) {
        idRefreshmentPoint = pIdRefreshmentPoint;
    }

    /**
     * Returns the value of IdTourist
     *
     * @Return value IdTourist.
     */
    public int getIdTourist() {
        return IdTourist;
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
