/*
	Copyright 2007-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institut f�r Graphische Datenverarbeitung
 */

package org.universAAL.tools.logmonitor.all_log.gui;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.universAAL.tools.logmonitor.Utils;
import org.universAAL.tools.logmonitor.all_log.LogEntry;
import org.universAAL.tools.logmonitor.all_log.LogMonitor;

/**
 * The main frame containing the list of all messages/log entry and the panel
 * for graphical drawing of the graph.
 * 
 * @author Carsten Stockloew
 * 
 */
public class AllLogPanel extends JPanel implements ListSelectionListener {

    private static final long serialVersionUID = 1L;

    /**
     * The graphics panel where the node graph is drawn.
     */
    private MessagePanel messages = new MessagePanel();

    /**
     * The table containing a list of all messages.
     */
    private JTable table;

    private LogMonitor logMonitor;

    /**
     * Create the main frame.
     */
    public AllLogPanel(LogMonitor logMonitor) {
	this.logMonitor = logMonitor;

	this.setLayout(new BorderLayout());

	// Message View
	JScrollPane scrollpane = new JScrollPane(messages);
	// getContentPane().add(scrollpane, BorderLayout.CENTER);
	scrollpane
		.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
	scrollpane
		.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	// Message overview
	DefaultTableModel model = new DefaultTableModel();
	model.addColumn("Date");
	model.addColumn("level");
	model.addColumn("module");
	model.addColumn("Package");
	model.addColumn("Class");
	model.addColumn("Method");
	model.addColumn("Message");
	table = new JTable(model);
	// getContentPane().add(table, BorderLayout.WEST);
	JScrollPane scrollpaneTable = new JScrollPane(table);
	table.getColumnModel().getColumn(0).setPreferredWidth(150);
	// table.getColumnModel().getColumn(1).setPreferredWidth(80);
	// table.getColumnModel().getColumn(2).setPreferredWidth(100);
	table.getColumnModel().getColumn(0).setMaxWidth(150);
	table.getColumnModel().getColumn(1).setMaxWidth(65);
	table.getSelectionModel().addListSelectionListener(this);

	// Split pane
	JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
		scrollpaneTable, scrollpane);
	add(split, BorderLayout.CENTER);
	split.setDividerLocation(0.4);
    }

    /**
     * Add a new log entry.
     * 
     * @param le
     *            The log entry.
     */
    public void addMessage(final LogEntry le) {
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.addRow(new Object[] { Utils.getDateString(le.date),
			Utils.getLevel(le.logLevel), le.module, le.pkg, le.cls,
			le.method, Utils.buildMessage(le.msgPart) });
	    }
	});
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
		LogEntry le = logMonitor.getLogEntry(viewRow);
		if (le != null)
		    messages.show(le);
	    }
	}
    }
}
