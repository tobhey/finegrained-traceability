package edu.ncsu.csc.itrust;

/**
 * Escapes a few key HTML characters and does some other checking
 * 
 * @author Andy
 * 
 */
public class HtmlEncoder {
	/**
	 *  Escapes a few key HTML characters
	 * @param input String to check and escape
	 * @return
	 */
	public static String encode(String input) {
		if (input == null)
			return input;
		String str = input.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll("\n", "<br />");
		return str;
	}

	/**
	 * Checks URL
	 * 
	 * @param input URL to check
	 * @return false if the input contains http://, true otherwise
	 */
	public static boolean URLOnSite(String input) {
		return !(input.contains("http://"));
	}
}
