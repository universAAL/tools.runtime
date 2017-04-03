package org.universAAL.ucc.controller.aalspace;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

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
import org.universAAL.ucc.startup.api.impl.SetupImpl;
import org.universAAL.ucc.startup.model.Role;
import org.universAAL.ucc.startup.model.UserAccountInfo;
import org.universAAL.ucc.windows.AddNewPersonWindow;
import org.universAAL.ucc.windows.HumansWindow;
import org.universAAL.ucc.windows.TabForm;
import org.universAAL.ucc.windows.UccUI;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
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
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Window.Notification;

public class PersonWindowController  implements Property.ValueChangeListener, Button.ClickListener {
	private HumansWindow win;
	private UccUI app;
	private BundleContext context;
	private DataAccess dataAccess;
	private TabSheet tabSheet;
	private HashMap<String, Subprofile>subprofiles;
	private HashMap<String, ArrayList<TabForm>>userForms;
	private HashMap<String, ArrayList<Subprofile>>ontInstances;
	private String selectedItem;
	private String device;
	private String actualFlat;
	private ModuleConfigHome mc;
	private Setup setup;
	private String path;

	
	public PersonWindowController(HumansWindow window, UccUI app) throws JAXBException, IOException, ParseException {
		mc = new ModuleConfigHome("uccDB", "");
		device = mc.getAbsolutePath();
		this.app = app;
		this.win = window;
		actualFlat = device + "/Users.xml";
		context = Activator.bc;//FrameworkUtil.getBundle(getClass()).getBundleContext();
		ServiceReference ref = context.getServiceReference(DataAccess.class.getName());
		dataAccess = (DataAccess)context.getService(ref);
		context.ungetService(ref);
		ontInstances = new HashMap<String, ArrayList<Subprofile>>();
		subprofiles = new HashMap<String, Subprofile>();
		userForms = new HashMap<String, ArrayList<TabForm>>();
		tabSheet = new TabSheet();
		tabSheet.setSizeFull();
		tabSheet.setImmediate(true);
		win.getUserTree().addListener(this);
		loadData();
		win.addFirstComponent(win.getUserTree());
		win.addSecondComponent(tabSheet);
		setup = new SetupImpl();
	}
	
	private void loadData() throws JAXBException, IOException, ParseException {
		//Creating Tabs with Forms
				ArrayList<OntologyInstance>tabs = dataAccess.getEmptyCHEFormFields("User");
//				ArrayList<OntologyInstance> tabs = dataAccess.getFormFields(actualFlat);
				TabForm f = null;
				for(OntologyInstance o : tabs) {
					userForms.put(o.getId(), new ArrayList<TabForm>());
					ontInstances.put(o.getId(), new ArrayList<Subprofile>());
					//Every Subprofile is shown in a seperate tab
					for(Subprofile tab : o.getSubprofiles()) {
						f = new TabForm();
						//Save Subprofile Tabs for later use
						subprofiles.put(tab.getName(), tab);
						String selectedRole = null;
						//Creating User tree and Comboboxes
						for(EnumObject enumObj : tab.getEnums()) {
							NativeSelect box = new NativeSelect(enumObj.getLabel());
							if(enumObj.isTreeParentNode()) {
								for(String item : enumObj.getValues()) {
									win.getUserTree().addItem(item);
									win.getUserTree().setChildrenAllowed(item, true);
									win.getUserTree().expandItemsRecursively(item);
									selectedRole = enumObj.getSelectedValue();
									
									win.getUserTree().addItem(o.getId());
									win.getUserTree().setParent(o.getId(), selectedRole);
									win.getUserTree().setChildrenAllowed(o.getId(), false);
									
								}
							} 
								//Create ComboBox with enum objects and add to form
								for(String item : enumObj.getValues()) {
									box.addItem(item);
									box.setNullSelectionAllowed(false);
									box.setNewItemsAllowed(false);
									box.setValue(enumObj.getSelectedValue());
//									box.select(enumObj.getSelectedValue());
								} box.setImmediate(true);
								box.setDescription(enumObj.getDescription());
								f.addField(enumObj.getType(), box);
								if(enumObj.isRequired()) {
									box.setRequired(true);
									box.setRequiredError(enumObj.getLabel()+ " is required");
								}
						}
						//Add simpel objects to form
						for(SimpleObject simpl : tab.getSimpleObjects()) {
							createForm(simpl, f);
						} 
						//Adding collection objects as a list to form
						if(tab.getCollections().size() > 0) {
						for(CollectionValues cols : tab.getCollections()) {
							ListSelect list = new ListSelect();
							list.setCaption(cols.getLabel());
							list.setWidth("120px");
							list.setDescription(cols.getDescription());
							if(cols.isMultiselect()) {
								list.setMultiSelect(true);
							}
							
							if(cols.getValue_type().equals("string")) {
								for(SimpleObject sim : cols.getCollection()) {
									StringValue s = (StringValue)sim;
									list.addItem(s.getValue());
									list.select(s.getValue());
								} 
							}
							if(cols.getValue_type().equals("integer")) {
								for(SimpleObject sim : cols.getCollection()) {
									IntegerValue i = (IntegerValue)sim;
									list.addItem(i.getValue());
									list.select(i.getValue());
								} 
								
							}
							if(cols.getValue_type().equals("double")) {
								for(SimpleObject sim : cols.getCollection()) {
									DoubleValue d = (DoubleValue)sim;
									list.addItem(d.getValue());
									list.select(d.getValue());
								} 
								
							}
							//Adding List to Form
							list.setImmediate(true);
							list.setNullSelectionAllowed(false);
							list.setRows(5);
							list.setNewItemsAllowed(true);
							f.addField(cols.getLabel(), list);
							}
						}
						f.createFooter();
						f.getSaveButton().addListener((Button.ClickListener)this);
						f.getEditButton().addListener((Button.ClickListener)this);
						f.getResetButton().addListener((Button.ClickListener)this);
						f.getDeleteButton().addListener((Button.ClickListener)this);
						f.setReadOnly(true);
						f.setHeader(tab.getName());
						userForms.get(o.getId()).add(f);
						ontInstances.get(o.getId()).add(tab);
					} 
				}
	}
	
