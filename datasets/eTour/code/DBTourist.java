package unisa.gps.etour.repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import unisa.gps.etour.bean.BeanTourist;
import unisa.gps.etour.repository.IDBTourist;

public class DBTourist implements IDBTourist {

    public boolean clearCulturalHeritagePreference(int pIdTourist, int pIdCulturalHeritage) throws SQLException {
        return false;
    }

    public boolean clearRefreshmentPointPreference(int pIdTourist, int pIdRefreshmentPoint) throws SQLException {
        return false;
    }

    public boolean delete(int pIdTourist) throws SQLException {
        return false;
    }

    public boolean insertCulturalHeritagePreference(int pIdTourist, int pIdCulturalHeritage) throws SQLException {
        return false;
    }

    public boolean insertRefreshmentPointPreference(int pIdTourist, int pIdRefreshmentPoint) throws SQLException {
        return false;
    }

    public boolean insertTourist(BeanTourist pTourist) throws SQLException {
        return false;
    }

    public boolean modifyTourist(BeanTourist pTourist) throws SQLException {
        return false;
    }

    public BeanTourist getTourist(String pUsername) throws SQLException {
        return null;
    }

    public BeanTourist getTourist(int pIdTourist) throws SQLException {
        return (new BeanTourist(1, "username", "Astrubale", "Silberschatz", "Naples", "Naples", "0111111", "80100th",
                "Way of the systems, 1", "NA", "trapano@solitario.it", "passwordsolomia", new Date(), new Date(),
                true));
    }

    public ArrayList<BeanTourist> getTourists(String pUsernameTourist) throws SQLException {
        return null;
    }

    public ArrayList<BeanTourist> getTourist(boolean condition) throws SQLException {
        return null;
    }
}
