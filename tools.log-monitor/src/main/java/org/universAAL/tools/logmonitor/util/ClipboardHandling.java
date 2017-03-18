/*
	Copyright 2007-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institut f�r Graphische Datenverarbeitung
 */
package org.universAAL.tools.logmonitor.util;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.JTextPane;
import javax.swing.TransferHandler;

/**
 * 
 * @author Carsten Stockloew
 */
public class ClipboardHandling extends AbstractAction {
    private static final long serialVersionUID = 1L;
    
    private HashMap<String, String> urlReplacement;
    private TransferHandler th;
    private JTextPane pane;
    
    public ClipboardHandling(HashMap<String, String> urlReplacement, TransferHandler th, JTextPane pane) {
	this.urlReplacement = urlReplacement;
	this.th = th;
	this.pane = pane;
    }

    // needed to overwrite ctrl-c
    private class TransferableProxy implements Transferable {

	String html = null;
	String plain = null;

	TransferableProxy(Clipboard c) {
	    Transferable transferData = c.getContents(null);
	    for (DataFlavor dataFlavor : transferData.getTransferDataFlavors()) {
		Object content = null;
		try {
		    content = transferData.getTransferData(dataFlavor);
		} catch (Exception e1) {
		    e1.printStackTrace();
		}

		if (dataFlavor.getMimeType().startsWith(
			"text/html; class=java.lang.String")) {
		    html = (String) content;

		    for (Iterator<String> it = urlReplacement.keySet()
			    .iterator(); it.hasNext();) {
			String key = it.next();
			String val = urlReplacement.get(key);
			html = html.replaceAll(key, val);
		    }
		} else if (dataFlavor.getMimeType().startsWith(
			"text/plain; class=java.lang.String")) {
		    plain = (String) content;
		}
	    }
	}

	public Object getTransferData(DataFlavor flavor)
		throws UnsupportedFlavorException, IOException {
	    if (flavor.getMimeType().startsWith(
		    "text/html; class=java.lang.String")) {
		return html;
	    } else if (flavor.getMimeType().startsWith(
		    "text/plain; class=java.lang.String")) {
		return plain;
	    }
	    return null;
	}

	public DataFlavor[] getTransferDataFlavors() {
	    ArrayList<DataFlavor> a = new ArrayList<DataFlavor>();
	    if (html != null) {
		try {
		    DataFlavor f = new DataFlavor(
			    "text/html; class=java.lang.String");
		    a.add(f);
		} catch (ClassNotFoundException e) {
		    e.printStackTrace();
		}
	    }
	    if (plain != null) {
		try {
		    DataFlavor f = new DataFlavor(
			    "text/plain; class=java.lang.String");
		    a.add(f);
		} catch (ClassNotFoundException e) {
		    e.printStackTrace();
		}
	    }
	    return (DataFlavor[]) a.toArray(new DataFlavor[0]);
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
	    if (flavor.getMimeType().startsWith(
		    "text/html; class=java.lang.String")) {
		return true;
	    } else if (flavor.getMimeType().startsWith(
		    "text/plain; class=java.lang.String")) {
		return true;
	    }
	    return false;
	}
    };

    private void printClipboardContent(Clipboard c) {
	Transferable transferData = c.getContents(null);
	for (DataFlavor dataFlavor : transferData.getTransferDataFlavors()) {
	    Object content = null;
	    try {
		content = transferData.getTransferData(dataFlavor);
	    } catch (Exception e1) {
		e1.printStackTrace();
	    }
	    System.out.println("--Content: " + dataFlavor.getMimeType() + "\n"
		    + content);
	}
    }

    public void actionPerformed(ActionEvent e) {
	// System.out.println("--- clipboard");
	// String selection = getSelectedText();
	// StringSelection data = new StringSelection(selection);
	// Clipboard clipboard = Toolkit.getDefaultToolkit()
	// .getSystemClipboard();
	// clipboard.setContents(data, data);
	// System.out.println("-- selection: " + selection);

	// copy the current selection to our own clipboard
	Clipboard clipboard = new Clipboard("");
	th.exportToClipboard(pane, clipboard, TransferHandler.COPY);
	// System.out.println("\n-------------------------------------------------\ninternal clipboard\n");
	// printClipboardContent(clipboard);

	// create a new Transferable
	Transferable contents = new TransferableProxy(clipboard);

	// copy content to system clipboard
	Clipboard systemClipboard = Toolkit.getDefaultToolkit()
		.getSystemClipboard();
	systemClipboard.setContents(contents, null);
	// System.out.println("\n-------------------------------------------------\nsystem clipboard\n");
	// printClipboardContent(systemClipboard);
    }
}
