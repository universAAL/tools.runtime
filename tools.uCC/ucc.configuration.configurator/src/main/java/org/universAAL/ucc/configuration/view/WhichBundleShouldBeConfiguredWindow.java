package org.universAAL.ucc.configuration.view;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.universAAL.ucc.configuration.configdefinitionregistry.interfaces.ConfigurationDefinitionRegistry;
import org.universAAL.ucc.configuration.configdefinitionregistry.interfaces.ConfigurationDefinitionRegistryChanged;
import org.universAAL.ucc.configuration.model.configurationdefinition.Configuration;

import com.vaadin.ui.Button;
import com.vaadin.ui.Select;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

/**
 * 
 * On this window the user selects which bundle he wants to configure.
 * 
 * @author Sebastian Schoebinger
 *
 */

@SuppressWarnings("serial")
public class WhichBundleShouldBeConfiguredWindow extends Window implements ConfigurationDefinitionRegistryChanged {
	
	Select bundles;

	BundleContext context;
	
//	String flatId;
	
	WhichBundleShouldBeConfiguredWindow twin;
	
	public WhichBundleShouldBeConfiguredWindow(String usecase) {
		super("Configure "+usecase);
		setWidth("450px");
		setHeight("365px");
		center();
//		this.flatId = flatId;
		this.twin = this;
		context = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
		ServiceReference reference = context.getServiceReference(ConfigurationDefinitionRegistry.class.getName());
		ConfigurationDefinitionRegistry configReg = (ConfigurationDefinitionRegistry) context.getService(reference);
		configReg.addConfigurationDefinitionRegistryChanged(this);
		context.ungetService(reference);
		
		bundles = new Select("Configuration definitions");
		bundles.setNullSelectionAllowed(false);
		bundles.setDescription("Please select the bundle which you want to configure.");
		Button configure = new Button("Configure");
		configure.addListener(new Button.ClickListener() {
			
			
			public void buttonClick(ClickEvent event) {
				//Start the configuration overview window with the selected bundle.
				Configuration config = (Configuration)bundles.getValue();
				if(config != null){
					ConfigurationOverviewWindow configWindow = (ConfigurationOverviewWindow)WhichBundleShouldBeConfiguredWindow.this.getApplication().getWindow(ConfigurationOverviewWindow.NAME); 
					if(configWindow == null){
						configWindow = new ConfigurationOverviewWindow(config);
					}else{
						configWindow.setConfiguration(config);
					}
					//WhichBundleShouldBeConfiguredWindow.this.getApplication().addWindow(configWindow);
					getApplication().getMainWindow().addWindow(configWindow);
					getApplication().getMainWindow().removeWindow(twin);
//					open(new ExternalResource(configWindow.getURL()));
				}else{
					WhichBundleShouldBeConfiguredWindow.this.showNotification("Please select a bundle!");
				}
			}
		});
		
		fillSelect();
		
		addComponent(bundles);
		addComponent(configure);
		
	}

	private void fillSelect() {
		
		ServiceReference reference = context.getServiceReference(ConfigurationDefinitionRegistry.class.getName());
		ConfigurationDefinitionRegistry configReg = (ConfigurationDefinitionRegistry) context.getService(reference);
		
		for(Configuration config : configReg.getAllConfigDefinitions()){
			bundles.addItem(config);
		}
		
		context.ungetService(reference);
	}

	
	public void configurationDefinitionRegistryChanged() {
		bundles.removeAllItems();
		fillSelect();
	}
	
}
