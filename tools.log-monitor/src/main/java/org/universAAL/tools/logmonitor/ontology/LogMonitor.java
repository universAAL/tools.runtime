/*
	Copyright 2007-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institut f�r Graphische Datenverarbeitung
 */
package org.universAAL.tools.logmonitor.ontology;

import java.util.Arrays;

import javax.swing.JComponent;

import org.universAAL.middleware.owl.OntologyManagement;
import org.universAAL.middleware.util.OntologyListener;
import org.universAAL.tools.logmonitor.Activator;
import org.universAAL.tools.logmonitor.LogListenerEx;
import org.universAAL.tools.logmonitor.ontology.gui.OntologyGui;

/**
 * 
 * @author Carsten Stockloew
 * 
 */
public class LogMonitor implements LogListenerEx, OntologyListener {

    private OntologyGui gui = new OntologyGui();

    public LogMonitor() {
	// start ontology listener
	OntologyManagement.getInstance()
		.addOntologyListener(Activator.mc, this);
	// add all existing ontologies
	String uris[] = OntologyManagement.getInstance().getOntoloyURIs();
	Arrays.sort(uris);
	for (String uri : uris) {
	    gui.add(uri);
	}
    }

    // dummy method for integration in main gui, not used
    public void log(int logLevel, String module, String pkg, String cls,
	    String method, Object[] msgPart, Throwable t) {
    }

    public JComponent getComponent() {
	return gui;
    }

    public String getTitle() {
	return "Ontologies";
    }

    public void stop() {
	OntologyManagement.getInstance().removeOntologyListener(Activator.mc,
		this);
    }

    @Override
    public void ontologyAdded(String ontURI) {
	gui.add(ontURI);
    }

    @Override
    public void ontologyRemoved(String ontURI) {
	gui.remove(ontURI);
    }
}
