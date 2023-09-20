package edu.ncsu.csc.itrust;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	private static final String BUNDLE_NAME = "edu.ncsu.csc.itrust.messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME,Localization.instance().getCurrentLocale());

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}

