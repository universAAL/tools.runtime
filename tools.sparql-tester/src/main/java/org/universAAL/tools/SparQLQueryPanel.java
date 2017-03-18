/*******************************************************************************
 * Copyright 2016 2011 Universidad Politécnica de Madrid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.universAAL.tools;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.BoxLayout;
import java.io.File;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.FlowLayout;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

/**
 * @author amedrano
 *
 */
public class SparQLQueryPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4140386150426349342L;
	private JButton btnQuery;
	private JTabbedPane sparqlQueries;
	private int tabcount = 2;
	private JButton btnLoad;
	private JButton btnClear;

	/**
	 * Create the panel.
	 */
	public SparQLQueryPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.EAST);
		
		btnClear = new JButton("Clear");
		btnClear.setMinimumSize(new Dimension(63, 23));
		btnClear.setMaximumSize(new Dimension(63, 23));
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Component c = sparqlQueries.getSelectedComponent();
				if (c instanceof SparQLQuerySubPanel){
					((SparQLQuerySubPanel)c).clear();
				}
			}
		});
		
		btnLoad = new JButton("Load");
		btnLoad.setMinimumSize(new Dimension(63, 23));
		btnLoad.setMaximumSize(new Dimension(63, 23));
		btnLoad.setMnemonic('l');
		btnLoad.addActionListener( new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser choose = new JFileChooser();
				int ret = choose.showOpenDialog(ProjectActivator.frame);
				if (ret == JFileChooser.APPROVE_OPTION) {
					File f = choose.getSelectedFile();
					Component c = sparqlQueries.getSelectedComponent();
					if (c instanceof SparQLQuerySubPanel){
						((SparQLQuerySubPanel)c).load(f.getAbsolutePath());
					}
				}
				
			}
		});
		
		btnQuery = new JButton("Query");
		btnQuery.setMnemonic('q');
		btnQuery.addActionListener(new ActionListener() {
		    

			public void actionPerformed(ActionEvent e) {
			Component c = sparqlQueries.getSelectedComponent();
			if (c instanceof SparQLQuerySubPanel){
			    ((SparQLQuerySubPanel)c).query();
			}
		    }
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addComponent(btnQuery))
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addComponent(btnLoad, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addComponent(btnClear, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnQuery)
					.addGap(7)
					.addComponent(btnLoad, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnClear, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(207))
		);
		panel.setLayout(gl_panel);
		
		
		sparqlQueries = new JTabbedPane(JTabbedPane.LEFT);
		add(sparqlQueries, BorderLayout.CENTER);
		
		sparqlQueries.addTab("1", null, new SparQLQuerySubPanel(), null);
		
		JPanel add = new JPanel();
		add.addComponentListener(new ComponentListener() {


			public void componentShown(ComponentEvent e) {
			int lastIndex = sparqlQueries.getComponentCount() -1;
			sparqlQueries.insertTab(Integer.toString(tabcount ++), null, new SparQLQuerySubPanel(), null, lastIndex);
			sparqlQueries.setSelectedIndex(lastIndex);
		    }
		    
		    public void componentResized(ComponentEvent e) {   }
		    
		    public void componentMoved(ComponentEvent e) {   }
		    
		    public void componentHidden(ComponentEvent e) {  }
		});
		sparqlQueries.addTab("+", null, add, null);

//		this.pack();
	}

}
