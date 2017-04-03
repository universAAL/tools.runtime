package org.universAAL.ucc.service.manager;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;
import org.universAAL.middleware.service.DefaultServiceCaller;
import org.universAAL.middleware.service.ServiceCaller;
//import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.ucc.api.IDeinstaller;
import org.universAAL.ucc.api.IInstaller;
import org.universAAL.ucc.client.util.UstoreUtil;
import org.universAAL.ucc.database.aalspace.DataAccess;
import org.universAAL.ucc.frontend.api.IFrontend;
import org.universAAL.ucc.frontend.api.impl.FrontendImpl;
import org.universAAL.ucc.model.jaxb.EnumObject;
import org.universAAL.ucc.model.jaxb.OntologyInstance;
import org.universAAL.ucc.model.jaxb.StringValue;
import org.universAAL.ucc.model.jaxb.Subprofile;
import org.universAAL.ucc.database.parser.ParserService;
import org.universAAL.ucc.service.api.IServiceManagement;
import org.universAAL.ucc.service.api.IServiceModel;
import org.universAAL.ucc.service.api.IServiceRegistration;
import org.universAAL.ucc.service.impl.Model;
//import org.universAAL.ucc.webconnection.WebConnector;
//import org.universAAL.ucc.subscriber.SensorEventSubscriber;

public class Activator implements BundleActivator {
	private static IInstaller installer;
	private static IDeinstaller deinstaller;
	private static ServiceReference ref;
	private static ServiceReference dRef;
	public static BundleContext bc;
	private static ServiceRegistration regis;
	private static IServiceManagement mgmt;
	private static IServiceModel model;
	private static IServiceRegistration reg;
	private static ModuleContext mContext;
	private static String sessionKey;
	private UstoreUtil client;
	private static DataAccess dataAccess;
	private static ParserService parserService;
	private static File configHome;
	private static File tempUsrvFiles;	// tempUsrvFiles
	private static File setupProps;	// setup/setup.properties
	private static ServiceCaller sc;

