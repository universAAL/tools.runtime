/*
    Copyright 2016-2020 Carsten Stockloew

    See the NOTICE file distributed with this work for additional
    information regarding copyright ownership

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */
package org.universAAL.tools.logmonitor.msgflow.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JViewport;

import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.tools.logmonitor.MainGui;
import org.universAAL.tools.logmonitor.msgflow.FlowData;
import org.universAAL.tools.logmonitor.msgflow.MemberDataEx;
import org.universAAL.tools.logmonitor.rdfvis.Edge;
import org.universAAL.tools.logmonitor.rdfvis.gui.GraphPanel;
import org.universAAL.tools.logmonitor.service_bus_matching.LogMonitor;
import org.universAAL.tools.logmonitor.service_bus_matching.Matchmaking;

/**
 * The graphical panel showing the message flow.
 * 
 * @author Carsten Stockloew
 */
public class MsgFlowPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public static MsgFlowPanel instance;

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

    Stroke thick = new BasicStroke(3F);
    Stroke thin = new BasicStroke(1F);
    Stroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
	    BasicStroke.JOIN_MITER, 10.0f, new float[] { 10.0f }, 0.0f);

    JScrollBar scrollBarHorizontal;
    JScrollBar scrollBarVertical;

    public final static Color busColor[] = new Color[] {
	    new Color(74, 172, 197), new Color(154, 186, 87),
	    new Color(244, 152, 67) };
    /**
     * The coordinates of the bottom-right corner. Stored to set the slider
     * correctly, but only when the coordinates have been changed instead of
     * calling it ever time the re-painting method is called.
     */
    Point ptBottomRight = new Point(0, 0);

    List<MemberDataEx> members = new LinkedList<MemberDataEx>();
    List<FlowData> messages = new LinkedList<FlowData>();
    HashMap<String, Integer> mapMember2Index = new HashMap<String, Integer>();

    //
    int numCurrentlyShownMembers = 0;

    // number of messages for each bus
    // int numMessages[] = new int[] { 0, 0, 0 };

    // true, if the messages for a certain should be shown
    boolean busActive[] = new boolean[] { true, true, true };

    // x-pos of the first member
    int member1Pos = 0;

    // distance between members
    int memberDist = 30;

    // height of a message entry (one row)
    int msgHeight = 0;

    int titleHeight = 150;

    private final int SRC = 8;
    private final int DST = 6;

    /**
     * The MouseAdapter for implementing MouseEvents.
     */
    private class MyMouseAdapter extends MouseAdapter {

	@Override
	public void mouseClicked(MouseEvent e) {
	    super.mouseClicked(e);
	    if (e.getClickCount() == 2) {
		//System.out.println("double click");
		synchronized (messages) {
		    for (FlowData d : messages) {
			int dist = d.y - e.getY();
			if (dist < msgHeight && dist >= 0) {
			    //System.out.println("Found " + date2String(d.date));
			    MainGui.tabbedPane.setSelectedIndex(2);
			    LogMonitor.instance.gui
				    .setSelection((Integer) d.ref);
			    break;
			}
		    }
		}
	    }
	}

	@Override
	public void mousePressed(MouseEvent e) {
	    // right mouse click: show context menu
	    super.mousePressed(e);
	    if (e.getButton() == MouseEvent.BUTTON3)
		startContextMenu(e.getX(), e.getY());
	}
    }

    // the viewport paints parts of the picture that are at fixed position
    // -> the bus member at the top
    public class MyViewport extends JViewport {
	private static final long serialVersionUID = 1L;

	@Override
	public void paint(Graphics graphics) {
	    super.paint(graphics);
	    // System.out.println("-- MyViewport");
	    // Graphics2D g2 = (Graphics2D) g;
	    // g2.setPaint(Color.BLACK);
	    // g2.drawRect(getWidth() / 4, getHeight() / 4, getWidth() / 2,
	    // getHeight() / 2);
	    Graphics2D g = (Graphics2D) graphics;
	    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		    RenderingHints.VALUE_ANTIALIAS_ON);
	    g.setFont(fontNormal);
	    g.setColor(Color.WHITE);
	    g.fillRect(0, 0, (int) getVisibleRect().getWidth(),
		    titleHeight + 10);
	    g.setColor(Color.BLACK);

	    Stroke oldStroke = g.getStroke();

	    // draw members
	    synchronized (members) {
		int x = member1Pos - scrollBarHorizontal.getValue();
		for (MemberDataEx dat : members) {
		    if (!dat.active)
			continue;
		    // draw a member
		    g.setFont(GraphPanel.fontNormal);
		    // g.drawString(dat.name, x, 100);
		    drawRotate(g, x, titleHeight, 315, dat.name);
		    // g.drawRect(x, 110, 1, 1);
		    Color c = g.getColor();
		    g.setColor(busColor[dat.bus]);
		    g.setStroke(thick);
		    g.drawLine(x - memberDist / 3, titleHeight + 10, x
			    + memberDist / 3, titleHeight + 10);
		    g.setStroke(dashed);
		    g.drawLine(x, titleHeight + 10, x, 100000);
		    g.setColor(c);

		    x += memberDist;
		}
	    }

	    g.setStroke(oldStroke);
	}
    }

    /**
     * Create the panel.
     */
    public MsgFlowPanel(JScrollBar scrollBarHorizontal,
	    JScrollBar scrollBarVertical) {
	instance = this;
	setDoubleBuffered(true);
	setBackground(Color.WHITE);
	addMouseListener(new MyMouseAdapter());
	this.scrollBarHorizontal = scrollBarHorizontal;
	this.scrollBarVertical = scrollBarVertical;
	setPreferredSize(new Dimension(2000, 2000));
    }

    private void addBusVisivilityAction(JPopupMenu menu, String name, int busidx) {
	menu.add(new AbstractAction(name, null) {
	    private static final long serialVersionUID = 1L;
	    private int busidx;

	    public void actionPerformed(ActionEvent e) {
		synchronized (members) {
		    busActive[busidx] = !busActive[busidx];
		    for (MemberDataEx m : members) {
			if (m.bus == busidx)
			    m.active = busActive[busidx];
		    }
		    calcAllIndices();
		    repaint();
		}
	    }

	    AbstractAction setBusIdx(int busidx) {
		this.busidx = busidx;
		return this;
	    }
	}.setBusIdx(busidx));
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
	String busNames[] = { "Service", "Context", "UI" };
	for (int busidx = 0; busidx < 3; busidx++) {
	    String name = (busActive[busidx] ? "Hide" : "Show") + " all "
		    + busNames[busidx] + " Bus members";
	    addBusVisivilityAction(menu, name, busidx);
	}

	int y2 = y - scrollBarVertical.getValue();
	if (y2 < titleHeight) {
	    // click on a member?
	    int idx = (x - member1Pos + memberDist / 3 - (titleHeight - y2))
		    / memberDist;
	    if (idx >= 0 && idx < numCurrentlyShownMembers) {
		// get the name of the selected member
		MemberDataEx member = null;
		synchronized (members) {
		    for (MemberDataEx m : members) {
			if (!m.active)
			    continue;
			if (idx == 0) {
			    member = m;
			    break;
			}
			idx--;
		    }
		}
		if (member != null) {
		    // found the member that needs to be hidden
		    menu.add(new AbstractAction("Hide bus member \""
			    + member.name + "\"", null) {
			private static final long serialVersionUID = 1L;
			private MemberDataEx member;

			public void actionPerformed(ActionEvent e) {
			    member.active = false;
			    numCurrentlyShownMembers--;
			    calcAllIndices();
			    repaint();
			}

			public AbstractAction setMember(MemberDataEx member) {
			    this.member = member;
			    return this;
			}
		    }.setMember(member));
		}
	    }
	    // System.out.println(x + "\t" + idx);
	}
	menu.show(this, x, y);
    }

    /**
     * Paint this component.
     * 
     * @param g
     *            The graphics context.
     */
    protected void paintComponent(final Graphics graphics) {
	super.paintComponent(graphics);

	// setup
	Graphics2D g = (Graphics2D) graphics;
	g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);
	if (fmNormal == null) {
	    fmNormal = g.getFontMetrics(fontNormal);
	    fmBold = g.getFontMetrics(fontBold);
	}
	g.setFont(fontNormal);
	if (member1Pos == 0) {
	    member1Pos = fmNormal.stringWidth(date2String(new Date())) + 5 + 40;
	    msgHeight = fmNormal.getHeight() + 10;
	}

	// g.drawString(date2String(new Date()), 5, 110 + msgHeight);
	int y = titleHeight + 10 + msgHeight;
	synchronized (messages) {
	    int xs, xd;
	    for (FlowData d : messages) {
		d.y = -1;
		if (!busActive[d.bus])
		    continue;
		int xname = member1Pos + memberDist * numCurrentlyShownMembers;
		Color c = g.getColor();
		if (d.bus == 0) {
		    if (d.idxSource == -1)
			continue; // source member not visible

		    d.y = y; // set the reference y-value for double-click
		    g.drawString(date2String(d.date), 5, y);

		    int y2 = y - msgHeight / 2 + 7;
		    // draw source
		    g.setColor(busColor[d.bus]);
		    xs = member1Pos + memberDist * d.idxSource;
		    g.fillRect(xs - SRC, y2 - SRC, SRC * 2, SRC * 2);

		    // draw dests
		    for (int i : d.idxDest) {
			// rect
			xd = member1Pos + memberDist * i;
			g.fillRect(xd - DST, y2 - DST, DST * 2, DST * 2);
			// arrow
			int xda = xd;
			if (i < d.idxSource)
			    xda += DST;
			else
			    xda -= DST;
			Edge.drawArrow(g, xs, y2, xda, y2);
		    }
		} else if (d.bus == 1) {
		    g.drawString(date2String(d.date), 5, y);
		    g.setColor(busColor[d.bus]);
		    g.drawLine(member1Pos - memberDist / 3, y, xname
			    - memberDist * 2 / 3, y);
		}
		g.setColor(c);

		// draw name
		g.drawString(d.name, xname, y);

		y += msgHeight;
	    }
	}

	// check for slider update
	Point p = new Point(member1Pos + memberDist * numCurrentlyShownMembers
		+ 500, y);
	if (!ptBottomRight.equals(p)) {
	    ptBottomRight = p;
	    setPreferredSize(new Dimension(ptBottomRight.x, ptBottomRight.y));
	    revalidate();
	}
    }

    private String date2String(Date d) {
	// SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss-SSSS");
	String reportDate = df.format(d);
	return reportDate;
    }

    public static void drawRotate(Graphics2D g2d, double x, double y,
	    int angle, String text) {
	g2d.translate(x, y);
	g2d.rotate(Math.toRadians(angle));
	g2d.drawString(text, 0, 0);
	g2d.rotate(-Math.toRadians(angle));
	g2d.translate(-x, -y);
    }

    public void addMember(MemberDataEx dat) {
	synchronized (members) {
	    members.add(dat);
	}

	synchronized (mapMember2Index) {
	    if (busActive[0]) {
		mapMember2Index.put(dat.member.id, numCurrentlyShownMembers);
		numCurrentlyShownMembers++;
	    } else {
		dat.active = false;
	    }
	}
	repaint();
    }

    public void stopMember(String id, int idxEnd) {
	synchronized (members) {
	    for (MemberDataEx dat : members) {
		if (dat.member.id.equals(id)) {
		    dat.idxEnd = idxEnd;
		}
	    }
	}
	repaint();
    }

    private void calcAllIndices() {
	synchronized (mapMember2Index) {
	    mapMember2Index.clear();
	    int i = 0;
	    for (MemberDataEx m : members) {
		if (!m.active)
		    continue;
		mapMember2Index.put(m.member.id, i);
		i++;
	    }
	    numCurrentlyShownMembers = i;
	}
	synchronized (messages) {
	    for (FlowData d : messages) {
		calcIndices(d);
	    }
	}
    }

    private void calcIndices(FlowData dat) {
	synchronized (mapMember2Index) {
	    dat.idxSource = -1;
	    if (mapMember2Index.containsKey(dat.source))
		dat.idxSource = mapMember2Index.get(dat.source);

	    List<Integer> idx = new ArrayList<Integer>();
	    for (String d : dat.dest) {
		if (mapMember2Index.containsKey(d))
		    idx.add(mapMember2Index.get(d));
	    }
	    dat.idxDest = new int[idx.size()];
	    int loop = 0;
	    for (Integer i : idx) {
		dat.idxDest[loop] = i;
		loop++;
	    }
	}
    }

    public void addEvent(ContextEvent evt) {
	synchronized (messages) {
	    // create the new FlowData
	    FlowData dat = new FlowData(evt);
	    calcIndices(dat);

	    addMessage(dat);
	}
	repaint();
    }

    public void addMatching(Matchmaking m, int ref) {
	synchronized (messages) {
	    // create the new FlowData
	    FlowData dat = new FlowData(m, ref);
	    calcIndices(dat);

	    addMessage(dat);
	}
	repaint();
    }

    private void addMessage(FlowData dat) {
	// sort into the list 'messages'
	// we need to sort, e.g., because the matchmaking takes some time
	// and we are called after it is done. During this time, other
	// messages could have been sent
	ListIterator<FlowData> it = messages.listIterator(messages.size());
	while (it.hasPrevious()) {
	    FlowData d = it.previous();
	    if (d.date.compareTo(dat.date) < 0)
		break;
	}
	if (it.hasNext())
	    it.next();
	it.add(dat);
    }
}
