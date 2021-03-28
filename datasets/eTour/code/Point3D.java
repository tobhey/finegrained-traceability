package unisa.gps.etour.util;

/**
 * Bean that contains the coordinates of a point on the surface of the earth
 * "and That it realizes the calculation of the distance from the system. The
 * values of Coordinates must be represented in radians and must fall in Target
 * range: 0 to greek-Pi / 4 for the latitude south of the equator 0 to + Pi
 * greek / 4 for the latitude north of the equator from 0 to Pi-greek / 2 for
 * the meridian of longitude west of Greenwitch greek from 0 to + Pi / 2 For the
 * meridian of longitude east of Greenwitch
 *
 */

public class Point3D {
// Radius of the earth
    final double EARTH_RADIUS = 6371.0;
    private double latitude, longitude, altitude;

    public Point3D() {
        latitude = longitude = altitude = 0;
    }

    public Point3D(double pLatitude, double pLongitude, double pAltitude) {
        latitude = pLatitude;
        longitude = pLongitude;
        altitude = pAltitude;
    }

    /**
     * Returns the latitude
     *
     * @Return
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude
     *
     * @Param pLatitude
     */
    public void setLatitude(double pLatitude) {
        this.latitude = pLatitude;
    }

    /**
     * Returns the longitude
     *
     * @Return
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude
     *
     * @Param pLongitude
     */
    public void setLongitude(double pLongitude) {
        this.longitude = pLongitude;
    }

    /**
     * Returns the altitude
     *
     * @Return
     */
    public double getAltitude() {
        return altitude;
    }

    /**
     * Sets the altitude
     *
     * @Param pAltitude
     */
    public void setAltitude(double pAltitude) {
        this.altitude = pAltitude;
    }

    /**
     * Calculate the distance between the point and another point given as argument
     *
     * @Param p
     * @Return
     */
    public double distance(Point3D p) {
        double differenzaLongitude = this.longitude - p.longitude;
        double p1 = Math.pow(Math.cos(p.latitude) * Math.sin(differenzaLongitude), 2);
        double p2 = Math.pow(Math.cos(latitude) * Math.sin(p.latitude)
                - Math.sin(latitude) * Math.cos(p.latitude) * Math.cos(differenzaLongitude), 2);
        double p3 = Math.sin(latitude) * Math.sin(p.latitude)
                + Math.cos(latitude) * Math.cos(p.latitude) * Math.cos(differenzaLongitude);
        return (Math.atan(Math.sqrt(p1 + p2) / p3) * EARTH_RADIUS);
    }

    /**
     * Method which creates a 3D point from coordinates measured in degrees. The 3D
     * point instead represents the coordinates in radians
     *
     * @Param pLatitude latitude in degrees
     * @Param pLongitude Longitude in degrees *
     * @Param pAltitude
     * @Return Punto3D with the coordinates in radians
     */
    public static Point3D gradiRadianti(double pLatitude, double pLongitude, double pAltitude) {
        Point3D point = new Point3D();
        point.setLatitude(pLatitude * Math.PI / 180);
        point.setLongitude(pLongitude * Math.PI / 180);
        point.setAltitude(pAltitude);
        return point;
    }
}