package unisa.gps.etour.repository;

import java.sql.SQLException;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanNews;

/**
 * Interface for the management of news in the Database
 *
 */
public interface IDBNews {
    /**
     * Add a news database
     *
     * @Param pNews News to add
     */
    public boolean insertNews(BeanNews pNews) throws SQLException;

    /**
     * Modify a news database
     *
     * @Param pNews News to change with the new data
     * @Return True if there 'was a modified false otherwise
     */
    public boolean modifyNews(BeanNews pNews) throws SQLException;

    /**
     * Delete a database from news
     *
     * @Param ID pIdNews News to eliminate
     * @Return True if and 'have been deleted false otherwise
     */
    public boolean clearNews(int pIdNews) throws SQLException;

    /**
     * Returns the active news
     *
     * @Return list of active news
     */
    public ArrayList<BeanNews> getNews() throws SQLException;
}