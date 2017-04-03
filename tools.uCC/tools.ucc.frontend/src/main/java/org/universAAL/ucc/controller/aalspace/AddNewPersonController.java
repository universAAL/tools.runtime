package org.universAAL.ucc.controller.aalspace;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.universAAL.middleware.container.utils.ModuleConfigHome;
import org.universAAL.ucc.database.aalspace.DataAccess;
import org.universAAL.ucc.model.jaxb.BooleanValue;
import org.universAAL.ucc.model.jaxb.CalendarValue;
import org.universAAL.ucc.model.jaxb.CollectionValues;
import org.universAAL.ucc.model.jaxb.DoubleValue;
import org.universAAL.ucc.model.jaxb.EnumObject;
import org.universAAL.ucc.model.jaxb.IntegerValue;
import org.universAAL.ucc.model.jaxb.OntologyInstance;
import org.universAAL.ucc.model.jaxb.SimpleObject;
import org.universAAL.ucc.model.jaxb.StringValue;
import org.universAAL.ucc.model.jaxb.Subprofile;
import org.universAAL.ucc.service.manager.Activator;
import org.universAAL.ucc.startup.api.Setup;
import org.universAAL.ucc.startup.model.Role;
import org.universAAL.ucc.startup.model.UserAccountInfo;
import org.universAAL.ucc.windows.AddNewPersonWindow;
import org.universAAL.ucc.windows.HumansWindow;
import org.universAAL.ucc.windows.SelectUserWindow;
import org.universAAL.ucc.windows.TabForm;
import org.universAAL.ucc.windows.UccUI;

import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.Notification;

/**
 * User profile controller for adding new person to AAL space.
 * 
 * @author Nicole Merkle
 *
 */
public class AddNewPersonController implements Button.ClickListener, Window.CloseListener {
	private AddNewPersonWindow win;
	private UccUI app;
	private BundleContext context;
	private DataAccess dataAccess;
	private TabSheet tabSheet;
	private HashMap<String, Subprofile> subprofiles;
	private String ontId;
	private ArrayList<OntologyInstance> objects;
	private ArrayList<OntologyInstance>savedObjects;
	private OntologyInstance instance;
	private static String ontoProfile;
	private String actualFlat;
	private boolean saved;
	private String device;
	private Setup setup;
	private ModuleConfigHome mc;
	private SelectUserWindow selWin;

	public AddNewPersonController(AddNewPersonWindow window, HumansWindow hWin, SelectUserWindow sel,
			UccUI app) throws JAXBException,
			IOException, ParseException {
		context = Activator.bc;//FrameworkUtil.getBundle(getClass()).getBundleContext();
		mc = new ModuleConfigHome("uccDB", "");
		device = mc.getAbsolutePath();
		System.out.println("uccDB is in folder: " + device);
		ontoProfile = device+"/EmptyUser.xml";
		this.app = app;
		selWin = sel;
		this.saved = false;
		this.win = window;
		this.win.addListener(this);
		actualFlat = device + "/Users.xml";
		ServiceReference ref = context.getServiceReference(DataAccess.class
				.getName());
		dataAccess = (DataAccess) context.getService(ref);
		ServiceReference ref2 = context.getServiceReference(Setup.class.getName());
		setup = (Setup)context.getService(ref2);
		context.ungetService(ref);
		context.ungetService(ref2);
//		savedObjects = dataAccess.getCHEFormFields("User", );
		savedObjects = dataAccess.getFormFields(actualFlat);
		instance = new OntologyInstance();
		subprofiles = new HashMap<String, Subprofile>();
		tabSheet = new TabSheet();
		tabSheet.setSizeFull();
		tabSheet.setImmediate(true);
		loadData();
		if (hWin != null) {
			TabForm tab = (TabForm) tabSheet.getSelectedTab();
			tab.getField(tab.getId()).setValue(hWin.getUserTree().getValue());
		}
		win.addWindowContent(tabSheet);

	}

