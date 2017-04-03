package org.universAAL.ucc.database;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;
import org.universAAL.ontology.phThing.Device;
import org.universAAL.ontology.profile.AALSpace;
import org.universAAL.ontology.profile.User;
import org.universAAL.ucc.database.aalspace.DataAccess;
import org.universAAL.ucc.database.aalspace.DataAccessImpl;
import org.universAAL.ucc.database.listener.interfaces.OntologySupplierService;
import org.universAAL.ucc.database.listener.services.OntologySupplierServiceImpl;
import org.universAAL.ucc.database.parser.ParserService;
import org.universAAL.ucc.database.parser.ParserServiceImpl;
import org.universAAL.ucc.profile.agent.ProfileAgent;
import org.universAAL.ucc.startup.api.Setup;
import org.universAAL.ucc.startup.api.impl.SetupImpl;
import org.universAAL.ucc.startup.model.Role;
import org.universAAL.ucc.startup.model.UserAccountInfo;

/**
 * Activator of ucc.database
 * 
 * @author Nicole Merkle
 *
 */
public class Activator implements BundleActivator {

	private static BundleContext context;
	private static ModuleContext mc;
	private ServiceRegistration reg1;
	private ServiceRegistration reg2;
	private ServiceRegistration reg3;
	private ServiceRegistration reg4;
	private static ProfileAgent pAgent;
	private static ServiceReference pRef;
	public static final String USER_SPACE = "urn:org.universAAL.aal_space:user_env#";
	public static final String HOME_SPACE = "urn:org.universAAL.aal.space:home_env#my_home_space";
	public static final String DEVICE_SPACE = "urn:org.universAAL.aal.space:home_env#";
	private static File userxml;

	public static BundleContext getContext() {
		return context;
	}
	
	public static File getUserxml() {
		return userxml;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		mc = uAALBundleContainer.THE_CONTAINER
			.registerModule(new Object[] { context });
		
		userxml = new File(new File(mc.getConfigHome(), "user"), "users.xml");
		
		if (!userxml.exists()) {
			userxml.createNewFile();
			UserAccountInfo u = new UserAccountInfo();
			u.setName("");
			u.setPassword("");
			List<Role> roles = new ArrayList<Role>();
			roles.add(Role.ENDUSER);
			u.setRole(roles);
			u.setChecked(false);
			Setup set = new SetupImpl();
			List<UserAccountInfo> users = new ArrayList<UserAccountInfo>();
			users.add(u);
			set.saveUsers(users);
		}

		reg1 = context.registerService(Setup.class.getName(), new SetupImpl(),
				null);
		reg2 = context.registerService(DataAccess.class.getName(),
				new DataAccessImpl(), null);
		reg3 = context.registerService(OntologySupplierService.class.getName(),
				new OntologySupplierServiceImpl(), null);
		reg4 = context.registerService(ParserService.class.getName(),
				new ParserServiceImpl(), null);

		// connection to profile agent and setting an empty user from xml
		// definition
		System.err.println("Before profileagent");
		pRef = context.getServiceReference(ProfileAgent.class.getName());
		pAgent = (ProfileAgent) context.getService(pRef);
		createEmptyUser();
		// createEmptyDevice();

	}

	/**
	 * Creates a empty user with username, password and role,
	 * and stores this user with profileagent in CHE.
	 */
	private void createEmptyUser() {
		User enduser = new User(USER_SPACE + "User");
		enduser.setProperty(USER_SPACE + "username", "");
		enduser.setProperty(USER_SPACE + "password", "");
		// enduser.setProperty(USER_SPACE+"confirmpassword", "");
		enduser.setProperty(USER_SPACE + "userRole", new String("ENDUSER"));

		User deployer = new User(USER_SPACE + "Deployer");
		deployer.setProperty(USER_SPACE + "username", "");
		deployer.setProperty(USER_SPACE + "password", "");
		// deployer.setProperty(USER_SPACE+"confirmpassword", "");
		deployer.setProperty(USER_SPACE + "userRole", "DEPLOYER");

		User tec = new User(USER_SPACE + "Technician");
		tec.setProperty(USER_SPACE + "username", "");
		tec.setProperty(USER_SPACE + "password", "");
		// tec.setProperty(USER_SPACE+"confirmpassword", "");
		tec.setProperty(USER_SPACE + "userRole", "TECHNICIAN");

		User care = new User(USER_SPACE + "Caregiver");
		care.setProperty(USER_SPACE + "username", "");
		care.setProperty(USER_SPACE + "password", "");
		// care.setProperty(USER_SPACE+"confirmpassword", "");
		care.setProperty(USER_SPACE + "userRole", "CAREGIVER");

		User assisted = new User(USER_SPACE + "AssistedPerson");
		assisted.setProperty(USER_SPACE + "username", "");
		assisted.setProperty(USER_SPACE + "password", "");
		// assisted.setProperty(USER_SPACE+"confirmpassword", "");
		assisted.setProperty(USER_SPACE + "userRole", "ASSISTEDPERSON");

		pAgent.addUser(enduser);
		pAgent.addUser(deployer);
		pAgent.addUser(tec);
		pAgent.addUser(care);
		pAgent.addUser(assisted);

	}

