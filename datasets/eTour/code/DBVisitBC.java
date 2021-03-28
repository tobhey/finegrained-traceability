package unisa.gps.etour.repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import unisa.gps.etour.bean.BeanVisitBC;
import unisa.gps.etour.bean.BeanVisitPR;
import unisa.gps.etour.repository.IDBVisitBC;

public class DBVisitBC implements IDBVisitBC {
    public boolean insertVisitBC(BeanVisitBC PVIS) throws SQLException {
        return false;
    }

    public boolean modifyVisitBC(BeanVisitBC PVIS) throws SQLException {
        return false;
    }

    public ArrayList<BeanVisitBC> getListVisitBC(int pIdCulturalHeritage) throws SQLException {
        ArrayList<BeanVisitBC> fakeVisits = new ArrayList<BeanVisitBC>(0);

        fakeVisits.add(new BeanVisitBC(4, 1, "beautiful exhibition", 1, new Date()));
        fakeVisits.add(new BeanVisitBC(3, 1, "show particular but interesting", 1, new Date()));
        fakeVisits.add(new BeanVisitBC(4, 1, "beautiful exhibition", 1, new Date()));
        fakeVisits.add(new BeanVisitBC(3, 1, "show particular but interesting", 1, new Date()));
        fakeVisits.add(new BeanVisitBC(4, 1, "beautiful exhibition", 1, new Date()));
        fakeVisits.add(new BeanVisitBC(3, 1, "show particular but interesting", 1, new Date()));
        fakeVisits.add(new BeanVisitBC(4, 1, "beautiful exhibition", 1, new Date()));
        fakeVisits.add(new BeanVisitBC(3, 1, "show particular but interesting", 1, new Date()));
        fakeVisits.add(new BeanVisitBC(4, 1, "beautiful exhibition", 1, new Date()));
        fakeVisits.add(new BeanVisitBC(3, 1, "show particular but interesting", 1, new Date()));

        fakeVisits.add(
                new BeanVisitBC(5, 1, "This show is not 'evil'", 1, new Date(new Date().getTime() - 2591000000L)));
        fakeVisits.add(new BeanVisitBC(3, 1, "This show is not 'evil'", 1,
                new Date(new Date().getTime() - (unisa.gps.etour.util.GlobalConstants.THIRTY_DAYS * 2))));

        return fakeVisits;
    }

    public ArrayList<BeanVisitBC> getListVisitBCTourist(int pIdTourist) throws SQLException {
        return null;
    }

    public BeanVisitBC getVisitBC(int pIdCulturalHeritage, int pIdTourist) throws SQLException {
        return null;
    }
}
