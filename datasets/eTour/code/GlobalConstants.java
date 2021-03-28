package unisa.gps.etour.util;

import java.io.File;

/**
 * Class that contains the constants of the environmental system.
 *
 */
public class GlobalConstants {
    /** Highest precedence of news. */
    public static final int MAX_PRIORITY_NEWS = 5;
    /** Priority least one news. */
    public static final int MIN_PRIORITY_NEWS = 1;
    /** Maximum number of news on the machine. */
    public static final int MAX_NEWS_ACTIVE = 10;
    /**
     * Path to directory containing the images Banners stored on the server.
     */
    public static final String SERVER_IMAGE_PATH = "c:" + File.separator + "ImmaginiBanner" + File.separator;
    /** URL of the server for RMI calls */
    public static final String server_url = "localhost /";
    /** Milliseconds of 30 days */
    public static final long THIRTY_DAYS = 2592000000L;
}
