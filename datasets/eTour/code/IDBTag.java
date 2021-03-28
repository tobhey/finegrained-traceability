package unisa.gps.etour.repository;

import java.sql.SQLException;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanTag;

/**
 * Interface for managing the database Tag
 *
 */
public interface IDBTag {
    /**
     * Add a tag
     *
     * @Param pTag Tag to add
     * @Throws SQLException
     */
    public boolean insertTag(BeanTag pTag) throws SQLException;

    /**
     * Modify the data in a tag
     *
     * @Param pTag Tag to modify
     * @Return True if and 'been changed otherwise false
     */
    public boolean modifyTag(BeanTag pTag) throws SQLException;

    /**
     * Delete a tag from the database
     *
     * @Param pIdTag ID Tag to be deleted
     * @Return True if and 'was deleted false otherwise
     */
    public boolean clearTag(int pIdTag) throws SQLException;

    /**
     * Returns the list of tags in the database
     *
     * @Return List containing the tags
     */
    public ArrayList<BeanTag> getListTag() throws SQLException;

    /**
     * Returns a single tag
     *
     * @Param pId ID tag
     * @Return Tags
     */
    public BeanTag getTag(int pId) throws SQLException;

    /**
     * Tag with immovable cultural
     *
     * @Param ID pIdCulturalHeritage of Cultural Heritage
     * @Param pIdTag ID tag
     */
    public boolean addTagCulturalHeritage(int pIdCulturalHeritage, int pIdTag) throws SQLException;

    /**
     * Tag to a refreshment
     *
     * @Param pIdRefreshmentPoint point identification Refreshments
     * @Param pIdTag ID tag
     */
    public boolean addTagRefreshmentPoint(int pIdRefreshmentPoint, int pIdTag) throws SQLException;

    /**
     * Returns the list of tags of a cultural
     *
     * @Param ID pIdCulturalHeritage of Cultural Heritage
     * @Return list of tags
     */
    public ArrayList<BeanTag> getTagCulturalHeritage(int pIdCulturalHeritage) throws SQLException;

    /**
     * Returns a list of tags of a refreshment
     *
     * @Param pIdRefreshmentPoint point identification Refreshments
     * @Return list of tags
     */
    public ArrayList<BeanTag> getTagRefreshmentPoint(int pIdRefreshmentPoint) throws SQLException;

    /**
     * Delete a tag to a cultural
     *
     * @Param ID pIdCulturalHeritage of Cultural Heritage
     * @Param pIdTag ID tag
     * @Return True if and 'was deleted false otherwise
     */
    public boolean clearTagCulturalHeritage(int pIdCulturalHeritage, int pIdTag) throws SQLException;

    /**
     * Delete a tag to a refreshment
     *
     * @Param pIdRefreshmentPoint ID
     * @Param pIdTag ID tag
     * @Return True if and 'was deleted false otherwise
     */
    public boolean clearTagRefreshmentPoint(int pIdRefreshmentPoint, int pIdTag) throws SQLException;

}