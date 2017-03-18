package org.universAAL.ucc.configuration.view;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.ucc.configuration.controller.VaadinConfigurationController;
import org.universAAL.ucc.configuration.internal.Activator;
import org.universAAL.ucc.configuration.model.ConfigurationOption;
import org.universAAL.ucc.configuration.model.MapConfigurationOption;
import org.universAAL.ucc.configuration.model.SimpleConfigurationOption;
import org.universAAL.ucc.configuration.model.configurationdefinition.Category;
import org.universAAL.ucc.configuration.model.configurationdefinition.Configuration;
import org.universAAL.ucc.configuration.model.configurationinstances.ConfigurationInstance;
import org.universAAL.ucc.configuration.model.configurationinstances.ObjectFactory;
import org.universAAL.ucc.configuration.model.interfaces.ModelRegistryChangedListener;
import org.universAAL.ucc.configuration.storage.interfaces.ConfigurationInstancesStorage;
import org.universAAL.ucc.configuration.storage.interfaces.StorageChangedListener;
//import de.fzi.ipe.jcc.db.DataAccess;
//import de.fzi.ipe.jcc.model.jaxb.EnumObject;
//import de.fzi.ipe.jcc.model.jaxb.OntologyInstance;
//import de.fzi.ipe.jcc.model.jaxb.SimpleObject;
//import de.fzi.ipe.jcc.model.jaxb.StringValue;
//import de.fzi.ipe.jcc.model.jaxb.Subprofile;

/**
 * 
 * This window shows an overview about the configuration.
 * E. g. the id of the configuration definition and the configuration instances. 
 * 
 * @author Sebastian Schoebinger
 *
 */

@SuppressWarnings("serial")
public class ConfigurationOverviewWindow extends Window implements ModelRegistryChangedListener, StorageChangedListener {
//	public static String usersFlat1;
//	public static String usersFlat2;
//	public static String usersFlat3;
//	public static String hwFlat1;
//	public static String hwFlat2;
//	public static String hwFlat3;
//	private String device;
//	private String actualUsers;
//	private String actualHW;

	
	public static String NAME = "ConfigurationOverview";
	
	VaadinConfigurationController controller;
	
//	String flatId;
	
	public Window configurationWindow;
	
	Label configurationTitle;
	Label bundleName;
	Label configurationAuthor;
	Label configurationVersion;
	Window twin;
//	ArrayList<OntologyInstance> ontInstances;
	
	ListSelect configInstances;
	
	Button edit;
	Button createNew;
	Button delete;
	
	VerticalLayout vLayout;
	HorizontalLayout buttonLayout;
	
	HashMap<String, Panel> panels;
	
	ConfigurationInstancesStorage storage;
//	DataAccess dataAccess;
	
	public ConfigurationOverviewWindow(/*String flatId,*/ Configuration config) {
		super("Configuration of "+config.getBundlename());

		setName(NAME);
//		device = System.getenv("systemdrive");
//		usersFlat1 = device+"/jcc_datastore/flat1/Users.xml";
//		usersFlat2 = device+"/jcc_datastore/flat2/Users.xml";
//		usersFlat3 = device+"/jcc_datastore/flat3/Users.xml";
//		hwFlat1 = device+"/jcc_datastore/flat1/Hardware.xml";
//		hwFlat2 = device+"/jcc_datastore/flat2/Hardware.xml";
//		hwFlat3 = device+"/jcc_datastore/flat3/Hardware.xml";
//		this.flatId = flatId;
//		if(flatId.equals("Flat1")) {
//			this.actualUsers = usersFlat1;
//			this.actualHW = hwFlat1;
//		}
//		else if(flatId.equals("Flat2")) {
//			this.actualUsers = usersFlat2;
//			this.actualHW = hwFlat2;
//		}
//		else if(flatId.equals("Flat3")) {
//			this.actualUsers = usersFlat3;
//			this.actualHW = hwFlat3;
//		}
		this.twin = this;
		setWidth("450px");
		setHeight("365px");
		BundleContext context = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
		ServiceReference reference = context.getServiceReference(ConfigurationInstancesStorage.class.getName());
		storage = (ConfigurationInstancesStorage) context.getService(reference);
		storage.addListener(this);
		//�nderung
//		ServiceReference<DataAccess>ref = context.getServiceReference(DataAccess.class);
//		dataAccess = context.getService(ref);
//		context.ungetService(ref);
		//Ende
		initializeControllerAndView(config);
	}
	
