/*
	Copyright 2007-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institut f�r Graphische Datenverarbeitung
 */

package org.universAAL.tools.logmonitor.rdfvis.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.universAAL.middleware.rdf.Resource;
import org.universAAL.tools.logmonitor.rdfvis.Node;

/**
 * The main frame containing the list of all messages/log entry and the panel
 * for graphical drawing of the graph.
 * 
 * @author Carsten Stockloew
 */
public class RDFVis extends JPanel implements ListSelectionListener {

    private static final long serialVersionUID = 1L;

    /**
     * The instance of this class for calling from static methods.
     */
    private static RDFVis instance;

    // private String dir = "c:\\";

    /**
     * All information of one log entry.
     */
    private class Message {
	/**
	 * The date.
	 */
	Date date;

	/**
	 * The class which has called the logger.
	 */
	String cls;

	/**
	 * The method in which the logger was called.
	 */
	String method;

	/**
	 * The actual message.
	 */
	String message;

	/**
	 * The root node; corresponds to the Resource given to the logger.
	 */
	Node node;

	/**
	 * A short interpretation of the resource, if available.
	 */
	String shortDescription;

	/**
	 * Get the date as a string.
	 * 
	 * @return The date as a string.
	 */
	String getDateString() {
	    Calendar c = new GregorianCalendar();
	    c.setTime(date);
	    String dateString = new String();
	    dateString += c.get(Calendar.YEAR) + "-";
	    dateString += c.get(Calendar.MONTH) + "-";
	    dateString += c.get(Calendar.DAY_OF_MONTH) + " ";
	    dateString += c.get(Calendar.HOUR_OF_DAY) + ":";
	    dateString += c.get(Calendar.MINUTE) + ":";
	    dateString += c.get(Calendar.SECOND) + ".";
	    dateString += c.get(Calendar.MILLISECOND);
	    return dateString;
	}
    }

    /**
     * The graphics panel where the node graph is drawn.
     */
    GraphPanel panel = new GraphPanel();

    /**
     * The table containing a list of all messages.
     */
    JTable table;

    /**
     * The list of all messages.
     */
    LinkedList<Message> messages = new LinkedList<Message>();

    /**
     * Create the main frame.
     */
    public RDFVis() {
	this.setLayout(new BorderLayout());

	// RDF View
	JScrollPane scrollpane = new JScrollPane(panel);
	// getContentPane().add(scrollpane, BorderLayout.CENTER);
	scrollpane
		.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
	scrollpane
		.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	// Message overview
	DefaultTableModel model = new DefaultTableModel();
	model.addColumn("Date");
	model.addColumn("Class");
	model.addColumn("Method");
	model.addColumn("Message");
	model.addColumn("Interpretation");
	table = new JTable(model);
	// getContentPane().add(table, BorderLayout.WEST);
	JScrollPane scrollpaneTable = new JScrollPane(table);
	// table.getColumnModel().getColumn(0).setPreferredWidth(120);
	// table.getColumnModel().getColumn(1).setPreferredWidth(20);
	// table.getColumnModel().getColumn(2).setPreferredWidth(100);
	table.getSelectionModel().addListSelectionListener(this);

	// Split pane
	JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
		scrollpaneTable, scrollpane);
	add(split, BorderLayout.CENTER);

