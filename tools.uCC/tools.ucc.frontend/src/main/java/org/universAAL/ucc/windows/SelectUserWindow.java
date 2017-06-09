package org.universAAL.ucc.windows;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.middleware.util.Constants;
import org.universAAL.ontology.profile.Profilable;
import org.universAAL.ontology.profile.Profile;
import org.universAAL.ontology.profile.User;
import org.universAAL.ontology.profile.service.ProfilingService;
import org.universAAL.ontology.profile.ui.mainmenu.MenuEntry;
import org.universAAL.ontology.profile.ui.mainmenu.MenuProfile;
import org.universAAL.ucc.database.aalspace.DataAccess;
import org.universAAL.ucc.model.AALService;
import org.universAAL.ucc.model.jaxb.EnumObject;
import org.universAAL.ucc.model.jaxb.OntologyInstance;
import org.universAAL.ucc.model.jaxb.SimpleObject;
import org.universAAL.ucc.model.jaxb.StringValue;
import org.universAAL.ucc.model.jaxb.Subprofile;
import org.universAAL.ucc.service.manager.Activator;
import org.universAAL.ucc.startup.model.Role;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class SelectUserWindow extends Window implements Button.ClickListener {
	private ListSelect list;
	private Button ok;
	private Button cancel;
	private Button addUser;
	private AALService aal;
	private UccUI app;
	private VerticalLayout vl;

	public SelectUserWindow(List<String> users, AALService aal, UccUI app) {
		super("Select User");
		this.aal = aal;
		this.app = app;
		vl = new VerticalLayout();
		vl.setSizeFull();
		vl.setMargin(true);
		vl.setSpacing(true);
		setContent(vl);
		Label l = new Label("For which user do you want to install the AAL service?");
		list = new ListSelect("List of users in AAL space");
		list.setImmediate(true);
		list.setMultiSelect(false);
		list.setWidth("200px");
		list.setNullSelectionAllowed(false);
		// list.setNewItemsAllowed(false);
		for (String u : users) {
			list.addItem(u);
		}
		vl.addComponent(l);
		vl.addComponent(list);
		vl.setComponentAlignment(list, Alignment.TOP_CENTER);

		addUser = new Button("Add User");
		addUser.addListener(this);
		ok = new Button("OK");
		ok.addListener(this);
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		hl.setMargin(true);
		hl.addComponent(addUser);
		hl.addComponent(ok);
		cancel = new Button("Cancel");
		cancel.addListener(this);
		hl.addComponent(cancel);
		vl.addComponent(hl);
		vl.setComponentAlignment(hl, Alignment.BOTTOM_CENTER);

		setWidth("450px");
		setHeight("400px");
		center();

	}

	public void buttonClick(ClickEvent event) {
		if (event.getButton() == addUser) {
			AddNewPersonWindow anpw = null;
			try {
				anpw = new AddNewPersonWindow(null, this, app);
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			app.getMainWindow().addWindow(anpw);
		}
		if (event.getButton() == ok) {
			System.err.println("AAL-NAME: " + aal.getName());
			System.err.println("ONTOLOGY-URI: " + aal.getOntologyUri());
			System.err.println("ICON-PATH: " + aal.getIconPath());
			addEntry((list.getValue()).toString(), aal.getMenuName(), aal.getUaapList().get(0).getProvider()
					.getWebsite()/* getProvider() */, aal.getOntologyUri(), aal.getIconPath());
			close();
			app.getMainWindow().showNotification("", "The MenuEntry was successfully added",
					Notification.TYPE_HUMANIZED_MESSAGE);

		}
		if (event.getButton() == cancel) {
			close();
		}

	}

	// Adds a MenuEntry for new installed AAL service to Endusers view
	// private void addEntry(String userID, String entryName, String vendor,
	// String serviceClass, String iconURL)
	// {
	// System.err.println("User-ID:
	// "+Constants.uAAL_MIDDLEWARE_LOCAL_ID_PREFIX+userID);
	// System.err.println("Menu-Entry Name: "+entryName);
	// System.err.println("Vendor: "+vendor);
	// System.err.println("Service-Class: "+serviceClass);
	// System.err.println("Icon-URL: "+iconURL);
	// String[] pathElems = null;
	// Activator.getMgmt().addUserIDToMenuEntry(aal.getServiceId(), userID);
	// if(iconURL.contains("/")) {
	// pathElems = iconURL.split("/");
	// } else if(iconURL.contains("\\")) {
	// pathElems = iconURL.split("\\");
	// }
	// Resource r = null;
	// Resource icon = null;
	// String category = "";
	// if(pathElems != null) {
	// for(int i = 0; i < pathElems.length; i++) {
	// if(i == (pathElems.length - 1)) {
	// icon = new Resource(pathElems[i]);
	// icon.setResourceLabel(entryName);
	// } else {
	// category += pathElems[i]+"/";
	// }
	// }
	// r = new Resource();
	// r.setResourceLabel(category);
	// } else {
	// icon = new Resource(iconURL);
	// icon.setResourceLabel(entryName);
	// }
	//
	// MenuEntry me = new MenuEntry(null);
	// me.setVendor(new Resource(vendor));
	// me.setServiceClass(new Resource(serviceClass));
	//// Resource pathElem = new Resource(iconURL);
	//// pathElem.setResourceLabel(entryName);
	// if(!category.equals(""))
	// me.setPath(new Resource[] {
	// r,
	// /*pathElem*/ icon
	// });
	// else
	// me.setPath(new Resource[]{
	// icon
	// });
	// System.err.println("The ICON: "+icon);
	// System.err.println("The Category-Path: "+category);
	// ServiceRequest sr = new ServiceRequest(new ProfilingService(), null);
	// sr.addValueFilter(new String[] { ProfilingService.PROP_CONTROLS },
	// new User(Constants.uAAL_MIDDLEWARE_LOCAL_ID_PREFIX+userID));
	// sr.addAddEffect(new String[] { ProfilingService.PROP_CONTROLS,
	// Profilable.PROP_HAS_PROFILE, Profile.PROP_HAS_SUB_PROFILE,
	// MenuProfile.PROP_ENTRY }, me);
	// ServiceResponse res = Activator.getSc().call(sr);
	// if(res.getCallStatus() == CallStatus.succeeded)
	// LogUtils.logDebug(Activator.getmContext(), SelectUserWindow.class,
	// "addEntry", new Object[] {
	// "new menu entry ", entryName, "for user"+
	// Constants.uAAL_MIDDLEWARE_LOCAL_ID_PREFIX+userID+" added."
	// }, null);
	// else
	// LogUtils.logDebug(Activator.getmContext(), SelectUserWindow.class,
	// "addEntry", new Object[] {
	// "callstatus is not succeeded"
	// }, null);
	// }

	public void addEntry(String userID, String entryName, String vendor, String serviceClass, String iconURL) {
		if ("".equals(iconURL))
			iconURL = null;
		Activator.getMgmt().addUserIDToMenuEntry(aal.getServiceId(), userID);
		MenuEntry me = new MenuEntry(null);
		me.setVendor(new Resource(vendor));
		me.setServiceClass(new Resource(serviceClass));
		Resource pathElem = new Resource(iconURL);
		pathElem.setResourceLabel(entryName);
		me.setPath(new Resource[] { pathElem });

		ServiceRequest sr = new ServiceRequest(new ProfilingService(), null);
		sr.addValueFilter(new String[] { ProfilingService.PROP_CONTROLS },
				new User(Constants.uAAL_MIDDLEWARE_LOCAL_ID_PREFIX + userID));
		sr.addAddEffect(new String[] { ProfilingService.PROP_CONTROLS, Profilable.PROP_HAS_PROFILE,
				Profile.PROP_HAS_SUB_PROFILE, MenuProfile.PROP_ENTRY }, me);

		ServiceResponse res = Activator.getSc().call(sr);
		if (res.getCallStatus() == CallStatus.succeeded) {
			LogUtils.logDebug(Activator.getmContext(), SelectUserWindow.class, "addEntry",
					new Object[] { "new menu entry " + entryName + " for user ",
							Constants.uAAL_MIDDLEWARE_LOCAL_ID_PREFIX + userID, " added." },
					null);
		} else {
			LogUtils.logDebug(Activator.getmContext(), SelectUserWindow.class, "addEntry",
					new Object[] { "callstatus is not succeeded" }, null);
		}
	}

	public ListSelect getList() {
		return list;
	}

	public void setList(ListSelect list) {
		this.list = list;
	}

	public VerticalLayout getVl() {
		return vl;
	}

	public void setVl(VerticalLayout vl) {
		this.vl = vl;
	}

	public void addUserToList() {
		removeComponent(vl);
		List<String> users = new ArrayList<String>();
		DataAccess da = Activator.getDataAccess();
		ArrayList<OntologyInstance> ontList = da.getEmptyCHEFormFields("User");
		String uname = "";
		String role = "";
		for (OntologyInstance o : ontList) {
			System.err.println("Getting all users!");
			for (Subprofile s : o.getSubprofiles()) {
				for (SimpleObject sim : s.getSimpleObjects()) {
					StringValue st = (StringValue) sim;
					if (st.getName().equals("username")) {
						uname = st.getValue();
					}
				}
				System.err.println(s.getEnums().size());
				for (EnumObject en : s.getEnums()) {
					System.err.println(en.getType());
					if (en.equals("userRole")) {
						role = en.getSelectedValue();
						System.err.println(role);
					}
				}
				if (role.equals(Role.ASSISTEDPERSON.name()) || role.equals(Role.ENDUSER.name())) {
					System.err.println(role);
					users.add(uname);
				}

			}
			users.add(uname);
		}

		vl = new VerticalLayout();
		vl.setSizeFull();
		vl.setMargin(true);
		vl.setSpacing(true);
		setContent(vl);
		Label l = new Label("For which user do you want to install the AAL service?");
		list = new ListSelect("List of users in AAL space");
		list.setImmediate(true);
		list.setMultiSelect(false);
		list.setWidth("200px");
		list.setNullSelectionAllowed(false);
		// list.setNewItemsAllowed(false);
		for (String u : users) {
			list.addItem(u);
		}
		vl.addComponent(l);
		vl.addComponent(list);
		vl.setComponentAlignment(list, Alignment.TOP_CENTER);

		addUser = new Button("Add User");
		addUser.addListener(this);
		ok = new Button("OK");
		ok.addListener(this);
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		hl.setMargin(true);
		hl.addComponent(addUser);
		hl.addComponent(ok);
		cancel = new Button("Cancel");
		cancel.addListener(this);
		hl.addComponent(cancel);
		vl.addComponent(hl);
		vl.setComponentAlignment(hl, Alignment.BOTTOM_CENTER);
	}

}
