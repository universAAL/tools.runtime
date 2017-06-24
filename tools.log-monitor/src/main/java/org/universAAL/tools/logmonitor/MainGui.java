/*
	Copyright 2007-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institut f�r Graphische Datenverarbeitung
 */
package org.universAAL.tools.logmonitor;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Carsten Stockloew
 */
public class MainGui extends JFrame {

	private static final long serialVersionUID = 1L;

	public static JTabbedPane tabbedPane;

	/**
	 * Create the main frame.
	 */
	public MainGui() {
		super("Log Monitor");

		tabbedPane = new JTabbedPane();
		getContentPane().add(tabbedPane, BorderLayout.CENTER);

		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setSize(1000, 700);
		setVisible(true);
	}

	public void addComponent(final String title, final JComponent comp) {
		// System.out.println("Logmonitor: Adding Panel " + title);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				tabbedPane.addTab(title, comp);
			}
		});
	}
}
