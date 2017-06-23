package org.universAAL.tools.ucc.database.space;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.bind.JAXB;

import org.universAAL.ontology.phThing.Device;
import org.universAAL.ontology.profile.AALSpace;
import org.universAAL.ontology.profile.User;
import org.universAAL.tools.ucc.database.Activator;
import org.universAAL.tools.ucc.model.jaxb.*;
import org.universAAL.tools.ucc.profile.agent.ProfileAgent;

public class DataAccessImpl implements DataAccess {

	public synchronized ArrayList<OntologyInstance> getFormFields(String file) {
		ArrayList<OntologyInstance> ontologies = new ArrayList<OntologyInstance>();
		Profiles profiles = (Profiles) JAXB.unmarshal(new File(file), Profiles.class);
		for (OntologyInstance o : profiles.getOntologyInstances()) {
			ontologies.add(o);
		}

		return ontologies;
	}

	public synchronized void updateUserData(String file, String id,
			HashMap<String, ArrayList<Subprofile>> subprofiles) {
		File f = new File(file);
		Profiles profiles = JAXB.unmarshal(f, Profiles.class);
		ArrayList<OntologyInstance> list = new ArrayList<OntologyInstance>();
		for (OntologyInstance ont : profiles.getOntologyInstances()) {
			if (!ont.getId().equals(id)) {
				list.add(ont);
			} else {
				OntologyInstance upToDate = new OntologyInstance();
				upToDate.setId(id);
				upToDate.setSubprofiles(subprofiles.get(id));
				upToDate.setType(ont.getType());
				list.add(upToDate);
			}
		}
		Profiles p = new Profiles();
		p.setOntologyInstances(list);
		f.delete();
		JAXB.marshal(p, new File(file));
	}

	public synchronized boolean saveUserData(String path, OntologyInstance ontologyInstance) {
		File file = new File(path);
		Profiles profiles = JAXB.unmarshal(file, Profiles.class);
		profiles.getOntologyInstances().add(ontologyInstance);
		file.delete();
		JAXB.marshal(profiles, new File(path));

		return true;
	}

	public synchronized boolean deleteUserData(String file, String instance) {
		File f = new File(file);
		Profiles profiles = JAXB.unmarshal(f, Profiles.class);
		for (OntologyInstance o : profiles.getOntologyInstances()) {
			if (o.getId().equals(instance)) {
				profiles.getOntologyInstances().remove(o);
				JAXB.marshal(profiles, new File(file));
				return true;
			}
		}
		return false;
	}

	public synchronized ArrayList<OntologyInstance> getEmptyProfile(String profile) {
		ArrayList<OntologyInstance> ontologies = new ArrayList<OntologyInstance>();
		Profiles profiles = (Profiles) JAXB.unmarshal(new File(profile), Profiles.class);
		for (OntologyInstance o : profiles.getOntologyInstances()) {
			ontologies.add(o);
		}
		return ontologies;
	}

	public void saveUserDataInCHE(OntologyInstance ont) {
		ProfileAgent pAgent = Activator.getProfileAgent();
		// Create new user with userprofile
		System.err.println(ont.getId());
		System.err.println(ont.getSubprofiles().size());
		String userId = ont.getId().replace(" ", "_");
		User user = new User(Activator.USER_SPACE + userId);
		System.err.println(ont.getSubprofiles().size());
		for (Subprofile sub : ont.getSubprofiles()) {
			for (SimpleObject sim : sub.getSimpleObjects()) {
				StringValue st = (StringValue) sim;
				System.err.println(st.getName());
				if (st.getName().equals("username")) {
					String id = st.getValue().replace(" ", "_");
					user.setProperty(Activator.USER_SPACE + "username", id);
				} else if (st.getName().equals("password")) {
					user.setProperty(Activator.USER_SPACE + "password", st.getValue());
				}
			}
			for (EnumObject eo : sub.getEnums()) {
				if (eo.getType().equals("userRole")) {
					String role = eo.getSelectedValue().replace(" ", "_");
					user.setProperty(Activator.USER_SPACE + "userRole", role);
				}
			}
		}
		if (pAgent.addUser(user) != null) {
			System.err.println("User was added to CHE");
		}
	}

