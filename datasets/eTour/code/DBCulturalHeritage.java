package unisa.gps.etour.repository;

import java.sql.SQLException;
import java.util.ArrayList;
import unisa.gps.etour.bean.BeanCulturalHeritage;
import unisa.gps.etour.bean.BeanTag;
import unisa.gps.etour.repository.IDBCulturalHeritage;
import unisa.gps.etour.util.Point3D;

public class DBCulturalHeritage implements IDBCulturalHeritage {
    private ArrayList<BeanCulturalHeritage> b;

    public DBCulturalHeritage() {
        b = new ArrayList<BeanCulturalHeritage>(0);
    }

    public boolean clearCulturalHeritage(int pIdBene) throws SQLException {
        boolean res = false;

        for (int i = 0; i < b.size(); i++)
            if (b.get(i).getId() == pIdBene) {
                b.remove(i);
                res = true;
            }

        return res;
    }

    public boolean insertCulturalHeritage(BeanCulturalHeritage pBene) throws SQLException {
        return (b.add(pBene));
    }

    public boolean modifyCulturalHeritage(BeanCulturalHeritage pBene) throws SQLException {
        boolean res = false;

        for (int i = 0; i < b.size(); i++)
            if (b.get(i).getId() == pBene.getId()) {
                b.set(i, pBene);
                return true;
            }

        return res;
    }

    public BeanCulturalHeritage getCulturalHeritage(int pid) throws SQLException {
        BeanCulturalHeritage res = null;

        for (int i = 0; i < b.size(); i++)
            if (b.get(i).getId() == pid)
                res = b.get(i);

        return res;
    }

    public ArrayList<BeanCulturalHeritage> getListBC() throws SQLException {
        return b;
    }

    public int getSearchResultNumber(String pKeyword, ArrayList<BeanTag> pTags, Point3D pPosition,
            double pMaxDistance) throws SQLException {
        return 0;
    }

    public int getSearchResultNumberAdvanced(int pIdTourist, String PKeyword, ArrayList<BeanTag> pTags,
            Point3D pPosition, double pMaxDistance) throws SQLException {
        return 0;
    }

    public ArrayList<BeanCulturalHeritage> search(String pKeyword, ArrayList<BeanTag> pTags, int pNumPage,
            int pNumberElementsPerPage, Point3D pPosition, double pMaxDistance) throws SQLException {
        return null;
    }

    public ArrayList<BeanCulturalHeritage> searchAdvanced(int pIdTourist, String PKeyword, ArrayList<BeanTag> pTags,
            int pNumPage, int pNumberElementsPerPage, Point3D pPosition, double pMaxDistance) throws SQLException {
        return null;
    }
}