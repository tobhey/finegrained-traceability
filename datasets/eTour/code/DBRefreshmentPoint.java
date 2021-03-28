package unisa.gps.etour.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

import unisa.gps.etour.bean.BeanRefreshmentPoint;
import unisa.gps.etour.bean.BeanTag;
import unisa.gps.etour.util.Point3D;

/**
 * Class that implements the interface of Refreshment
 *
 */
public class DBRefreshmentPoint implements IDBRefreshmentPoint {
// Empty constructor
    public DBRefreshmentPoint() {

    }

    public boolean clearRefreshmentPoint(int pIdRefreshmentPoint) throws SQLException {
// Variables for database connection
        Connection conn = null;
// Variable for the query
        Statement stat = null;
        try {
// Get the connection
            conn = DBConnectionPool.getConnection();
// Create the Statement
            stat = conn.createStatement();
// Query clearing
            String query = "DELETE FROM puntodiristoro WHERE Id =" + pIdRefreshmentPoint;
// You run the query Cancellation
            int i = stat.executeUpdate(query);
// This returns the backup
            return (i == 1);
        }
// Always runs and takes care of closing the Statement and the
// Connect
        finally {
            if (stat != null) {
                stat.close();
            }
            if (conn != null) {
                DBConnectionPool.releaseConnections(conn);
            }
        }

    }

    public boolean insertRefreshmentPoint(BeanRefreshmentPoint pRefreshmentPoint) throws SQLException {
// Create the organization of the Opening and Closing
        java.sql.Time openingTime = new Time(pRefreshmentPoint.getOpeningTime().getTime());
        java.sql.Time closingTime = new Time(pRefreshmentPoint.getClosingTime().getTime());
// Variables for database connection
        Connection conn = null;
// Variable for the query
        Statement stat = null;
        try {
// Get the connection
            conn = DBConnectionPool.getConnection();
// Create the Statement
            stat = conn.createStatement();
// Query for the insertion of a refreshment
            String query = "INSERT INTO puntodiristoro (Name," + "Description, Phoneephone, Latitude, Longitude,"
                    + "Altitude, openingTime, closingTime, ClosingDay"
                    + "Town, city, street, postcode, Province, AverageRating, RatingNumber" + "Party) VALUES ( '"
                    + pRefreshmentPoint.getName() + "','" + pRefreshmentPoint.getDescription() + "','"
                    + pRefreshmentPoint.getPhone() + " '," + pRefreshmentPoint.getPosition().getLatitude() + ""
                    + pRefreshmentPoint.getPosition().getLongitude() + ""
                    + pRefreshmentPoint.getPosition().getAltitude() + " '" + openingTime.toString() + "','"
                    + closingTime.toString() + "','" + pRefreshmentPoint.getClosingDay() + "','"
                    + pRefreshmentPoint.getLocation() + "','" + pRefreshmentPoint.getCity() + "','" + pRefreshmentPoint.getStreet()
                    + "','" + pRefreshmentPoint.getCap() + "','" + pRefreshmentPoint.getProvince() + " ',"
                    + pRefreshmentPoint.getAverageRating() + "" + pRefreshmentPoint.getRatingNumber() + " '"
                    + pRefreshmentPoint.getParty() + "');";
// You run the insert query
            int i = stat.executeUpdate(query);
            return (i == 1);
        }
// Is always done and takes care of closing the statement, and
// Connect
        finally {
            if (stat != null) {
                stat.close();
            }
            if (conn != null) {
                DBConnectionPool.releaseConnections(conn);
            }
        }
    }

