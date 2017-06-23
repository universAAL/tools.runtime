package org.universAAL.tools.ucc.configuration.model.interfaces;

import org.universAAL.tools.ucc.configuration.model.ConfigOptionRegistry;
import org.universAAL.tools.ucc.configuration.model.ConfigurationOption;

/**
 * The listener interfaces for configuration option changes
 * 
 * @author Sebastian.Schoebinge
 *
 */

public interface OnConfigurationChangedListener {

	/**
	 * 
	 * @param registry
	 *            is the ConfigurationOptionRegistry from which you can get all
	 *            configuration options.
	 * @param option
	 *            is the configuration option which was modified.
	 */
	public void configurationChanged(ConfigOptionRegistry registry, ConfigurationOption option);

}
