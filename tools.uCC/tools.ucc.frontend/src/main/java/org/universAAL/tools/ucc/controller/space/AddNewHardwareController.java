package org.universAAL.tools.ucc.controller.space;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import javax.xml.bind.JAXBException;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.universAAL.tools.ucc.database.space.DataAccess;
import org.universAAL.tools.ucc.model.jaxb.BooleanValue;
import org.universAAL.tools.ucc.model.jaxb.CalendarValue;
import org.universAAL.tools.ucc.model.jaxb.CollectionValues;
import org.universAAL.tools.ucc.model.jaxb.DoubleValue;
import org.universAAL.tools.ucc.model.jaxb.EnumObject;
import org.universAAL.tools.ucc.model.jaxb.IntegerValue;
import org.universAAL.tools.ucc.model.jaxb.OntologyInstance;
import org.universAAL.tools.ucc.model.jaxb.SimpleObject;
import org.universAAL.tools.ucc.model.jaxb.StringValue;
import org.universAAL.tools.ucc.model.jaxb.Subprofile;
import org.universAAL.tools.ucc.service.manager.Activator;
import org.universAAL.tools.ucc.windows.AddNewHardwareWindow;
import org.universAAL.tools.ucc.windows.HardwareWindow;
import org.universAAL.tools.ucc.windows.RoomsWindow;
import org.universAAL.tools.ucc.windows.TabForm;
import org.universAAL.tools.ucc.windows.UccUI;

import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.Notification;

/**
 * Controller for adding a new Person to space.
 *
 * @author Nicole Merkle
 *
 */

public class AddNewHardwareController implements Button.ClickListener, Window.CloseListener {
	private AddNewHardwareWindow win;
	private UccUI app;
	private BundleContext context;
	private DataAccess dataAccess;
	private TabSheet tabSheet;
	private HashMap<String, Subprofile> subprofiles;
	private HashMap<String, Subprofile> roomprofiles;
	private String ontId;
	private ArrayList<OntologyInstance> objects;
	private ArrayList<OntologyInstance> rooms;
	private OntologyInstance instance;
	private OntologyInstance roomInstance;
	private RoomsWindow rWindow;
	private String flatId;
	private String device;
	private static String ontoProfile;
	private static String roomProfile;
	private String actualFlat;
	private String actualRoomFile;
	private ArrayList<OntologyInstance> savedObjects;
	private boolean saved;

	public AddNewHardwareController(AddNewHardwareWindow window, HardwareWindow hWin, RoomsWindow rWin, UccUI app)
			throws JAXBException, IOException, ParseException {
		device = Activator.getDB().getAbsolutePath();
		ontoProfile = device + "/EmptyHardware.xml";
		roomProfile = device + "/EmptyRoom.xml";
		this.app = app;
		this.saved = false;
		this.win = window;
		this.win.addListener(this);
		this.rWindow = rWin;
		actualFlat = device + "/Hardware.xml";
		actualRoomFile = device + "/Rooms.xml";
		context = Activator.bc;// FrameworkUtil.getBundle(getClass()).getBundleContext();
		ServiceReference ref = context.getServiceReference(DataAccess.class.getName());
		dataAccess = (DataAccess) context.getService(ref);
		context.ungetService(ref);
		savedObjects = dataAccess.getFormFields(actualFlat);
		// savedObjects = dataAccess.getEmptyCHEFormFields("Device");
		instance = new OntologyInstance();
		roomInstance = new OntologyInstance();
		subprofiles = new HashMap<String, Subprofile>();
		roomprofiles = new HashMap<String, Subprofile>();
		tabSheet = new TabSheet();
		tabSheet.setSizeFull();
		tabSheet.setImmediate(true);
		loadData();
		if (hWin != null) {
			TabForm tab = (TabForm) tabSheet.getSelectedTab();
			tab.getField(tab.getId()).setValue(hWin.getUserTree().getValue());
		}
		if (rWin != null) {
			TabForm tab = (TabForm) tabSheet.getSelectedTab();
			tab.getField(tab.getId()).setValue(rWin.getUserTree().getValue());
		}
		win.addWindowContent(tabSheet);

	}