	public ArrayList<OntologyInstance> getEmptyCHEFormFields(String instance) {
		ProfileAgent pAgent = Activator.getProfileAgent();
		ArrayList<OntologyInstance> ontologies = new ArrayList<OntologyInstance>();
		ArrayList<String> roles = new ArrayList<String>();
		ArrayList<String> rooms = new ArrayList<String>();
		ArrayList<String> devTypes = new ArrayList<String>();
		ArrayList<String> sensType = new ArrayList<String>();
		if (instance.equals("User")) {
			List<User> users = pAgent.getAllUsers();
			for (User ur : users) {
				if (ur != null && (String) ur.getProperty(Activator.USER_SPACE + "userRole") != null)
					roles.add((String) ur.getProperty(Activator.USER_SPACE + "userRole"));
			}
			for (User u : users) {
				Object prop = u.getProperty(Activator.USER_SPACE + "username");
				if (prop != null) {// HACK to prevent analyzing correct users :(

					String filter = ((String) u.getProperty(Activator.USER_SPACE + "username")).replace("_", " ");
					System.err.println(filter);
					OntologyInstance ont = new OntologyInstance();
					ont.setId(filter);
					ont.setType("User");

					Subprofile sub = new Subprofile();
					String header = u.getURI().substring(u.getURI().indexOf("#") + 1);
					sub.setName(header);
					StringValue st = new StringValue();
					st.setLabel("Username:");
					st.setName("username");
					st.setId(true);
					st.setRequired(true);
					st.setValue(filter);
					sub.getSimpleObjects().add(st);

					StringValue pw = new StringValue();
					pw.setLabel("Password:");
					pw.setName("password");
					pw.setRequired(true);
					pw.setValue((String) u.getProperty(Activator.USER_SPACE + "password"));
					sub.getSimpleObjects().add(pw);

					EnumObject en = new EnumObject();
					en.setType("userRole");
					en.setTreeParentNode(true);
					en.setLabel("Role:");
					en.setRequired(true);
					en.setValues(roles);
					en.setSelectedValue((String) u.getProperty(Activator.USER_SPACE + "userRole"));
					sub.getEnums().add(en);

					ont.getSubprofiles().add(sub);
					ontologies.add(ont);
				}
			}

		}
		// Devices
		else if (instance.equals("Device")) {
			for (AALSpace space : pAgent.getSpaces()) {
				for (Device dev : pAgent.getAllDevices(space)) {
					rooms.add((String) dev.getProperty(Activator.DEVICE_SPACE + "room"));
					devTypes.add((String) dev.getProperty(Activator.DEVICE_SPACE + "deviceType"));
					sensType.add((String) dev.getProperty(Activator.DEVICE_SPACE + "deviceName"));
				}
				for (Device dev : pAgent.getAllDevices(space)) {
					String filter = ((String) dev.getProperty(Activator.DEVICE_SPACE + "deviceId")).replace("_", " ");
					System.err.println(filter);
					OntologyInstance ont = new OntologyInstance();
					ont.setId(filter);
					Subprofile sub = new Subprofile();
					String header = dev.getURI().substring(dev.getURI().indexOf("#") + 1);
					sub.setName(header);
					// Device-Address
					StringValue st = new StringValue();
					st.setLabel("Device-Adress:");
					st.setName("deviceId");
					st.setId(true);
					st.setRequired(true);
					st.setValue((String) dev.getProperty(Activator.DEVICE_SPACE + "deviceId"));
					sub.getSimpleObjects().add(st);

					// Setting Time
					CalendarValue setTime = new CalendarValue();
					setTime.setLabel("Setting Time:");
					setTime.setName("hardwareSettingTime");
					setTime.setCalendar((String) dev.getProperty(Activator.DEVICE_SPACE + "hardwareSettingTime"));
					sub.getSimpleObjects().add(setTime);
					// Last Activity Time
					CalendarValue actTime = new CalendarValue();
					actTime.setLabel("Last activity time:");
					actTime.setName("lastActivityTime");
					actTime.setCalendar((String) dev.getProperty(Activator.DEVICE_SPACE + "lastActivityTime"));
					sub.getSimpleObjects().add(actTime);

					// Enums 3
					EnumObject en = new EnumObject();
					en.setLabel("Room:");
					en.setRequired(true);
					en.setSelectedValue((String) dev.getProperty(Activator.DEVICE_SPACE + "room"));
					en.setType("rooms");
					en.setValues(rooms);
					en.setTreeParentNode(true);
					sub.getEnums().add(en);

					EnumObject dTypes = new EnumObject();
					dTypes.setLabel("Device Type:");
					dTypes.setRequired(true);
					dTypes.setSelectedValue((String) dev.getProperty(Activator.DEVICE_SPACE + "deviceType"));
					dTypes.setType("deviceType");
					dTypes.setValues(devTypes);
					sub.getEnums().add(dTypes);

					EnumObject sTypes = new EnumObject();
					sTypes.setLabel("Sensor/Actor Type:");
					sTypes.setRequired(true);
					sTypes.setSelectedValue((String) dev.getProperty(Activator.DEVICE_SPACE + "deviceName"));
					sTypes.setType("deviceName");
					sTypes.setValues(sensType);
					sub.getEnums().add(sTypes);
					ont.getSubprofiles().add(sub);
					ontologies.add(ont);
				}
			}

		}
		return ontologies;

	}

	public void updateUserData(String id, HashMap<String, ArrayList<Subprofile>> subprofiles) {
		ProfileAgent pAgent = Activator.getProfileAgent();
		String temp = id.replace(" ", "_");
		User user = new User(Activator.USER_SPACE + temp);
		for (Subprofile sub : subprofiles.get(id)) {
			for (SimpleObject sim : sub.getSimpleObjects()) {
				if (sim instanceof StringValue) {
					StringValue sv = (StringValue) sim;
					user.setProperty(Activator.USER_SPACE + sv.getName(), sv.getValue());
				}
				// TODO: other types like IntegerValue, DoubleValue,
				// BooleanValue to test
			}
			for (EnumObject en : sub.getEnums()) {
				user.setProperty(Activator.USER_SPACE + en.getType(), en.getSelectedValue());
			}
		}
		pAgent.updateUser(user);

	}

	public void deleteUserDataInChe(String instance) {
		ProfileAgent pAgent = Activator.getProfileAgent();
		String id = instance.replace(" ", "_");
		pAgent.deleteUser(Activator.USER_SPACE + id);

	}

}
