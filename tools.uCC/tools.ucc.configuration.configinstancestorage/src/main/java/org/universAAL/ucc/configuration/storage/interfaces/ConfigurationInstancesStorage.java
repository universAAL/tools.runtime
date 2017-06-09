package org.universAAL.ucc.configuration.storage.interfaces;

import java.util.List;

import org.osgi.framework.Bundle;

import org.universAAL.ucc.configuration.model.configurationinstances.ConfigurationInstance;
import org.universAAL.ucc.configuration.storage.exceptions.NoConfigurationFoundException;

/**
 * 
 * The configuration instance storage interface.
 * 
 * @author Sebastian.Schoebinge
 *
 */

public interface ConfigurationInstancesStorage {

	/**
	 * Loads the configuration instances from physical storage.
	 */
	void loadInstances();

	/**
	 * Adds the configuration instance and saves it to the physical storage.
	 * 
	 * @param instance
	 */
	void addConfigurationInstance(ConfigurationInstance instance);

	/**
	 * Replace the instance with the same id.
	 * 
	 * @param instance
	 */
	void replaceConfigurationInstance(ConfigurationInstance instance);

	/**
	 * Removes the given instance also from the physical storage
	 * 
	 * @param instance
	 * @return if the deletion was successful or net.
	 */
	boolean removeConfigurationInstance(ConfigurationInstance instance);

	/**
	 * 
	 * @param bundlename
	 * @return all instances for the given bundle.
	 */
	List<ConfigurationInstance> getAllInstancesForBundle(String bundlename);

	boolean contains(ConfigurationInstance configInstance);

	/**
	 * 
	 * @param bundle
	 * @return a configuration instance for the given bundle.
	 * @throws NoConfigurationFoundException
	 */
	ConfigurationInstance getConfigurationForBundle(Bundle bundle) throws NoConfigurationFoundException;

	void addListener(StorageChangedListener listener);

	void removeListener(StorageChangedListener listener);

}
