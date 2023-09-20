package edu.ncsu.csc.itrust;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides a utility method that converts the "Map" from the JSP container to a type-checked hashmap
 * 
 * @author Andy
 * 
 */
public class ParameterUtil {
	/**
	 * Provides a utility method that converts the "Map" from the JSP container to a type-checked hashmap
	 * @param params Map to convert
	 * @return converted Map
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, String> convertMap(Map params) {
		HashMap<String, String> myMap = new HashMap<String, String>();
		for (Object key : params.keySet()) {
			String[] value = ((String[]) params.get(key));
			if (value != null)
				myMap.put(key.toString(), value[0]);
			else
				myMap.put(key.toString(), null);
		}
		return myMap;
	}
}