	private void loadData() throws JAXBException, IOException, ParseException {
		// Creating Tabs with Forms
		objects = dataAccess.getEmptyCHEFormFields("User");
//		objects = dataAccess.getEmptyProfile(ontoProfile);
		TabForm f = null;
		// Every Subprofile is shown in a seperate tab
		for (Subprofile tab : objects.get(0).getSubprofiles()) {
			instance.getSubprofiles().add(tab);
			f = new TabForm();
			// Save Subprofile Tabs for later use
			subprofiles.put(tab.getName(), tab);
			// Creating User tree and Comboboxes
			for (EnumObject enumObj : tab.getEnums()) {
				NativeSelect box = new NativeSelect(enumObj.getLabel());
//				box.addItem("");
				box.setDescription(enumObj.getDescription());
				// Create ComboBox with enum objects and add to form
				for (String item : enumObj.getValues()) {
//					if(item != null)
						box.addItem(item);
				}
				box.setImmediate(true);
				if (enumObj.isRequired()) {
					box.setRequired(true);
					box.setRequiredError(enumObj.getLabel() + " is required");
				}
				if (enumObj.isTreeParentNode()) {
					f.setId(enumObj.getType());
				}
				f.addField(enumObj.getType(), box);

			}
			// Add simpel objects to form
			for (SimpleObject simpl : tab.getSimpleObjects()) {
				createForm(simpl, f);
			}
			// Adding collection objects as a list to form
			if (tab.getCollections().size() > 0) {
				for (CollectionValues cols : tab.getCollections()) {
					ListSelect list = new ListSelect();
					list.setCaption(cols.getLabel());
					list.setWidth("120px");
					list.setDescription(cols.getDescription());
					if (cols.isMultiselect()) {
						list.setMultiSelect(true);
					}
					if(cols.isRequired()) {
						list.setRequired(true);
						list.setRequiredError("You have to add one or many preferred Languages");
					}
					if(cols.getCollection().size()>0) {
						for(SimpleObject v : cols.getCollection()) {
							StringValue st = (StringValue)v;
							list.addItem(st.getValue());
							list.select(st.getValue());
						}
					}
						
					// Adding List to Form
					list.setImmediate(true);
					list.setNullSelectionAllowed(false);
					list.setRows(5);
					list.setNewItemsAllowed(true);
					f.addField(cols.getLabel(), list);
				}
			}
			f.createFooter();
			f.getSaveButton().addListener((Button.ClickListener) this);
			f.getEditButton().addListener((Button.ClickListener) this);
			f.getResetButton().addListener((Button.ClickListener) this);
			f.getDeleteButton().addListener((Button.ClickListener) this);
			f.getEditButton().setVisible(false);
			f.getSaveButton().setVisible(true);
			f.getDeleteButton().setVisible(false);
			f.setHeader(tab.getName());
			f.setReadOnly(false);
			tabSheet.addTab(f, tab.getName());
			if(tabSheet.getTabPosition(tabSheet.getTab(f)) != 0) {
				tabSheet.getTab(f).setEnabled(false);
			}
				
		}

	}

