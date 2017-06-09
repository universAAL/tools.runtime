/*
	Copyright 2007-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institut f�r Graphische Datenverarbeitung
 */
package org.universAAL.tools.logmonitor.service_bus_matching;

import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;

import javax.swing.JComponent;

import org.universAAL.middleware.container.LogListener;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.service.DefaultServiceCaller;
import org.universAAL.middleware.service.ServiceBus;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.service.owls.profile.ServiceProfile;
import org.universAAL.tools.logmonitor.Activator;
import org.universAAL.tools.logmonitor.LogListenerEx;
import org.universAAL.tools.logmonitor.msgflow.gui.MsgFlowPanel;
import org.universAAL.tools.logmonitor.service_bus_matching.Matchmaking.SingleMatching;
import org.universAAL.tools.logmonitor.service_bus_matching.gui.Gui;
import org.universAAL.tools.logmonitor.util.ProfileInfo;

/**
 * 
 * @author Carsten Stockloew
 * 
 */
public class LogMonitor implements LogListenerEx {

	public Gui gui = new Gui();

	public static LogMonitor instance;
	public static boolean checkModule = true;

	// a service caller is needed to retrieve service profiles
	DefaultServiceCaller caller = new DefaultServiceCaller(Activator.mc);

	// list of Matchmaking
	private LinkedList<Matchmaking> matchmakings = new LinkedList<Matchmaking>();

	// maps ID of the thread (Long) to Matchmaking
	private Hashtable<Long, Matchmaking> threads = new Hashtable<Long, Matchmaking>();

	// maps URI of service of profile to ServiceProfile
	// key+"Process" = process URI
	private Hashtable<String, ProfileInfo> profiles = new Hashtable<String, ProfileInfo>();

	public LogMonitor() {
		instance = this;
		caller.setLabel("Log Monitor Dummy Service Caller");
		caller.setComment(
				"The Log Monitor requires a service caller to retrieve service profiles from the bus. It is not used to call services.");
	}

	public Matchmaking getMatchmaking(int index) {
		return (Matchmaking) matchmakings.get(index);
	}

	/*-
	 * @see org.universAAL.middleware.container.LogListener#log(int,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.Object[], java.lang.Throwable)
	 * 
	 * The general format that we assume:
	 * 0:     a String containing something like "Mismatch detected: "
	 * 1:     a reason (very short)
	 * 2*n+2: information text      (for n=0 something like " error code: ")
	 *        the text starts with "\n"
	 * 2*n+3: information parameter (for n=0: integer with error ID)
	 * l-3:   something like " detailed error message: "
	 * l-2:   the detailed error message
	 * l-1:   log ID
	 * 
	 * error code overview:
	 * 0-999:     data representation
	 * 1000-1999: service bus
	 * 2000-2999: context bus
	 * 
	 * error code details:
	 * 1000: ProcessResult "requested output not available" (none available)
	 * 1001: ProcessResult "requested output not available"
	 * 1010: ProcessResult "offered effect not requested" (none available)
	 * 1011: ProcessResult "requested effect not offered" (none available)
	 * 1012: ProcessResult "number of effects do not match"
	 * 1013: ProcessResult "requested effect not offered"
	 * 1020: ServiceRealization "no subset relationship for restricted property"
	 * 1021: ServiceRealization "no subset relationship for restricted property"
	 * 1022: ServiceRealization "no subset relationship for restricted property"
	 * 1023: ServiceRealization "input in request not defined in offer"
	 * 1030: ServiceStrategy "No service available."
	 * 1031: ServiceStrategy "input in profile not given in request"
	 */
	public void log(int logLevel, String module, String pkg, String cls, String method, Object[] msgPart, Throwable t) {
		if (logLevel == LogListener.LOG_LEVEL_TRACE) {
			// System.out.println("--TRACE: " + module + " " + pkg + " " + cls
			// + " " + method + " " + msgPart);

			if (checkModule && !"mw.bus.service.osgi".equals(module))
				return;

			Long id = null;
			try {
				id = (Long) msgPart[msgPart.length - 1];
			} catch (Exception e) {
			}
			if (id == null)
				return;

			if ("ServiceStrategy".equals(cls))
				handleServiceStrategyMessage(id, msgPart);
			else if ("ServiceRealization".equals(cls))
				handleServiceRealizationMessage(id, msgPart);
			else if ("ProcessResult".equals(cls))
				handleProcessResultMessage(id, msgPart, method);
		}
	}

