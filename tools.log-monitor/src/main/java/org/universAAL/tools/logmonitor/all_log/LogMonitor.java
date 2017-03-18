/*
	Copyright 2007-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institut f�r Graphische Datenverarbeitung
 */
package org.universAAL.tools.logmonitor.all_log;

import java.util.Hashtable;

import javax.swing.JComponent;
import org.universAAL.tools.logmonitor.LogListenerEx;
import org.universAAL.tools.logmonitor.all_log.gui.AllLogPanel;

/**
 * Implementation of the {@link org.universAAL.middleware.util.LogListener}
 * interface to be called from
 * {@link org.universAAL.middleware.container.utils.LogUtils}
 * 
 * @author Carsten Stockloew
 * 
 */
public class LogMonitor implements LogListenerEx {

    private Hashtable<Integer, LogEntry> entries = new Hashtable<Integer, LogEntry>();
    int num = 0;
    AllLogPanel panel;

    public LogMonitor() {
	panel = new AllLogPanel(this);
    }

    /**
     * @see org.universAAL.middleware.container.LogListener
     */
    public void log(int logLevel, String module, String pkg, String cls,
	    String method, Object[] msgPart, Throwable t) {

	LogEntry le = new LogEntry(logLevel, module, pkg, cls, method, msgPart,
		t);
	entries.put(Integer.valueOf(num++), le);
	panel.addMessage(le);
    }

    public JComponent getComponent() {
	return panel;
    }

    public String getTitle() {
	return "All Messages";
    }

    public LogEntry getLogEntry(int index) {
	return entries.get(Integer.valueOf(index));
    }

    @Override
    public void stop() {
    }
}
