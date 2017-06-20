/*
	Copyright 2007-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institut f�r Graphische Datenverarbeitung
 */

package org.universAAL.tools.logmonitor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.container.LogListener;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.OSGiContainer;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.serialization.MessageContentSerializer;

/**
 * The bundle activator to create the log monitor and register this OSGi
 * service.
 * 
 * @author Carsten Stockloew
 * 
 */
public class Activator implements BundleActivator {

	public static ModuleContext mc;
	public static BundleContext bc;
	public static MessageContentSerializer contentSerializer = null;
	public static LogMonitor lm;

	/**
	 * Start this bundle. This is not done in a separate thread so that we can
	 * get all messages even from the beginning.
	 */
	public void start(BundleContext context) throws Exception {
		bc = context;
		mc = OSGiContainer.THE_CONTAINER.registerModule(new Object[] { context });

		start();
		// context.registerService(new String[] { LogListener.class.getName() },
		// lm, null);
	}

	public void start() {
		lm = new LogMonitor();
		mc.getContainer().shareObject(mc, lm, new Object[] { LogListener.class.getName() });
	}

	public void stop(BundleContext arg0) throws Exception {
		mc.getContainer().removeSharedObject(mc, lm, new Object[] { LogListener.class.getName() });
		lm.stop();
	}

	// public static void main(String[] args) {
	// LogMonitor lm = new LogMonitor();
	// lm.log(0, "module", "pkg", "cls", "method", new Object[] { "" }, null);
	// }

	/**
	 * Get resource
	 */
	public static URL getResource(String name) {
		URL r = null;
		if (Activator.bc != null) {
			Bundle b = null;
			try {
				b = Activator.bc.getBundle();
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
			if (b.getEntry(name) != null) {
				String path = b.getEntry(name).getPath();
				r = b.getResource(path);
			}
		} else {
			try {
				r = new URL("http://depot.universaal.org/images/LogMonitor/" + name.substring(5));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		return r;
	}

	public static String serialize(Resource r) {
		if (contentSerializer == null) {
			contentSerializer = (MessageContentSerializer) mc.getContainer().fetchSharedObject(mc,
					new Object[] { MessageContentSerializer.class.getName() });
			if (contentSerializer == null)
				return "- not possible to get the serializer -";
		}

		try {
			return contentSerializer.serialize(r);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			return "<error in serialization:>\n" + sw.toString() + "\n\nfor the Resource:\n" + r.toStringRecursive();
		}
	}

	public static Resource deserialize(String s) {
		if (contentSerializer == null) {
			contentSerializer = (MessageContentSerializer) mc.getContainer().fetchSharedObject(mc,
					new Object[] { MessageContentSerializer.class.getName() });
			if (contentSerializer == null)
				return null;
		}

		return (Resource) contentSerializer.deserialize(s);
	}
}