	private void loadData() throws JAXBException, IOException, ParseException {
		// Creating Tabs with Forms
		// objects = dataAccess.getEmptyCHEFormFields("Device");
		objects = dataAccess.getEmptyProfile(ontoProfile);
		rooms = dataAccess.getEmptyProfile(roomProfile);
		// rooms = dataAccess.getEmptyCHEFormFields("Device");
		TabForm f = null;
		TabForm form = null;
		// Every Subprofile is shown in a seperate tab
		for (Subprofile tab : objects.get(0).getSubprofiles()) {
			instance.getSubprofiles().add(tab);
			f = new TabForm();
			// Save Subprofile Tabs for later use
			subprofiles.put(tab.getName(), tab);
			// Creating User tree and Comboboxes
			for (EnumObject enumObj : tab.getEnums()) {
				NativeSelect box = new NativeSelect(enumObj.getLabel());
				box.setDescription(enumObj.getDescription());
				// Create ComboBox with enum objects and add to form
				for (String item : enumObj.getValues()) {
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

					// Adding List to Form
					list.setImmediate(true);
					list.setNullSelectionAllowed(true);
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
			if (rWindow == null)
				tabSheet.addTab(f, tab.getName());
		}

		// Load Rooms Profile
		for (Subprofile tab : rooms.get(0).getSubprofiles()) {
			roomInstance.getSubprofiles().add(tab);
			form = new TabForm();
			roomprofiles.put(tab.getName(), tab);
			// Creating User tree and Comboboxes
			for (EnumObject enumObj : tab.getEnums()) {
				NativeSelect box = new NativeSelect(enumObj.getLabel());
				box.setDescription(enumObj.getDescription());
				// Create ComboBox with enum objects and add to form
				for (String item : enumObj.getValues()) {
					box.addItem(item);
				}
				box.setImmediate(true);
				if (enumObj.isRequired()) {
					box.setRequired(true);
					box.setRequiredError(enumObj.getLabel() + " is required");
				}
				if (enumObj.isTreeParentNode()) {
					form.setId(enumObj.getType());
				}
				form.addField(enumObj.getType(), box);

			}
			// Add simpel objects to form
			for (SimpleObject simpl : tab.getSimpleObjects()) {
				createForm(simpl, form);
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

					// Adding List to Form
					list.setImmediate(true);
					list.setNullSelectionAllowed(true);
					list.setRows(5);
					list.setNewItemsAllowed(true);
					form.addField(cols.getLabel(), list);
				}
			}
			form.createFooter();
			form.getSaveButton().addListener((Button.ClickListener) this);
			form.getEditButton().addListener((Button.ClickListener) this);
			form.getResetButton().addListener((Button.ClickListener) this);
			form.getDeleteButton().addListener((Button.ClickListener) this);
			form.getEditButton().setVisible(false);
			form.getSaveButton().setVisible(true);
			form.getDeleteButton().setVisible(false);
			form.setHeader(tab.getName());
			form.setReadOnly(false);
			if (rWindow != null)
				tabSheet.addTab(form, tab.getName());
		}

	}

	private TabForm createForm(SimpleObject simpleObject, TabForm form) throws ParseException {
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
			date.setEnabled(false);
			form.addField(cal.getLabel(), date);
		} else if (simpleObject instanceof StringValue) {
			StringValue st = (StringValue) simpleObject;
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
						area.addValidator(new EmailValidator("The Emailaddress isn't correct"));
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
						tf.addValidator(new EmailValidator("The Emailaddress isn't correct"));
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
						t.addValidator(new RegexpValidator("[1-9][0-9]{4}", "The postal code isn't correct"));
					} else {
						t.addValidator(new RegexpValidator("[1-9][0-9]*", "Please insert an valid number"));
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
					tf.addValidator(new RegexpValidator("[0-9]*[.][0-9]{5}", "Please insert a valid floating number"));
				}
			}
			form.addField(doub.getLabel(), tf);
		}

		return form;
	}

	String hw = "";
	String ro = "";

	public void buttonClick(ClickEvent event) {
		if (event.getButton() == ((TabForm) tabSheet.getSelectedTab()).getSaveButton()) {
			saved = true;
			TabForm tab = (TabForm) tabSheet.getSelectedTab();
			String tabHeader = tabSheet.getTab(tab).getCaption();
			Subprofile sub = new Subprofile();
			Subprofile room = new Subprofile();
			sub.setName(tabHeader);
			room.setName(tabHeader);
			// SimpleObjects
			ArrayList<SimpleObject> simpleObjects = new ArrayList<SimpleObject>();
			for (SimpleObject sim : subprofiles.get(tabHeader).getSimpleObjects()) {
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
					if (isIntegerNum(tab.getField(sim.getLabel()).getValue().toString()))
						integer.setValue(Integer.parseInt(tab.getField(sim.getLabel()).getValue().toString()));
					else
						integer.setValue(0);
					simpleObjects.add(integer);
				}
				if (sim instanceof DoubleValue) {
					DoubleValue doub = (DoubleValue) sim;
					if (isDoubleNum(tab.getField(doub.getLabel()).getValue().toString()))
						doub.setValue(Double.parseDouble(tab.getField(doub.getLabel()).getValue().toString()));
					else
						doub.setValue(0.0);
					simpleObjects.add(doub);
				}
				if (sim instanceof BooleanValue) {
					BooleanValue bool = (BooleanValue) sim;
					bool.setValue((Boolean) tab.getField(bool.getLabel()).getValue());
					simpleObjects.add(bool);
				}
				if (sim instanceof CalendarValue) {
					CalendarValue cal = (CalendarValue) sim;
					DateFormat df = new SimpleDateFormat();
					if (cal.getName().equals("hardwareSettingTime")) {
						tab.getField(sim.getLabel()).setValue(df.format(new Date()));
						String date = df.format((Date) tab.getField(sim.getLabel()).getValue());
						cal.setCalendar(date);
					} else {
						cal.setCalendar("");
					}

					simpleObjects.add(cal);
				}
			}
			sub.setSimpleObjects(simpleObjects);

			// EnumObjects
			ArrayList<EnumObject> enums = new ArrayList<EnumObject>();
			for (EnumObject en : subprofiles.get(tabHeader).getEnums()) {
				en.setSelectedValue((String) tab.getField(en.getType()).getValue());
				enums.add(en);
			}
			sub.setEnums(enums);

			// CollectionValues
			ArrayList<CollectionValues> collections = new ArrayList<CollectionValues>();
			for (CollectionValues cols : subprofiles.get(tabHeader).getCollections()) {
				Collection<SimpleObject> values = new ArrayList<SimpleObject>();
				Collection<SimpleObject> newVal = null;
				for (SimpleObject sim : cols.getCollection()) {
					if (sim instanceof StringValue) {
						newVal = (Collection<SimpleObject>) tab.getField(cols.getLabel()).getValue();
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
						newVal = (Collection<SimpleObject>) tab.getField(cols.getLabel()).getValue();
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
						newVal = (Collection<SimpleObject>) tab.getField(cols.getLabel()).getValue();
						Object[] array = newVal.toArray();
						for (int i = 0; i < array.length; i++) {
							DoubleValue n = new DoubleValue();
							n.setDescription(sim.getDescription());
							n.setLabel(sim.getLabel());
							n.setRequired(sim.isRequired());
							n.setValidator(sim.getValidator());
							if (isDoubleNum(array[i].toString()))
								n.setValue(Double.parseDouble(array[i].toString()));
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

			// /ROOM PRofile
			ArrayList<SimpleObject> roomObjects = new ArrayList<SimpleObject>();
			for (SimpleObject sim : roomprofiles.get(tabHeader).getSimpleObjects()) {
				if (sim instanceof StringValue) {
					StringValue s = (StringValue) sim;
					s.setValue((String) tab.getField(s.getLabel()).getValue());
					roomObjects.add(s);

					if (s.isId()) {
						ontId = s.getValue();
					}
				}
				if (sim instanceof IntegerValue) {
					IntegerValue integer = (IntegerValue) sim;
					if (isIntegerNum(tab.getField(sim.getLabel()).getValue().toString()))
						integer.setValue(Integer.parseInt(tab.getField(sim.getLabel()).getValue().toString()));
					else
						integer.setValue(0);
					roomObjects.add(integer);
				}
				if (sim instanceof DoubleValue) {
					DoubleValue doub = (DoubleValue) sim;
					if (isDoubleNum(tab.getField(doub.getLabel()).getValue().toString()))
						doub.setValue(Double.parseDouble(tab.getField(doub.getLabel()).getValue().toString()));
					else
						doub.setValue(0.0);
					roomObjects.add(doub);
				}
				if (sim instanceof BooleanValue) {
					BooleanValue bool = (BooleanValue) sim;
					bool.setValue((Boolean) tab.getField(bool.getLabel()).getValue());
					roomObjects.add(bool);
				}
				if (sim instanceof CalendarValue) {
					CalendarValue cal = (CalendarValue) sim;
					DateFormat df = new SimpleDateFormat();
					if (cal.getName().equals("hardwareSettingTime")) {
						String date = df.format(new Date());
						cal.setCalendar(date);
					} else {
						cal.setCalendar("");
					}
					roomObjects.add(cal);

				}
			}
			room.setSimpleObjects(simpleObjects);

			// EnumObjects
			ArrayList<EnumObject> ens = new ArrayList<EnumObject>();
			for (EnumObject en : roomprofiles.get(tabHeader).getEnums()) {
				en.setSelectedValue((String) tab.getField(en.getType()).getValue());
				ens.add(en);
				if (en.getType().equals("devicetype")) {
					hw = en.getSelectedValue();
				} else if (en.getType().equals("rooms")) {
					ro = en.getSelectedValue();
				}
			}
			room.setEnums(ens);

			// CollectionValues
			ArrayList<CollectionValues> rCollections = new ArrayList<CollectionValues>();
			for (CollectionValues cols : roomprofiles.get(tabHeader).getCollections()) {
				Collection<SimpleObject> values = new ArrayList<SimpleObject>();
				Collection<SimpleObject> newVal = null;
				for (SimpleObject sim : cols.getCollection()) {
					if (sim instanceof StringValue) {
						newVal = (Collection<SimpleObject>) tab.getField(cols.getLabel()).getValue();
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
						newVal = (Collection<SimpleObject>) tab.getField(cols.getLabel()).getValue();
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
						newVal = (Collection<SimpleObject>) tab.getField(cols.getLabel()).getValue();
						Object[] array = newVal.toArray();
						for (int i = 0; i < array.length; i++) {
							DoubleValue n = new DoubleValue();
							n.setDescription(sim.getDescription());
							n.setLabel(sim.getLabel());
							n.setRequired(sim.isRequired());
							n.setValidator(sim.getValidator());
							if (isDoubleNum(array[i].toString()))
								n.setValue(Double.parseDouble(array[i].toString()));
							else
								n.setValue(0.0);
							values.add(n);
						}

					}
					cols.setCollection(values);
				}
				rCollections.add(cols);
			}
			room.setCollections(rCollections);
			for (OntologyInstance ont : savedObjects) {
				if (tab.getField("Device Address:") != null
						&& tab.getField("Device Address:").getValue().equals(ont.getId())) {
					tab.getField("Device Address:").setValue("");
					app.getMainWindow().showNotification("You can't add a device twice",
							Notification.TYPE_HUMANIZED_MESSAGE);
					return;
				}
			}
			instance.setId(ontId);
			roomInstance.setId(ontId);
			for (Subprofile prof : instance.getSubprofiles()) {
				if (prof.getName().equals(sub.getName())) {
					prof = sub;
				}
			}
			for (Subprofile r : roomInstance.getSubprofiles()) {
				if (r.getName().equals(sub.getName())) {
					r = room;
				}
			}
			objects.add(instance);
			rooms.add(roomInstance);
			// Test
			if (tabSheet.getComponentCount() > 0) {
				for (Window w : app.getMainWindow().getChildWindows()) {
					if (w instanceof RoomsWindow) {
						RoomsWindow rooms = (RoomsWindow) w;
						rooms.getUserTree().addItem(ontId);
						rooms.getUserTree().setParent(ontId, tab.getField("rooms").getValue());
						rooms.getUserTree().setChildrenAllowed(ontId, false);
					}
					if (w instanceof HardwareWindow) {
						HardwareWindow hw = (HardwareWindow) w;
						hw.getUserTree().addItem(ontId);
						hw.getUserTree().setParent(ontId, tab.getField("devicetype").getValue());
						hw.getUserTree().setChildrenAllowed(ontId, false);
					}
				}
			}
			tab.setReadOnly(true);
			tabSheet.removeComponent(tab);
			if (tabSheet.getComponentCount() == 0) {
				dataAccess.saveUserData(actualFlat, instance);
				dataAccess.saveUserData(actualRoomFile, roomInstance);
				app.getMainWindow().removeWindow(win);

			}

			if (tabSheet.getComponentCount() == 0) {
				for (Window w : app.getMainWindow().getChildWindows()) {
					if (w instanceof HardwareWindow) {
						HardwareWindow users = (HardwareWindow) w;
						users.getUserTree().addItem(ontId);
						users.getUserTree().setParent(ontId, hw);
						users.getUserTree().setChildrenAllowed(ontId, false);
					} else if (w instanceof RoomsWindow) {
						RoomsWindow rs = (RoomsWindow) w;
						rs.getUserTree().addItem(ontId);
						rs.getUserTree().setParent(ontId, ro);
						rs.getUserTree().setChildrenAllowed(ontId, false);
					}
				}
			}

			app.getMainWindow().showNotification(tab.getHeader() + " was saved", Notification.POSITION_CENTERED);

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
		if (tabSheet.getComponentCount() > 0 && saved)
			app.getMainWindow().showNotification("The device won't be added, <br> because you've broken the operation",
					Notification.TYPE_HUMANIZED_MESSAGE);

	}

}
