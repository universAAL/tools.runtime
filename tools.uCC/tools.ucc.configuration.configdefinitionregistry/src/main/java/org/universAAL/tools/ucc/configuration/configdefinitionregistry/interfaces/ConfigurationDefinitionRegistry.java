package org.universAAL.tools.ucc.configuration.configdefinitionregistry.interfaces;

import java.net.URL;
import java.util.List;

import org.universAAL.tools.ucc.configuration.model.configurationdefinition.Configuration;

/**
 *
 * The implementations of the configuration definition interface could be used
 * to register configuration definitions so the bundles could be configured with
 * the configurator.
 *
 * @author Sebastian.Schoebinge
 *
 */

public interface ConfigurationDefinitionRegistry {

	/**
	 * Register the configuration definition for the defined bundle.
	 *
	 * @param configFile
	 */
	public void registerConfigurationDefinition(URL configURL);

	/**
	 * Unregister the configuration definition.
	 *
	 * @param configFile
	 */
	public void unregisterConfigurationDefinition(URL configURL);

	/**
	 *
	 * @return all registered configuration definitions.
	 */
	public List<Configuration> getAllConfigDefinitions();

	public void addConfigurationDefinitionRegistryChanged(ConfigurationDefinitionRegistryChanged listener);

	public void removeConfigurationDefinitionRegistryChanged(ConfigurationDefinitionRegistryChanged listener);

}
