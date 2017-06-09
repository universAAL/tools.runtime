/*
	Copyright 2007-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institut f�r Graphische Datenverarbeitung
 */
package org.universAAL.tools.logmonitor.service_bus_matching;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;

import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.service.ServiceRequest;

/**
 * 
 * @author Carsten Stockloew
 * 
 */
public class Matchmaking {

	// a single matching contains information about the matchmaking between a
	// service request and a specific service profile
	public class SingleMatching {
		public static final int REASON_INPUT = 0;
		public static final int REASON_OUTPUT = 1;
		public static final int REASON_EFFECT = 2;

		// true, iff this service matches the request
		public Boolean success = null;

		// URI of the service of the profile for this single matching
		public String serviceURI = "";

		// the reason, one of REASON_INPUT, REASON_OUTPUT, REASON_EFFECT
		public int reason = -1;

		// the complete message as given in the log message
		public Object[] msgPart;

		// the code as given in the log message
		public int code = -1;

		// the short reason for a mismatch as given in the log message
		public String shortReason = "";

		// the detailed reason for a mismatch as given in the log message
		public String detailedReason = "";

		// detailed information if reason == REASON_INPUT
		public String restrictedProperty;
		public boolean isOffer = false; // true, if the problem is on the
		// 'offer'-side

		// detailed information if reason == REASON_OUTPUT
		public String outputURI;
		public Resource output;

		// detailed information if reason == REASON_EFFECT
		public Resource effect;

		/**
		 * A message is a standardized message if:
		 * 
		 * - element 1 is a String with a short reason
		 * 
		 * - element n-4 is an Integer with an error code
		 * 
		 * - element n-2 is a String with a detailed reason
		 * 
		 * - element n-1 (the last element) is an Integer with the logID (to
		 * track different log messages that belong together)
		 * 
		 * - elements inbetween (n-3 and n-5 are typically Strings that describe
		 * the elements n-4 and n-2 for readers of the log (for people not using
		 * this LogMonitor)
		 * 
		 * @param msgPart
		 */
		public void processStandardMessage(Object[] msgPart) {
			this.msgPart = msgPart;
			shortReason = (String) msgPart[1];
			code = ((Integer) msgPart[msgPart.length - 4]).intValue();
			detailedReason = (String) msgPart[msgPart.length - 2];
		}
	}

	// true, iff the request has some matches. In this case: numMatches > 0
	public Boolean success = null;

	// the number of matches after testing all profiles, before filtering
	public int numMatchingProfiles = -1;

	// the number of matches after filtering for providers
	public int numMatchingAfterProviderFilter = -1;

	// the number of matches at the end
	public int numMatches = -1;

	// the service request
	public ServiceRequest request;

	// the serialized service request (e.g. turtle)
	public String serializedRequest = null;

	// URI of the requested service
	public String serviceURI;

	// the set of single matchings after profile matching and before filtering
	public LinkedList<SingleMatching> matchings = new LinkedList<SingleMatching>();

	// the set of profile service URIs after filtering for the provider
	public LinkedList<String> matchingsProvFilt = new LinkedList<String>();

	// the exact time when the matchmaking starts
	public Date date;

	// error 1010
	public boolean registeredServicesAvailable = true;

	/**
	 * Get the date as a string.
	 * 
	 * @return The date as a string.
	 */
	public String getDateString() {
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

	/**
	 * Determines whether the request has exactly one matching profile.
	 * 
	 * @return
	 */
	public boolean hasOneResult() {
		if (success != null)
			if (success.booleanValue())
				if (numMatches == 1)
					return true;
		return false;
	}

	public String getResult() {
		String result = "<unknown>";
		if (success != null) {
			if (success.booleanValue()) {
				if (numMatches == 1) {
					// if we have only one match, print the URI of that match
					result = "1 match";

					LinkedList<SingleMatching> l = matchings;
					for (Iterator<SingleMatching> it = l.iterator(); it.hasNext();) {
						SingleMatching s = (SingleMatching) it.next();
						if (s.success.booleanValue()) {
							result = URI.get(s.serviceURI, true);
							break;
						}
					}
				} else
					result = numMatches + " matches";
			} else
				result = "- no_match -";
		}
		return result;
	}
}
