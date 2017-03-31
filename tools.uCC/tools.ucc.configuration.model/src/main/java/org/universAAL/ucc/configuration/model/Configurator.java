package org.universAAL.ucc.configuration.model;


import org.universAAL.ucc.configuration.model.configurationdefinition.Configuration;
import org.universAAL.ucc.configuration.model.configurationinstances.ConfigurationInstance;
import org.universAAL.ucc.configuration.model.configurationinstances.ObjectFactory;

/**
 * 
 * This class holds both, the configuration definition and the configuration instance.
 * 
 * @author Sebastian.Schoebinge
 *
 */

public class Configurator {
	private Configuration configDefinition;
	private ConfigurationInstance configInstance;
	
	public Configuration getConfigDefinition() {
		return configDefinition;
	}
	
	public ConfigurationInstance getConfigInstance() {
		return configInstance;
	}

	public Configurator(Configuration config) {
		configDefinition = config;
		configInstance = new ObjectFactory().createConfigurationInstance();
	}

	public void setConfigurationInstance(ConfigurationInstance config) {
		if(config != null){
			configInstance = config;
		}
	}

}