	private void initializeControllerAndView(Configuration config) {
		panels = new HashMap<String, Panel>();
		
		controller = new VaadinConfigurationController(this, config);
		controller.getModelRegistry().addListener(this);
		
		initializeView();
		
		buttonLayout.addComponent(edit);
		buttonLayout.addComponent(createNew);
		buttonLayout.addComponent(delete);
		
		vLayout.addComponent(configurationTitle);
		vLayout.addComponent(bundleName);
		vLayout.addComponent(configurationAuthor);
		vLayout.addComponent(configurationVersion);
		vLayout.addComponent(configInstances);
		vLayout.addComponent(buttonLayout);
		addComponent(vLayout);
	}
	
	private void reinitializeControllerAndView(Configuration config) {
		controller.getModelRegistry().removeListener(this);
		removeAllComponents();
		initializeControllerAndView(config);
	}

	private void initializeView() {
		
		configurationTitle = new Label("Configuration ID: " + controller.getConfigurator().getConfigDefinition().getId());
		bundleName = new Label("Usecase bundle name: " + controller.getConfigurator().getConfigDefinition().getBundlename());
		configurationAuthor = new Label("Author: " +  controller.getConfigurator().getConfigDefinition().getAuthor());
		configurationVersion = new Label("Version: " +  controller.getConfigurator().getConfigDefinition().getVersion());
		configInstances = new ListSelect("Configuration instances");
		fillSelect();
		configInstances.setNullSelectionAllowed(false);
		configInstances.setImmediate(true);
		edit = new Button("Edit");
		edit.addListener(new Button.ClickListener() {
			
			
			public void buttonClick(ClickEvent event) {
				//Set the selected configuration instance as configuration instance of the controller.
				controller.setConfigInstance((ConfigurationInstance)configInstances.getValue());
				controller.loadConfigurationItems();
				controller.initializeValues();
			}
		});
		createNew = new Button("Create new");
		createNew.addListener(new Button.ClickListener() {
			
			
			public void buttonClick(ClickEvent event) {
				controller.setConfigInstance(new ObjectFactory().createConfigurationInstance());
				controller.loadConfigurationItems();
			}
		});
		
		delete = new Button("Delete");
		delete.addListener(new Button.ClickListener() {
			
			
			public void buttonClick(ClickEvent event) {
				controller.setConfigInstance((ConfigurationInstance)configInstances.getValue());
				controller.deleteConfigurationInstance();
			}
		});
		
		vLayout = new VerticalLayout();
		buttonLayout = new HorizontalLayout();
	}
	
	/**
	 * Get all configuration instances for the defined bundle.
	 */
	private void fillSelect() {
		configInstances.removeAllItems();
		for(ConfigurationInstance instance : storage.getAllInstancesForBundle(controller.getConfigurator().getConfigDefinition().getBundlename())){
			configInstances.addItem(instance);
		}
	}

	public void setConfiguration(Configuration config){
		reinitializeControllerAndView(config);
	}
	
	public void openConfigurationWindow() {
		configurationWindow = new Window("Configuration window");
		configurationWindow.addListener(new CloseListener() {
			
			
			public void windowClose(CloseEvent e) {
				removeWindow(e.getWindow());
				e.getWindow().removeAllComponents();
				configurationWindow = null;
				controller.closeConfiguration();
				panels.clear();
			}
		});
//		configurationWindow.setSizeFull();
		configurationWindow.setWidth("450px");
		configurationWindow.setHeight("365px");
		configurationWindow.center();
//		addWindow(configurationWindow);
		getApplication().getMainWindow().addWindow(configurationWindow);
//		getApplication().getMainWindow().getWindow().setVisible(false);
	}
	
	public Panel createNewPanelForCategory(Category category) {
		ConfigurationPanel p = new ConfigurationPanel(category);
		configurationWindow.addComponent(p);
		panels.put(category.getId(), p);
		return p;
	}

	public void addSimpleConfigItemToPanel(Panel p, SimpleConfigurationOption option) {
		if(option.getId().toLowerCase().contains("password")) {
			SimpleConfiguratorPasswordField field = new SimpleConfiguratorPasswordField(option, controller);
			field.setEnabled(option.isActive());
			field.setDescription(option.getDescription());
			p.addComponent(field);
		} else {
			SimpleConfiguratorTextField field = new SimpleConfiguratorTextField(option, controller);
			field.setEnabled(option.isActive());
			field.setDescription(option.getDescription());
			p.addComponent(field);
		}
	}

