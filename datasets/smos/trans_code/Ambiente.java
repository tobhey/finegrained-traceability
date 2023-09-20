package smos;

/**
 * Class used to hold GESA environment variables
 */
public class Environment {

	/**
	 * Default error message.
	 */
	public static String DEFAULT_ERROR_MESSAGE = "An error occurred while processing the request..<br><br>";

	private static String poolPropertiesPath = "";

	/**
	 * @return getPoolPropertiesPath()
	 */
	public static String getPoolPropertiesPath() {
		return poolPropertiesPath;
	}

	/**
	 * @param poolPropertiesPath
	 */
	public static void setPoolPropertiesPath(String poolPropertiesPath) {
		Environment.poolPropertiesPath = poolPropertiesPath;
	}
}