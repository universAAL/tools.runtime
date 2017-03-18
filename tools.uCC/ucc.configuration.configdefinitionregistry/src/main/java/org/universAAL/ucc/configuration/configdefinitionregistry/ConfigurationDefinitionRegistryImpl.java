package org.universAAL.ucc.configuration.configdefinitionregistry;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.JAXB;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.ucc.configuration.configdefinitionregistry.interfaces.ConfigurationDefinitionRegistry;
import org.universAAL.ucc.configuration.configdefinitionregistry.interfaces.ConfigurationDefinitionRegistryChanged;
import org.universAAL.ucc.configuration.configdefinitionregistry.internal.Activator;
import org.universAAL.ucc.configuration.model.configurationdefinition.Configuration;

/**
 * The implementation of the configuration definition registry interface.
 * @author Sebastian.Schoebinge
 *
 */

public class ConfigurationDefinitionRegistryImpl implements
		ConfigurationDefinitionRegistry {

	//Logger logger = LoggerFactory.getLogger(ConfigurationDefinitionRegistryImpl.class);
	
	HashMap<String, Configuration> configDefs;
	
	LinkedList<ConfigurationDefinitionRegistryChanged> listeners;
	
	public ConfigurationDefinitionRegistryImpl() {
		configDefs = new HashMap<String, Configuration>();
		listeners = new LinkedList<ConfigurationDefinitionRegistryChanged>();
	}
	
	public void registerConfigurationDefinition(URL configURL) {
		
		LogUtils.logInfo(Activator.getContext(), this.getClass(), "registerConfigurationDefinition",
				new Object[] { "register file: " + configURL }, null);
		
		try{
			Configuration config = JAXB.unmarshal(configURL, Configuration.class);
			configDefs.put(config.getBundlename(), config);
			updateListeners();
		}catch(Exception e){
			LogUtils.logError((ModuleContext) Activator.getContext(), this.getClass(), "registerConfigurationDefinition",
					new Object[] { "Failed to register configuration definition: " + e.getMessage() }, null);
		}
	}

	public List<Configuration> getAllConfigDefinitions() {
		return new LinkedList<Configuration>(configDefs.values());
	}

	public void unregisterConfigurationDefinition(URL configURL) {
		
		LogUtils.logInfo(Activator.getContext(), this.getClass(), "unregisterConfigurationDefinition",
				new Object[] { "unregister file: " + configURL }, null);

		try{
			Configuration config = JAXB.unmarshal(configURL, Configuration.class);
			configDefs.remove(config.getBundlename());
			updateListeners();
		}catch(Exception e){
			LogUtils.logError((ModuleContext) Activator.getContext(), this.getClass(), "unregisterConfigurationDefinition",
					new Object[] { "Failed to unregister configuration definition: " + e.getMessage() }, null);
		}
	}

	public void addConfigurationDefinitionRegistryChanged(
			ConfigurationDefinitionRegistryChanged listener) {
		if(listener != null){
			listeners.add(listener);
		}
	}

	public void removeConfigurationDefinitionRegistryChanged(
			ConfigurationDefinitionRegistryChanged listener) {
		listeners.remove(listener);
	}
	
	private void updateListeners(){
		for(ConfigurationDefinitionRegistryChanged listener : listeners){
			listener.configurationDefinitionRegistryChanged();
		}
	}

}
