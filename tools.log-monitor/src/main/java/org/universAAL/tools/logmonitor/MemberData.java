package org.universAAL.tools.logmonitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.universAAL.middleware.bus.member.BusMemberType;
import org.universAAL.middleware.bus.model.AbstractBus;
import org.universAAL.middleware.context.ContextBusFacade;
import org.universAAL.middleware.context.ContextEventPattern;
import org.universAAL.middleware.interfaces.PeerCard;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.service.ServiceBusFacade;
import org.universAAL.middleware.service.owls.profile.ServiceProfile;
import org.universAAL.middleware.ui.UIBusFacade;
import org.universAAL.tools.logmonitor.util.PatternInfo;
import org.universAAL.tools.logmonitor.util.ProfileInfo;

/**
 * 
 * @author Carsten Stockloew
 * 
 */
public class MemberData {
    public String id;
    public String space;
    public String peer;
    public String module;
    public String type; // BusMemberType
    public int number;
    public String busNameReadable;
    public PeerCard origin;
    public String busName;
    public String label;
    public String comment;
    public List<ProfileInfo> profiles = new ArrayList<ProfileInfo>();
    public List<PatternInfo> patterns = new ArrayList<PatternInfo>();

    private static Map<String, String> busNames = new HashMap<String, String>();
    
    public static final String BUS_NAME_SERVICE = "Service";
    public static final String BUS_NAME_CONTEXT = "Context";
    public static final String BUS_NAME_UI = "UI";
    

    static {
	AbstractBus bus;
	bus = (AbstractBus) ServiceBusFacade.fetchBus(Activator.mc);
	busNames.put(bus.getBrokerName(), BUS_NAME_SERVICE);
	bus = (AbstractBus) ContextBusFacade.fetchBus(Activator.mc);
	busNames.put(bus.getBrokerName(), BUS_NAME_CONTEXT);
	bus = (AbstractBus) UIBusFacade.fetchBus(Activator.mc);
	busNames.put(bus.getBrokerName(), BUS_NAME_UI);
    }

    public MemberData(PeerCard origin, String busMemberID, String busName,
	    BusMemberType memberType, String label, String comment) {
	this.id = busMemberID;
	this.origin = origin;
	this.busName = busName;
	this.label = label;
	this.comment = comment;
	busNameReadable = busNames.get(busName);
	extractInfoFromID(busMemberID);
    }

    public boolean hasProfiles() {
	return profiles.size() != 0;
    }

    public boolean hasPatterns() {
	return patterns.size() != 0;
    }

    public int getNumberOfRegParams() {
	return profiles.size() + patterns.size();
    }

    public void regParamsAdded(String busMemberID, Resource[] params) {
	if (params == null || params.length == 0)
	    return;

	for (Resource par : params) {
	    if (par instanceof ServiceProfile) {
		ServiceProfile sp = (ServiceProfile) par;
		ProfileInfo info = new ProfileInfo(sp, busMemberID);
		info.serviceURI = sp.getTheService().getURI();
		profiles.add(info);
	    } else if (par instanceof ContextEventPattern) {
		patterns.add(new PatternInfo((ContextEventPattern) par));
	    }
	}
    }

    public void regParamsRemoved(Resource[] params) {
	if (params == null || params.length == 0)
	    return;

	if (params[0] instanceof ServiceProfile) {
	    for (Resource rRem : params) {
		Iterator<ProfileInfo> it = profiles.iterator();
		while (it.hasNext()) {
		    ProfileInfo info = it.next();
		    ServiceProfile spRem = (ServiceProfile) rRem;
		    ServiceProfile spExi = info.profile;

		    if (spRem.getProcessURI().equals(spExi.getProcessURI())) {
			// remove
			it.remove();
		    }
		}
	    }
	} else if (params[0] instanceof ContextEventPattern) {
	    for (Resource rRem : params) {
		Iterator<PatternInfo> it = patterns.iterator();
		while (it.hasNext()) {
		    PatternInfo info = it.next();
		    ContextEventPattern cepRem = (ContextEventPattern) rRem;
		    ContextEventPattern cepExi = info.pattern;

		    if (cepRem.matches(cepExi)) {
			// remove
			it.remove();
		    }
		}
	    }
	}
    }

    private void extractInfoFromID(String id) {
	String s = id;
	int idx = id.lastIndexOf('#');
	s = id.substring(0, idx);
	if (s.startsWith(AbstractBus.uAAL_OPTIONAL_URI_PREFIX))
	    s = s.substring(AbstractBus.uAAL_OPTIONAL_URI_PREFIX.length());
	int idx2 = s.lastIndexOf('/');
	space = s.substring(0, idx2);
	peer = s.substring(idx2 + 1);

	s = id.substring(idx);
	idx2 = s.indexOf('.');
	module = s.substring(idx2 + 1);

	s = s.substring(1, idx2);
	// System.out.println("--s: " + s);
	for (BusMemberType t : BusMemberType.values()) {
	    if (s.endsWith(t.name())) {
		type = t.name();
		s = s.substring(0, s.length() - type.length());
		number = Integer.parseInt(s);
		break;
	    }
	}
    }

    public void print() {
	System.out.println("---- MemberData");
	System.out.println("  ID:     " + id);
	System.out.println("  space:  " + space);
	System.out.println("  peer:   " + peer);
	System.out.println("  module: " + module);
	System.out.println("  type:   " + type);
	System.out.println("  bus:    " + busNameReadable);
	System.out.println("  number: " + number);
    }
}