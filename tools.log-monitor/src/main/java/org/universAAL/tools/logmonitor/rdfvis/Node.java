/*
	Copyright 2007-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institut f�r Graphische Datenverarbeitung
 */

package org.universAAL.tools.logmonitor.rdfvis;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.rdf.TypeMapper;
import org.universAAL.middleware.rdf.UnmodifiableResource;
import org.universAAL.tools.logmonitor.rdfvis.gui.GraphPanel;

/**
 * A node in the graphical view. Every
 * {@link org.universAAL.middleware.rdf.Resource} corresponds to a Node in the
 * visualization and every property to an
 * {@link org.universAAL.tools.logmonitor.rdfvis.Edge}.
 * 
 * @author Carsten Stockloew
 */
public class Node {

	/**
	 * The resource is an instance of this class.
	 */
	public Class<?> theClass = null;

	/**
	 * For literals: denotes the type of this literal.
	 */
	String literalType;

	/**
	 * The name of the node, corresponds to the URI of the RDF resource.
	 */
	String name = "";

	/**
	 * The list of all children.
	 */
	public LinkedList<Edge> children = new LinkedList<Edge>();

	/**
	 * The x-coordinate of the top left corner of this node, or -1 if the
	 * coordinates are not yet calculated.
	 */
	public int x = -1;

	/**
	 * The y-coordinate of the top left corner of this node.
	 */
	public int y;

	/**
	 * The width of this node.
	 */
	public int width;

	/**
	 * The height of this node.
	 */
	public int height;

	/**
	 * Denotes whether this node represents an RDF Literal.
	 */
	boolean isLiteral = false;

	/**
	 * Denotes whether this node represents a type node. Type nodes are nodes
	 * which have a property rdfs:type pointing at them.
	 */
	boolean isType = false;

	/**
	 * Color for regular nodes.
	 */
	private static final Color colResource = new Color(237, 244, 246);

	/**
	 * Color for literal nodes.
	 */
	private static final Color colLiteral = new Color(255, 255, 150);

	/**
	 * Color for type node. Type nodes are nodes which have a property rdfs:type
	 * pointing at them.
	 */
	private static final Color colType = new Color(150, 255, 150);

	/**
	 * Create a new node.
	 * 
	 * @param name
	 *            The {@link #name} of the node.
	 */
	Node(String name) {
		this.name = name;
	}

	/**
	 * Create a new literal node.
	 * 
	 * @param literalType
	 *            The type of the RDF literal (e.g. int, String..).
	 * @param name
	 *            The {@link #name} of the node.
	 */
	Node(String literalType, String name) {
		isLiteral = true;
		this.literalType = literalType;
		this.name = name;
	}

	/**
	 * Add a new child to the end of the list of children. Thus, the list of
	 * children may be unsorted after insertion.
	 * 
	 * @param edgeName
	 *            The name of the edge.
	 * @param child
	 *            The child node.
	 */
	void addChild(String edgeName, Node child) {
		children.add(new Edge(edgeName, child));
	}

	/**
	 * Add a new child to the end of the list of children. Thus, the list of
	 * children may be unsorted after insertion.
	 * 
	 * @param edgeName
	 *            The name of the edge.
	 * @param child
	 *            The child node.
	 * @param visited
	 *            true, if the child node is a node that has already been
	 *            processed before.
	 */
	void addChild(String edgeName, Node child, boolean visited) {
		Edge e = new Edge(edgeName, child);
		e.visited = visited;
		children.add(e);
	}

	/**
	 * Create the node structure from a resource.
	 * 
	 * @param r
	 *            The source Resource.
	 * @return The node containing all important information from the Resource.
	 */
	public static Node copyFrom(Resource r) {
		return copyFrom(r, new Hashtable<Resource, Node>());
	}

