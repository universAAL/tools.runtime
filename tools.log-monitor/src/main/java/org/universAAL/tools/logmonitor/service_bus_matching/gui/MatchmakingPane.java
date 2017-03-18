/*
	Copyright 2007-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institut f�r Graphische Datenverarbeitung
 */
package org.universAAL.tools.logmonitor.service_bus_matching.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.universAAL.tools.logmonitor.service_bus_matching.LogMonitor;
import org.universAAL.tools.logmonitor.service_bus_matching.Matchmaking;
import org.universAAL.tools.logmonitor.service_bus_matching.URI;
import org.universAAL.tools.logmonitor.service_bus_matching.Matchmaking.SingleMatching;
import org.universAAL.tools.logmonitor.util.HTMLBusOperationsPane;
import org.universAAL.tools.logmonitor.util.ProfileInfo;

/**
 * 
 * @author Carsten Stockloew
 * 
 */
public class MatchmakingPane extends HTMLBusOperationsPane {
    private static final long serialVersionUID = 1L;

    /**
     * Defines how the overview will look like: 0 means that the results are
     * grouped according to the service provider, 1 means no grouping
     */
    public int overviewMethod = 0;

    private Matchmaking currentMatch = null;

    // private Sparul sparul = new Sparul();

    public MatchmakingPane() {
    }

    public void show(Matchmaking m) {
	currentMatch = m;
	super.clear();
	setText(createHTML(m));
    }

    @Override
    protected void updateAfterHyperlink() {
	setText(createHTML(currentMatch));
    }

    private void getOverviewHTML(StringBuilder s, List<SingleMatching> group) {
	for (Iterator<SingleMatching> it = group.iterator(); it.hasNext();) {
	    SingleMatching single = it.next();
	    String val2 = "-";
	    switch (single.reason) {
	    case SingleMatching.REASON_INPUT:
		val2 = "<i>input</i>";
		break;
	    case SingleMatching.REASON_OUTPUT:
		val2 = "<i>output</i>";
		break;
	    case SingleMatching.REASON_EFFECT:
		val2 = "<i>effect</i>";
		break;
	    }
	    String val3 = getURIHTML(single.serviceURI);
	    if (single.success.booleanValue())
		s.append(getTableRowHTML(getImageHTML("OK_16.png"), val2, val3));
	    else
		s.append(getTableRowHTML(getImageHTML("ERROR_16.png"), val2,
			val3));
	}
    }

