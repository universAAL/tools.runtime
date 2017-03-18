/*
	Copyright 2007-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institut f�r Graphische Datenverarbeitung
 */
package org.universAAL.tools.logmonitor;

import javax.swing.JComponent;

import org.universAAL.middleware.container.LogListener;

/**
 * 
 * @author Carsten Stockloew
 */
public interface LogListenerEx extends LogListener {

    public JComponent getComponent();

    public String getTitle();
    
    public void stop();
}
