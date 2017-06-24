package org.universAAL.tools.ucc.configuration.model.validators;

import java.util.regex.Pattern;

import org.universAAL.tools.ucc.configuration.model.ConfigOptionRegistry;
import org.universAAL.tools.ucc.configuration.model.configurationinstances.Value;
import org.universAAL.tools.ucc.configuration.model.exceptions.ValidationException;
import org.universAAL.tools.ucc.configuration.model.interfaces.ConfigurationValidator;

/**
 *
 * This validator let you define a regular expression and cheks whether the
 * value fits this expression or not.
 *
 * @author Sebastian.Schoebinge
 *
 */

public class RegExpValidator implements ConfigurationValidator {

	String[] attributes;
	String regExp;

	public boolean isValid(ConfigOptionRegistry registry, Value value) {
		if (attributes == null || attributes.length < 1) {
			return false;
		}
		regExp = attributes[0];
		if (!Pattern.matches(regExp, value.getValue())) {
			return false;
		}
		return true;
	}

	public void validate(ConfigOptionRegistry registry, Value value) throws ValidationException {
		if (!isValid(registry, value)) {
			throw new ValidationException("Your entry needs to match this regular expression: " + regExp);
		}
	}

	public void setAttributes(String[] attributes) {
		this.attributes = attributes;
	}

}
