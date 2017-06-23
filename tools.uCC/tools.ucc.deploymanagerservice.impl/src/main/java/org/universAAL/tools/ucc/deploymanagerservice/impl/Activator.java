package org.universAAL.tools.ucc.deploymanagerservice.impl;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.net.InetAddress;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.OSGiContainer;
import org.universAAL.tools.ucc.deploymanagerservice.DeployManagerService;
import org.universAAL.tools.ucc.frontend.api.IFrontend;

public class Activator implements BundleActivator {
	private ServiceRegistration registration;
	private static IFrontend frontend = null;
	private static BundleContext context;

	public void start(BundleContext bc) throws Exception {
		context = bc;
		ModuleContext mc = OSGiContainer.THE_CONTAINER.registerModule(new Object[] { context });

		File setupProps = new File(new File(new File(mc.getConfigHome().getParentFile(), "uCC"), "setup"),
				"setup.properties");
		Dictionary<String, String> props = new Hashtable<String, String>();
		System.err.println("DEPLOYMANAGER STARTED");
		props.put("service.exported.interfaces", "*");
		props.put("service.exported.configs", "org.apache.cxf.ws");
		InetAddress thisIp = InetAddress.getLocalHost();
		// Get port of uCC from setup.properties
		Properties prop = new Properties();
		Reader reader = new FileReader(setupProps);
		prop.load(reader);
		String url = "http://" + thisIp.getHostAddress() + ":" + prop.getProperty("uccPort") + "/deploymanager";
		System.out.println("[DeployManagerServiceImpl] url:" + url);
		// props.put("org.apache.cxf.ws.address",
		// "http://localhost:9090/deploymanager");
		props.put("org.apache.cxf.ws.address", url);

		registration = bc.registerService(DeployManagerService.class.getName(), new DeployManagerServiceImpl(), props);

		getServices(context);

	}

	public void stop(BundleContext bc) throws Exception {
		registration.unregister();
	}

	private static void getServices(BundleContext bc) {
		System.out.println("[DeployManagerServiceImpl.activator.getServices]");
		if (frontend == null)
			getFrontend();

	}

	public static IFrontend getFrontend() {
		System.out.println("[DeployManagerServiceImpl.activator.getFrontend]");
		if (frontend == null) {
			ServiceReference sr = context.getServiceReference(IFrontend.class.getName());
			if (sr != null)
				frontend = (IFrontend) context.getService(sr);
			else
				System.out.println("[DeployManagerServiceImpl.activator] service reference for IFrontend is null!");
			if (frontend == null)
				System.out.println("[DeployManagerServiceImpl.activator] can not get frontend! ");
			else
				System.out.println("[DeployManagerServiceImpl.activator.getServices] got frontend ");
		}
		return frontend;
	}

}
