package unisa.gps.etour.bean;

/**
  * Bean containing information relating to the News
  *
*/

import java.io.Serializable;
import java.util.Date;

public class BeanNews implements Serializable {
    private String news;
    private Date dataPublication;
    private Date dataDeadline;
    private int priority;
    private int id;
    private static final long serialVersionUID = -6249491056436689386L;

/**
* Parameterized constructor
*
* @Param pNews
* @Param pDataPublication
* @Param pDataDeadline
* @Param pPriority
* @Param pId
*/
public BeanNews (String pNews, Date pDataPublication, Date pDataDeadline,
        int pPriority , int pId)
{
setNews (pNews);
setDataPublication (pDataPublication);
setDataDeadline (pDataDeadline);
setPriority (pPriority);
setId (pId);
}

    /**
     * Empty Constructor
     *
     */
    public BeanNews() {

    }

    /**
     * Returns the value of dataPublication
     *
     * @Return value dataPublication.
     */
    public Date getDataPublication() {
        return dataPublication;
    }

    /**
     * Sets the new value of dataPublication
     *
     * @Param value pDataPublication New dataPublication.
     */
    public void setDataPublication(Date pDataPublication) {
        dataPublication = pDataPublication;
    }

    /**
     * Returns the value of dataDeadline
     *
     * @Return value dataDeadline.
     */
    public Date getDataDeadline() {
        return dataDeadline;
    }

    /**
     * Sets the new value of dataDeadline
     *
     * @Param value pDataDeadline New dataDeadline.
     */
    public void setDataDeadline(Date pDataDeadline) {
        dataDeadline = pDataDeadline;
    }

    /**
     * Returns the value of news
     *
     * @Return value of news.
     */
    public String getNews() {
        return news;
    }

    /**
     * Sets the new value of news
     *
     * @Param value New pNews news.
     */
    public void setNews(String pNews) {
        news = pNews;
    }

    /**
     * Returns the priority value
     *
     * @Return the priority value.
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Set the new priority value
     *
     * @Param pPriority New priority value.
     */
    public void setPriority(int pPriority) {
        priority = pPriority;
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
     * Sets the new value of id
     *
     * @Param pId New value for id.
     */
    public void setId(int pId) {
        id = pId;
    }

}