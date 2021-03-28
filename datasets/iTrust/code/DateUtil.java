package edu.ncsu.csc.itrust;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Provides a few extra date utilities
 * 
 * @author Andy
 * 
 */
public class DateUtil {
	public static final long YEAR_IN_MS = 1000L * 60L * 60L * 24L * 365L;

	/**
	 * Returns a MM/dd/yyyy format of the date for the given years ago
	 * 
	 * @param years
	 * @return
	 */
	public static String yearsAgo(long years) {
		long time = System.currentTimeMillis();
		return new SimpleDateFormat("MM/dd/yyyy").format(new Date(time - years * YEAR_IN_MS));
	}

	/**
	 * Checks to see if a given date is within a range of months <strong>INCLUSIVELY</strong>, agnostic of
	 * the year. <br />
	 * <br />
	 * 
	 * The range "wraps" so that if the first month is after the second month, then the definition of "is in
	 * month range" is:<br />
	 * the date falls outside of secondMonth, firstMonth, but including secondMonth and firstMonth.
	 * 
	 * Modular arithmetic is used to adjust month values into the valid range.
	 * 
	 * @param date
	 * @param firstMonth
	 * @param secondMonth
	 * @return
	 */
	public static boolean isInMonthRange(java.util.Date date, int firstMonth, int secondMonth) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		firstMonth %= 12;
		secondMonth %= 12;
		if (secondMonth >= firstMonth) {
			return ((cal.get(Calendar.MONTH) >= firstMonth) && (cal.get(Calendar.MONTH) <= secondMonth));
		}
		return ((cal.get(Calendar.MONTH) >= firstMonth) || (cal.get(Calendar.MONTH) <= secondMonth));
	}

	/**
	 * Same as isInMonthRange but uses the current date as the date value.
	 * 
	 * @see DateUtil#isInMonthRange(Date, int, int)
	 * @param firstMonth
	 * @param secondMonth
	 * @return
	 */
	public static boolean currentlyInMonthRange(int firstMonth, int secondMonth) {
		return isInMonthRange(new Date(), firstMonth, secondMonth);
	}

	/**
	 * Returns the date a certain number of years ago
	 * @param years how many years ago
	 * @return the date it was however many years ago
	 */
	public static Date getDateXyearsAgoFromNow(int years) {
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.YEAR, -years);
		return cal.getTime();
	}

	/**
	 * Returns the date a certain number of years ago
	 * @param years how many years ago
	 * @return the date it was however many years ago
	 */
	public static java.sql.Date getSQLdateXyearsAgoFromNow(int years) {
		return new java.sql.Date(getDateXyearsAgoFromNow(years).getTime());
	}

	/**
	 * Returns the date a certain number of days ago
	 * @param days how many days ago
	 * @return the date it was however many days ago
	 */
	public static Date getDateXDaysAgoFromNow(int days) {
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.DAY_OF_YEAR, -days);
		return cal.getTime();
	}
	
	/**
	 * Returns the date a certain number of days ago
	 * @param days how many days ago
	 * @return the date it was however many days ago
	 */
	public static java.sql.Date getSQLdateXDaysAgoFromNow(int days) {
		return new java.sql.Date(getDateXDaysAgoFromNow(days).getTime());
	}

	/**
	 * <!--Pass in INSTANTIATED sql date objects and they will be set to the specified range, ie, FROM
	 * <current year> - yearsAgo1/monthValue1/01 TO <current year> - yearsAgo2/monthValue2/<last day of
	 * month2>--> Pass in INSTANTIATED sql date objects and they will be set to the specified range, ie, FROM
	 * &lt;current year&gt; - yearsAgo1/monthValue1/01 TO &lt;current year&gt; -
	 * yearsAgo2/monthValue2/&lt;last day of month2&gt;
	 * 
	 * @param month1
	 *            First sql.Date object to be set
	 * @param monthValue1
	 * @param yearsAgo1
	 * @param month2
	 *            Second sql.Date object to be set
	 * @param monthValue2
	 * @param yearsAgo2
	 */
	public static void setSQLMonthRange(java.sql.Date month1, int monthValue1, int yearsAgo1,
			java.sql.Date month2, int monthValue2, int yearsAgo2) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.add(Calendar.YEAR, -yearsAgo1);
		cal.set(Calendar.MONTH, monthValue1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		month1.setTime(cal.getTimeInMillis());
		cal.add(Calendar.YEAR, yearsAgo1);
		cal.add(Calendar.YEAR, -yearsAgo2);
		cal.set(Calendar.MONTH, monthValue2);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		month2.setTime(cal.getTimeInMillis());
	}
}
