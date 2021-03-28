package unisa.gps.etour.control.AdvertisementManager.test.stubs;

import java.sql.SQLException;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanNews;
import unisa.gps.etour.repository.IDBNews;
import unisa.gps.etour.util.GlobalConstants;

public class DBNews implements IDBNews {
    /** Static field that stores the number of test */
    private static int NUM_TEST = 0;

    public boolean clearNews(int pIdNews) throws SQLException {
        if (NUM_TEST == 1) {
            return true;
        } else if (NUM_TEST == 2) {
            throw new SQLException();
        }
        return true;

    }

    public boolean insertNews(BeanNews pNews) throws SQLException {
        if (NUM_TEST == 7) {
            return false;
        } else {
            return true;

        }
    }

    public boolean modifyNews(BeanNews pNews) throws SQLException {
        return true;
    }


    public ArrayList<BeanNews> getNews() throws SQLException {
        ArrayList<BeanNews> toReturn = new ArrayList<BeanNews>();

        if (NUM_TEST == 5) {
            for (int i = 1; i <= GlobalConstants.MAX_NEWS_ACTIVE; i++) {
                toReturn.add(new BeanNews());
            }
            return toReturn;

        } else {
            toReturn.add(new BeanNews());
            return toReturn;
        }
    }

    /**
     * Set the number of tests in progress
     *
     */
    public static void setNUM_TEST(int num_test) {
        NUM_TEST = num_test;
    }

}