package org.universAAL.ucc.configuration.model.validators;

import org.universAAL.ucc.configuration.model.ConfigOptionRegistry;
import org.universAAL.ucc.configuration.model.configurationinstances.Value;
import org.universAAL.ucc.configuration.model.exceptions.ValidationException;
import org.universAAL.ucc.configuration.model.interfaces.ConfigurationValidator;

/**
 * 
 * This validator class checks whether the value is of type int or not.
 * 
 * @author Sebastian.Schoebinge
 *
 */

public class IntegerValidator implements ConfigurationValidator {
	
	String msg;
	
	public IntegerValidator(String msg){
		this.msg = msg;
	}

	
	public boolean isValid(ConfigOptionRegistry registry, Value value) {
		if(value == null || value.getValue() == null || value.getValue().equals("")){
			return true;
		}else{
			try {
				Integer.parseInt(value.getValue());
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}

	
	public void validate(ConfigOptionRegistry registry, Value value)
			throws ValidationException {
		if(!isValid(registry, value)){
			throw new ValidationException(msg);
		}
	}

	
	public void setAttributes(String[] attributes) {
		// TODO Auto-generated method stub

	}

}