	public void addMapConfigItemToPanel(Panel p, MapConfigurationOption mapOption) {
		ConfigurationListSelect list;
//		MultiselectionList multi;
//		if(mapOption != null) {
//			if(mapOption.getCategory().getId().equals("Hardware")) {
//				multi = new MultiselectionList(controller, mapOption, controller.getModelRegistry());
//				multi.addListener(this);
//				p.addComponent(multi);
//				
//			} 
//		else {
				list = new ConfigurationListSelect(controller, mapOption, controller.getModelRegistry());
//				if(mapOption.getCategory().getId().equals("Persons")) {
//					list.addListener(this);
//				}
				p.addComponent(list);
//			}
//		} 
	}

	public void addSaveButton() {
		Button save = new Button("Save");
		save.setClickShortcut(KeyCode.S, ModifierKey.CTRL);
		save.addListener(new Button.ClickListener() {
			
			public void buttonClick(ClickEvent event) {			
				
				Window configurationWindow = event.getButton().getWindow();
				
				//LoggerFactory.getLogger(ConfiguratorApplication.class).debug("SaveAction");
				List<ConfigurationOption> configOptions = new LinkedList<ConfigurationOption>();
				// Get all configuration options
				Iterator<Component> i = configurationWindow.getComponentIterator();
				while(i.hasNext()){
					Component component = i.next();
					if(component instanceof ConfigurationPanel){
						
						LogUtils.logInfo(Activator.getContext(), this.getClass(), "addSaveButton",
								new Object[] { "panel found: " + component.getCaption() }, null);
								
						configOptions.addAll(((ConfigurationPanel)component).getConfigOptions());
					}
				}
				
				//validate the configuration options
				i = configurationWindow.getComponentIterator();
				while(i.hasNext()){
					Component component = i.next();
					if(component instanceof ConfigurationPanel){
						if(!((ConfigurationPanel)component).isValid()){
							configurationWindow.showNotification("Couldn't save because of validation or required errors!", Window.Notification.TYPE_WARNING_MESSAGE);
							return;
						}
					}
				}
				
				Window configSaveWindow = new ConfigurationSaveWindow(controller, configOptions);
//				addWindow(configSaveWindow);	
				getApplication().getMainWindow().addWindow(configSaveWindow);
				getApplication().getMainWindow().removeWindow(configurationWindow);
				getApplication().getMainWindow().removeWindow(twin);
			}
		});
		configurationWindow.addComponent(save);
	}
	
	public void createView() {
		if(configurationWindow == null){
			
			LogUtils.logInfo(Activator.getContext(), this.getClass(), "createView",
					new Object[] { "create new configuration window." }, null);
					
			openConfigurationWindow();
		}
		clearConfigurationWindow();
		createViewFromModel();
		addSaveButton();
	}

	private void clearConfigurationWindow() {
		
		LogUtils.logInfo(Activator.getContext(), this.getClass(), "clearConfigurationWindow",
				new Object[] { "remove all components from configuration window." }, null);
				
		configurationWindow.removeAllComponents();
		
		LogUtils.logInfo(Activator.getContext(), this.getClass(), "clearConfigurationWindow",
				new Object[] { "clear the panel cache." }, null);
				
		panels.clear();
	}

