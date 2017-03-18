/*
	Copyright 2007-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institut f�r Graphische Datenverarbeitung
 */
package org.universAAL.tools.logmonitor;

import java.util.ArrayList;

import org.universAAL.middleware.bus.member.BusMemberType;
import org.universAAL.middleware.interfaces.PeerCard;
import org.universAAL.middleware.managers.distributedmw.api.DistributedBusMemberListener;
import org.universAAL.middleware.managers.distributedmw.api.DistributedBusMemberManager;
import org.universAAL.middleware.rdf.Resource;

/**
 * 
 * @author Carsten Stockloew
 * 
 */
public class BusMemberMonitor implements DistributedBusMemberListener {
    public static BusMemberMonitor instance = null;

    private ArrayList<BusMemberListener> listeners = new ArrayList<BusMemberListener>();

    public BusMemberMonitor() {
	instance = this;
    }

    public void addListener(BusMemberListener l) {
	synchronized (listeners) {
	    listeners.add(l);
	}
    }

    public void removeListener(BusMemberListener l) {
	synchronized (listeners) {
	    listeners.remove(l);
	}
    }

    public void start() {
	// register this BusMemberRegistryListener
	DistributedBusMemberManager registry = (DistributedBusMemberManager) Activator.mc
		.getContainer().fetchSharedObject(
			Activator.mc,
			new Object[] { DistributedBusMemberManager.class
				.getName() });
	registry.addListener(this, null);
    }

    public void stop() {
	DistributedBusMemberManager registry = (DistributedBusMemberManager) Activator.mc
		.getContainer().fetchSharedObject(
			Activator.mc,
			new Object[] { DistributedBusMemberManager.class
				.getName() });
	registry.removeListener(this, null);
    }

    @Override
    public void busMemberAdded(PeerCard origin, String busMemberID,
	    String busName, BusMemberType memberType, String label,
	    String comment) {
	// System.out.println("  --  ADD: " + busMemberID);
	MemberData data = new MemberData(origin, busMemberID, busName,
		memberType, label, comment);
	synchronized (listeners) {
	    for (BusMemberListener l : listeners)
		l.add(data);
	}
    }

    @Override
    public void busMemberRemoved(PeerCard origin, String busMemberID) {
	// System.out.println("  --  REM: " + busMemberID);
	synchronized (listeners) {
	    for (BusMemberListener l : listeners)
		l.remove(busMemberID);
	}
    }

    @Override
    public void regParamsAdded(PeerCard origin, String busMemberID,
	    Resource[] params) {
	// System.out.println("  --  ADD-PARAM: " + busMemberID + "   " +
	// params);
	synchronized (listeners) {
	    for (BusMemberListener l : listeners)
		l.regParamsAdded(busMemberID, params);
	}
    }

    @Override
    public void regParamsRemoved(PeerCard origin, String busMemberID,
	    Resource[] params) {
	synchronized (listeners) {
	    for (BusMemberListener l : listeners)
		l.regParamsRemoved(busMemberID, params);
	}
    }
}
