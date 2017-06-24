/*
	Copyright 2007-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institut f�r Graphische Datenverarbeitung
 */
package org.universAAL.tools.logmonitor;

import org.universAAL.middleware.container.LogListener;

/**
 * Implementation of the {@link org.universAAL.middleware.util.LogListener}
 * interface to be called from
 * {@link org.universAAL.middleware.container.utils.LogUtils}
 *
 * @author Carsten Stockloew
 *
 */
public class LogMonitor implements LogListener {

	LogListenerEx listeners[] = new LogListenerEx[6];
	MainGui gui = new MainGui();
	public static BusMemberMonitor busMemberMonitor = null;

	public LogMonitor() {
		listeners[0] = new org.universAAL.tools.logmonitor.all_log.LogMonitor();
		listeners[1] = new org.universAAL.tools.logmonitor.rdfvis.LogMonitor();
		listeners[2] = new org.universAAL.tools.logmonitor.service_bus_matching.LogMonitor();
		listeners[3] = new org.universAAL.tools.logmonitor.bus_member.LogMonitor();
		listeners[4] = new org.universAAL.tools.logmonitor.ontology.LogMonitor();
		listeners[5] = new org.universAAL.tools.logmonitor.msgflow.LogMonitor();

		for (int i = 0; i < listeners.length; i++)
			gui.addComponent(listeners[i].getTitle(), listeners[i].getComponent());

		// SwingUtilities.invokeLater(new Runnable() {
		// public void run() {
		// gui.tabbedPane.setSelectedIndex(5);
		// }
		// });

		// start bus member listener
		busMemberMonitor = new BusMemberMonitor();
		busMemberMonitor.addListener((BusMemberListener) listeners[3].getComponent());
		busMemberMonitor.addListener((BusMemberListener) listeners[5]);
		busMemberMonitor.start();
	}

	public void stop() {
		busMemberMonitor.stop();
		for (int i = 0; i < listeners.length; i++)
			listeners[i].stop();
	}

	public void log(int logLevel, String module, String pkg, String cls, String method, Object[] msgPart, Throwable t) {

		if (msgPart == null)
			msgPart = new Object[0];

		for (int i = 0; i < listeners.length; i++) {
			try {
				listeners[i].log(logLevel, module, pkg, cls, method, msgPart, t);
			} catch (Exception e) {
				System.out.println("Error found in LogMonitor: ");
				e.printStackTrace();
			}
		}
	}
}
