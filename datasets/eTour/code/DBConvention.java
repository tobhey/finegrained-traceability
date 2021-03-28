
package unisa.gps.etour.repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import unisa.gps.etour.bean.BeanConvention;
import unisa.gps.etour.bean.BeanRefreshmentPoint;
import unisa.gps.etour.repository.IDBConvention;
/**
Attribute_Definition
*
*/
public class DBConvention implements IDBConvention {
    private static int NUM_TEST = 1;

    /*
     * (Non-Javadoc)
     * 
     * @See unisa.gps.etour.repository.IDBConvention # clearConvention (int)
     */
    public boolean clearConvention(int pIdConvention) throws SQLException {
        return false;
    }

    /*
     * (Non-Javadoc)
     * 
     * @See unisa.gps.etour.repository.IDBConvention # getHistoricalConvention (int)
     */
    public ArrayList<BeanConvention> getHistoricalConvention(int idRefreshmentPoint) throws SQLException {
        return null;
    }

    /*
     * (Non-Javadoc)
     * 
     * @See unisa.gps.etour.repository.IDBConvention # insertConvention
     * (unisa.gps.etour.bean.BeanConvention)
     */
    public boolean insertConvention(BeanConvention pConvention) throws SQLException {
        return false;
    }

    /*
     * (Non-Javadoc)
     * 
     * @See unisa.gps.etour.repository.IDBConvention # modifyConvention
     * (unisa.gps.etour.bean.BeanConvention)
     */
    public boolean modifyConvention(BeanConvention pConvention) throws SQLException {
        return false;
    }

    /*
     * (Non-Javadoc)
     * 
     * @See unisa.gps.etour.repository.IDBConvention # getConvezioneActive
     * (int)
     */
    public BeanConvention getConventionActive(int pIdRefreshmentPoint) throws SQLException {
        BeanConvention convention = new BeanConvention();
        convention.setActive(true);
        convention.setDataEnd(new Date());
        convention.setDataStart(new Date());
        convention.setId(12);
        convention.setIdRefreshmentPoint(3);
        convention.setPrice(100);

        if (NUM_TEST == 1) {
            /* Test banners allowed */
            convention.setMaxBanner(4);
            return convention;
        } else if (NUM_TEST == 2) {
            /* Test banners not allowed */
            convention.setMaxBanner(3);
            return convention;
        } else {
            return null;
        }
    }

    /*
     * (Non-Javadoc)
     * 
     * @See unisa.gps.etour.repository.IDBConvention #
     * getListConventionActivePR ()
     */
    public ArrayList<BeanRefreshmentPoint> getListConventionActivePR() throws SQLException {
        return null;
    }

    public static void setNUM_TEST(int num_test) {
        NUM_TEST = num_test;
    }

}