	private void handleProcessResultMessage(Long id, Object[] msgPart, String method) {
		if (ServiceBus.LOG_MATCHING_MISMATCH.equals(msgPart[0])) {
			Matchmaking m = getMatchmaking(id, "handleProcessResultMessage");

			if ("checkEffects".equals(method)) {
				SingleMatching single = (SingleMatching) m.matchings.getLast();
				single.processStandardMessage(msgPart);
				single.reason = SingleMatching.REASON_EFFECT;
				if (single.code == 1013)
					single.effect = (Resource) msgPart[3];
			} else if ("checkOutputBindings".equals(method)) {
				SingleMatching single = (SingleMatching) m.matchings.getLast();
				single.processStandardMessage(msgPart);
				single.reason = SingleMatching.REASON_OUTPUT;
				single.outputURI = (String) msgPart[3];
				single.output = m.request.getRequiredOutputs()[((Integer) msgPart[5]).intValue()];
			} else {
				System.out.println("ERROR in matching log tool: handleProcessResultMessage, unknown method.");
			}
		}
	}

	private void handleServiceRealizationMessage(Long id, Object[] msgPart) {
		if (ServiceBus.LOG_MATCHING_MISMATCH.equals(msgPart[0])) {
			Matchmaking m = getMatchmaking(id, "handleServiceRealizationMessage");
			SingleMatching single = (SingleMatching) m.matchings.getLast();
			single.processStandardMessage(msgPart);
			single.reason = SingleMatching.REASON_INPUT;
			if (single.code != 1023)
				single.restrictedProperty = (String) msgPart[3];
		}
	}

	private void handleServiceStrategyMessage(Long id, Object[] msgPart) {
		// System.out.println("--- ServiceBus.LOG_MATCHING_SUCCESS: "
		// + ServiceBus.LOG_MATCHING_SUCCESS);

		if (ServiceBus.LOG_MATCHING_START.equals(msgPart[0])) {
			// start matching a request
			Resource request = (Resource) msgPart[1];
			startMatching(id, request);
		} else if (ServiceBus.LOG_MATCHING_PROFILE.equals(msgPart[0])) {
			// start matchmaking for one profile with the request
			String profileServiceClassURI = (String) msgPart[1];
			String profileServiceURI = (String) msgPart[2];
			String profileProviderURI = (String) msgPart[3];
			startMatching(id, profileServiceClassURI, profileServiceURI, profileProviderURI);
		} else if (ServiceBus.LOG_MATCHING_SUCCESS.equals(msgPart[0])) {
			// matching for one profile with the request was successful
			endMatching(id, true);
		} else if (ServiceBus.LOG_MATCHING_NOSUCCESS.equals(msgPart[0])) {
			// matching for one profile with the request was not successful
			endMatching(id, false);
		} else if (ServiceBus.LOG_MATCHING_PROFILES_END.equals(msgPart[0])) {
			// the matching with each single profile is done, next step is the
			// filtering
			endProfileMatching(id, (Integer) msgPart[2]);
		} else if (ServiceBus.LOG_MATCHING_MISMATCH.equals(msgPart[0])) {
			Matchmaking m = getMatchmaking(id, "handleServiceStrategyMessage");
			String serviceURI = (String) msgPart[3];
			for (SingleMatching single : m.matchings) {
				if (single.serviceURI.equals(serviceURI)) {
					single.processStandardMessage(msgPart);
					single.reason = SingleMatching.REASON_INPUT;
					single.isOffer = true;
					single.success = Boolean.FALSE;
					break;
				}
			}
		} else if (ServiceBus.LOG_MATCHING_PROVIDER_END.equals(msgPart[0])) {
			endProviderMatching(id, msgPart);
		} else if (ServiceBus.LOG_MATCHING_END.equals(msgPart[0])) {
			// matching done
			if (msgPart.length == 7)
				endMatching(id, msgPart);
			else
				endMatching(id, (Integer) msgPart[2]);
		}
	}