	private TabForm createForm(SimpleObject simpleObject, TabForm form)
			throws ParseException {
		if (simpleObject instanceof CalendarValue) {
			CalendarValue cal = (CalendarValue) simpleObject;
			PopupDateField date = new PopupDateField(cal.getLabel());
			date.setResolution(PopupDateField.RESOLUTION_MIN);
			date.setImmediate(true);
			date.setInputPrompt(cal.getLabel());
			date.setShowISOWeekNumbers(true);
			date.setDescription(cal.getDescription());
			if (cal.isRequired()) {
				date.setRequired(true);
				date.setRequiredError(cal.getLabel() + " is required");
			}
			form.addField(cal.getLabel(), date);
		} else if (simpleObject instanceof StringValue) {
			StringValue st = (StringValue) simpleObject;
			if(st.getName().contains("password") || st.getName().contains("Password")) {
				PasswordField pwd = new PasswordField(st.getLabel());
				pwd.setWriteThrough(false);
				pwd.setImmediate(true);
				pwd.setDescription(st.getDescription());
				if (st.isRequired()) {
					pwd.setRequired(true);
					pwd.setRequiredError(st.getLabel() + " is required");
				}
				form.addField(st.getLabel(), pwd);
			} else 
			if (st.getValue().length() > 30) {
				TextArea area = new TextArea(st.getLabel());
				area.setImmediate(true);
				area.setWriteThrough(false);
				area.setRows(5);
				area.setDescription(st.getDescription());
				if (st.isRequired()) {
					area.setRequired(true);
					area.setRequiredError(st.getLabel() + " is required");
				}
				if (st.getValidator() != null) {
					if (st.getValidator().equals("EmailValidator")) {
						area.addValidator(new EmailValidator(
								"The Emailaddress isn't correct"));
					}
				}
				form.addField(st.getLabel(), area);
			} else {
				TextField tf = new TextField(st.getLabel());
				tf.setWriteThrough(false);
				tf.setImmediate(true);
				tf.setDescription(st.getDescription());
				if (st.isRequired()) {
					tf.setRequired(true);
					tf.setRequiredError(st.getLabel() + " is required");
				}
				if (st.getValidator() != null) {
					if (st.getValidator().equals("EmailValidator")) {
						tf.addValidator(new EmailValidator(
								"The Emailaddress isn't correct"));
					}
				}
				form.addField(simpleObject.getLabel(), tf);
			}
			form.createFooter();
		} else if (simpleObject instanceof IntegerValue) {
			IntegerValue integer = (IntegerValue) simpleObject;
			TextField t = new TextField(integer.getLabel());
			t.setImmediate(true);
			t.setWriteThrough(false);
			t.setDescription(integer.getDescription());
			if (integer.isRequired()) {
				t.setRequired(true);
				t.setRequiredError(integer.getLabel() + " is required");
			}
			if (integer.getValidator() != null) {
				if (integer.getValidator().equals("RegexpValidator")) {
					if (integer.getName().contains("postalCode")) {
						t.addValidator(new RegexpValidator("[1-9][0-9]{4}",
								"The postal code isn't correct"));
					} else {
						t.addValidator(new RegexpValidator("[1-9][0-9]*",
								"Please insert an valid number"));
					}
				}
			}
			form.addField(simpleObject.getLabel(), t);
		} else if (simpleObject instanceof BooleanValue) {
			BooleanValue bool = (BooleanValue) simpleObject;
			CheckBox box = new CheckBox(bool.getLabel());
			box.setImmediate(true);
			box.setWriteThrough(false);
			box.setDescription(bool.getDescription());
			if (bool.isRequired()) {
				box.setRequired(true);
				box.setRequiredError(bool.getLabel() + " is required");
			}
			form.addField(bool.getLabel(), box);
		} else if (simpleObject instanceof DoubleValue) {
			DoubleValue doub = (DoubleValue) simpleObject;
			TextField tf = new TextField(doub.getLabel());
			tf.setImmediate(true);
			tf.setWriteThrough(false);
			tf.setDescription(doub.getDescription());
			if (doub.isRequired()) {
				tf.setRequired(true);
				tf.setRequiredError(doub.getLabel() + " is required");
			}
			if (doub.getValidator() != null) {
				if (doub.getValidator().equals("RegexpValidator")) {
					tf.addValidator(new RegexpValidator("[0-9]*[.][0-9]{5}",
							"Please insert a valid floating number"));
				}
			}
			form.addField(doub.getLabel(), tf);
		}

		return form;
	}

