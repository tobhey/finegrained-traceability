package unisa.gps.etour.control.SearchManager;

public class AdvancedSearch extends Search {
    private static final BeanCulturalHeritage CULTURAL_HERITAGE = null;
    private static final BeanRefreshmentPoint REFRESHMENT_POINT = null;

    public AdvancedSearch(int pIdTourist) {

    }

    public int getSearchResultNumber() throws RemoteException {
    }

    public int getSearchPageNumber() throws RemoteException {
    }

    public BeanCulturalHeritage[] getPageResultCulturalHeritage(int pPage) throws RemoteException {
    
    }

    public BeanRefreshmentPoint[] getPageResultRefreshmentPoint(int pPage) throws RemoteException {
    }

    public int search(int pIdTourist, String pKeyword, int[] pTagsId, double pMaxRadius, int pElementsPerPage,
    Point3D pUserPosition, byte pSiteType) throws RemoteException {
    }

}