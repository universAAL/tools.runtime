package org.universAAL.tools.makrorecorder.swingGUI.pattern.resource;

import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.universAAL.middleware.rdf.Resource;
import org.universAAL.tools.makrorecorder.swingGUI.Activator;

/**
 *
 * @author maxim djakow
 */
public class EditResourceGUI extends Dialog {

	private static final long serialVersionUID = 1L;

	Resource resource;

	JTextArea ta;
	JButton tb;
	JButton sb;
	JButton cb;

	public EditResourceGUI(Resource resource) {
		super(null, true);

		setLayout(new GridBagLayout());

		GridBagConstraints gbc;

		ta = new JTextArea();
		ta.setColumns(20);
		ta.setLineWrap(true);
		ta.setRows(5);
		ta.setText(Activator.getSerializer().serialize(resource));
		gbc = new java.awt.GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.insets = new java.awt.Insets(5, 5, 5, 5);
		add(new JScrollPane(ta), gbc);

		tb = new JButton();
		tb.setText("Test");
		tb.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Activator.getMakroRecorder().sendOut((Resource) Activator.getSerializer().deserialize(ta.getText()));
			}
		});
		gbc = new java.awt.GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.insets = new java.awt.Insets(5, 5, 5, 5);
		add(tb, gbc);

		sb = new JButton();
		sb.setText("Save");
		sb.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setResource((Resource) Activator.getSerializer().deserialize(ta.getText()));
				dispose();
			}
		});
		gbc = new java.awt.GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.insets = new java.awt.Insets(5, 5, 5, 5);
		add(sb, gbc);

		cb = new JButton();
		cb.setText("cancel");
		cb.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setResource(null);
				dispose();
			}
		});
		gbc = new java.awt.GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.insets = new java.awt.Insets(5, 5, 5, 5);
		add(cb, gbc);
	}

	public static Resource edit(Resource resource) {
		EditResourceGUI gui = new EditResourceGUI(resource);
		gui.setVisible(true);
		return gui.getResource();
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}
}
