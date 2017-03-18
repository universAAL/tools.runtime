package org.universAAL.ucc.configuration.model.interfaces;

import org.universAAL.ucc.configuration.model.ConfigOptionRegistry;
import org.universAAL.ucc.configuration.model.configurationinstances.Value;
import org.universAAL.ucc.configuration.model.exceptions.ValidationException;

/**
 * 
 * The interface for validators how want to validate configuration options
 * 
 * @author Sebastian.Schoebinge
 *
 */

public interface ConfigurationValidator {
	
	/**
	 * Checks if the value is valid.
	 * @param value
	 * @return true if the value is valid else false.
	 */
	public boolean isValid(ConfigOptionRegistry registry, Value value);
	
	/**
	 * Does the same as the isValid method but is able to thow an exception with an error message.
	 * @param value
	 * @throws ValidationException
	 */
	public void validate(ConfigOptionRegistry registry, Value value) throws ValidationException;
	
	/**
	 * This method is used to set the attributes which where defined in the configuration definition.
	 * @param attributes
	 */
	public void setAttributes(String[] attributes);
	
}
