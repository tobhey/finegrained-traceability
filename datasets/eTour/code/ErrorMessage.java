package unisa.gps.etour.util;

/**
 * Standard error message self-describing
 *
 */
public class ErrorMessage {
// Occurs when connecting to the DBMS
    public static final String ERROR_CONNECTION_DBMS = "Connection to DBMS Failed";

// Occurs when you can not perform an operation on the DBMS
    public static final String ERROR_DBMS = "Error DBMS";

// It occurs in conditions not specified
    public static final String ERROR_UNKNOWN = "Unknown error";

// Occurs when there are format errors in a bean
    public static final String ERROR_FORMAT_BEAN = "Error data bean";

// Occurs when a data error
    public static final String ERROR_DATA = "Data Error";

// Occurs when an error occurs on read / write files
    public static final String ERROR_FILE = "Error reading / writing file";

// Occurs when you have reached the maximum number of banners displayed
    public static final String ERROR_NUM_BANNER = "count exceeded banner inserted";
}
