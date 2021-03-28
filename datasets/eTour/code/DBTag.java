package unisa.gps.etour.repository;

import java.sql.SQLException;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanTag;
import unisa.gps.etour.repository.IDBTag;

public class DBTag implements IDBTag {
    private ArrayList<BeanTag> b;

    public DBTag() {
        b = new ArrayList<BeanTag>(0);
    }

    public boolean addTagCulturalHeritage(int pIdCulturalHeritage, int pIdTag) throws SQLException {
        return true;
    }

    public boolean addTagRefreshmentPoint(int pIdRefreshmentPoint, int pIdTag) throws SQLException {
        return true;
    }

    public boolean clearTag(int pIdTag) throws SQLException {
        return true;
    }

    public boolean clearTagCulturalHeritage(int pIdCulturalHeritage, int pIdTag) throws SQLException {
        return true;
    }

    public boolean clearTagRefreshmentPoint(int pIdRefreshmentPoint, int pIdTag) throws SQLException {
        return true;
    }

    public boolean insertTag(BeanTag ptagi) throws SQLException {
        return true;
    }

    public boolean modifyTag(BeanTag ptagi) throws SQLException {
        return false;
    }

    public ArrayList<BeanTag> getListTag() throws SQLException {
        return null;
    }

    public BeanTag getTag(int pid) throws SQLException {
        return null;
    }

    public ArrayList<BeanTag> getTagCulturalHeritage(int pIdCulturalHeritage) throws SQLException {
        return null;
    }

    public ArrayList<BeanTag> getTagRefreshmentPoint(int pIdRefreshmentPoint) throws SQLException {
        return null;
    }

}