	private void createViewFromModel() {
		
		LogUtils.logInfo(Activator.getContext(), this.getClass(), "createViewFromModel",
				new Object[] { "fill the configuration window." }, null);
				
		//Nicoles �nderungen f�r Infoframe
//		if(controller.getConfigurator().getConfigDefinition().getBundlename().toLowerCase().contains("infoframe")) {
//				ontInstances = dataAccess.getFormFields(actualUsers);
//				Category cat = new Category();
//				cat.setDescription("Personel parameters");
//				cat.setId("Persons");
//				cat.setLabel("Persons");
//				ConfigurationPanel cp = new ConfigurationPanel(cat);
//				configurationWindow.addComponent(cp);
//				panels.put(cat.getId(), cp);
//				MapConfigItem item = new MapConfigItem();
//				item.setOptions(new Options());
//				item.setActive(true);
//				item.setDescription("Involved Persons of the Use Case");
//				item.setLabel("Persons to select");
//				item.setId("persons");
//				int key = 0;
//				for(OntologyInstance o : ontInstances) {
//					key++;
//					if(o.getId() != null && !o.getId().equals("")) {
//						Option option = new Option();
//						String role = "";
//						for(Subprofile sub : o.getSubprofiles()) {
//							if(sub.getName().equals("User Identification")) {
//								for(EnumObject en : sub.getEnums()) {
//									if(en.getType().equals("userRole")) {
//										role = en.getSelectedValue();
//									}
//								}
//							}
//						}
//						option.setValue(o.getId()+" / "+role);
//						option.setKey(key);
//						item.getOptions().getOption().add(option);
//					}
//				}
//		
//				MapConfigurationOption map = new MapConfigurationOption(item, cat, controller.getModelRegistry());
//				addMapConfigItemToPanel(cp, map);
//				for(Iterator<Component> iter = cp.getComponentIterator(); iter.hasNext();) {
//					Component c = iter.next();
//					if(c instanceof ConfigurationListSelect) {
//						ConfigurationListSelect select = (ConfigurationListSelect)c;
//						select.addListener(this);
//					}
//				}
//		}
		//Nicoles �nderungen f�r Housestatus
//		if(controller.getConfigurator().getConfigDefinition().getBundlename().toLowerCase().contains("housestatus")
//				||controller.getConfigurator().getConfigDefinition().getBundlename().toLowerCase().contains("plantcare")) {
//			ontInstances = dataAccess.getFormFields(actualHW);
//			Category cat = new Category();
//			cat.setDescription("Hardware parameters");
//			cat.setId("Hardware");
//			cat.setLabel("Hardware");
//			ConfigurationPanel cp = new ConfigurationPanel(cat);
//			configurationWindow.addComponent(cp);
//			panels.put(cat.getId(), cp);
//			MapConfigItem item = new MapConfigItem();
//			item.setOptions(new Options());
//			item.setActive(true);
//			item.setDescription("Involved Hardware of the Use Case");
//			item.setLabel("Hardware to select");
//			item.setId("hardware");
//			int key = 0;
//			for(OntologyInstance o : ontInstances) {
//				key++;
//				if(o.getId() != null && !o.getId().equals("")) {
//					Option opt = new Option();
//					String room = "";
//					String hwType = "";
//					for(Subprofile sub : o.getSubprofiles()) {
//						if(sub.getName().equals("Control-Information")) {
//							for(EnumObject en : sub.getEnums()) {
//								if(en.getType().equals("rooms")) {
//									room = en.getSelectedValue();
//								}
//							}
//							for(EnumObject en : sub.getEnums()) {
//								if(en.getType().equals("type")) {
//									hwType = en.getSelectedValue();
//								}
//							}
//						}
//					}
//					opt.setValue(o.getId() +" / " + hwType+ " / " +room);
//					opt.setKey(key);
//					item.getOptions().getOption().add(opt);
//				}
//			}
//			MapConfigurationOption map = new MapConfigurationOption(item, cat, controller.getModelRegistry());
////			addMapConfigItemToPanel(cp, map);
//			
//	}
				
		
		for(ConfigurationOption option: controller.getModelRegistry().getAll()){
			Panel p = getPanelForCategory(option.getCategory());
			if(option instanceof SimpleConfigurationOption){
				addSimpleConfigItemToPanel(p, ((SimpleConfigurationOption)option));
			}else if(option instanceof MapConfigurationOption){
				addMapConfigItemToPanel(p, ((MapConfigurationOption) option));
			}
		
		} 
		
		
	}

	private Panel getPanelForCategory(Category category) {
		Panel p;
		if(panels.containsKey(category.getId())){
			p = panels.get(category.getId());
		}else{
			p = createNewPanelForCategory(category);
		}
		return p;
	}