	private Matchmaking getMatchmaking(Long id, String methodName) {
		Matchmaking m = (Matchmaking) threads.get(id);
		if (m == null)
			throw new IllegalArgumentException("ERROR in matching log tool: " + methodName + ", id not available.");
		return m;
	}

	private String getProfileURI(ServiceProfile profile) {
		return profile.getTheService().getURI();
	}

	private void startMatching(Long id, Resource request) {
		// start matching a request
		if (threads.containsKey(id)) {
			System.out.println("ERROR in matching log tool: id already available.");
			return;
		}

		Matchmaking m = new Matchmaking();
		m.date = new Date();
		m.serializedRequest = Activator.serialize(request);
		m.request = (ServiceRequest) Activator.deserialize(m.serializedRequest);
		if (m.request == null)
			return;
		m.serviceURI = m.request.getRequestedService().getType();
		matchmakings.add(m);
		threads.put(id, m);
	}

	private void startMatching(Long id, String profileServiceClassURI, String profileServiceURI,
			String profileProviderURI) {
		// start matchmaking for one profile with the request
		Matchmaking m = getMatchmaking(id, "startMatching");

		// find the profile; if not available, query it
		if (!profiles.containsKey(profileServiceURI)) {
			// this profile is new -> query all profiles
			ServiceProfile[] allProfiles = caller.getMatchingService(profileServiceClassURI);
			for (int i = 0; i < allProfiles.length; i++) {
				// we have to check for the service profile URI, we cannot just
				// add all service profiles because otherwise we cannot get the
				// provider URI
				String queriedProfileServiceURI = getProfileURI(allProfiles[i]);
				if (queriedProfileServiceURI != null && queriedProfileServiceURI.equals(profileServiceURI))
					profiles.put(queriedProfileServiceURI, new ProfileInfo(allProfiles[i], profileProviderURI));
			}
			if (!profiles.containsKey(profileServiceURI)) {
				System.out.println("ERROR in matching log tool: matching with a profile that does not exist.");
				return;
			}
		}

		// add this matching to the list of matchings for a request
		SingleMatching single = m.new SingleMatching();
		single.serviceURI = profileServiceURI;
		m.matchings.add(single);
	}

	private void endMatching(Long id, boolean success) {
		Matchmaking m = getMatchmaking(id, "endMatching, success: " + success);
		SingleMatching single = (SingleMatching) m.matchings.getLast();
		single.success = Boolean.valueOf(success);
	}

	private void endMatching(Long id, Object[] msgPart) {
		// no service has been found
		Matchmaking m = getMatchmaking(id, "endMatching (no-service)");
		m.registeredServicesAvailable = false;
		m.numMatches = 0;
		m.success = Boolean.FALSE;
		finish(id, m);
	}

	private void endProfileMatching(Long id, Integer numMatches) {
		Matchmaking m = getMatchmaking(id, "endProfileMatching");
		m.numMatchingProfiles = numMatches.intValue();
	}

	private void endProviderMatching(Long id, Object[] msgPart) {
		Matchmaking m = getMatchmaking(id, "endProviderMatching");
		m.numMatchingAfterProviderFilter = (Integer) msgPart[2];
		for (int i = 4; i < msgPart.length - 1; i++) {
			m.matchingsProvFilt.add((String) msgPart[i]);
		}
	}

	private void endMatching(Long id, Integer numMatches) {
		Matchmaking m = getMatchmaking(id, "endMatching (final)");

		m.numMatches = numMatches.intValue();
		if (m.numMatches != 0)
			m.success = Boolean.TRUE;
		else
			m.success = Boolean.FALSE;

		finish(id, m);
	}

	private void finish(Long id, Matchmaking m) {
		// print(m);
		threads.remove(id);
		int ref = gui.notify(m);
		MsgFlowPanel.instance.addMatching(m, ref);
	}

	public ProfileInfo getProfile(String uri) {
		return (ProfileInfo) profiles.get(uri);
	}

	public JComponent getComponent() {
		return gui;
	}

	public String getTitle() {
		return "Service Matchmaking";
	}

	@Override
	public void stop() {
	}
}
