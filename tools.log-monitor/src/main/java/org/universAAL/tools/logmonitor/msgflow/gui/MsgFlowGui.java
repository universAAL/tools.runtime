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

import java.awt.Dimension;
import java.awt.Color;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;

/**
 * The scroll pane holding the graphical panel showing the message flow.
 * 
 * @author Carsten Stockloew
 */
public class MsgFlowGui extends JScrollPane {

    private static final long serialVersionUID = 1L;

    public MyScrollBarUI scrollbarUI;
    public MsgFlowPanel panel;

    /**
     * Create the pane.
     */
    public MsgFlowGui() {
	panel = new MsgFlowPanel(getHorizontalScrollBar(),
		getVerticalScrollBar());
	// setViewportView(panel);

	panel.setOpaque(false);
	JViewport viewport = panel.new MyViewport();
	viewport.setView(panel);
	viewport.setBackground(Color.WHITE);
	viewport.setOpaque(true);
	setViewport(viewport);

	// set scroll bar
	setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	// setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	// setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
	JScrollBar sb = getVerticalScrollBar();
	scrollbarUI = new MyScrollBarUI();
	sb.setPreferredSize(new Dimension(60, Integer.MAX_VALUE));
	sb.setUI(scrollbarUI);
	setVerticalScrollBar(sb);
    }
}
