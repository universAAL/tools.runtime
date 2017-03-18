/*
	Copyright 2007-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institut f�r Graphische Datenverarbeitung
 */
package org.universAAL.tools.logmonitor.all_log.gui;

import javax.swing.JTextPane;
import javax.swing.text.DefaultCaret;

import org.universAAL.tools.logmonitor.Utils;
import org.universAAL.tools.logmonitor.all_log.LogEntry;

/**
 * 
 * @author Carsten Stockloew
 */
public class MessagePanel extends JTextPane {

    private static final long serialVersionUID = 1L;

    MessagePanel() {
	setEditable(false);
	setContentType("text/plain");
	setCaret(new DefaultCaret());
	((DefaultCaret) getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
    }

    public void show(LogEntry le) {
	setText(Utils.buildMessage(le.msgPart));
    }
}
