/*
	Copyright 2007-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institut f�r Graphische Datenverarbeitung
 */
package org.universAAL.tools.logmonitor.util;

import java.util.Hashtable;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * HTML-based Pane that handles visibility of parts of the text.
 * 
 * @author Carsten Stockloew
 * 
 */
public class HTMLVisibilityPane extends HTMLPaneBase {
    private static final long serialVersionUID = 1L;

    private boolean all = false;

    // Storage visible objects; objects that are not visible, are not
    // contained in this table. The value is ignored, only the key is used.
    private Hashtable<String, String> visible = new Hashtable<String, String>();

    private class HTMLListener implements HyperlinkListener {
	public void hyperlinkUpdate(HyperlinkEvent e) {
	    if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
		String link = e.getDescription();
		togglevisibility(link);

		// test: print content of system clipboard
		// Clipboard systemClipboard;
		// systemClipboard = Toolkit.getDefaultToolkit()
		// .getSystemClipboard();
		// printClipboardContent(systemClipboard);

		updateAfterHyperlink();
	    }
	}
    }

    public HTMLVisibilityPane() {
	addHyperlinkListener(new HTMLListener());
    }

    protected void updateAfterHyperlink() {
    }

    protected void clear() {
	all = false;
	visible.clear();
    }

    protected boolean isVisible(String uri) {
	if (all)
	    return true;
	Object o = visible.get(uri);
	if (o == null)
	    return false;
	else
	    return true;
    }

    private void makeVisible(String uri) {
	visible.put(uri, uri);
    }

    private void makeInvisible(String uri) {
	visible.remove(uri);
    }

    private void togglevisibility(String uri) {
	if ("all".equals(uri)) {
	    if (all)
		visible.clear();
	    all = !all;
	} else if (visible.get(uri) != null)
	    makeInvisible(uri);
	else
	    makeVisible(uri);
    }
}