	public void start(BundleContext context) throws Exception {
		Activator.bc = context;
		mContext = uAALBundleContainer.THE_CONTAINER
			.registerModule(new Object[] { context });

		configHome = mContext.getConfigHome();
		tempUsrvFiles = new File(configHome, "tempUsrvFiles");
//		 client = new UstoreUtil();
		ServiceReference ref = bc.getServiceReference(DataAccess.class
				.getName());
		dataAccess = (DataAccess) bc.getService(ref);
		// Setting setup properties in etc/ucc directory
		File confHome = new File(configHome, "setup");
		if (!confHome.exists()) {
			confHome.mkdir();
		}
		setupProps = new File(confHome, "setup.properties");
		if (!setupProps.exists()) {
			// Setting default values for setup configuration
			Properties prop = new Properties();
			prop.setProperty("admin", "admin");
			prop.setProperty("pwd", "uAAL");
			prop.setProperty("uccPort", "9090");
			prop.setProperty("uccUrl", "ucc-universaal.no-ip.org");
			prop.setProperty(
					"shopUrl",
					"srv-ustore.haifa.il.ibm.com/webapp/wcs/stores/servlet/TopCategories_10001_10001");
			if (Locale.getDefault() == Locale.GERMAN) {
				prop.setProperty("lang", "de");
				Locale.setDefault(Locale.GERMAN);
			} else {
				prop.setProperty("lang", "en");
				Locale.setDefault(Locale.ENGLISH);
			}
			System.err.println(Locale.getDefault());
			System.err.println(confHome.getAbsolutePath());
			Writer in = new FileWriter(new File(confHome.getAbsolutePath(),
					"setup.properties"));
			prop.store(in, "Setup properties for initial setup of uCC");
			in.close();
		}

		// Write Techician/Deployer into AALSpace
		OntologyInstance ont = new OntologyInstance();
		ont.setId("admin");
		ont.setType("User");
		Subprofile sub = new Subprofile();
		StringValue name = new StringValue();
		name.setId(false);
		name.setLabel("Username:");
		name.setName("username");
		name.setRequired(true);
		name.setValue("admin");
		sub.getSimpleObjects().add(name);

		StringValue pass = new StringValue();
		pass.setId(false);
		pass.setLabel("Password:");
		pass.setName("password");
		pass.setRequired(true);
		pass.setValue("uAAL");
		sub.getSimpleObjects().add(pass);
		EnumObject role = new EnumObject();
		role.setLabel("Role:");
		role.setRequired(true);
		role.setSelectedValue("DEPLOYER");
		role.setTreeParentNode(true);
		role.setType("userRole");
		sub.getEnums().add(role);
		ont.getSubprofiles().add(sub);
		dataAccess.saveUserDataInCHE(ont);

		if (!tempUsrvFiles.exists()) {
		    tempUsrvFiles.mkdir();
		}

		ref = context.getServiceReference(IInstaller.class.getName());
		installer = (IInstaller) context.getService(ref);

		//
		dRef = context.getServiceReference(IDeinstaller.class.getName());
		deinstaller = (IDeinstaller) context.getService(dRef);

		try {
			regis = bc.registerService(IFrontend.class.getName(),
					new FrontendImpl(), null);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		model = new Model();
		context.registerService(new String[] { IServiceModel.class.getName() },
				model, null);
		mgmt = model.getServiceManagment();

		reg = model.getServiceRegistration();

		// Get SessionKey from uStore
		if (client != null && client.getSessionKey() != null) {
			sessionKey = client.getSessionKey();
			if (sessionKey == null || sessionKey.equals("")) {
				System.err
						.println("No Session key when trying to setup connection to uStore");
			} else {
				System.err.println("WS-ANSWER: " + sessionKey);
				client.registerUser(sessionKey);
			}
		}

		ServiceReference sr = context.getServiceReference(ParserService.class
				.getName());
		parserService = (ParserService) context.getService(sr);

		// ServiceCaller init
		sc = new DefaultServiceCaller(mContext);

		// Dedication to Gema :D
		System.err.println(" ");
		System.err
				.println("\033[36m--------------------------------------------------------------------------");
		System.err.println(" ");
		System.err
				.println("This programm is dedicated to a mad and unique person, whose name starts with G.:P");
		System.err
				.println("If you get this message, you could successfully install and run uCC. :)");
		System.err.println(" ");
		System.err.println("Greetings from Germany to Spain :D @>->-");
		System.err.println(" ");
		System.err
				.println("----------------------------------------------------------------------------------");
		System.err.println("\033[37m ");
		System.err.println(" ");
				
		
	}

	public static String getSessionKey() {
		return sessionKey;
	}

	public static IInstaller getInstaller() {
		if (installer == null) {
			installer = (IInstaller) bc.getService(ref);
		}
		return installer;
	}

	public static IDeinstaller getDeinstaller() {
		if (deinstaller == null) {
			deinstaller = (IDeinstaller) bc.getService(dRef);
		}
		return deinstaller;
	}

	public static IServiceManagement getMgmt() {
		return mgmt;
	}

	public static void setMgmt(IServiceManagement mgmt) {
		Activator.mgmt = mgmt;
	}

	public static IServiceModel getModel() {
		return model;
	}

	public static void setModel(IServiceModel model) {
		Activator.model = model;
	}

	public static IServiceRegistration getReg() {
		return reg;
	}

	public static void setReg(IServiceRegistration reg) {
		Activator.reg = reg;
	}

	public void stop(BundleContext context) throws Exception {
		Activator.bc = null;
		regis.unregister();
		deleteFiles(tempUsrvFiles);
//		WebConnector.getInstance().stopListening();
	}

	private void deleteFiles(File path) {
		File[] files = path.listFiles();
		for (File del : files) {
			if (del.isDirectory()
					&& !del.getPath()
							.substring(del.getPath().lastIndexOf(".") + 1)
							.equals("usrv")) {
				deleteFiles(del);
			}
			if (!del.getPath().substring(del.getPath().lastIndexOf(".") + 1)
					.equals("usrv"))
				del.delete();
		}

	}

	public static DataAccess getDataAccess() {
		return dataAccess;
	}

	public static ParserService getParserService() {
		return parserService;
	}

	public static File getConfigHome() {
		return configHome;
	}
	
	public static File getTempUsrvFiles() {
	    return tempUsrvFiles;
	}

	public static File getSetupProps() {
	    return setupProps;
	}

	public static ServiceCaller getSc() {
		return sc;
	}

	public static void setSc(ServiceCaller sc) {
		Activator.sc = sc;
	}

	public static ModuleContext getmContext() {
		return mContext;
	}
}
