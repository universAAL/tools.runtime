/*
	Copyright 2007-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institut f�r Graphische Datenverarbeitung
 */
package org.universAAL.tools.logmonitor;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.universAAL.middleware.container.LogListener;

/**
 *
 * @author Carsten Stockloew
 */
public class Utils {

	/**
	 * Get the date as a string.
	 *
	 * @return The date as a string.
	 */
	public static String getDateString(Date date) {
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		String dateString = new String();
		dateString += c.get(Calendar.YEAR) + "-";
		dateString += c.get(Calendar.MONTH) + "-";
		dateString += c.get(Calendar.DAY_OF_MONTH) + " ";
		dateString += c.get(Calendar.HOUR_OF_DAY) + ":";
		dateString += c.get(Calendar.MINUTE) + ":";
		dateString += c.get(Calendar.SECOND) + ".";
		dateString += c.get(Calendar.MILLISECOND);
		return dateString;
	}

	public static String buildMessage(Object[] msgPart) {
		StringBuffer sb = new StringBuffer(256);
		if (msgPart != null)
			for (int i = 0; i < msgPart.length; i++)
				sb.append(msgPart[i]);
		return sb.toString();
	}

	public static String getLevel(int logLevel) {
		String level = "-";
		switch (logLevel) {
		case LogListener.LOG_LEVEL_TRACE:
			level = "TRACE";
			break;
		case LogListener.LOG_LEVEL_DEBUG:
			level = "DEBUG";
			break;
		case LogListener.LOG_LEVEL_INFO:
			level = "INFO";
			break;
		case LogListener.LOG_LEVEL_WARN:
			level = "WARN";
			break;
		case LogListener.LOG_LEVEL_ERROR:
			level = "ERROR";
			break;
		}
		return level;
	}
}
