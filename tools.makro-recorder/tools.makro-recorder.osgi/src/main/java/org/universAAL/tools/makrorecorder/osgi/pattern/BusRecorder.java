package org.universAAL.tools.makrorecorder.osgi.pattern;

import org.osgi.framework.ServiceRegistration;
import org.universAAL.middleware.container.LogListener;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.tools.makrorecorder.osgi.Activator;

/**
 *
 * @author maxim djakow
 */
public class BusRecorder implements LogListener {

	private Pattern pattern = null;

	ModuleContext context = null;
	private ServiceRegistration sr = null;

	public void start() {
		if (sr == null) {
			sr = Activator.getBundleContext().registerService(new String[] { LogListener.class.getName() }, this, null);
			System.out.println(" -- BusRecorder registered");
		}
	}

	public void stop() {
		if (sr != null) {
			sr.unregister();
			sr = null;
		}
	}

	public BusRecorder(Pattern pattern) {
		this.pattern = pattern;
	}

	public void log(int logLevel, String module, String pkg, String cls, String method, Object[] msgPart, Throwable t) {
		if (pattern != null) {
			if ("assessContentSerialization".equals(method)) {
				if ("ContextBusImpl".equals(cls) || "ServiceBusImpl".equals(cls)) {
					addToPattern(convertToResource(msgPart[1]));
				}
			}
		}
	}

	private Object extractResource(Object[] msgPart) {
		if (msgPart != null) {
			if (msgPart.length >= 5) {
				if (msgPart[3] instanceof String) {
					if (((String) msgPart[3]).contains(":")) {
						return msgPart[4];
					}
				}
			}
		}
		return null;
	}

	private Resource convertToResource(Object o) {
		if (o != null) {
			if (o instanceof String) {
				return (Resource) Activator.getSerializer().deserialize((String) o);
			}
		}
		return null;
	}

	private void addToPattern(Resource r) {
		if (r != null) {
			if (r instanceof ContextEvent) {
				pattern.addInput(r);
			} else if (r instanceof ServiceRequest) {
				pattern.addOutput(r);
			}
		}
	}

}
