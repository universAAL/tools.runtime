package org.universAAL.tools.ucc.controller.space;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

/**
 * Profile controller for adding devices to rooms.
 *
 * @author Nicole Merkle
 *
 */
public class RoomsWindowController implements Property.ValueChangeListener, Button.ClickListener {
	private RoomsWindow win;
	private UccUI app;
	private BundleContext context;
	private DataAccess dataAccess;
	private TabSheet tabSheet;
	private HashMap<String, Subprofile> subprofiles;
	private HashMap<String, Subprofile> roomprofiles;
	private HashMap<String, ArrayList<TabForm>> userForms;
	private HashMap<String, ArrayList<Subprofile>> ontInstances;
	private HashMap<String, ArrayList<Subprofile>> roomInstances;
	private String selectedItem;
	private String actualFlat;
	private String actualHW;
	private String device;

	public RoomsWindowController(RoomsWindow window, UccUI app) throws JAXBException, IOException, ParseException {
		device = Activator.getDB().getAbsolutePath();
		this.app = app;
		this.win = window;
		actualFlat = device + "/Rooms.xml";
		actualHW = device + "/Hardware.xml";
		context = Activator.bc;
		ServiceReference ref = context.getServiceReference(DataAccess.class.getName());
		dataAccess = (DataAccess) context.getService(ref);
		context.ungetService(ref);
		ontInstances = new HashMap<String, ArrayList<Subprofile>>();
		roomInstances = new HashMap<String, ArrayList<Subprofile>>();
		subprofiles = new HashMap<String, Subprofile>();
		roomprofiles = new HashMap<String, Subprofile>();
		userForms = new HashMap<String, ArrayList<TabForm>>();
		tabSheet = new TabSheet();
		tabSheet.setSizeFull();
		tabSheet.setImmediate(true);
		win.getUserTree().addListener(this);
		loadData();
		win.addFirstComponent(win.getUserTree());
		win.addSecondComponent(tabSheet);

	}