	instance = this;
    }

    /**
     * Save all graphs as images.
     */
    static void saveAll() {
	if (instance != null) {
	    if (instance.messages.size() == 0)
		return;

	    // File testFile = new File("WriteData2.dat");
	    // System.out.println("testFile=" + testFile.getAbsolutePath());

	    // don't use JDirectoryChooser, it hangs!!!
	    // File dir = JDirectoryChooser.showDialog(singleton);
	    // JFileChooser chooser = new JFileChooser();//instance.dir);
	    // chooser.setDialogTitle("Select target directory");
	    // chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    // if
	    // (chooser.showOpenDialog(instance)!=JFileChooser.APPROVE_OPTION)
	    // return;
	    // File myDir = chooser.getSelectedFile();
	    // if (myDir == null)
	    // return;
	    // instance.dir = ""+myDir+ "\\";
	    String myDir = "";
	    String textFilename = myDir
		    + "LogMonitor_"
		    + instance.messages.get(0).getDateString()
			    .replace(':', '-') + ".txt";
	    File textFile = new File(textFilename);
	    FileWriter writer = null;
	    try {
		writer = new FileWriter(textFile, true);
	    } catch (IOException e1) {
		System.out.println("Can't open file: " + textFilename);
		e1.printStackTrace();
	    }

	    synchronized (instance.messages) {
		String lastFilename = "";
		int i = 0;
		Node node = instance.panel.node;
		for (Message m : instance.messages) {
		    instance.panel.show(m.node);

		    // calc width and height, just draw everything
		    // (performance?)
		    BufferedImage bi;
		    bi = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		    Graphics2D g2 = bi.createGraphics();
		    Point p = instance.panel.drawNodes(g2, m.node, 20, 20,
			    new Point(0, 0));
		    p.y += 20;

		    // draw
		    bi = new BufferedImage(p.x, p.y, BufferedImage.TYPE_INT_RGB);
		    g2 = bi.createGraphics();
		    g2.setColor(Color.WHITE);
		    g2.fillRect(0, 0, p.x, p.y);
		    instance.panel.drawNodes(g2, m.node, 20, 20,
			    new Point(0, 0));

		    // save
		    String filename = myDir
			    + m.getDateString().replace(':', '-') + "_";

		    if (m.node != null && m.node.theClass != null)
			filename += m.node.theClass.getSimpleName();
		    else
			filename += "null";
		    // + m.cls + "_"
		    // + m.method + "_"
		    // + m.message.replaceAll("[\\\\/:*?\"<>|]", "_");
		    if (filename.equals(lastFilename)) {
			lastFilename = filename;
			filename += "_" + i;
			i++;
		    } else {
			lastFilename = filename;
			i = 0;
		    }
		    filename += ".png";

		    File outFile = new File(filename);
		    System.out.println("File: width " + p.x + " height " + p.y
			    + " " + outFile.getAbsolutePath()
			    + outFile.getName());
		    try {
			ImageIO.write(bi, "png", outFile);
		    } catch (IOException e) {
			e.printStackTrace();
		    }

		    // save text entry
		    if (writer != null) {
			try {
			    writer.write(filename + "\t");
			    writer.write(m.cls + "->" + m.method
				    + " (Interpretation: " + m.shortDescription
				    + ")\n");
			    writer.write(m.message + "\n");
			} catch (IOException e) {
			}
		    }
		}
		instance.panel.show(node);

	    }

	    if (writer != null) {
		try {
		    writer.flush();
		    writer.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}
    }

    /**
     * Add a new log entry.
     * 
     * @param cls
     *            The class which has called the logger.
     * @param method
     *            The method in which the logger was called.
     * @param message
     *            The actual message.
     * @param r
     *            The {@link org.universAAL.middleware.rdf.Resource}
     */
    public void addMessage(String cls, String method, String message,
	    Resource r, String shortDescription) {

	Node node = Node.copyFrom(r);
	addMessage(cls, method, message, node, new Date(), shortDescription);
    }

    /**
     * Add a new log entry.
     * 
     * @param cls
     *            The class which has called the logger.
     * @param method
     *            The method in which the logger was called.
     * @param message
     *            The actual message.
     * @param node
     *            The root node.
     * @param date
     *            The date.
     */
    private void addMessage(String cls, String method, String message,
	    Node node, Date date, String shortDescription) {

	synchronized (messages) {
	    Message m = new Message();
	    m.shortDescription = shortDescription;
	    m.message = message;
	    m.method = method;
	    m.date = date;
	    m.node = node;
	    m.cls = cls;
	    messages.add(m);

	    DefaultTableModel model = (DefaultTableModel) table.getModel();
	    model.addRow(new Object[] { m.getDateString(), cls, method,
		    message, shortDescription });

	    // panel.show(node);
	}
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
		Message m = messages.get(viewRow);
		panel.show(m.node);
	    }
	}
    }

    /*
     * // main method for testing public static void main(String[] args) {
     * RDFVis frame = new RDFVis();
     * 
     * Node n = new Node("root"); for (int i=0; i<4; i++)
     * n.addChild("link2child"+i, new Node("child"+i));
     * n.children.get(0).getChild().theClass = String.class;
     * n.children.get(1).getChild().addChild("link2child_1_1", new
     * Node("child_1_1"));
     * n.children.get(2).getChild().addChild("link2child_2_1ccccc", n, true);
     * n.children.get(3).getChild().addChild("link2child_3_1", n, true);
     * frame.addMessage("","","4", n, new Date());
     * 
     * n = new Node("root"); frame.addMessage("","","single", n, new Date());
     * 
     * n = new Node("root"); n.addChild("a1", new Node("a1")); n.addChild("a5",
     * new Node("a5")); n.addChild("a3", new Node("a3"));
     * n.addChild(Edge.typeString, new Node(Edge.typeString));
     * n.addChild(Edge.typeString, new Node(Edge.typeString)); n.addChild("a3",
     * new Node("a3")); n.addChild("a3", new Node("a3")); n.addChild("z2", new
     * Node("z2")); n.addChild("z9", new Node("z9")); n.addChild("b", new
     * Node("b")); Collections.sort(n.children); frame.addMessage("","","sort",
     * n, new Date()); }
     */
}