	String user = "";
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == ((TabForm) tabSheet.getSelectedTab())
				.getSaveButton()) {
			saved = true;
			TabForm tab = (TabForm) tabSheet.getSelectedTab();
			String tabHeader = tabSheet.getTab(tab).getCaption();
			Subprofile sub = new Subprofile();
			sub.setName(tabHeader);
			// SimpleObjects
			ArrayList<SimpleObject> simpleObjects = new ArrayList<SimpleObject>();
			for (SimpleObject sim : subprofiles.get(tabHeader)
					.getSimpleObjects()) {
				if (sim instanceof StringValue) {
					StringValue s = (StringValue) sim;
					s.setValue((String) tab.getField(s.getLabel()).getValue());
					simpleObjects.add(s);
					if (s.isId()) {
						ontId = s.getValue();
					}
				}
				if (sim instanceof IntegerValue) {
					IntegerValue integer = (IntegerValue) sim;
					if (isIntegerNum(tab.getField(sim.getLabel()).getValue()
							.toString()))
						integer.setValue(Integer.parseInt(tab
								.getField(sim.getLabel()).getValue().toString()));
					else
						integer.setValue(0);
					simpleObjects.add(integer);
				}
				if (sim instanceof DoubleValue) {
					DoubleValue doub = (DoubleValue) sim;
					if (isDoubleNum(tab.getField(doub.getLabel()).getValue()
							.toString()))
						doub.setValue(Double.parseDouble(tab
								.getField(doub.getLabel()).getValue()
								.toString()));
					else
						doub.setValue(0.0);
					simpleObjects.add(doub);
				}
				if (sim instanceof BooleanValue) {
					BooleanValue bool = (BooleanValue) sim;
					bool.setValue((Boolean) tab.getField(bool.getLabel())
							.getValue());
					simpleObjects.add(bool);
				}
				if (sim instanceof CalendarValue) {
					CalendarValue cal = (CalendarValue) sim;
					DateFormat df = new SimpleDateFormat();
					if (tab.getField(sim.getLabel()).getValue() != null) {
						String date = df.format((Date) tab.getField(
								sim.getLabel()).getValue());
						cal.setCalendar(date);
						simpleObjects.add(cal);
					}
				}
			}
			sub.setSimpleObjects(simpleObjects);

			// EnumObjects
			ArrayList<EnumObject> enums = new ArrayList<EnumObject>();
			for (EnumObject en : subprofiles.get(tabHeader).getEnums()) {
				en.setSelectedValue((String) tab.getField(en.getType())
						.getValue());
				enums.add(en);
				if(en.getType().equals("userRole")) {
					user = en.getSelectedValue();
				}
			}
			sub.setEnums(enums);

			// CollectionValues
			ArrayList<CollectionValues> collections = new ArrayList<CollectionValues>();
			for (CollectionValues cols : subprofiles.get(tabHeader)
					.getCollections()) {
				Collection<SimpleObject> values = new ArrayList<SimpleObject>();
				Collection<SimpleObject> newVal = null;
				for (SimpleObject sim : cols.getCollection()) {
					if (sim instanceof StringValue) {
						newVal = (Collection<SimpleObject>) tab.getField(
								cols.getLabel()).getValue();
						Object[] array = newVal.toArray();
						for (int i = 0; i < array.length; i++) {
							StringValue n = new StringValue();
							n.setDescription(sim.getDescription());
							n.setLabel(sim.getLabel());
							n.setRequired(sim.isRequired());
							n.setValidator(sim.getValidator());
							n.setValue(array[i].toString());
							values.add(n);
						}
					} else if (sim instanceof IntegerValue) {
						newVal = (Collection<SimpleObject>) tab.getField(
								cols.getLabel()).getValue();
						Object[] array = newVal.toArray();
						for (int i = 0; i < array.length; i++) {
							IntegerValue n = new IntegerValue();
							n.setDescription(sim.getDescription());
							n.setLabel(sim.getLabel());
							n.setRequired(sim.isRequired());
							n.setValidator(sim.getValidator());
							if (isIntegerNum(array[i].toString()))
								n.setValue(Integer.parseInt(array[i].toString()));
							else
								n.setValue(0);
							values.add(n);
						}

					} else if (sim instanceof DoubleValue) {
						newVal = (Collection<SimpleObject>) tab.getField(
								cols.getLabel()).getValue();
						Object[] array = newVal.toArray();
						for (int i = 0; i < array.length; i++) {
							DoubleValue n = new DoubleValue();
							n.setDescription(sim.getDescription());
							n.setLabel(sim.getLabel());
							n.setRequired(sim.isRequired());
							n.setValidator(sim.getValidator());
							if (isDoubleNum(array[i].toString()))
								n.setValue(Double.parseDouble(array[i]
										.toString()));
							else
								n.setValue(0.0);
							values.add(n);
						}

					}
					cols.setCollection(values);
				}
				collections.add(cols);
			}
			sub.setCollections(collections);
			for(OntologyInstance ont : savedObjects) {
				if(tab.getField("Given name:") != null && tab.getField("Given name:").getValue().equals(ont.getId())) {
					tab.getField("Given name:").setValue("");
					app.getMainWindow().showNotification("You can't add a person twice", Notification.TYPE_HUMANIZED_MESSAGE);
					return;
				}
			}
			instance.setId(ontId);
			for (Subprofile prof : instance.getSubprofiles()) {
				if (prof.getName().equals(sub.getName())) {
					prof = sub;
				}
			}
			objects.add(instance);
			
				
			tab.setReadOnly(true);
			tabSheet.removeComponent(tab);
			if(tabSheet.getComponentCount() > 0) {
				tabSheet.getTab(tabSheet.getTabIndex()).setEnabled(true);
			}
//			if (hWindow != null && tabSheet.getComponentCount() == 0) {
//				hWindow.getUserTree().addItem(ontId);
//				hWindow.getUserTree().setParent(ontId,
//						hWindow.getUserTree().getValue());
//				hWindow.getUserTree().setChildrenAllowed(ontId, false);
//			}

			if (tabSheet.getComponentCount() == 0) {
				dataAccess.saveUserDataInCHE(instance);
				//Save in Users.xml
				List<UserAccountInfo>ul = setup.getUsers();
				List<UserAccountInfo>temp = new ArrayList<UserAccountInfo>();
				boolean flag = false;
				UserAccountInfo uinfo = new UserAccountInfo();
				uinfo.setChecked(true);
				ArrayList<Role> roles = new ArrayList<Role>();
				for(Subprofile s : instance.getSubprofiles()) {
					for(SimpleObject si : s.getSimpleObjects()) {
						StringValue sv = (StringValue)si;
						System.err.println(sv.getName());
						System.err.println(sv.getValue());
						if(sv.getName().equals("username")) {
							for(UserAccountInfo us : ul) {
								if(us.getName().equals(sv.getValue())) {
									flag = true;
								}
							}
							if(!flag) {
								uinfo.setName(sv.getValue());
							}
						}
						if(sv.getName().equals("password")) {
							if(!flag)
								uinfo.setPassword(sv.getValue());
						}
						
					}
					if(!flag) {
					for(EnumObject eo : s.getEnums()) {
						System.err.println(eo.getType());
						if(eo.getType().equals("userRole")) {
							System.err.println(eo.getSelectedValue());
							roles.add(Role.valueOf(eo.getSelectedValue()));
							uinfo.setRole(roles);
						}
					}
				}
				}
				if(!flag) {
					temp.add(uinfo);
					setup.saveUsers(temp);
				}
				
//				dataAccess.saveUserData(actualFlat, instance);
				app.getMainWindow().removeWindow(win);
			}
			if(tabSheet.getComponentCount() == 0) {
				for(Window w : app.getMainWindow().getChildWindows()) {
					if(w instanceof HumansWindow) {
						   HumansWindow users = (HumansWindow)w;
//						   if(flatId.equals(users.getFlatId())) {
							   users.getUserTree().addItem(ontId);
							   users.getUserTree().setParent(ontId, user);
							   users.getUserTree().setChildrenAllowed(ontId, false);
//						   }
						}
					}
				}
			app.getMainWindow().showNotification(
					tab.getHeader() + " was saved",
					Notification.POSITION_CENTERED);
			selWin.addUserToList();
			
		}

	}

	private boolean isDoubleNum(String s) {
		try {
			Double.parseDouble(s);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	private boolean isIntegerNum(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public void windowClose(CloseEvent e) {
		if(tabSheet.getComponentCount() > 0 && saved)
			app.getMainWindow().showNotification("The person won't be added, <br> because you've broken the operation", Notification.TYPE_HUMANIZED_MESSAGE);
		
	}

}