	private void loadData() throws JAXBException, IOException, ParseException {
		// Creating Tabs with Forms
		ArrayList<OntologyInstance> tabs = dataAccess.getFormFields(actualFlat);
		ArrayList<OntologyInstance> roomSubs = dataAccess.getFormFields(actualHW);
		// ArrayList<OntologyInstance>tabs =
		// dataAccess.getEmptyCHEFormFields("Device");
		// ArrayList<OntologyInstance>roomSubs =
		// dataAccess.getEmptyCHEFormFields("Device");
		TabForm f = null;
		for (OntologyInstance r : roomSubs) {
			if (roomInstances.get(r.getId()) == null) {
				roomInstances.put(r.getId(), new ArrayList<Subprofile>());
			} else {
				roomInstances.get(r.getId()).clear();
			}

			for (Subprofile s : r.getSubprofiles()) {
				roomInstances.get(r.getId()).add(s);
				roomprofiles.put(s.getName(), s);
			}

		}
		for (OntologyInstance o : tabs) {
			if (userForms.get(o.getId()) == null) {
				userForms.put(o.getId(), new ArrayList<TabForm>());
			} else {
				userForms.get(o.getId()).clear();
			}
			if (ontInstances.get(o.getId()) == null) {
				ontInstances.put(o.getId(), new ArrayList<Subprofile>());
			} else {
				ontInstances.get(o.getId()).clear();
			}

			// Every Subprofile is shown in a seperate tab
			for (Subprofile tab : o.getSubprofiles()) {
				f = new TabForm();
				if (subprofiles.get(tab.getName()) != null)
					subprofiles.remove(tab.getName());
				// Save Subprofile Tabs for later use
				subprofiles.put(tab.getName(), tab);
				String selectedRole = null;
				// Creating User tree and Comboboxes
				for (EnumObject enumObj : tab.getEnums()) {
					NativeSelect box = new NativeSelect(enumObj.getLabel());
					box.setImmediate(true);
					if (enumObj.isTreeParentNode()) {
						for (String item : enumObj.getValues()) {
							win.getUserTree().addItem(item);
							win.getUserTree().setChildrenAllowed(item, true);
							win.getUserTree().expandItemsRecursively(item);
						}

						selectedRole = enumObj.getSelectedValue();
						win.getUserTree().addItem(o.getId());
						win.getUserTree().setParent(o.getId(), selectedRole);
						win.getUserTree().setChildrenAllowed(o.getId(), false);
					}

					// Create ComboBox with enum objects and add to form
					for (String item : enumObj.getValues()) {
						box.addItem(item);
						box.setValue(enumObj.getSelectedValue());
						box.setNullSelectionAllowed(false);
						box.setNewItemsAllowed(false);
						// box.select(enumObj.getSelectedValue());
					}
					box.setImmediate(true);
					box.setDescription(enumObj.getDescription());
					f.addField(enumObj.getType(), box);
					if (enumObj.isRequired()) {
						box.setRequired(true);
						box.setRequiredError(enumObj.getLabel() + " is required");
					}
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

						if (cols.getValue_type().equals("string")) {
							for (SimpleObject sim : cols.getCollection()) {
								StringValue s = (StringValue) sim;
								list.addItem(s.getValue());
								list.select(s.getValue());
							}
						}
						if (cols.getValue_type().equals("integer")) {
							for (SimpleObject sim : cols.getCollection()) {
								IntegerValue i = (IntegerValue) sim;
								list.addItem(i.getValue());
								list.select(i.getValue());
							}

						}
						if (cols.getValue_type().equals("double")) {
							for (SimpleObject sim : cols.getCollection()) {
								DoubleValue d = (DoubleValue) sim;
								list.addItem(d.getValue());
								list.select(d.getValue());
							}

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
				f.setReadOnly(true);
				f.setHeader(tab.getName());
				userForms.get(o.getId()).add(f);
				ontInstances.get(o.getId()).add(tab);
			}
		}
	}

	private TabForm createForm(SimpleObject simpleObject, TabForm form) throws ParseException {
		if (simpleObject instanceof CalendarValue) {
			CalendarValue cal = (CalendarValue) simpleObject;
			PopupDateField date = new PopupDateField(cal.getLabel());
			DateFormat format = new SimpleDateFormat();
			if (cal.getCalendar() != null && !cal.getCalendar().equals("")) {
				String d = cal.getCalendar();
				Date da = null;
				if (d.contains("/")) {
					format = new SimpleDateFormat("MM/dd/yy H:mm a");
					da = format.parse(d);
					date.setLocale(Locale.US);

				} else {
					format = new SimpleDateFormat("dd.MM.yy H:mm");
					da = format.parse(d);
					date.setLocale(Locale.GERMANY);
				}

				date.setValue(da);
				date.setResolution(PopupDateField.RESOLUTION_MIN);
				date.setImmediate(true);
				date.setInputPrompt(cal.getLabel());
				date.setShowISOWeekNumbers(true);
				date.setDescription(cal.getDescription());
				form.addField(cal.getLabel(), date);
			} else {
				if (cal.getName().equals("hardwareSettingTime")) {
					date.setValue(format.format(new Date()));
				} else {
					date.setInputPrompt("Last activity");
				}
				date.setDescription(cal.getDescription());
				form.addField(cal.getLabel(), date);
			}
			if (cal.isRequired()) {
				date.setRequired(true);
				date.setRequiredError(cal.getLabel() + " is required");
			}
		} else if (simpleObject instanceof StringValue) {
			StringValue st = (StringValue) simpleObject;
			if (st.getValue().length() > 30) {
				TextArea area = new TextArea(st.getLabel());
				area.setImmediate(true);
				area.setWriteThrough(false);
				area.setRows(5);
				area.setValue(st.getValue());
				area.setDescription(st.getDescription());
				form.addField(st.getLabel(), area);
				if (st.isRequired()) {
					area.setRequired(true);
					area.setRequiredError(st.getLabel() + " is required");
				}
				if (st.getValidator() != null) {
					if (st.getValidator().equals("EmailValidator")) {
						area.addValidator(new EmailValidator("Emailaddress isn't correct"));
					}
				}
			} else {
				TextField tf = new TextField(st.getLabel());
				tf.setWriteThrough(false);
				tf.setImmediate(true);
				tf.setValue(st.getValue());
				tf.setDescription(st.getDescription());
				form.addField(simpleObject.getLabel(), tf);
				if (st.isRequired()) {
					tf.setRequired(true);
					tf.setRequiredError(st.getLabel() + " is required");
				}
				if (st.getValidator() != null) {
					if (st.getValidator().equals("EmailValidator")) {
						tf.addValidator(new EmailValidator("Emailaddress isn't correct"));
					}
				}
			}
			form.createFooter();
		} else if (simpleObject instanceof IntegerValue) {
			IntegerValue integer = (IntegerValue) simpleObject;
			TextField t = new TextField(integer.getLabel());
			t.setImmediate(true);
			t.setWriteThrough(false);
			t.setValue(((IntegerValue) simpleObject).getValue());
			t.setDescription(integer.getDescription());
			form.addField(simpleObject.getLabel(), t);
			if (integer.isRequired()) {
				t.setRequired(true);
				t.setRequiredError(integer.getLabel() + " is required");
			}
			if (integer.getValidator() != null) {
				if (integer.getValidator().equals("RegexpValidator")) {
					if (integer.getName().contains("postalCode")) {
						t.addValidator(new RegexpValidator("[1-9][0-9]{4}", "Postal Code isn't correct"));
					} else {
						t.addValidator(new RegexpValidator("[1-9][0-9]*", "Number isn't correct"));
					}
				}
			}
		} else if (simpleObject instanceof BooleanValue) {
			BooleanValue bool = (BooleanValue) simpleObject;
			CheckBox box = new CheckBox(bool.getLabel());
			box.setImmediate(true);
			box.setWriteThrough(false);
			if (bool.getValue()) {
				box.setValue(true);
			} else {
				box.setValue(false);
			}
			box.setDescription(bool.getDescription());
			form.addField(bool.getLabel(), box);
			if (bool.isRequired()) {
				box.setRequired(true);
				box.setRequiredError(bool.getLabel() + " is required");
			}
		} else if (simpleObject instanceof DoubleValue) {
			DoubleValue doub = (DoubleValue) simpleObject;
			TextField tf = new TextField(doub.getLabel());
			tf.setImmediate(true);
			tf.setWriteThrough(false);
			tf.setValue(doub.getValue());
			tf.setDescription(doub.getDescription());
			form.addField(doub.getLabel(), tf);
			if (doub.isRequired()) {
				tf.setRequired(true);
				tf.setRequiredError(doub.getLabel() + " is required");
			}
			if (doub.getValidator() != null) {
				if (doub.getValidator().equals("RegexpValidator")) {
					tf.addValidator(new RegexpValidator("[0-9]*[.][0-9]{5}", "The floating value isn't correct"));
				}
			}
		}

		return form;
	}

	public void buttonClick(ClickEvent event) {
		String id = selectedItem;
		if (event.getButton() == ((TabForm) tabSheet.getSelectedTab()).getSaveButton()) {
			TabForm tab = ((TabForm) tabSheet.getSelectedTab());
			Subprofile sub = subprofiles.get(tabSheet.getTab(tab).getCaption());
			Subprofile subRoom = roomprofiles.get(tabSheet.getTab(tab).getCaption());
			// Aktuelles Subprofile ubernimmt die anderungen des Formulars
			ArrayList<SimpleObject> tempSim = new ArrayList<SimpleObject>();
			for (SimpleObject simi : sub.getSimpleObjects()) {
				tempSim.add(simi);
			}
			for (SimpleObject simpl : tempSim/* sub.getSimpleObjects() */) {
				// tab.getItemProperty(simpl.getLabel());
				if (simpl instanceof StringValue) {
					StringValue val = (StringValue) simpl;
					if (val.isId())
						id = (String) tab.getField(simpl.getLabel()).getValue();
					val.setValue((String) tab.getField(simpl.getLabel()).getValue());
				} else if (simpl instanceof IntegerValue) {
					IntegerValue val = (IntegerValue) simpl;
					if (isIntegerNum(tab.getField(val.getLabel()).getValue().toString())) {
						val.setValue(Integer.parseInt(tab.getField(simpl.getLabel()).getValue().toString()));
					} else
						val.setValue(val.getDefaultValue());
				} else if (simpl instanceof DoubleValue) {
					DoubleValue val = (DoubleValue) simpl;
					if (isDoubleNum(tab.getField(val.getLabel()).getValue().toString()))
						val.setValue(Double.parseDouble(tab.getItemProperty(simpl.getLabel()).getValue().toString()));
					else
						val.setValue(val.getDefaultValue());
				} else if (simpl instanceof BooleanValue) {
					BooleanValue val = (BooleanValue) simpl;
					val.setValue((Boolean) tab.getField(simpl.getLabel()).getValue());

				} else if (simpl instanceof CalendarValue) {
					CalendarValue cal = (CalendarValue) simpl;
					DateFormat df = new SimpleDateFormat();
					if (cal.getName().equals("hardwareSettingTime")) {
						String date = df.format(new Date());
						cal.setCalendar(date);
					}
				}
			}
			// Enum Objecte
			ArrayList<EnumObject> tempEnums = new ArrayList<EnumObject>();
			for (EnumObject e : sub.getEnums()) {
				tempEnums.add(e);
			}
			for (EnumObject en : tempEnums/* sub.getEnums() */) {
				if (en.isTreeParentNode()) {
					String sel = (String) ((Tree) win.getUserTree()).getValue();
					win.getUserTree().setParent(sel, tab.getField(en.getType()).getValue());
				}
				en.setSelectedValue((String) tab.getField(en.getType()).getValue());
			}

			// Collections
			ArrayList<CollectionValues> tempCols = new ArrayList<CollectionValues>();
			for (CollectionValues v : sub.getCollections()) {
				tempCols.add(v);
			}
			for (CollectionValues col : tempCols /* sub.getCollections() */) {
				Collection<SimpleObject> values = new ArrayList<SimpleObject>();
				Collection<SimpleObject> newVal = null;
				for (SimpleObject sim : col.getCollection()) {
					if (sim instanceof StringValue) {
						newVal = (Collection<SimpleObject>) tab.getField(col.getLabel()).getValue();
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
						newVal = (Collection<SimpleObject>) tab.getField(col.getLabel()).getValue();
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
								n.setValue(n.getDefaultValue());
							values.add(n);
						}

					} else if (sim instanceof DoubleValue) {
						newVal = (Collection<SimpleObject>) tab.getField(col.getLabel()).getValue();
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
								n.setValue(n.getDefaultValue());
							values.add(n);
						}

					}
					col.setCollection(values);
				}
			}

			// Roomsfile
			ArrayList<SimpleObject> roomSimpls = new ArrayList<SimpleObject>();
			for (SimpleObject teSim : subRoom.getSimpleObjects()) {
				roomSimpls.add(teSim);
			}
			for (SimpleObject simpl : roomSimpls/* subRoom.getSimpleObjects() */) {
				// tab.getItemProperty(simpl.getLabel());
				if (simpl instanceof StringValue) {
					StringValue val = (StringValue) simpl;
					val.setValue((String) tab.getField(simpl.getLabel()).getValue());
				} else if (simpl instanceof IntegerValue) {
					IntegerValue val = (IntegerValue) simpl;
					if (isIntegerNum(tab.getField(val.getLabel()).getValue().toString())) {
						val.setValue(Integer.parseInt(tab.getField(simpl.getLabel()).getValue().toString()));
					} else
						val.setValue(val.getDefaultValue());
				} else if (simpl instanceof DoubleValue) {
					DoubleValue val = (DoubleValue) simpl;
					if (isDoubleNum(tab.getField(val.getLabel()).getValue().toString()))
						val.setValue(Double.parseDouble(tab.getItemProperty(simpl.getLabel()).getValue().toString()));
					else
						val.setValue(val.getDefaultValue());
				} else if (simpl instanceof BooleanValue) {
					BooleanValue val = (BooleanValue) simpl;
					val.setValue((Boolean) tab.getField(simpl.getLabel()).getValue());

				} else if (simpl instanceof CalendarValue) {
					CalendarValue cal = (CalendarValue) simpl;
					DateFormat df = new SimpleDateFormat();
					if (cal.getName().equals("hardwareSettingTime")) {
						String date = df.format(new Date());
						cal.setCalendar(date);
					}
				}
			}
			// Enum Objecte
			ArrayList<EnumObject> roomEns = new ArrayList<EnumObject>();
			for (EnumObject te : subRoom.getEnums()) {
				roomEns.add(te);
			}
			for (EnumObject en : roomEns/* subRoom.getEnums() */) {
				en.setSelectedValue((String) tab.getItemProperty(en.getType()).getValue());
			}

			ArrayList<CollectionValues> roomCols = new ArrayList<CollectionValues>();
			for (CollectionValues c : subRoom.getCollections()) {
				roomCols.add(c);
			}
			for (CollectionValues col : roomCols /* subRoom.getCollections() */) {
				Collection<SimpleObject> values = new ArrayList<SimpleObject>();
				Collection<SimpleObject> newVal = null;
				for (SimpleObject sim : col.getCollection()) {
					if (sim instanceof StringValue) {
						newVal = (Collection<SimpleObject>) tab.getField(col.getLabel()).getValue();
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
						newVal = (Collection<SimpleObject>) tab.getField(col.getLabel()).getValue();
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
								n.setValue(n.getDefaultValue());
							values.add(n);
						}

					} else if (sim instanceof DoubleValue) {
						newVal = (Collection<SimpleObject>) tab.getField(col.getLabel()).getValue();
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
								n.setValue(n.getDefaultValue());
							values.add(n);
						}

					}
					col.setCollection(values);
				}
			}

