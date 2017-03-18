package org.universAAL.tools.makrorecorder.swingGUI.pattern;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.universAAL.middleware.rdf.Resource;
import org.universAAL.tools.makrorecorder.osgi.pattern.Pattern;
import org.universAAL.tools.makrorecorder.swingGUI.Activator;
import org.universAAL.tools.makrorecorder.swingGUI.pattern.resource.EditResourceGUI;
import org.universAAL.tools.makrorecorder.swingGUI.pattern.resource.ResourceInfoPanel;
import org.universAAL.tools.makrorecorder.swingGUI.pattern.resource.ResourceList;

/**
*
* @author maxim djakow
*/
public class PatternPanel extends JPanel {

	private static final long serialVersionUID = 1L;


	private Pattern pattern;
	
	
	private ResourceList inputsList;
	private ResourceList outputsList;
	private ResourceInfoPanel resourceInfoPanel;
	
	
	public PatternPanel(Pattern pattern) {
		this.pattern = pattern;
		init();
	}
	
	public void reload() {
		inputsList.reload();
		outputsList.reload();
	}
	
	private void init() {
		
		setBorder(javax.swing.BorderFactory.createTitledBorder(""));
		
		setLayout(new java.awt.GridBagLayout());
		GridBagConstraints gbc;
		
		inputsList = new ResourceList(pattern.getInput());
		inputsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
				if(inputsList.getSelectedIndex()>-1) {
					Resource r = inputsList.getSelectedResource();
	            	outputsList.removeSelection();
	            	resourceInfoPanel.load(r);
	            	}
	            }
	        });
		gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.insets = new java.awt.Insets(5, 5, 5, 5);
        JPanel ilp = new JPanel(new BorderLayout());
        ilp.add(new JScrollPane(inputsList),BorderLayout.CENTER);
        ilp.setBorder(javax.swing.BorderFactory.createTitledBorder("Conditions:"));
        ilp.setMinimumSize(new java.awt.Dimension(200, 200));
        ilp.setPreferredSize(new java.awt.Dimension(200, 200));
        add(ilp,gbc);
		
		outputsList = new ResourceList(pattern.getOutput());
		outputsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
				if(outputsList.getSelectedIndex()>-1) {
					Resource r = outputsList.getSelectedResource();
	            	inputsList.removeSelection();
	            	resourceInfoPanel.load(r);
	            	}
	            }
	        });
		gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.insets = new java.awt.Insets(5, 5, 5, 5);
        JPanel olp = new JPanel(new BorderLayout());
        olp.add(new JScrollPane(outputsList),BorderLayout.CENTER);
        olp.setBorder(javax.swing.BorderFactory.createTitledBorder("Actions:"));
        olp.setMinimumSize(new java.awt.Dimension(200, 200));
        olp.setPreferredSize(new java.awt.Dimension(200, 200));
        add(olp,gbc);
		
        resourceInfoPanel = new ResourceInfoPanel();
        
        JPanel resourceEditPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton editButton = new JButton();
        editButton.setText("Edit");
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	Vector<Resource> resources;
            	int index;
            	if(inputsList.getSelectedIndex()<0) {
            		resources = pattern.getOutput();
            		index = outputsList.getSelectedIndex();
            	} else {
            		resources = pattern.getInput();
            		index = inputsList.getSelectedIndex();
            	}
            	if(index>=0) {
	        		Resource r = EditResourceGUI.edit(resources.get(index));
	        		if(r!=null) {
	    	    		resources.set(index, r);
	    	    		inputsList.reload();
	    	    		outputsList.reload();
	        		}
            	}
            }
        });
        resourceEditPanel.add(editButton);
        
        JButton deleteButton = new JButton();
        deleteButton.setText("Delete");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	if(inputsList.getSelectedIndex()>=0) {
            		if (JOptionPane.showConfirmDialog(null, "delete Resource?") == JOptionPane.YES_OPTION) {
            			pattern.getInput().remove(inputsList.getSelectedIndex());
            			inputsList.reload();
            		}
            	} else if(outputsList.getSelectedIndex()>=0) {
            		if (JOptionPane.showConfirmDialog(null, "delete Resource?") == JOptionPane.YES_OPTION) {
            			pattern.getOutput().remove(outputsList.getSelectedIndex());
            			outputsList.reload();
            		}
            	}
            }
        });
        resourceEditPanel.add(deleteButton);
        
        JButton sendButton = new JButton();
        sendButton.setText("Test");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	Activator.getMakroRecorder().sendOut(resourceInfoPanel.getResource());            		
            }
        });
        resourceEditPanel.add(sendButton);
        
        resourceInfoPanel.add(resourceEditPanel, BorderLayout.SOUTH);
        
        gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight =2;
        gbc.fill = java.awt.GridBagConstraints.BOTH;
        gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new java.awt.Insets(10, 5, 0, 5);
        add(resourceInfoPanel, gbc);
		
	}
	
	public void inputsListReload() {
		inputsList.reload();
	}
	
	public void outputsListReload() {
		outputsList.reload();
	}
}