	private TabForm createForm(SimpleObject simpleObject, TabForm form) throws ParseException {
		if(simpleObject instanceof CalendarValue) {
			CalendarValue cal = (CalendarValue)simpleObject; 
			PopupDateField date = new PopupDateField(cal.getLabel());
			date.setResolution(PopupDateField.RESOLUTION_MIN);
			date.setImmediate(true);
			date.setInputPrompt(cal.getLabel());
			date.setShowISOWeekNumbers(true);
			date.setDescription(cal.getDescription());
			DateFormat format = new SimpleDateFormat();
			if(cal.getCalendar() != null && !cal.getCalendar().equals("")) {
			String dat = cal.getCalendar();
			Date da = null;
			if(dat.contains("/")) {
				format = new SimpleDateFormat("MM/dd/yy H:mm a");
				da = format.parse(dat);
				date.setLocale(Locale.US);
			} else {
				format = new SimpleDateFormat("dd.MM.yy H:mm");
				da = format.parse(dat);
				date.setLocale(Locale.GERMANY);
			}
			date.setValue(da);
			form.addField(cal.getLabel(), date);
			} else {
				date.setValue(format.format(new Date()));
				date.setDescription(cal.getDescription());
				form.addField(cal.getLabel(), date);
			}
			if(cal.isRequired()) {
				date.setRequired(true);
				date.setRequiredError(cal.getLabel()+" is required");
			}
		} 
		else if(simpleObject instanceof StringValue) {
				StringValue st = (StringValue)simpleObject;
				if(st.getName().toLowerCase().contains("password")) {
					PasswordField pwd = new PasswordField(st.getLabel());
					pwd.setWriteThrough(false);
					pwd.setImmediate(true);
					pwd.setDescription(st.getDescription());
					pwd.setValue(st.getValue());
					if (st.isRequired()) {
						pwd.setRequired(true);
						pwd.setRequiredError(st.getLabel() + " is required");
					}
					form.addField(st.getLabel(), pwd);
				} else 
				if(st.getValue().length() > 30) {
					TextArea area = new TextArea(st.getLabel());
					area.setImmediate(true);
					area.setWriteThrough(false);
					area.setRows(5);
					area.setValue(st.getValue());
					area.setDescription(st.getDescription());
					form.addField(st.getLabel(), area);
					if(st.isRequired()) {
						area.setRequired(true);
						area.setRequiredError(st.getLabel()+" is required");
					}
					if(st.getValidator() != null) {
						if(st.getValidator().equals("EmailValidator")) {
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
				if(st.isRequired()) {
					tf.setRequired(true);
					tf.setRequiredError(st.getLabel()+" is required");
				}
				if(st.getValidator() != null) {
					if(st.getValidator().equals("EmailValidator")) {
						tf.addValidator(new EmailValidator("Emailaddress isn't correct"));
					}
				}
				}
				form.createFooter();
			}
			else if(simpleObject instanceof IntegerValue) {
				IntegerValue integer = (IntegerValue)simpleObject;
				TextField t = new TextField(integer.getLabel());
				t.setImmediate(true);
				t.setWriteThrough(false);
				t.setValue(((IntegerValue) simpleObject).getValue());
				t.setDescription(integer.getDescription());
				form.addField(simpleObject.getLabel(), t);
				if(integer.isRequired()) {
					t.setRequired(true);
					t.setRequiredError(integer.getLabel()+" is required");
				}
				if(integer.getValidator() != null) {
					if(integer.getValidator().equals("RegexpValidator")) {
						if(integer.getName().contains("postalCode")) {
							t.addValidator(new RegexpValidator("[1-9][0-9]{4}", "Postal Code isn't correct"));
						} else {
							t.addValidator(new RegexpValidator("[1-9][0-9]*", "Number isn't correct"));
						}
					}
				}
			}
			else if(simpleObject instanceof BooleanValue) {
				BooleanValue bool = (BooleanValue)simpleObject;
				CheckBox box = new CheckBox(bool.getLabel());
				box.setImmediate(true);
				box.setWriteThrough(false);
				if(bool.getValue()) {
					box.setValue(true);
				} else {
					box.setValue(false);
				}
				box.setDescription(bool.getDescription());
				form.addField(bool.getLabel(), box);
				if(bool.isRequired()) {
					box.setRequired(true);
					box.setRequiredError(bool.getLabel()+" is required");
				}
			}
			else if(simpleObject instanceof DoubleValue) {
				DoubleValue doub = (DoubleValue)simpleObject;
				TextField tf = new TextField(doub.getLabel());
				tf.setImmediate(true);
				tf.setWriteThrough(false);
				tf.setValue(doub.getValue());
				tf.setDescription(doub.getDescription());
				form.addField(doub.getLabel(), tf);
				if(doub.isRequired()) {
					tf.setRequired(true);
					tf.setRequiredError(doub.getLabel()+ " is required");
				}
				if(doub.getValidator() != null) {
					if(doub.getValidator().equals("RegexpValidator")) {
						tf.addValidator(new RegexpValidator("[0-9]*[.][0-9]{5}", "The floating value isn't correct"));
					}
				}
			}
			
		return form;
	}

	int index = 0;

	public void buttonClick(ClickEvent event) {
		if(event.getButton() == ((TabForm)tabSheet.getSelectedTab()).getSaveButton()) {
			TabForm tab = ((TabForm)tabSheet.getSelectedTab());
			Subprofile sub = subprofiles.get(tabSheet.getTab(tab).getCaption());
			//Aktuelles Subprofile ubernimmt die anderungen des Formulars
			ArrayList<SimpleObject>tempSim = new ArrayList<SimpleObject>();
			for(SimpleObject simi : sub.getSimpleObjects()) {
				tempSim.add(simi);
			}
				for(SimpleObject simpl : tempSim /*sub.getSimpleObjects()*/) {
					if(simpl instanceof StringValue) {
						StringValue val = (StringValue)simpl;
						val.setValue((String)tab.getField(simpl.getLabel()).getValue());
					} else 
					if(simpl instanceof IntegerValue) {
						IntegerValue val = (IntegerValue)simpl;
						if(isIntegerNum(tab.getField(val.getLabel()).getValue().toString())) {
							val.setValue(Integer.parseInt(tab.getField(simpl.getLabel()).getValue().toString()));
						} else 
							val.setValue(val.getDefaultValue());
					} else
						if(simpl instanceof DoubleValue) {
							DoubleValue val = (DoubleValue)simpl;
							if(isDoubleNum(tab.getField(val.getLabel()).getValue().toString())) 
								val.setValue(Double.parseDouble(tab.getItemProperty(simpl.getLabel()).getValue().toString()));
							else
								val.setValue(val.getDefaultValue());
					} else
						if(simpl instanceof BooleanValue) {
							BooleanValue val = (BooleanValue)simpl;
							val.setValue((Boolean)tab.getField(simpl.getLabel()).getValue());
							
					} else 
						if(simpl instanceof CalendarValue) {
							CalendarValue cal = (CalendarValue)simpl;
							DateFormat df = new SimpleDateFormat();
							String date = df.format((Date)tab.getField(simpl.getLabel()).getValue());
							cal.setCalendar(date);
					}
			}
				//Enum Objecte
				ArrayList<EnumObject>tempEnums = new ArrayList<EnumObject>();
				for(EnumObject e : sub.getEnums()) {
					tempEnums.add(e);
				}
				for(EnumObject en : tempEnums /*sub.getEnums()*/) {
						if(en.isTreeParentNode()) {
							String sel = (String)((Tree)win.getUserTree()).getValue();
							win.getUserTree().setParent(sel, tab.getItemProperty(en.getType()).getValue());
						}
						en.setSelectedValue((String)tab.getItemProperty(en.getType()).getValue());
				}
				
				//Collections
				ArrayList<CollectionValues>tempCols = new ArrayList<CollectionValues>();
				for(CollectionValues vs : sub.getCollections()) {
					tempCols.add(vs);
				}
				for(CollectionValues col : tempCols /*sub.getCollections()*/) {
					Collection<SimpleObject>values = new ArrayList<SimpleObject>();
					Collection<SimpleObject>newVal = null;
					for(SimpleObject sim : col.getCollection()) {
						if(sim instanceof StringValue) {
							newVal = (Collection<SimpleObject>)tab.getField(col.getLabel()).getValue();
							Object[] array = newVal.toArray();
							for(int i = 0; i < array.length; i++) {
								StringValue n = new StringValue();
								n.setDescription(sim.getDescription());
								n.setLabel(sim.getLabel());
								n.setRequired(sim.isRequired());
								n.setValidator(sim.getValidator());
								n.setValue(array[i].toString());
								values.add(n);
							}
					
						} 
						else if(sim instanceof IntegerValue) {
								newVal = (Collection<SimpleObject>)tab.getField(col.getLabel()).getValue();
								Object[] array = newVal.toArray();
								for(int i = 0; i < array.length; i++) {
									IntegerValue n = new IntegerValue();
									n.setDescription(sim.getDescription());
									n.setLabel(sim.getLabel());
									n.setRequired(sim.isRequired());
									n.setValidator(sim.getValidator());
									if(isIntegerNum(array[i].toString()))
										n.setValue(Integer.parseInt(array[i].toString()));
									else
										n.setValue(n.getDefaultValue());
									values.add(n);
							}
					
						} 
						else if(sim instanceof DoubleValue) {
							newVal = (Collection<SimpleObject>)tab.getField(col.getLabel()).getValue();
							Object[] array = newVal.toArray();
							for(int i = 0; i < array.length; i++) {
								DoubleValue n = new DoubleValue();
								n.setDescription(sim.getDescription());
								n.setLabel(sim.getLabel());
								n.setRequired(sim.isRequired());
								n.setValidator(sim.getValidator());
								if(isDoubleNum(array[i].toString()))
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
				
				HashMap<String, ArrayList<Subprofile>> nOntInstances = new HashMap<String, ArrayList<Subprofile>>();
				for(Map.Entry<String, ArrayList<Subprofile>>tOnt : ontInstances.entrySet()) {
					
					if(tOnt.getKey().equals(selectedItem)) {
						nOntInstances.put(tOnt.getKey(), new ArrayList<Subprofile>());
						for(Subprofile sp : tOnt.getValue()) {
							if(sp.getName().equals(sub.getName())) {
								nOntInstances.get(tOnt.getKey()).add(sub);
							} else {
								nOntInstances.get(tOnt.getKey()).add(sp);
							}
						}
					} else {
						nOntInstances.put(tOnt.getKey(), tOnt.getValue());
					}
				}
			tab.setReadOnly(true);
			tab.getSaveButton().setVisible(false);
			tab.getEditButton().setVisible(true);
			tab.getDeleteButton().setVisible(true);
			dataAccess.updateUserData(selectedItem, nOntInstances);
			//Update user in xml
			UserAccountInfo uinfo = new UserAccountInfo();
			uinfo.setChecked(true);
			ArrayList<Role> roles = new ArrayList<Role>();
			
				
				ArrayList<Subprofile> list = nOntInstances.get(selectedItem);
				for(Subprofile s : list) {
					for(SimpleObject si : s.getSimpleObjects()) {
						StringValue sv = (StringValue)si;
						if(sv.getName().equals("username")) {
							uinfo.setName(sv.getValue());
						}
						if(sv.getName().equals("password")) {
							uinfo.setPassword(sv.getValue());
						}
					
					}
					for(EnumObject eo : s.getEnums()) {
						if(eo.getType().equals("userRole")) {
							roles.add(Role.valueOf(eo.getSelectedValue()));
							uinfo.setRole(roles);
						}
					}
				}
			setup.updateUser(uinfo);
			//Update admin user in setup.properties
			if(selectedItem.equals("admin")) {
				Properties prop = new Properties();
				Properties prop2 = new Properties();
				Reader reader = null;
				try {
					reader = new FileReader(Activator.getSetupProps());
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					prop.load(reader);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				for(Map.Entry entry : prop.entrySet()) {
					System.err.println(entry.getKey().toString());
					
					if(entry.getKey().toString().equals("pwd")) {
						prop2.setProperty(entry.getKey().toString(), tab.getField("Password:").getValue().toString());
					} else {
						prop2.setProperty(entry.getKey().toString(), entry.getValue().toString());
					}
				}
				Writer wr = null;
				try {
					wr = new FileWriter(Activator.getSetupProps());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					prop2.store(wr, "");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					wr.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					reader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} 
			
				
			
//			dataAccess.updateUserData(actualFlat, selectedItem, nOntInstances);
			app.getMainWindow().showNotification(tab.getHeader()+" was updated", Notification.POSITION_CENTERED);	
		
			
		} //Edit button was pushed
		else if(event.getButton() == ((TabForm)tabSheet.getSelectedTab()).getEditButton()) {
			TabForm tab = ((TabForm)tabSheet.getSelectedTab());
			tab.getSaveButton().setVisible(true);
			tab.getEditButton().setVisible(false);
			tab.getResetButton().setVisible(true);
			tab.getDeleteButton().setVisible(false);
			tab.setReadOnly(false);
//			if(tab.getField("Given name:") != null) {
//				tab.getField("Given name:").setReadOnly(true);
//			}
			if(tab.getField("Username:") != null) {
				tab.getField("Username:").setReadOnly(true);
			}
		} //Delete Button was pushed
		else if(event.getButton() == ((TabForm)tabSheet.getSelectedTab()).getDeleteButton()) {
			dataAccess.deleteUserDataInChe(selectedItem);
			List<UserAccountInfo> uinfo = setup.getUsers();
			for(UserAccountInfo ui : uinfo) {
				if(ui.getName().equals(selectedItem)) {
					setup.deleteUser(ui);
				}
			}
//			dataAccess.deleteUserData(actualFlat, selectedItem);
			win.getUserTree().removeListener(this);
			win.getUserTree().removeItem(selectedItem);
			win.getUserTree().addListener(this);
			tabSheet.removeAllComponents();
			app.getMainWindow().showNotification(selectedItem+" was deleted", Notification.POSITION_CENTERED);
		}
		
	}

	
	public void valueChange(ValueChangeEvent event) {
		Tree tree = ((Tree)event.getProperty());
		if(!tree.isRoot(tree.getValue())) {
			selectedItem = (String)tree.getValue();
			
					try {
						loadData();
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

			tabSheet.removeAllComponents();
			for(TabForm form : userForms.get(tree.getValue())) {
				Tab act = tabSheet.addTab(form, form.getHeader());
				act.setClosable(true);
			}

		} else {
			try {
				AddNewPersonWindow personWindow = new AddNewPersonWindow(win, null, app);
				personWindow.setPositionX(this.win.getPositionX()+100);
				personWindow.setPositionY(this.win.getPositionY()+100);
				app.getMainWindow().addWindow(personWindow);
			} catch (JAXBException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private boolean isDoubleNum(String s) {
		try {
		Double.parseDouble(s);
		}
		catch (NumberFormatException nfe) {
		return false;
		}
		return true;
	}
	
	private boolean isIntegerNum(String s) {
		try {
			Integer.parseInt(s);
			}
			catch (NumberFormatException nfe) {
			return false;
			}
			return true;
	}

}