			sub.setCollections(tempCols);
			sub.setEnums(tempEnums);
			sub.setSimpleObjects(tempSim);
			subRoom.setCollections(roomCols);
			subRoom.setEnums(roomEns);
			subRoom.setSimpleObjects(roomSimpls);
			HashMap<String, ArrayList<Subprofile>> nOntInstances = new HashMap<String, ArrayList<Subprofile>>();
			for (Map.Entry<String, ArrayList<Subprofile>> tOnt : ontInstances.entrySet()) {

				if (tOnt.getKey().equals(selectedItem)) {
					nOntInstances.put(tOnt.getKey(), new ArrayList<Subprofile>());
					for (Subprofile sp : tOnt.getValue()) {
						if (sp.getName().equals(sub.getName())) {
							nOntInstances.get(tOnt.getKey()).add(sub);
						} else {
							nOntInstances.get(tOnt.getKey()).add(sp);
						}
					}
				} else {
					nOntInstances.put(tOnt.getKey(), tOnt.getValue());
				}
			}
			// For room
			HashMap<String, ArrayList<Subprofile>> ri = new HashMap<String, ArrayList<Subprofile>>();
			for (Map.Entry<String, ArrayList<Subprofile>> tOnt : roomInstances.entrySet()) {

				if (tOnt.getKey().equals(id)) {
					ri.put(tOnt.getKey(), new ArrayList<Subprofile>());
					for (Subprofile sp : tOnt.getValue()) {
						if (sp.getName().equals(subRoom.getName())) {
							ri.get(tOnt.getKey()).add(subRoom);
						} else {
							ri.get(tOnt.getKey()).add(sp);
						}
					}
				} else {
					ri.put(tOnt.getKey(), tOnt.getValue());
				}
			}
			dataAccess.updateUserData(actualFlat, id, nOntInstances);
			dataAccess.updateUserData(actualHW, id, ri);
			tab.setReadOnly(true);
			tab.getSaveButton().setVisible(false);
			tab.getEditButton().setVisible(true);
			tab.getDeleteButton().setVisible(true);
			app.getMainWindow().showNotification(tab.getHeader() + " was updated", Notification.POSITION_CENTERED);

		} // Edit button was pushed
		else if (event.getButton() == ((TabForm) tabSheet.getSelectedTab()).getEditButton()) {
			TabForm tab = ((TabForm) tabSheet.getSelectedTab());
			tab.getSaveButton().setVisible(true);
			tab.getEditButton().setVisible(false);
			tab.getResetButton().setVisible(true);
			tab.getDeleteButton().setVisible(false);
			tab.setReadOnly(false);
			if (tab.getField("Device Address:") != null)
				tab.getField("Device Address:").setReadOnly(true);
			if (tab.getField("Last activity time:") != null)
				tab.getField("Last activity time:").setReadOnly(true);
			if (tab.getField("Setting time:") != null)
				tab.getField("Setting time:").setReadOnly(true);
		} // Delete Button was pushed
		else if (event.getButton() == ((TabForm) tabSheet.getSelectedTab()).getDeleteButton()) {
			dataAccess.deleteUserData(actualFlat, selectedItem);
			dataAccess.deleteUserData(actualHW, selectedItem);
			win.getUserTree().removeListener(this);
			win.getUserTree().removeItem(selectedItem);
			win.getUserTree().addListener(this);

			HardwareWindow hWin = null;
			TabForm tab = (TabForm) tabSheet.getSelectedTab();
			for (Window w : app.getMainWindow().getChildWindows()) {
				if (w instanceof HardwareWindow) {
					hWin = (HardwareWindow) w;
					if (hWin.getUserTree().containsId(tab.getField("Device Address:").getValue())) {
						hWin.getUserTree().removeItem(tab.getField("Device Address:").getValue());
						if (hWin.getHwc().getTabSheet().getComponentCount() > 0)
							hWin.getHwc().getTabSheet().removeAllComponents();
					}
				}
			}
			tabSheet.removeAllComponents();
			app.getMainWindow().showNotification(selectedItem + " was deleted", Notification.POSITION_CENTERED);
		}

	}

	public TabSheet getTabSheet() {
		return tabSheet;
	}

	public void setTabSheet(TabSheet tabSheet) {
		this.tabSheet = tabSheet;
	}

	public void valueChange(ValueChangeEvent event) {
		Tree tree = ((Tree) event.getProperty());
		if (tree.getValue() != null) {
			if (!tree.isRoot(tree.getValue())) {
				selectedItem = (String) tree.getValue();

				try {
					loadData();
				} catch (JAXBException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}

				tabSheet.removeAllComponents();
				for (TabForm form : userForms.get(selectedItem)) {
					Tab act = tabSheet.addTab(form, form.getHeader());
					act.setClosable(true);
				}

			} else {
				try {
					AddNewHardwareWindow roomWindow = new AddNewHardwareWindow(null, win, app);
					roomWindow.setPositionX(this.win.getPositionX() + 100);
					roomWindow.setPositionY(this.win.getPositionY() + 100);
					app.getMainWindow().addWindow(roomWindow);
				} catch (JAXBException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
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

}
