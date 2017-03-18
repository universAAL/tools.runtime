/*
	Copyright 2007-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institut f�r Graphische Datenverarbeitung
 */

package org.universAAL.tools.logmonitor.service_bus_matching.gui;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.universAAL.tools.logmonitor.service_bus_matching.LogMonitor;
import org.universAAL.tools.logmonitor.service_bus_matching.Matchmaking;
import org.universAAL.tools.logmonitor.service_bus_matching.URI;

/**
 * The main frame.
 * 
 * @author cstockloew
 * 
 */
public class Gui extends JPanel implements ListSelectionListener {

    private static final long serialVersionUID = 1L;

    /**
     * The graphics panel where the content is drawn.
     */
    MatchmakingPane pane = new MatchmakingPane();

    /**
     * The table containing a list of all messages.
     */
    JTable table;

    /**
     * Create the main frame.
     */
    public Gui() {
	this.setLayout(new BorderLayout());
	// Message overview
	DefaultTableModel model = new DefaultTableModel();
	model.addColumn("Time");
	model.addColumn("Request");
	model.addColumn("Matches");
	model.addColumn("Result");
	table = new JTable(model);
	table.getSelectionModel().addListSelectionListener(this);

	// overall view
	JScrollPane scrollPaneLeft = new JScrollPane(table);
	JScrollPane scrollPaneRight = new JScrollPane(pane);

	JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
		scrollPaneLeft, scrollPaneRight);
	splitPane.setDividerLocation(0.4);
	add(splitPane, BorderLayout.CENTER);
    }

    /**
     * Called whenever the value of the selection in the table showing the list
     * of all messages changes.
     * 
     * @param e
     *            The event that characterizes the change.
     */
    public void valueChanged(ListSelectionEvent e) {
	if (!e.getValueIsAdjusting()) {
	    int viewRow = table.getSelectedRow();
	    if (viewRow >= 0) {
		Matchmaking m = LogMonitor.instance.getMatchmaking(viewRow);
		// Matchmaking m = new Matchmaking();
		// m.serviceURI = "serviceURI";
		// m.numMatches = 0;
		// m.date = new Date();
		pane.show(m);
		pane.setSelectionStart(0);
		pane.setSelectionEnd(0);
	    }
	}
    }
    
    public void setSelection(int idx) {
	ListSelectionModel selectionModel = table.getSelectionModel();
	selectionModel.setSelectionInterval(idx, idx);
    }

    /**
     * Add a new entry.
     * 
     * @param m
     *            The matchmaking.
     */
    public int notify(Matchmaking m) {
	// add entry to table
	DefaultTableModel model = (DefaultTableModel) table.getModel();
	String result = m.getResult();
	model.addRow(new Object[] { m.getDateString(),
		URI.get(m.serviceURI, true), "" + m.numMatches, result });
	return model.getRowCount() - 1;

	// if this is the first entry: show in panel
	// if (model.getRowCount() == 1) {
	// model.setsel
	// panel.show(m);
	// }
    }

    // // main method for testing
    // public static void main(String[] args) {
    // Gui frame = new Gui();
    //
    // // Matchmaking m = new Matchmaking();
    // // m.serviceURI = "serviceURI";
    // // m.numMatches = 0;
    // // m.date = new Date();
    // // frame.notify(m);
    // // frame.notify(m);
    // }
}
