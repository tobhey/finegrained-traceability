package edu.ncsu.csc.itrust;

import java.util.Locale;

/** Provides a singleton for accessing the current locale of iTrust
 *  Could possibly load the country and language from a file.
 *
 */
public class Localization {
	private Locale currentLocale;
	
	public Localization(){
		currentLocale = new Locale("en","US");
	}

	/**
	 * Returns the current locale
	 * @return the current locale
	 */
	public Locale getCurrentLocale(){
		return currentLocale;
	}
	
	static Localization currentInstance = null;
	
	/**
	 * singleton method, may want to make this thread safe, as far as I know
	 * iTrust doesn't do any multithreading though...
	 * @return Localization instance
	 */
	public static Localization instance(){
		if(currentInstance == null){
			currentInstance = new Localization();
		}
		return currentInstance;
	}
}
