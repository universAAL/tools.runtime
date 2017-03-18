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
package org.universAAL.tools.logmonitor.msgflow;

import javax.swing.JComponent;

import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.context.ContextEventPattern;
import org.universAAL.middleware.context.ContextSubscriber;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.tools.logmonitor.Activator;
import org.universAAL.tools.logmonitor.BusMemberListener;
import org.universAAL.tools.logmonitor.LogListenerEx;
import org.universAAL.tools.logmonitor.MemberData;
import org.universAAL.tools.logmonitor.msgflow.gui.MsgFlowGui;

/**
 * 
 * @author Carsten Stockloew
 * 
 */
public class LogMonitor extends ContextSubscriber implements LogListenerEx,
	BusMemberListener {

    private MsgFlowGui gui = new MsgFlowGui();
    private int msgCounter = 0;

    public LogMonitor() {
	super(Activator.mc,
		new ContextEventPattern[] { new ContextEventPattern() });
	setLabel("Log Monitor Context Subscriber for Message Flow");
	setComment("The Log Monitor requires a context subscriber with an empty pattern to receive all context events from nodes in the AAL Space.");
    }

    public void log(int logLevel, String module, String pkg, String cls,
	    String method, Object[] msgPart, Throwable t) {
    }

    public JComponent getComponent() {
	return gui;
    }

    public String getTitle() {
	return "Message Flow";
    }

    public void stop() {
    }

    @Override
    public void add(MemberData member) {
	MemberDataEx d = new MemberDataEx(member, msgCounter);
	gui.panel.addMember(d);
    }

    @Override
    public void remove(String busMemberID) {
	gui.panel.stopMember(busMemberID, msgCounter);
    }

    @Override
    public void regParamsAdded(String busMemberID, Resource[] params) {
	// nothing to do
    }

    @Override
    public void regParamsRemoved(String busMemberID, Resource[] params) {
	// nothing to do
    }

    @Override
    public void handleContextEvent(ContextEvent event) {
	gui.panel.addEvent(event);
    }

    @Override
    public void communicationChannelBroken() {
    }
}
