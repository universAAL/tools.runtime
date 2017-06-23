package org.universAAL.tools.ucc.configuration.model;

import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.tools.ucc.configuration.model.configurationdefinition.Category;
import org.universAAL.tools.ucc.configuration.model.configurationdefinition.SimpleConfigItem;
import org.universAAL.tools.ucc.configuration.model.configurationinstances.ObjectFactory;
import org.universAAL.tools.ucc.configuration.model.configurationinstances.Value;
import org.universAAL.tools.ucc.configuration.model.exceptions.ValidationException;

/**
 * 
 * This class represents the the simple configuration item of xml configuration
 * definition.
 * 
 * @author Sebastian.Schoebinge
 *
 */

public class SimpleConfigurationOption extends ConfigurationOption {

	public SimpleConfigurationOption(SimpleConfigItem configItem, Category category,
			ConfigOptionRegistry configOptionRegestry) {
		super(configItem, category, configOptionRegestry);
	}

	public String getType() {
		return ((SimpleConfigItem) configItem).getType();
	}

	/**
	 * Set the default value from configuration definition.
	 */
	public void setDefaultValue() {
		Value v = new ObjectFactory().createValue();
		v.setValue(((SimpleConfigItem) configItem).getDefaultValue());
		if (v.getValue() != null) {
			try {
				setValue(v);
			} catch (ValidationException e) {
				LogUtils.logInfo(Activator.getContext(), this.getClass(), "setDefaultValue",
						new Object[] { "Value isn't valid!" }, null);

			}
		}
	}

}
