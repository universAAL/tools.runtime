/*******************************************************************************
 * Copyright 2013 Universidad Politécnica de Madrid
 * Copyright 2013 Fraunhofer-Gesellschaft - Institute for Computer Graphics Research
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

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.undo.UndoManager;

/**
 * @author amedrano
 *
 */
public class SparQLQuerySubPanel extends JPanel implements Runnable {

	private JTextArea query;
	private JTextArea result;
	private Color defaultBG;

	/**
	 * Create the panel.
	 */
	public SparQLQuerySubPanel() {
		setLayout(new BorderLayout(0, 0));

		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.5);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		add(splitPane, BorderLayout.CENTER);

		JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);

		query = new JTextArea();
		scrollPane.setViewportView(query);
		query.getDocument().addUndoableEditListener(new UndoManager());

		JScrollPane scrollPane_1 = new JScrollPane();
		splitPane.setRightComponent(scrollPane_1);

		result = new JTextArea();
		result.setEditable(false);
		scrollPane_1.setViewportView(result);
		defaultBG = result.getBackground();
	}

	public void query() {
		new Thread(this).start();
	}

	/** {@ inheritDoc} */
	public void run() {
		String text = this.query.getText();
		if (!text.isEmpty()) {
			try {
				String res = ProjectActivator.querrier.unserialisedQuery(text);
				this.result.setBackground(defaultBG);
				this.result.setText(res);
			} catch (Exception e) {
				this.result.setText(e.getMessage());
				this.result.setBackground(Color.red);
			}
		}
	}

	/**
	 * @param absolutePath
	 */
	public void load(String absolutePath) {

		String load;

		try {
			load = readFile(absolutePath, Charset.defaultCharset());
		} catch (IOException e) {
			load = null;
		}

		if (load != null) {
			this.query.setText(load);
		}

	}

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	/**
	 *
	 */
	public void clear() {
		this.query.setText("");
		this.result.setText("");
		this.result.setBackground(defaultBG);
	}

	/**
	 * @param absolutePath
	 */
	public void saveQuery(String absolutePath) {
		PrintWriter out;
		try {
			out = new PrintWriter(absolutePath);
			out.println(this.query.getText());
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param absolutePath
	 */
	public void saveResult(String absolutePath) {
		PrintWriter out;
		try {
			out = new PrintWriter(absolutePath);
			out.println(this.result.getText());
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