    public boolean modifyRefreshmentPoint(BeanRefreshmentPoint pRefreshmentPoint) throws SQLException {
// Create the organization of the Opening and Closing
        java.sql.Time openingTime = new Time(pRefreshmentPoint.getOpeningTime().getTime());
        java.sql.Time closingTime = new Time(pRefreshmentPoint.getClosingTime().getTime());
// Variables for database connection
        Connection conn = null;
// Variable for the query
        Statement stat = null;
        try {
// Get the connection
            conn = DBConnectionPool.getConnection();
// Create the Statement
            stat = conn.createStatement();
// Query for amendment
            String query = "UPDATE puntodiristoro SET" + "Name = '" + pRefreshmentPoint.getName() + " ', Description ='"
                    + pRefreshmentPoint.getDescription() + " ', Phone ='" + pRefreshmentPoint.getPhone()
                    + " ', Latitude =" + pRefreshmentPoint.getPosition().getLatitude() + "Longitude ="
                    + pRefreshmentPoint.getPosition().getLongitude() + ", Height ="
                    + pRefreshmentPoint.getPosition().getAltitude() + ", openingTime = '"
                    + openingTime.toString() + " ', closingTime ='" + closingTime.toString()
                    + " ', ClosingDay ='" + pRefreshmentPoint.getClosingDay() + " ', Location ='"
                    + pRefreshmentPoint.getLocation() + " ', City ='" + pRefreshmentPoint.getCity() + " ', Street ='"
                    + pRefreshmentPoint.getStreet() + " ', postcode ='" + pRefreshmentPoint.getCap() + " ', State ='"
                    + pRefreshmentPoint.getProvince() + " ', AverageRating =" + pRefreshmentPoint.getAverageRating()
                    + ", RatingNumber =" + pRefreshmentPoint.getRatingNumber() + ", party = '"
                    + pRefreshmentPoint.getParty() + " 'WHERE Id =" + pRefreshmentPoint.getId();
// You run the query for Change
            int i = stat.executeUpdate(query);
// This returns the backup
            return (i == 1);
        }
// Is always done and takes care of closing the Statement and the
// Connect
        finally {
            if (stat != null) {
                stat.close();
            }
            if (conn != null) {
                DBConnectionPool.releaseConnections(conn);
            }
        }
    }

    public int getSearchResultNumber(String pKeyword, ArrayList<BeanTag> pTags, Point3D pPosition,
            double pMaxDistance) throws SQLException {
// Variables for database connection
        Connection conn = null;
// Variable for the query
        Statement stat = null;
// Variable for the query results
        ResultSet result = null;
        try {
// Get the connection
            conn = DBConnectionPool.getConnection();
// Create the Statement
            stat = conn.createStatement();
// Query to get the number of pages
            String query = "";
            if (pTags.size() == 0) {
// Query without the control tag
                query = "SELECT count (number) FROM (SELECT count (puntodiristoro.Id) AS number" + "FROM puntodiristoro"
                        + "WHERE (puntodiristoro.Name LIKE '%" + pKeyword + "% 'OR puntodiristoro.Description LIKE'%"
                        + pKeyword + '%' + "AND distance (puntodiristoro.Latitude, puntodiristoro.Longitude"
                        + pPosition.getLatitude() + "" + pPosition.getLongitude() + ") <" + pMaxDistance
                        + "GROUP BY puntodiristoro.Id" + "ORDER BY count (puntodiristoro.Id) DESC) AS table";
            } else {
// Query with tags
                query = "SELECT count (number) FROM (SELECT count (puntodiristoro.Id) AS number" + "FROM puntodiristoro"
                        + "JOIN (appartenenzapr JOIN tag ON IdTag = Id)"
                        + "ON puntodiristoro.Id = appartenenzapr.IdRefreshmentPoint"
                        + "WHERE (puntodiristoro.Name LIKE '%" + pKeyword + "% 'OR puntodiristoro.Description LIKE'%"
                        + pKeyword + "'%' AND tag.Name =  " + pTags.get(0).getName();
                if (pTags.size() >= 2) {
                    query = query + " 'OR tag.Name ='" + pTags.get(1).getName();
                }
                if (pTags.size() >= 3) {
                    query = query + " 'OR tag.Name ='" + pTags.get(2).getName();
                }
                if (pTags.size() >= 4) {
                    query = query + " 'OR tag.Name ='" + pTags.get(3).getName();
                }
                if (pTags.size() >= 5) {
                    query = query + " 'OR tag.Name ='" + pTags.get(4).getName();
                }
                query = query + " ')" + "AND distance (puntodiristoro.Latitude, puntodiristoro.Longitude"
                        + pPosition.getLatitude() + "" + pPosition.getLongitude() + ") <" + pMaxDistance
                        + "GROUP BY puntodiristoro.Id" + "ORDER BY count (puntodiristoro.Id) DESC) AS table";

            }
// You run the query
            result = stat.executeQuery(query);
// It returns the value of count () that contains the number of
// Tuple
            int i = 0;
            if (result.next()) {
                i = result.getInt(1);
            }
            return i;
        }
// Always runs and takes care to close the Result, the Statement
// And Connection
        finally {
            if (result != null) {
                result.close();
            }
            if (stat != null) {
                stat.close();
            }
            if (conn != null) {
                DBConnectionPool.releaseConnections(conn);
            }
        }
    }