    private String createHTML(Matchmaking m) {
	ProfileInfo info;
	LinkedList<SingleMatching> l = new LinkedList<SingleMatching>();

	StringBuilder s = new StringBuilder(
		"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\"><html><body>\n");

	s.append("<h1>Results of Matchmaking process</h1>\n");
	if (m.success.booleanValue())
	    s.append(getImageHTML("OK_32.png"));
	else
	    s.append(getImageHTML("ERROR_32.png"));
	s.append("Service request: <b>" + m.serviceURI + "</b><br>");

	// ///////////////////////////////////////
	// overview of matchings
	s.append("<br><br>Matching with registered service profiles:<br>");
	if (m.registeredServicesAvailable) {
	    s.append(getTableStartHTML(1));
	    s.append(getTableRowTitleHTML("Result", "Reason", "Service URI"));
	    if (overviewMethod == 0) {
		l = m.matchings;
		HashMap<String, ArrayList<SingleMatching>> groups = new HashMap<String, ArrayList<SingleMatching>>();

		// create the grouping
		for (SingleMatching single : l) {
		    info = LogMonitor.instance.getProfile(single.serviceURI);
		    String providerURI = "<unknown>";
		    if (!(info == null) && !(info.providerURI == null))
			providerURI = info.providerURI;

		    ArrayList<SingleMatching> group = groups.get(providerURI);
		    if (group == null) {
			group = new ArrayList<SingleMatching>();
			groups.put(providerURI, group);
		    }
		    group.add(single);
		}

		// get a sorted set of provider URIs
		ArrayList<String> sortedProviderURIs = new ArrayList<String>(
			groups.keySet());
		Collections.sort(sortedProviderURIs);

		// we clear the list and add all elements again in a sorted
		// order
		l.clear();

		// get the html output
		for (String providerURI : sortedProviderURIs) {
		    ArrayList<SingleMatching> group = groups.get(providerURI);
		    Collections.sort(group, new Comparator<SingleMatching>() {
			public int compare(SingleMatching o1, SingleMatching o2) {
			    return o1.serviceURI.compareTo(o2.serviceURI);
			}
		    });

		    s.append(getTableRowHTML("Provider: "
			    + getURIHTML(providerURI), 3));
		    getOverviewHTML(s, group);

		    l.addAll(group);
		}
	    } else {
		l = m.matchings;
		getOverviewHTML(s, l);
	    }
	    s.append(getTableEndHTML());
	} else {
	    s.append(" - no services are registered for the requested URI -<br>");
	}

	// details
	s.append("<br>");
	s.append(getLinkHTML("all", isVisible("all") ? "hide all" : "show all"));
	s.append("<br><br>");

	// ///////////////////////////////////////
	// details for request
	s.append("Request: ");
	getAllServiceRequestHTML(s, m.request, m.serviceURI,
		m.serializedRequest);

	// details for request: query
	// if (isVisible("requestQuery")) {
	// s.append(getLink("requestQuery", "hide query"));
	// s.append("<pre>\n" + sparul.getSparul(m.request) + "\n</pre>\n");
	// } else {
	// s.append(getLink("requestQuery", "show query (experimentel)"));
	// }

	if (m.registeredServicesAvailable) {
	    // ///////////////////////////////////////
	    // details for each matchmaking
	    for (Iterator<SingleMatching> it = l.iterator(); it.hasNext();) {
		SingleMatching single = it.next();
		s.append("<hr><h2>Details for "
			+ URI.get(single.serviceURI, true) + "</h2>\n\n");

		if (single.success.booleanValue()) {
		    s.append(getImageHTML("OK_16.png") + "&#160;&#160;");
		    s.append("successful");

		} else {
		    s.append(getImageHTML("ERROR_16.png") + "&#160;&#160;");

		    switch (single.reason) {
		    case SingleMatching.REASON_INPUT:
			if (!single.isOffer) {
			    String prop = single.restrictedProperty;
			    if (prop == null)
				s.append("   input: number of input parameters do not match");
			    else
				s.append("   input: input parameters do not match for property "
					+ getURIHTML(prop));
			    s.append("<br>\n");
			}
			s.append("   input: ");
			s.append(single.code);
			s.append(" - ");
			s.append(single.shortReason);
			s.append("<br>\n<i>Details:</i> ");
			s.append(single.detailedReason);
			s.append("<br>\n");
			break;
		    case SingleMatching.REASON_OUTPUT:
			s.append("   output: ");
			s.append(single.code);
			s.append(" - ");
			s.append(single.shortReason);
			s.append("<br>\n<i>Details:</i> ");
			s.append(single.detailedReason);
			s.append("<br>\n");
			if (single.output != null)
			    s.append(getServiceOutputHTML(single.output));
			else
			    System.out.println("ERROR: no output found");
			break;
		    case SingleMatching.REASON_EFFECT:
			s.append("   effect: ");
			s.append(single.code);
			s.append(" - ");
			s.append(single.shortReason);
			s.append("<br>\n<i>Details:</i> ");
			s.append(single.detailedReason);
			s.append("<br>\n");
			s.append("<br>\n");
			if (single.effect != null)
			    s.append(getServiceEffectHTML(single.effect));
			break;
		    default:
			s.append("reason unknown");
		    }
		}

		s.append("<br>\n");

		s.append("ServiceProfile: ");
		info = LogMonitor.instance.getProfile(single.serviceURI);
		if (info == null || info.profile == null)
		    s.append("- unknown -<br>\n");
		else {
		    info.serviceURI = single.serviceURI;
		    getAllServiceProfileHTML(s, info);
		}
	    }

	    // ///////////////////////////////////////
	    // filtering : Provider
	    s.append("<hr><h2>Provider filtering</h2><p>The list of services is filtered,"
		    + " so that only <i>one</i> service for a service provider is taken."
		    + " If more than one service matches for a provider then the"
		    + " most specialized service is taken.</p>\n<p>The number of matching"
		    + " services is:</p>\n");

	    s.append(getTableStartHTML(0));
	    s.append(getTableRowHTML("before provider filtering:", ""
		    + m.numMatchingProfiles));
	    s.append(getTableRowHTML("after provider filtering:", ""
		    + m.numMatchingAfterProviderFilter));
	    s.append(getTableEndHTML());

	    s.append("<br>The matched profiles and their provider filtering results are:<br>\n");

	    s.append(getTableStartHTML(0));
	    for (SingleMatching single : l) {
		if (single.success.booleanValue()) {
		    String profileURI = single.serviceURI;
		    String res = m.matchingsProvFilt.contains(profileURI) ? "OK_16.png"
			    : "ERROR_16.png";
		    s.append(getTableRowHTML(getImageHTML(res),
			    getURIHTML(profileURI)));
		}
	    }
	    s.append(getTableEndHTML());

	    /*
	     * s.append(getTableStartHTML(0)); for (SingleMatching single : l) {
	     * if (single.success.booleanValue()) {
	     * s.append(getTableRowHTML(single.profileURI)); } }
	     * s.append(getTableEndHTML());
	     * 
	     * s.append("\n<p>The following " + m.numMatchingAfterProviderFilter
	     * + " services have are available after filtering:</p>\n");
	     * 
	     * s.append(getTableStartHTML(0)); for (String srvURI :
	     * m.matchingsProvFilt) { s.append(getTableRowHTML(srvURI)); }
	     * s.append(getTableEndHTML());
	     */
	}

	s.append("\n</body></html>");

	// System.out.println(s);

	return s.toString();
    }
}
