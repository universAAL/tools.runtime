package org.universAAL.tools.ucc.configuration.model.validators;

import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.tools.ucc.configuration.model.Activator;
import org.universAAL.tools.ucc.configuration.model.ConfigOptionRegistry;
import org.universAAL.tools.ucc.configuration.model.configurationinstances.Value;
import org.universAAL.tools.ucc.configuration.model.exceptions.ValidationException;
import org.universAAL.tools.ucc.configuration.model.interfaces.ConfigurationValidator;

public class DoubleMinMaxValidator implements ConfigurationValidator {

	double min, max;

	public boolean isValid(ConfigOptionRegistry registry, Value value) {
		try {
			double doubleValue = Double.parseDouble(value.getValue());
			return doubleValue <= max && doubleValue >= min;
		} catch (NumberFormatException e) {
			LogUtils.logError(Activator.getContext(), this.getClass(), "isValid", new Object[] { e.toString() }, null);

		}
		return false;
	}

	public void validate(ConfigOptionRegistry registry, Value value) throws ValidationException {
		if (!isValid(registry, value)) {
			try {
				double doubleValue = Double.parseDouble(value.getValue());
				if (doubleValue > max) {
					throw new ValidationException("Value is too large! It needs to be less than or equal to " + max);
				} else if (doubleValue < min) {
					throw new ValidationException("Value is too small! It needs to be greater than or equal to " + min);
				}
			} catch (NumberFormatException e) {
				LogUtils.logError(Activator.getContext(), this.getClass(), "validate", new Object[] { e.toString() },
						null);

			}
		}
	}

	public void setAttributes(String[] attributes) {
		if (attributes.length > 1) {
			try {
				min = Double.parseDouble(attributes[0]);
				max = Double.parseDouble(attributes[1]);
			} catch (NumberFormatException e) {
				LogUtils.logError(Activator.getContext(), this.getClass(), "setAttributes",
						new Object[] { e.toString() }, null);

			}
		}
	}

}