    public int getSearchResultNumberAdvanced(int pIdTourist, String pKeyword, ArrayList<BeanTag> pTags,
            Point3D pPosition, double pMaxDistance) throws SQLException {
// Variables for database connection
        Connection conn = null;
// Variable for the query
        Statement stat = null;
// Variable for the query results
        ResultSet result = null;
        try {
// Get the connection
            conn = DBConnectionPool.getConnection();
// Create the Statement
            stat = conn.createStatement();
// Query to get the number of pages of advanced
            String query = "";
            if (pTags.size() == 0) {
// Query without the control tag
                query = "SELECT count (number) FROM (SELECT count (puntodiristoro.Id) AS number"
                        + "FROM (puntodiristoro LEFT JOIN" + "(SELECT IdRefreshmentPoint" + "FROM associazionepr, ("
                        + "SELECT IdSearchPreference" + "FROM rating" + "WHERE IdTourist =" + pIdTourist
                        + ") AS pref" + "WHERE associazionepr.IdSearchPreference = pref.IdSearchPreference)"
                        + "Preferences AS ON puntodiristoro.Id preferenze.IdRefreshmentPoint =) "
                        + "WHERE (puntodiristoro.Name LIKE '%" + pKeyword + "% 'OR puntodiristoro.Description LIKE'%"
                        + pKeyword + "'%') " + "AND distance (puntodiristoro.Latitude, puntodiristoro.Longitude"
                        + pPosition.getLatitude() + "" + pPosition.getLongitude() + ") <" + pMaxDistance
                        + "GROUP BY puntodiristoro.Id" + "ORDER BY count (puntodiristoro.Id) DESC) AS table";
            } else {
// Query with tags
                query = "SELECT count (number) FROM (SELECT count (puntodiristoro.Id) AS number"
                        + "FROM (puntodiristoro LEFT JOIN" + "(SELECT IdRefreshmentPoint" + "FROM associazionepr, ("
                        + "SELECT IdSearchPreference" + "FROM rating" + "WHERE IdTourist =" + pIdTourist
                        + ") AS pref" + "WHERE associazionepr.IdSearchPreference = pref.IdSearchPreference)"
                        + "Preferences AS ON puntodiristoro.Id preferenze.IdRefreshmentPoint =) "
                        + "JOIN (appartenenzapr JOIN tag ON IdTag = Id)"
                        + "ON puntodiristoro.Id = appartenenzapr.IdRefreshmentPoint"
                        + "WHERE (puntodiristoro.Name LIKE '%" + pKeyword + "% 'OR puntodiristoro.Description LIKE'%"
                        + pKeyword + "'%') AND (tag.Name = ' " + pTags.get(0).getName();
                if (pTags.size() >= 2) {
                    query = query + " 'OR tag.Name ='" + pTags.get(1).getName();
                }
                if (pTags.size() >= 3) {
                    query = query + " 'OR tag.Name ='" + pTags.get(2).getName();
                }
                if (pTags.size() >= 4) {
                    query = query + " 'OR tag.Name ='" + pTags.get(3).getName();
                }
                if (pTags.size() >= 5) {
                    query = query + " 'OR tag.Name ='" + pTags.get(4).getName();
                }
                query = query + " ')" + "AND distance (puntodiristoro.Latitude, puntodiristoro.Longitude"
                        + pPosition.getLatitude() + "" + pPosition.getLongitude() + ") <" + pMaxDistance
                        + "GROUP BY puntodiristoro.Id" + "ORDER BY count (puntodiristoro.Id) DESC) AS table";
            }
// You run the query
            result = stat.executeQuery(query);
// It returns the value of count () that contains the number of
// Tuple
            int i = 0;
            if (result.next()) {
                i = result.getInt(1);
            }
            return i;
        }
// Always runs and takes care to close the Result, the Statement
// And Connection
        finally {
            if (result != null) {
                result.close();
            }
            if (stat != null) {
                stat.close();
            }
            if (conn != null) {
                DBConnectionPool.releaseConnections(conn);
            }
        }
    }