	public void modelRegistryChanged() {
		if(!controller.getModelRegistry().isEmpty()){
			
			LogUtils.logInfo(Activator.getContext(), this.getClass(), "modelRegistryChanged",
					new Object[] { "model registry changed, recreate view!" }, null);
					
			createView();
		}
	}

	
	public void storageChanged() {
		fillSelect();
	}

//	public String getFlatId() {
//		return flatId;
//	}
//
//	public void setFlatId(String flatId) {
//		this.flatId = flatId;
//	}
	
//	String location = "";
//	String mailserver = "";
//	String mailprotocol = "";
//	String username = "";
//	String password = "";
//	String room = "";
//	String sensor = "";
//	String deviceAdress = "";
//	int index = 0;
//	ArrayList<String> ads;
//	ArrayList<String> locs;


//	public void valueChange(ValueChangeEvent event) {
//		Iterator<Component> i = configurationWindow.getComponentIterator();
//		while(i.hasNext()){
//			Component component = i.next();
//			if(component instanceof ConfigurationPanel){
//				ConfigurationPanel ap = (ConfigurationPanel)component;
//				
//				for(ConfigurationOption op : ap.getConfigOptions()) {
//					if(op.getId().equals("persons")) {
//						for(OntologyInstance ont : ontInstances) {
//							if(op.getValue().contains(ont.getId())) {
//								for(Subprofile sub : ont.getSubprofiles()) {
//									if(sub.getName().equals("Contact Information")) {
//										for(SimpleObject simple : sub.getSimpleObjects()) {
//											if(simple instanceof StringValue) {
//												StringValue val = (StringValue)simple;
//												if(simple.getName().equals("city")) {
//													location = val.getValue();
//												}
//											}
//										}
//									}
//									
//									else if(sub.getName().equals("Email-Account Information")) {
//										for(SimpleObject simple : sub.getSimpleObjects()) {
//											if(simple instanceof StringValue) {
//												StringValue val = (StringValue)simple;
//												if(simple.getName().equals("emailServer")) {
//													mailserver = val.getValue();
//												} 
////												else if(simple.getName().equals("emailProtocol")) {
////													mailprotocol = val.getValue();
////												}
//												else if(simple.getName().equals("userName")) {
//													username = val.getValue();
//												} 
//												else if(simple.getName().equals("emailPassword")) {
//													password = val.getValue();
//												}
//											}
//										}
//										for(EnumObject en : sub.getEnums()) {
//											if(en.getType().equals("emailProtocol")) {
//												mailprotocol = en.getSelectedValue();
//											}
//										}
//									}
//								}
//							}
//						}
//					}
//					if(op.getId().equals("WeatherStationLocation") && !location.equals("")) {
//						Value value = new ObjectFactory().createValue();
//						if(location.toLowerCase().equals("karlsruhe")) {
//							value.setValue("zmw:00000.1.10702");
//						} else {
//							value.setValue(location);
//						}
//							try {
//								op.setValue(value);
//							} catch (ValidationException e) {
//								e.printStackTrace();
//							}
//					}
//					if(op.getId().contains("Server") && !mailserver.equals("")) {
//						Value value = new ObjectFactory().createValue();
//						value.setValue(mailserver);
//						try {
//							op.setValue(value);
//						} catch (ValidationException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//					if(op.getId().contains("Protocol") && !mailprotocol.equals("")) {
//						Value value = new ObjectFactory().createValue();
//						value.setValue(mailprotocol);
//						try {
//							op.setValue(value);
//						} catch (ValidationException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//					if(op.getId().contains("Username") && !username.equals("")) {
//						Value value = new ObjectFactory().createValue();
//						value.setValue(username);
//						try {
//							op.setValue(value);
//						} catch (ValidationException e) {
//							e.printStackTrace();
//						}
//					}
//					if(op.getId().contains("Password") && !password.equals("")) {
//						Value value = new ObjectFactory().createValue();
//						value.setValue(password);
//						try {
//							op.setValue(value);
//						} catch (ValidationException e) {
//							e.printStackTrace();
//						}
//						
//					}
//					
//					if(op.getId().toLowerCase().contains("hardware")) {
//						ads = new ArrayList<String>();
//						locs = new ArrayList<String>();
//						for(Value val : op.getValues()) {
//							deviceAdress = val.getValue().substring(0, val.getValue().indexOf("/")).trim();
//							ads.add(deviceAdress);
//							sensor = val.getValue().substring(val.getValue().indexOf("/")+ 1, val.getValue().lastIndexOf("/")).trim();
//							room = val.getValue().substring(val.getValue().lastIndexOf("/")+1).trim();
//							locs.add(room);
//						}
//						
//						
//					}
//					
//				}
//			}
//		}
//		int curr = 0;
//		for(Iterator<Component> it = configurationWindow.getComponentIterator(); it.hasNext(); curr++) {
//			Component com = it.next();
//			if(com instanceof ConfigurationPanel) {
//				ConfigurationPanel pan = (ConfigurationPanel)com;
//			for(ConfigurationOption co : pan.getConfigOptions()) {
//				if(co.getId().contains("ID")) {
//					Value value = new ObjectFactory().createValue();
//					if(ads != null && ads.size()> 0 && curr <= ads.size()) {
//						value.setValue(ads.get(curr-1));
//					try {
//						co.setValue(value);
//					} catch (ValidationException e) {
//						e.printStackTrace();
//					}
//					}
//				}
//				if(co.getId().contains("Location")) {
//					Value value = new ObjectFactory().createValue();
//					if(locs != null && locs.size()> 0 && curr <= locs.size()) {
//						value.setValue(locs.get(curr-1));
//					try {
//						co.setValue(value);
//					} catch (ValidationException e) {
//						e.printStackTrace();
//					}
//					}
//				}
//			} 
//		} 
//	}
//	
//	
//}
	
}
