package unisa.gps.etour.control.AdvertisementManager.test.stubs;

import java.sql.SQLException;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanBanner;
import unisa.gps.etour.repository.IDBBanner;

public class DBBanner implements IDBBanner {

    private static int NUM_TEST = 0;

    public boolean clearBanner(int pIdBanner) throws SQLException {
        if (NUM_TEST == 5) {
            throw new SQLException();
        } else {
            return true;
        }
    }

    public boolean insertBanner(BeanBanner pBanner) throws SQLException {
        if (NUM_TEST == 1 || NUM_TEST == 2) {
            return true;
        } else {
            return false;
        }
    }

    public boolean modifyBanner(BeanBanner pBanner) throws SQLException {
        return true;
    }

    public ArrayList<BeanBanner> getBanner(int pIdRefreshmentPoint) throws SQLException {
        ArrayList<BeanBanner> toReturn = new ArrayList<BeanBanner>();

        if (NUM_TEST == 1 || NUM_TEST == 2) {
            /* Must return an ArrayList with 3 elements */
            toReturn.add(new BeanBanner());
            toReturn.add(new BeanBanner());
            toReturn.add(new BeanBanner());
            return toReturn;
        } else if (NUM_TEST == 4) {
            toReturn.add(new BeanBanner(3, "c: \\ ProvaBannerInserimento.jpg", 55));
            toReturn.add(new BeanBanner(4, "c: \\ ProvaBannerInserimento.jpg", 55));
            toReturn.add(new BeanBanner(5, "c: \\ ProvaBannerInserimento.jpg", 55));
            toReturn.add(new BeanBanner(5, "c: \\ ProvaBannerInserimento.jpg", 55));
            return toReturn;
        } else {
            return null;
        }
    }

    public BeanBanner getBannerDaID(int pIdBanner) throws SQLException {
        if (NUM_TEST == 7) {
            return null;
        } else {
            return new BeanBanner(55, "c: // ProvaBanner.jpg", 3);
        }
    }

    public static void setNUM_TEST(int num_test) {
        NUM_TEST = num_test;
    }

}