    public BeanRefreshmentPoint getRefreshmentPoint(int pid) throws SQLException {
// Variables for database connection
        Connection conn = null;
// Variable for the query
        Statement stat = null;
// Variable for the query results
        ResultSet result = null;
        try {
// Get the connection
            conn = DBConnectionPool.getConnection();
// Create the Statement
            stat = conn.createStatement();
// Query
            String query = "SELECT * FROM puntodiristoro WHERE Id =" + pid;
// You run the query
            result = stat.executeQuery(query);
            if (result.next()) {
// We create the objects to be returned
                Point3D point = new Point3D(result.getDouble("Latitude"), result.getDouble("Longitude"),
                        result.getDouble("Elevation"));
                Date openingTime = new Date();
                Date closingTime = new Date();
// This generates the resting spot of bean
                BeanRefreshmentPoint puntoTemp = new BeanRefreshmentPoint(result.getInt("Id"), result.getInt("RatingNumber"),
                        result.getDouble("AverageRating"), result.getString("Name"), result.getString("Description"),
                        result.getString("Phone"), result.getString("Location"), result.getString("City"),
                        result.getString("Way"), result.getString("Cap"), result.getString("Province"),
                        result.getString("Party"), point, openingTime, closingTime,
                        result.getString("ClosingDay"));
// It returns the refreshment
                return puntoTemp;
            } else {
                return null;
            }
        }
// Always runs and takes care to close the Result, the Statement
// And Connection
        finally {
            if (result != null) {
                result.close();
            }
            if (stat != null) {
                stat.close();
            }
            if (conn != null) {
                DBConnectionPool.releaseConnections(conn);
            }
        }
    }

    public ArrayList<BeanRefreshmentPoint> search(String pKeyword, ArrayList<BeanTag> pTags, int pNumberPage,
            int pNumberElementsPerPage, Point3D pPosition, double pMaxDistance) throws SQLException {
// Variables for database connection
        Connection conn = null;
// Variable for the query
        Statement stat = null;
// Variable for the query results
        ResultSet result = null;
        try {
// Get the connection
            conn = DBConnectionPool.getConnection();
// Create the Statement
            stat = conn.createStatement();
// Query for search
            String query = "";
            if (pTags.size() == 0) {
// Query without the control tag
                query = "SELECT *" + "FROM puntodiristoro" + "WHERE (puntodiristoro.Name LIKE '%" + pKeyword
                        + "% 'OR puntodiristoro.Description LIKE'%" + pKeyword + "'%') "
                        + "AND distance (puntodiristoro.Latitude, puntodiristoro.Longitude"
                        + pPosition.getLatitude() + "" + pPosition.getLongitude() + ") <" + pMaxDistance
                        + "GROUP BY puntodiristoro.Id" + "ORDER BY count (puntodiristoro.Id) DESC LIMIT"
                        + (pNumberPage * pNumberElementsPerPage) + "" + pNumberElementsPerPage;
            } else {
// Query with tags
                query = "SELECT *" + "FROM puntodiristoro" + "JOIN (appartenenzapr JOIN tag ON IdTag = Id)"
                        + "ON puntodiristoro.Id = appartenenzapr.IdRefreshmentPoint"
                        + "WHERE (puntodiristoro.Name LIKE '%" + pKeyword + "% 'OR puntodiristoro.Description LIKE'%"
                        + pKeyword + "'%') AND (tag.Name = ' " + pTags.get(0).getName();
                if (pTags.size() >= 2) {
                    query = query + " 'OR tag.Name ='" + pTags.get(1).getName();
                }
                if (pTags.size() >= 3) {
                    query = query + " 'OR tag.Name ='" + pTags.get(2).getName();
                }
                if (pTags.size() >= 4) {
                    query = query + " 'OR tag.Name ='" + pTags.get(3).getName();
                }
                if (pTags.size() >= 5) {
                    query = query + " 'OR tag.Name ='" + pTags.get(4).getName();
                }
                query = query + " ')" + "AND distance (puntodiristoro.Latitude, puntodiristoro.Longitude"
                        + pPosition.getLatitude() + "" + pPosition.getLongitude() + ") <" + pMaxDistance
                        + "GROUP BY puntodiristoro.Id" + "ORDER BY count (puntodiristoro.Id) DESC LIMIT"
                        + (pNumberPage * pNumberElementsPerPage) + "" + pNumberElementsPerPage;
            }
// You run the query
            result = stat.executeQuery(query);
// List that will contain the BeanRefreshmentPoint
            ArrayList<BeanRefreshmentPoint> list = new ArrayList<BeanRefreshmentPoint>();
            while (result.next()) {
// We create the objects to be returned
                Point3D point = new Point3D(result.getDouble("Latitude"), result.getDouble("Longitude"),
                        result.getDouble("Elevation"));
                Date openingTime = new Date();
                Date closingTime = new Date();
// Build the refreshment
                BeanRefreshmentPoint puntoTemp = new BeanRefreshmentPoint(result.getInt("Id"), result.getInt("RatingNumber"),
                        result.getDouble("AverageRating"), result.getString("Name"), result.getString("Description"),
                        result.getString("Phone"), result.getString("Location"), result.getString("City"),
                        result.getString("Way"), result.getString("Cap"), result.getString("Province"),
                        result.getString("Party"), point, openingTime, closingTime,
                        result.getString("ClosingDay"));
// Insert the bean in the list
                list.add(puntoTemp);
            }
// It returns the list
            return list;
        }
// Always runs and takes care to close the Result, the Statement
// And Connection
        finally {
            if (result != null) {
                result.close();
            }
            if (stat != null) {
                stat.close();
            }
            if (conn != null) {
                DBConnectionPool.releaseConnections(conn);
            }
        }
    }