	/**
	 * Create the node structure from a resource.
	 * 
	 * @param r
	 *            The source Resource.
	 * @param visitedElements
	 *            The set of already visited Nodes of the RDF graph. This is
	 *            used to prevent infinite loops in case of cycles in the graph.
	 * @return
	 */
	private static Node copyFrom(Resource r, Hashtable<Resource, Node> visitedElements) {

		Node node = visitedElements.get(r);
		if (node == null) {
			// node does not exist -> create it here
			node = new Node(r.getURI());
			if (r instanceof UnmodifiableResource)
				node.theClass = ((UnmodifiableResource) r).getClassOfUnmodifiable();
			else
				node.theClass = r.getClass();
			visitedElements.put(r, node);

			// enum children and save all in 'temp'
			Hashtable<String, Resource> temp = new Hashtable<String, Resource>();
			Node child;
			Enumeration<?> e = r.getPropertyURIs();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				Object val = r.getProperty(key);
				if (val instanceof Resource) {
					Node existingChild = visitedElements.get((Resource) val);
					if (existingChild == null) {
						child = null;
						Edge edge = new Edge(key, child);
						temp.put(edge.getName(), (Resource) val);
						node.children.add(edge);
					} else {
						node.children.add(new Edge(key, existingChild, true));
					}
				} else if (val instanceof List<?>) {
					int i = 0;
					Iterator<?> iter = ((List<?>) val).iterator();
					while (iter.hasNext()) {
						Object o = iter.next();
						if (o instanceof Resource) {
							Node existingChild = visitedElements.get((Resource) o);
							if (existingChild == null) {
								child = null;
								Edge edge = new Edge(key + "(List " + i + ")", child);
								temp.put(edge.getName(), (Resource) o);
								node.children.add(edge);
							} else {
								node.children.add(new Edge(key, existingChild, true));
							}
						} else {
							child = new Node("");
							Edge edge = new Edge(key + "(List " + i + ")", child);
							node.children.add(edge);
						}
						i++;
					}
				} else {
					String type = TypeMapper.getDatatypeURI(val);
					if (type == null)
						node.children.add(new Edge(key, new Node("<unknown>")));
					else
						node.children.add(new Edge(key, new Node(type, val.toString())));
				}
			}

			// sort all children
			Collections.sort(node.children);

			// now add all grandchildren
			for (Edge edge : node.children) {
				r = temp.get(edge.getName());
				if (r != null) {
					child = copyFrom(r, visitedElements);
					edge.setChild(child);
				}
			}
		}
		return node;
	}

	/**
	 * Draw this node.
	 * 
	 * @param g
	 *            The Graphics object to draw to.
	 * @param xoff
	 *            The offset in x-direction.
	 * @param yoff
	 *            The offset in y-direction.
	 * @return The bottom-right corner
	 */
	public Point drawNode(Graphics2D g, int xoff, int yoff) {
		if (x == -1) {
			// calculate coordinates, only done once
			x = xoff;
			y = yoff;
			int border = 2 * GraphPanel.nodeBorder;
			if (isLiteral) {
				width = GraphPanel.fmNormal.stringWidth(name) + border;
				height = GraphPanel.fmNormal.getHeight() + border;
				int width2 = GraphPanel.fmBold.stringWidth(literalType) + border;
				if (width < width2)
					width = width2;

				height += GraphPanel.fmBold.getHeight();
			} else {
				width = GraphPanel.fmNormal.stringWidth(name) + border;
				height = GraphPanel.fmNormal.getHeight() + border;
				if (theClass != null) {
					int width2 = GraphPanel.fmBold.stringWidth(theClass.getName()) + border;
					if (width < width2)
						width = width2;

					height += GraphPanel.fmBold.getHeight();
				}
			}
		}

		// draw
		if (this == GraphPanel.selectedNode)
			g.setStroke(new BasicStroke(3F));
		else
			g.setStroke(new BasicStroke(1F));

		// g.setColor(Color.LIGHT_GRAY);
		if (isType)
			g.setColor(colType);
		else if (isLiteral)
			g.setColor(colLiteral);
		else
			g.setColor(colResource);
		g.fillRoundRect(xoff, yoff, width, height, 20, 20);

		g.setColor(Color.BLACK);
		g.drawRoundRect(xoff, yoff, width, height, 20, 20);
		int x = xoff + GraphPanel.nodeBorder;
		int y = yoff + GraphPanel.nodeBorder;
		if (isLiteral) {
			y += GraphPanel.fmBold.getAscent();
			g.setFont(GraphPanel.fontBold);
			g.drawString(literalType, x, y);
			y += GraphPanel.fmBold.getDescent() + GraphPanel.fmBold.getLeading();
		} else if (theClass != null) {
			y += GraphPanel.fmBold.getAscent();
			g.setFont(GraphPanel.fontBold);
			g.drawString(theClass.getName(), x, y);
			y += GraphPanel.fmBold.getDescent() + GraphPanel.fmBold.getLeading();
		}

		y += GraphPanel.fmNormal.getAscent();
		g.setFont(GraphPanel.fontNormal);
		g.drawString(name, x, y);

		g.setStroke(new BasicStroke(1F));

		return new Point(x + width, y + height);
	}
}
