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

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;

import org.universAAL.middleware.container.ModuleActivator;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;

public class ProjectActivator implements ModuleActivator {

	static ModuleContext context;
	static CHeQuerrier querrier;
	static JFrame frame;
	
	public void start(ModuleContext ctxt) throws Exception {	
		context = ctxt;
		LogUtils.logDebug(context, getClass(), "start", "Starting.");
		/*
		 * uAAL stuff
		 */
		querrier = new CHeQuerrier(context);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new JFrame();
					frame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
					frame.setTitle("SPARQL Tester");
					frame.setBounds(80, 66, 714, 492);
					frame.setMinimumSize(new Dimension(420, 330));
					frame.setPreferredSize(new Dimension(420, 330));
					frame.setResizable(true);
					frame.setContentPane(new SparQLQueryPanel());
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		LogUtils.logDebug(context, getClass(), "start", "Started.");
	}


	public void stop(ModuleContext ctxt) throws Exception {
		LogUtils.logDebug(context, getClass(), "stop", "Stopping.");
		/*
		 * close uAAL stuff
		 */
		querrier = null;
		frame.dispose();
		frame = null;
		LogUtils.logDebug(context, getClass(), "stop", "Stopped.");

	}

}