    public ArrayList<BeanRefreshmentPoint> searchAdvanced(int pIdTourist, String pKeyword, ArrayList<BeanTag> pTags,
            int pNumberPage, int pNumberElementsPerPage, Point3D pPosition, double pMaxDistance)
            throws SQLException {
// Variables for database connection
        Connection conn = null;
// Variable for the query
        Statement stat = null;
// Variable for the query results
        ResultSet result = null;
        try {
// Get the connection
            conn = DBConnectionPool.getConnection();
// Create the Statement
            stat = conn.createStatement();
// Query for advanced search
            String query = "";
            if (pTags.size() == 0) {
// Query without tags
                query = "SELECT *" + "FROM (puntodiristoro LEFT JOIN" + "(SELECT IdRefreshmentPoint"
                        + "FROM associazionepr, (" + "SELECT IdSearchPreference" + "FROM rating"
                        + "WHERE IdTourist =" + pIdTourist + ") CI pref"
                        + "WHERE associazionepr.IdSearchPreference = pref.IdSearchPreference)"
                        + "Preferences AS ON puntodiristoro.Id preferenze.IdRefreshmentPoint =) "
                        + "WHERE (puntodiristoro.Name LIKE '%" + pKeyword + "% 'OR puntodiristoro.Description LIKE'%"
                        + pKeyword + "%')" + "AND distance (puntodiristoro.Latitude, puntodiristoro.Longitude"
                        + pPosition.getLatitude() + "" + pPosition.getLongitude() + ") <" + pMaxDistance
                        + "GROUP BY puntodiristoro.Id" + "ORDER BY count (puntodiristoro.Id) DESC LIMIT"
                        + (pNumberPage * pNumberElementsPerPage) + "" + pNumberElementsPerPage;
            } else {
// Query with tags
                query = "SELECT *" + "FROM (puntodiristoro LEFT JOIN" + "(SELECT IdRefreshmentPoint"
                        + "FROM associazionepr, (" + "SELECT IdSearchPreference" + "FROM rating"
                        + "WHERE IdTourist =" + pIdTourist + ") AS pref"
                        + "WHERE associazionepr.IdSearchPreference = pref.IdSearchPreference)"
                        + "Preferences AS ON puntodiristoro.Id preferenze.IdRefreshmentPoint =) "
                        + "JOIN (appartenenzapr JOIN tag ON IdTag = Id)"
                        + "ON puntodiristoro.Id = appartenenzapr.IdRefreshmentPoint"
                        + "WHERE (puntodiristoro.Name LIKE '%" + pKeyword + "% 'OR puntodiristoro.Description LIKE'%"
                        + pKeyword + "'%') AND (tag.Name = ' " + pTags.get(0).getName();
                if (pTags.size() >= 2) {
                    query = query + " 'OR tag.Name ='" + pTags.get(1).getName();
                }
                if (pTags.size() >= 3) {
                    query = query + " 'OR tag.Name ='" + pTags.get(2).getName();
                }
                if (pTags.size() >= 4) {
                    query = query + " 'OR tag.Name ='" + pTags.get(3).getName();
                }
                if (pTags.size() >= 5) {
                    query = query + " 'OR tag.Name ='" + pTags.get(4).getName();
                }
                query = query + " ')" + "AND distance (puntodiristoro.Latitude, puntodiristoro.Longitude"
                        + pPosition.getLatitude() + "" + pPosition.getLongitude() + ") <" + pMaxDistance
                        + "GROUP BY puntodiristoro.Id" + "ORDER BY count (puntodiristoro.Id) DESC LIMIT"
                        + (pNumberPage * pNumberElementsPerPage) + "" + pNumberElementsPerPage;
            }
// You run the query
            result = stat.executeQuery(query);

            ArrayList<BeanRefreshmentPoint> list = new ArrayList<BeanRefreshmentPoint>();
            while (result.next()) {
// We create the objects to be returned
                Point3D point = new Point3D(result.getDouble("Latitude"), result.getDouble("Longitude"),
                        result.getDouble("Elevation"));
                Date openingTime = new Date();
                Date closingTime = new Date();
// This creates the cultural
                BeanRefreshmentPoint beneTemp = new BeanRefreshmentPoint(result.getInt("Id"), result.getInt("RatingNumber"),
                        result.getDouble("AverageRating"), result.getString("Name"), result.getString("Description"),
                        result.getString("Phone"), result.getString("Location"), result.getString("City"),
                        result.getString("Way"), result.getString("Cap"), result.getString("Province"),
                        result.getString("Party"), point, openingTime, closingTime,
                        result.getString("ClosingDay"));
// Insert the bean in the list
                list.add(beneTemp);
            }
// It returns the list
            return list;
        }
// Always runs and takes care to close the Result, the Statement
// And Connection
        finally {
            if (result != null) {
                result.close();
            }
            if (stat != null) {
                stat.close();
            }
            if (conn != null) {
                DBConnectionPool.releaseConnections(conn);
            }
        }
    }

