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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.plaf.ScrollBarUI;
import javax.swing.plaf.basic.BasicScrollBarUI;

import org.universAAL.tools.logmonitor.msgflow.FlowData;

/**
 * 
 * @author Carsten Stockloew
 * 
 */
public class MyScrollBarUI extends BasicScrollBarUI {
    public ScrollBarUI defaultUI = null;

    MyScrollBarUI() {
    }

    @Override
    protected void paintTrack(Graphics g, JComponent comp, Rectangle trackBounds) {
	// super.paintTrack(g, c, trackBounds);
	Color c = g.getColor();
	MsgFlowPanel panel = MsgFlowPanel.instance;
	synchronized (panel.messages) {
	    int size = panel.messages.size();
	    int i = 0;
	    for (FlowData d : panel.messages) {
		g.setColor(MsgFlowPanel.busColor[d.bus]);
		int third = trackBounds.width / 3;
		int x1 = d.bus * third;
		int x2 = d.bus * third + third;
		int y = i * trackBounds.height / size + 1;
		g.drawLine(x1, y, x2, y);
		i++;
	    }
	}
	g.setColor(Color.RED);
	g.drawRect(trackBounds.x, trackBounds.y, trackBounds.width - 1,
		trackBounds.height - 1);
	g.setColor(c);
    }

    @Override
    protected void paintThumb(Graphics g, JComponent comp, Rectangle thumbBounds) {
	// super.paintThumb(g, c, thumbBounds);
	Color c = g.getColor();
	g.setColor(Color.BLUE);
	g.drawRect(thumbBounds.x, thumbBounds.y, thumbBounds.width - 1,
		thumbBounds.height - 1);
	g.setColor(c);
    }
}