	/**
	 * Creates a empty device with devicename, room, deviceType, deviceId, hardwareSettingTime and
	 * lastActivityTime and stores it in spaceserver with profileagent.
	 */
	private void createEmptyDevice() {
		AALSpace mySpace = null;
		List<AALSpace> spaces = pAgent.getSpaces();
		if (spaces == null) {
			mySpace = new AALSpace(HOME_SPACE);
			pAgent.addSpace(mySpace);
		}
		Device dev = new Device(DEVICE_SPACE + "Device");
		dev.setProperty(DEVICE_SPACE + "deviceName", "Contact_Sensor");
		dev.setProperty(DEVICE_SPACE + "room", "Kitchen");
		dev.setProperty(DEVICE_SPACE + "deviceType", "FS20_Sensor");
		dev.setProperty(DEVICE_SPACE + "deviceId", "");
		dev.setProperty(DEVICE_SPACE + "hardwareSettingTime", new Date());
		dev.setProperty(DEVICE_SPACE + "lastActivityTime", new Date());
		pAgent.addDevice(dev, mySpace);

		Device dev2 = new Device(DEVICE_SPACE + "Motion_Sensor");
		dev2.setProperty(DEVICE_SPACE + "deviceName", "Motion Sensor");
		dev2.setProperty(DEVICE_SPACE + "room", "Bath");
		dev2.setProperty(DEVICE_SPACE + "deviceType", "FS20_Actuator");
		dev2.setProperty(DEVICE_SPACE + "deviceId", "");
		dev2.setProperty(DEVICE_SPACE + "hardwareSettingTime", new Date());
		dev2.setProperty(DEVICE_SPACE + "lastActivityTime", new Date());
		pAgent.addDevice(dev2, mySpace);

		Device dev3 = new Device(DEVICE_SPACE + "OnOff_Actuator");
		dev3.setProperty(DEVICE_SPACE + "deviceName", "On/Off_Actuator");
		dev3.setProperty(DEVICE_SPACE + "room", "Dining_Room");
		dev3.setProperty(DEVICE_SPACE + "deviceType", "FS20_Actuator");
		dev3.setProperty(DEVICE_SPACE + "deviceId", "");
		dev3.setProperty(DEVICE_SPACE + "hardwareSettingTime", new Date());
		dev3.setProperty(DEVICE_SPACE + "lastActivityTime", new Date());
		pAgent.addDevice(dev3, mySpace);

		Device dev4 = new Device(DEVICE_SPACE + "OnOff_Sensor");
		dev4.setProperty(DEVICE_SPACE + "deviceName", "On/Off_Sensor");
		dev4.setProperty(DEVICE_SPACE + "room", "Nursery");
		dev4.setProperty(DEVICE_SPACE + "deviceType", "FS20_Sensor");
		dev4.setProperty(DEVICE_SPACE + "deviceId", "");
		dev4.setProperty(DEVICE_SPACE + "hardwareSettingTime", new Date());
		dev4.setProperty(DEVICE_SPACE + "lastActivityTime", new Date());
		pAgent.addDevice(dev4, mySpace);

		Device dev5 = new Device(DEVICE_SPACE + "Plug_PC");
		dev5.setProperty(DEVICE_SPACE + "deviceName", "Plug_PC");
		dev5.setProperty(DEVICE_SPACE + "room", "Living_Room");
		dev5.setProperty(DEVICE_SPACE + "deviceType", "Other_Hardware");
		dev5.setProperty(DEVICE_SPACE + "deviceId", "");
		dev5.setProperty(DEVICE_SPACE + "hardwareSettingTime", new Date());
		dev5.setProperty(DEVICE_SPACE + "lastActivityTime", new Date());
		pAgent.addDevice(dev5, mySpace);

		Device dev6 = new Device(DEVICE_SPACE + "Humidity_Sensor");
		dev6.setProperty(DEVICE_SPACE + "deviceName", "Humidity_Sensor");
		dev6.setProperty(DEVICE_SPACE + "room", "Storage_Room");
		dev6.setProperty(DEVICE_SPACE + "deviceType", "FS20_Sensor");
		dev6.setProperty(DEVICE_SPACE + "deviceId", "");
		dev6.setProperty(DEVICE_SPACE + "hardwareSettingTime", new Date());
		dev6.setProperty(DEVICE_SPACE + "lastActivityTime", new Date());
		pAgent.addDevice(dev6, mySpace);

		Device dev7 = new Device(DEVICE_SPACE + "Temperature");
		dev7.setProperty(DEVICE_SPACE + "deviceName", "Tempearture");
		dev7.setProperty(DEVICE_SPACE + "room", "Bed_Room");
		dev7.setProperty(DEVICE_SPACE + "deviceType", "FS20_Sensor");
		dev7.setProperty(DEVICE_SPACE + "deviceId", "");
		dev7.setProperty(DEVICE_SPACE + "hardwareSettingTime", new Date());
		dev7.setProperty(DEVICE_SPACE + "lastActivityTime", new Date());
		pAgent.addDevice(dev7, mySpace);

		Device dev8 = new Device(DEVICE_SPACE + "Gesture_Sensor");
		dev8.setProperty(DEVICE_SPACE + "deviceName", "Gesture_Sensor");
		dev8.setProperty(DEVICE_SPACE + "room", "Corridor");
		dev8.setProperty(DEVICE_SPACE + "deviceType", "FS20_Sensor");
		dev8.setProperty(DEVICE_SPACE + "deviceId", "");
		dev8.setProperty(DEVICE_SPACE + "hardwareSettingTime", new Date());
		dev8.setProperty(DEVICE_SPACE + "lastActivityTime", new Date());
		pAgent.addDevice(dev8, mySpace);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
		reg1.unregister();
		reg2.unregister();
		reg3.unregister();
		reg4.unregister();
		// registry.unregister();
	}

	/**
	 * Gets an instance of the profileagent, which
	 * supports access to the CHE
	 * @return a profileagent
	 */
	public static ProfileAgent getProfileAgent() {
		if (pAgent == null) {
			ServiceReference sr = context
					.getServiceReference(ProfileAgent.class.getName());
			pAgent = (ProfileAgent) context.getService(sr);
		}
		return pAgent;
	}
}