    public ArrayList<BeanRefreshmentPoint> getListPR() throws SQLException {
// Variables for database connection
        Connection conn = null;
// Variable for the query
        Statement stat = null;
// Variable for the query results
        ResultSet result = null;
        try {
// Get the connection
            conn = DBConnectionPool.getConnection();
// Create the Statement
            stat = conn.createStatement();
// Query for a list of all the refreshment
            String query = "SELECT * FROM puntodiristoro";
// You run the query
            result = stat.executeQuery(query);
// List that will contain the BeanRefreshmentPoint
            ArrayList<BeanRefreshmentPoint> list = new ArrayList<BeanRefreshmentPoint>();
            while (result.next()) {
// We create the objects to be returned
                Point3D point = new Point3D(result.getDouble("Latitude"), result.getDouble("Longitude"),
                        result.getDouble("Elevation"));
                Date openingTime = new Date();
                Date closingTime = new Date();
// Build the refreshment
                BeanRefreshmentPoint puntoTemp = new BeanRefreshmentPoint(result.getInt("Id"), result.getInt("RatingNumber"),
                        result.getDouble("AverageRating"), result.getString("Name"), result.getString("Description"),
                        result.getString("Phone"), result.getString("Location"), result.getString("City"),
                        result.getString("Way"), result.getString("Cap"), result.getString("Province"),
                        result.getString("Party"), point, openingTime, closingTime,
                        result.getString("ClosingDay"));
// Insert the bean in the list
                list.add(puntoTemp);
            }
// It returns the list
            return list;
        }
// Always runs and takes care to close the Result, the Statement
// And Connection
        finally {
            if (result != null) {
                result.close();
            }
            if (stat != null) {
                stat.close();
            }
            if (conn != null) {
                DBConnectionPool.releaseConnections(conn);
            }
        }

    }

}
