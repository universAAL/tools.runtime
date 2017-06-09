/*
	Copyright 2007-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institut f�r Graphische Datenverarbeitung
 */

package org.universAAL.tools.logmonitor.rdfvis;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;

import org.universAAL.middleware.rdf.Resource;
import org.universAAL.tools.logmonitor.rdfvis.gui.GraphPanel;

/**
 * An edge in the graphical view connecting two nodes. Every
 * {@link org.universAAL.middleware.rdf.Resource} corresponds to a
 * {@link org.universAAL.tools.logmonitor.rdfvis.Node} in the visualization and
 * every property to an Edge.
 * 
 * @author Carsten Stockloew
 */
public class Edge implements Comparable<Object> {

	/**
	 * The name of this edge, corresponds to an RDF property.
	 */
	private String name;

	/**
	 * The child node.
	 */
	private Node child;

	/**
	 * True, if the child node has been visited before.
	 */
	public boolean visited = false;

	/**
	 * The width of the text for this edge in the Graphics context.
	 */
	int textWidth = -1;

	/**
	 * The string for type nodes. If the name is equal to this string then the
	 * child node is the rdfs:type of the parent node.
	 */
	// static String typeString = "type";
	static String typeString = Resource.PROP_RDF_TYPE;

	/**
	 * Create a new Edge.
	 * 
	 * @param name
	 *            The name of the edge.
	 * @param child
	 *            The child of the edge.
	 */
	Edge(String name, Node child) {
		this.name = name;
		setChild(child);
	}

	/**
	 * Create a new Edge.
	 * 
	 * @param name
	 *            The name of the edge.
	 * @param child
	 *            The child of the edge.
	 * @param visited
	 *            true, if the child has been visited before.
	 */
	Edge(String name, Node child, boolean visited) {
		this.name = name;
		this.visited = visited;
		setChild(child);
	}

	/**
	 * Set the child node.
	 * 
	 * @param child
	 *            The child node.
	 */
	void setChild(Node child) {
		this.child = child;
		if (child != null && name != null && name.startsWith(typeString))
			child.isType = true;
	}

	/**
	 * Get the child node of this object.
	 * 
	 * @return The child.
	 */
	public Node getChild() {
		return child;
	}

	/**
	 * Get the name of this object.
	 * 
	 * @return The name.
	 */
	String getName() {
		return name;
	}

	/**
	 * Draw this edge (draw an arrow from the parent node to the child node).
	 * 
	 * @param g
	 *            The graphics to draw to.
	 * @param parent
	 *            The parent node.
	 * @param x
	 *            The x-coordinate of the starting point.
	 * @param y
	 *            The y-coordinate of the starting point.
	 * @param currentWidth
	 *            The current width (i.e. the right edge) of all node drawn
	 *            before. This is used for links to already visited nodes to
	 *            prevent drawing over an existing node.
	 * @return The bottom-right corner.
	 */
	public Point drawEdge(Graphics2D g, Node parent, int x, int y, int currentWidth) {
		y += GraphPanel.nodeBorder;

		// draw text
		g.setFont(GraphPanel.fontNormal);
		y += GraphPanel.fmNormal.getAscent();
		g.drawString(name, x, y);
		y += GraphPanel.fmNormal.getDescent();
		y += GraphPanel.nodeBorder;
		if (textWidth == -1)
			textWidth = GraphPanel.fmNormal.stringWidth(name);

		if (parent == GraphPanel.selectedNode || child == GraphPanel.selectedNode)
			g.setStroke(new BasicStroke(3F));
		else
			g.setStroke(new BasicStroke(1F));

		// draw arrow
		g.drawLine(x - 15, parent.y + parent.height, x - 15, y);
		// drawArrow(g, x-15, this.y+this.height, x-15, y);

		if (visited) {
			int right = Math.max(currentWidth, x + textWidth) + GraphPanel.nodeBorder;
			g.drawLine(x - 15, y, right, y);
			g.drawLine(right, y, right, child.y + child.height / 2);
			drawArrow(g, right, child.y + child.height / 2, child.x + child.width, child.y + child.height / 2);
			g.setStroke(new BasicStroke(1F));
			return new Point(right + GraphPanel.nodeBorder, y);
		} else {
			// g.drawLine(x-15, y, x+15, y+15);
			drawArrow(g, x - 15, y, x, y + 5);
			g.setStroke(new BasicStroke(1F));
			return new Point(x + textWidth, y);
		}
	}

	/**
	 * Draws an arrow on the given Graphics2D context.
	 * 
	 * @param g
	 *            The Graphics2D context to draw on.
	 * @param x
	 *            The x location of the "tail" of the arrow.
	 * @param y
	 *            The y location of the "tail" of the arrow.
	 * @param xx
	 *            The x location of the "head" of the arrow.
	 * @param yy
	 *            The y location of the "head" of the arrow.
	 */
	public static void drawArrow(Graphics2D g, int x, int y, int xx, int yy) {
		// taken from http://www.bytemycode.com/snippets/snippet/82/
		float arrowWidth = 8.0f;// 10.0f;
		float theta = 0.8f;// 0.423f ;
		int[] xPoints = new int[3];
		int[] yPoints = new int[3];
		float[] vecLine = new float[2];
		float[] vecLeft = new float[2];
		float fLength;
		float th;
		float ta;
		float baseX, baseY;

		xPoints[0] = xx;
		yPoints[0] = yy;

		// build the line vector
		vecLine[0] = (float) xPoints[0] - x;
		vecLine[1] = (float) yPoints[0] - y;

		// build the arrow base vector - normal to the line
		vecLeft[0] = -vecLine[1];
		vecLeft[1] = vecLine[0];

		// setup length parameters
		fLength = (float) Math.sqrt(vecLine[0] * vecLine[0] + vecLine[1] * vecLine[1]);
		th = arrowWidth / (2.0f * fLength);
		ta = arrowWidth / (2.0f * ((float) Math.tan(theta) / 2.0f) * fLength);

		// find the base of the arrow
		baseX = ((float) xPoints[0] - ta * vecLine[0]);
		baseY = ((float) yPoints[0] - ta * vecLine[1]);

		// build the points on the sides of the arrow
		xPoints[1] = (int) (baseX + th * vecLeft[0]);
		yPoints[1] = (int) (baseY + th * vecLeft[1]);
		xPoints[2] = (int) (baseX - th * vecLeft[0]);
		yPoints[2] = (int) (baseY - th * vecLeft[1]);

		g.drawLine(x, y, (int) baseX, (int) baseY);
		g.fillPolygon(xPoints, yPoints, 3);
	}

	/**
	 * Compares this object with the specified object for order.
	 * 
	 * @param o
	 *            The Object to be compared.
	 * @return A negative integer, zero, or a positive integer as this object is
	 *         less than, equal to, or greater than the specified object.
	 */
	public int compareTo(Object o) {
		if (!(o instanceof Edge))
			return 0;
		Edge e = (Edge) o;

		if (e.name.startsWith(typeString) && name.startsWith(typeString))
			return name.compareTo(e.name);

		if (e.name.startsWith(typeString))
			return 1;
		if (name.startsWith(typeString))
			return -1;

		return name.compareTo(e.name);
	}
}
