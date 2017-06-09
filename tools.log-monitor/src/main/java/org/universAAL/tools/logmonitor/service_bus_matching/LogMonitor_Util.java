/*
	Copyright 2007-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institut f�r Graphische Datenverarbeitung
 */
package org.universAAL.tools.logmonitor.service_bus_matching;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.universAAL.middleware.owl.MergedRestriction;
import org.universAAL.middleware.rdf.PropertyPath;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.service.owl.Service;
import org.universAAL.middleware.service.owls.process.OutputBinding;
import org.universAAL.middleware.service.owls.process.ProcessEffect;
import org.universAAL.middleware.service.owls.profile.ServiceProfile;
import org.universAAL.tools.logmonitor.service_bus_matching.Matchmaking.SingleMatching;

/**
 * A utility class with methods to, e.g., print info to stdout.
 * 
 * @author Carsten Stockloew
 */
public class LogMonitor_Util {
	public Resource findInGraph(Resource root, String uri, HashMap visited) {
		System.out.println("findInGraph: " + root + "  " + uri);
		if (uri == null || root == null)
			return null;
		if (uri.equals(root.getURI()))
			return root;
		if (visited == null)
			visited = new HashMap();

		Resource el = (Resource) visited.get(root.getURI());
		if (el != null)
			return null;
		visited.put(root.getURI(), root);

		Enumeration e = root.getPropertyURIs();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			Object val = root.getProperty(key);

			if (val instanceof Resource) {
				Resource retval = findInGraph((Resource) val, uri, visited);
				if (retval != null)
					return retval;
			} else if (val instanceof List) {
				Iterator iter = ((List) val).iterator();
				while (iter.hasNext()) {
					Object o = iter.next();
					if (o instanceof Resource) {
						Resource retval = findInGraph((Resource) o, uri, visited);
						if (retval != null)
							return retval;
					}
				}
			}
		}

		return null;
	}

	// -------------------------------------
	public void print(Matchmaking m) {
		System.out.println("Matchmaking for service " + m.serviceURI + ": " + m.success);
		LinkedList l = m.matchings;
		for (Iterator it = l.iterator(); it.hasNext();) {
			SingleMatching s = (SingleMatching) it.next();
			System.out.println("   matching with offer " + s.serviceURI);
			System.out.print("      matching ");
			if (s.success.booleanValue())
				System.out.println("successful");
			else {
				System.out.println("NOT successful");

				switch (s.reason) {
				case SingleMatching.REASON_INPUT:
					System.out.println(
							"      reason (input): input parameters do not match for property " + s.restrictedProperty);
					break;
				case SingleMatching.REASON_OUTPUT:
					System.out.println("      reason (output): " + getMessage(s.msgPart));
					break;
				case SingleMatching.REASON_EFFECT:
					System.out.println("      reason (effect): " + getMessage(s.msgPart));
					break;
				default:
					System.out.println("      reason: unknown");
				}
			}
		}
	}

	public static String getMessage(Object[] msgPart) {
		StringBuffer sb = new StringBuffer(256);
		if (msgPart != null)
			for (int i = 0; i < msgPart.length - 1; i++)
				sb.append(msgPart[i]);
		return sb.toString();
	}

	// -------------------------------------

	// get service and instance level restrictions
	private String getServiceString(Service srv, boolean shortForm) {
		String s = "";

		s += URI.get(srv.getType(), shortForm) + "\n";
		if (srv != null) {
			String[] props = srv.getRestrictedPropsOnInstanceLevel();
			if (props.length != 0) {
				s += "  instance level restrictions:\n";
				for (int i = 0; i < props.length; i++) {
					MergedRestriction instRestr = srv.getInstanceLevelRestrictionOnProp(props[i]);
					s += CEStringUtil.toString("    ", instRestr, shortForm);
				}
			}
		}

		return s;
	}

	private String getEffectsString(Resource[] effects, boolean shortForm) {
		String s = "";

		if (effects.length != 0) {
			s += "  effect:\n";
			for (int i = 0; i < effects.length; i++) {
				Resource r = effects[i];
				PropertyPath path = (PropertyPath) r.getProperty(ProcessEffect.PROP_PROCESS_AFFECTED_PROPERTY);
				Object o = r.getProperty(ProcessEffect.PROP_PROCESS_PROPERTY_VALUE);

				if (ProcessEffect.TYPE_PROCESS_CHANGE_EFFECT.equals(r.getType())) {
					s += "    change effect:\n";
					s += "      on path: " + getPP(path, shortForm) + "\n";
					s += "      for value: " + o.toString() + "\n";
				} else if (ProcessEffect.TYPE_PROCESS_ADD_EFFECT.equals(r.getType())) {
					s += "    add effect:\n";
					s += "      on path: " + getPP(path, shortForm) + "\n";
					s += "      for value: " + o.toString() + "\n";
				} else if (ProcessEffect.TYPE_PROCESS_REMOVE_EFFECT.equals(r.getType())) {
					s += "    remove effect:\n";
					s += "      on path: " + getPP(path, shortForm) + "\n";
				} else {
					s += "    unknown: " + r.toStringRecursive("    ", false, null);
				}
			}
		}

		return s;
	}

	private String getSingleIOString(Resource r, boolean shortForm) {
		String s = "";

		if (OutputBinding.TYPE_OWLS_OUTPUT_BINDING.equals(r.getType())) {
			PropertyPath path = null;
			Object o = r.getProperty(OutputBinding.PROP_OWLS_BINDING_VALUE_FORM);
			if (o instanceof PropertyPath)
				// SimpleBinding
				path = (PropertyPath) o;
			if (path != null) {
				// the output info comes from
				// ServiceRequest.addRequiredOutput
				s += "    property path: " + getPP(path, shortForm) + "\n";
			} else {
				s += "    unknown output binding: " + r.getURI() + "\n";
			}
		} else {
			s += "    unknown: " + r.toStringRecursive("    ", false, null);
		}

		return s;
	}

	public String getProfileString(ServiceProfile profile, boolean shortForm) {
		Iterator it;
		String s = "Service profile ";

		// get service info
		s += getServiceString(profile.getTheService(), shortForm);

		// get input info
		it = profile.getInputs();
		if (it.hasNext()) {
			s += "  input:\n";
			while (it.hasNext())
				s += getSingleIOString((Resource) it.next(), shortForm);
		}

		// get effect info
		s += getEffectsString(profile.getEffects(), shortForm);

		// get output info
		it = profile.getOutputs();
		if (it.hasNext()) {
			s += "  output:\n";
			while (it.hasNext())
				s += getSingleIOString((Resource) it.next(), shortForm);
		}

		return s;
	}

	public String getRequestString(ServiceRequest request, boolean shortForm) {
		String s = "Requesting Service ";

		// get service info
		s += getServiceString(request.getRequestedService(), shortForm);

		// get effect info
		s += getEffectsString(request.getRequiredEffects(), shortForm);

		// get output info
		Resource[] output = request.getRequiredOutputs();
		if (output.length != 0) {
			s += "  output:\n";
			for (int i = 0; i < output.length; i++)
				s += getSingleIOString(output[i], shortForm);
		}

		return s;
	}

	private String getPP(PropertyPath path, boolean shortForm) {
		String s = "";
		if (path == null)
			return s;
		String[] p = path.getThePath();
		for (int i = 0; i < p.length; i++) {
			s += URI.get(p[i], shortForm);
			if (i < p.length - 1)
				s += ", ";
		}
		return s;
	}
}
