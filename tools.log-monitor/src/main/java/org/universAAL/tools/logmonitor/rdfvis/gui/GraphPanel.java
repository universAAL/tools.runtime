/*
	Copyright 2007-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institut f�r Graphische Datenverarbeitung
 */

package org.universAAL.tools.logmonitor.rdfvis.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.universAAL.tools.logmonitor.rdfvis.Edge;
import org.universAAL.tools.logmonitor.rdfvis.Node;

/**
 * The graphical panel showing the node graph.
 * 
 * @author Carsten Stockloew
 */
public class GraphPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    /**
     * The distance between elements.
     */
    public static int nodeBorder = 6;

    /**
     * The font for normal text.
     */
    public static Font fontNormal = new Font("Verdana", 0, 12);

    /**
     * The font for bold text (e.g. java class names shown in nodes).
     */
    public static Font fontBold = new Font("Verdana", Font.BOLD, 12);

    /**
     * Font metrics for normal text. Stored for performance reasons.
     */
    public static FontMetrics fmNormal;

    /**
     * Font metrics for bold text. Stored for performance reasons.
     */
    public static FontMetrics fmBold;

    /**
     * The root node of the graph.
     */
    Node node;

    /**
     * The currently selected node, or null, if no node is selected. The
     * selected node and all of its edges are shown in bold.
     */
    public static Node selectedNode;

    /**
     * The coordinates of the bottom-right corner. Stored to set the slider
     * correctly, but only when the coordinates have been changed instead of
     * calling it ever time the re-painting method is called.
     */
    Point ptBottomRight = new Point(0, 0);

    /**
     * The MouseAdapter for implementing MouseEvents.
     */
    private class MyMouseAdapter extends MouseAdapter {

	@Override
	public void mousePressed(MouseEvent e) {
	    // right mouse click: show context menu
	    super.mousePressed(e);
	    if (e.getButton() == MouseEvent.BUTTON3)
		startContextMenu(e.getX(), e.getY());
	}

	/**
	 * Internal method to get the node at a specific coordinate.
	 * 
	 * @param n
	 *            The root node; the method goes recursively through all
	 *            child nodes.
	 * @param x
	 *            The x-coordinate.
	 * @param y
	 *            The y-coordinate.
	 * @return The node at the specified coordinates.
	 */
	private Node getNode(Node n, int x, int y) {
	    if (n.x <= x && x <= n.x + n.width)
		if (n.y <= y && y <= n.y + n.height)
		    return n;

	    for (Edge edge : n.children) {
		if (!edge.visited) {
		    Node child = getNode(edge.getChild(), x, y);
		    if (child != null)
			return child;
		}
	    }
	    return null;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	    // left mouse click: select a node
	    super.mouseReleased(e);
	    if (e.getButton() != MouseEvent.BUTTON1)
		return;
	    Node n = node;
	    if (n != null) {
		n = getNode(n, e.getX(), e.getY());
		if (n != null) {
		    // we have a winner -> select or deselect
		    if (selectedNode == n)
			selectedNode = null;
		    else
			selectedNode = n;
		    repaint();
		}
	    }
	}
    }

    /**
     * Create the panel.
     */
    GraphPanel() {
	setDoubleBuffered(true);
	setBackground(Color.WHITE);
	addMouseListener(new MyMouseAdapter());
    }

    /**
     * Show the context menu at the given coordinates.
     * 
     * @param x
     *            The x-coordinate.
     * @param y
     *            The y-coordinate.
     */
    private void startContextMenu(int x, int y) {
	JPopupMenu menu = new JPopupMenu();
	menu.add(new AbstractAction("Save all as image", null) {
	    private static final long serialVersionUID = 1L;

	    public void actionPerformed(ActionEvent e) {
		RDFVis.saveAll();
	    }
	});
	menu.show(this, x, y);
    }

    /**
     * Get the maximum of two points, i.e. the returned point contains the
     * maximum of the two x-coordinates and the maximum of the two
     * y-coordinates.
     * 
     * @param p1
     *            The first point.
     * @param p2
     *            The second point.
     * @return The maximum point.
     */
    private Point getMax(Point p1, Point p2) {
	return new Point(Math.max(p1.x, p2.x), Math.max(p1.y, p2.y));
    }

    /**
     * Draw the complete graph with all nodes and all edges.
     * 
     * @param g
     *            The graphics context.
     * @param node
     *            The node to draw.
     * @param x
     *            The starting x-coordinate.
     * @param y
     *            The starting y-coordinate.
     * @param p
     *            The current bottom-right corner. When calling the method with
     *            the root node , this parameter should be (0,0).
     * @return The bottom-right corner after drawing this node and all of its
     *         children.
     */
    Point drawNodes(Graphics2D g, Node node, int x, int y, Point p) {
	// return: bottom-right corner
	Point p2;

	node.drawNode(g, x, y);
	x += 30;
	y += node.height;
	p2 = new Point(x + node.width, y);
	p = getMax(p, p2);

	for (Edge edge : node.children) {

	    p2 = edge.drawEdge(g, node, x, y, p.x);
	    y = p2.y;
	    p = getMax(p, p2);

	    if (!edge.visited) {
		p2 = drawNodes(g, edge.getChild(), x, y, p);
		y = p2.y;
		p = getMax(p, p2);
	    }
	}
	// System.out.println("right: " + p.x);
	return p;
    }

    /**
     * Paint this component.
     * 
     * @param g
     *            The graphics context.
     */
    protected void paintComponent(final Graphics g) {
	super.paintComponent(g);

	if (fmNormal == null) {
	    fmNormal = g.getFontMetrics(fontNormal);
	    fmBold = g.getFontMetrics(fontBold);
	}

	g.setFont(fontNormal);
	Node n = node;
	Graphics2D g2 = (Graphics2D) g;
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);
	if (n != null) {
	    // System.out.println("----------------");
	    Point p = drawNodes(g2, n, 20, 20, new Point(0, 0));
	    p.y += 20;
	    if (!ptBottomRight.equals(p)) {
		ptBottomRight = p;
		setPreferredSize(new Dimension(ptBottomRight.x, ptBottomRight.y));
		revalidate();
	    }
	}
    }

    /**
     * Show a specific graph.
     * 
     * @param n
     *            The root node of the graph to draw.
     */
    void show(Node n) {
	node = n;
	repaint();
    }
}
