package unisa.gps.etour.repository;

import java.sql.SQLException;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanMenu;

/**
 * Interface for managing the menu in the database
 *
 */
public interface IDBMenu {
    /**
     * Adds a menu in the database
     *
     * @Param pMenu menu to add
     */
    public boolean insertMenu(BeanMenu pMenu) throws SQLException;

    /**
     * Modify a menu in the database
     *
     * @Param pMenu Contains the data to change
     * @Return True if there 'was a modified false otherwise
     */
    public boolean modifyMenu(BeanMenu pMenu) throws SQLException;

    /**
     * Delete a menu from database
     *
     * @Param ID pIdMenu menu to delete
     * @Return True if and 'was deleted false otherwise
     */
    public boolean clearMenu(int pIdMenu) throws SQLException;

    /**
     * Returns the menu of the day of a refreshment
     *
     * @Param pIdRefreshmentPoint point identification Refreshments
     * @Param pDay Day of the week in which the menu Daily
     * @Return Day menu de Refreshment
     */
    public BeanMenu getMenuDelDay(int pIdRefreshmentPoint, String pDay) throws SQLException;

    /**
     * Returns a list of the menu of a refreshment
     *
     * @Param pIdRefreshmentPoint point identification Refreshment
     * @Return List of menus
     */
    public ArrayList<BeanMenu> getMenu(int pIdRefreshmentPoint) throws SQLException;
}