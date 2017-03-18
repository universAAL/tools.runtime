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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.tools.logmonitor.Utils;
import org.universAAL.tools.logmonitor.service_bus_matching.Matchmaking;
import org.universAAL.tools.logmonitor.service_bus_matching.Matchmaking.SingleMatching;
import org.universAAL.tools.logmonitor.util.ProfileInfo;

/**
 * 
 * @author Carsten Stockloew
 * 
 */
public class FlowData {

    // the exact time when the message occurs (e.g. when the matchmaking starts)
    public Date date;

    // short descriptive name
    public String name;

    // the bus: 0=service, 1=context, 2=ui
    public int bus;

    // bus member ID of the source
    public String source;

    // index for the source bus member in the list shown in the ui
    public int idxSource;

    // bus member ID of the destination
    public String dest[];

    // index for the destination bus members in the list shown in the ui
    public int idxDest[];

    // internal reference;
    // for service bus used as reference to the matchmaking tab
    public Object ref = null;
    
    public int y;

    public FlowData(ContextEvent evt) {
	date = new Date(evt.getTimestamp());
	System.out.println(evt.getTimestamp() + "  "
		+ Utils.getDateString(date));
	bus = 1;
	name = getShortURI(evt.getSubjectURI()) + " "
		+ getShortURI(evt.getRDFPredicate()) + " "
		+ getShortURI(evt.getRDFObject().toString());
	// System.out.println(evt.toStringRecursive());
	// we don't have the source
	// source = null;
	dest = new String[0];
    }

    private String getShortURI(String uri) {
	int idx = uri.lastIndexOf('#');
	if (idx == -1)
	    return uri;

	return uri.substring(idx + 1);
    }

    public FlowData(Matchmaking m, int ref) {
	date = m.date;
	bus = 0;
	if (m.hasOneResult()) {
	    name = m.getResult();
	} else {
	    name = getShortURI(m.serviceURI) + " " + m.getResult();
	}
	source = ((Resource) (m.request
		.getProperty(ServiceRequest.PROP_uAAL_SERVICE_CALLER)))
		.getURI();
	List<String> providers = new ArrayList<String>();
	for (SingleMatching single : m.matchings) {
	    if (single.success != null) {
		if (single.success.booleanValue()) {
		    ProfileInfo info = org.universAAL.tools.logmonitor.service_bus_matching.LogMonitor.instance
			    .getProfile(single.serviceURI);
		    if (!(info == null) && !(info.providerURI == null))
			providers.add(info.providerURI);
		}
	    }
	}
	dest = providers.toArray(new String[providers.size()]);
	this.ref = ref;
    }
}
