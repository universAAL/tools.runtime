/*
	Copyright 2007-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institut f�r Graphische Datenverarbeitung
 */
package org.universAAL.tools.logmonitor.rdfvis;

import java.util.LinkedList;

import javax.swing.JComponent;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.tools.logmonitor.LogListenerEx;
import org.universAAL.tools.logmonitor.rdfvis.gui.RDFVis;

/**
 * Implementation of the {@link org.universAAL.middleware.util.LogListener}
 * interface to be called from
 * {@link org.universAAL.middleware.container.utils.LogUtils}.
 *
 * @author Carsten Stockloew
 */
public class LogMonitor implements LogListenerEx {

	/**
	 * The main frame.
	 */
	private RDFVis vis = new RDFVis();

	/**
	 * @see org.universAAL.middleware.container.LogListener
	 */
	public void log(int logLevel, String module, String pkg, String cls, String method, Object[] msgPart, Throwable t) {

		String msg = "";
		LinkedList<Resource> lst = new LinkedList<Resource>();
		for (int i = 0; i < msgPart.length; i++) {
			if (i > 0)
				msg += " ";
			Object o = msgPart[i];
			msg += o;
			if (o instanceof Resource)
				lst.add((Resource) o);
		}
		for (Resource r : lst)
			vis.addMessage(cls, method, msg, r, ResourceInterpreter.getShortDescription(r));
	}

	public JComponent getComponent() {
		return vis;
	}

	public String getTitle() {
		return "RDF graph";
	}

	@Override
	public void stop() {
	}
}
