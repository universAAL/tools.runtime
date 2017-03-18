package org.universAAL.ucc.configuration.model.validators;


import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.ucc.configuration.model.Activator;
import org.universAAL.ucc.configuration.model.ConfigOptionRegistry;
import org.universAAL.ucc.configuration.model.configurationinstances.Value;
import org.universAAL.ucc.configuration.model.exceptions.ValidationException;
import org.universAAL.ucc.configuration.model.interfaces.ConfigurationValidator;

public class IntMinMaxValidator implements ConfigurationValidator {
	
	int min, max;
	
	
	public boolean isValid(ConfigOptionRegistry registry, Value value) {
		if("".equals(value.getValue())){
			return true;
		}
		try{
			int intValue = Integer.parseInt(value.getValue());
			return intValue <= max && intValue >= min;
		}catch (NumberFormatException e){
			LogUtils.logError(Activator.getContext(), this.getClass(), "isValid",
					new Object[] { e.toString() }, null);

		}
		return false;
	}

	
	public void validate(ConfigOptionRegistry registry, Value value) throws ValidationException {
		if(!isValid(registry, value))
		{
			try{
				int intValue = Integer.parseInt(value.getValue());
				if(intValue > max){
					throw new ValidationException("Value is too large! It needs to be less than or equal to " + max);
				}else if(intValue < min){
					throw new ValidationException("Value is too small! It needs to be greater than or equal to " + min);
				}
			}catch (NumberFormatException e){
				LogUtils.logError(Activator.getContext(), this.getClass(), "validate",
						new Object[] { e.toString() }, null);

			}
		}
	}

	
	public void setAttributes(String[] attributes) {
		if(attributes.length > 1){
			try {
				min = Integer.parseInt(attributes[0]);
				max = Integer.parseInt(attributes[1]);
			} catch (NumberFormatException e) {
				LogUtils.logError(Activator.getContext(), this.getClass(), "setAttributes",
						new Object[] { e.toString() }, null);

			}
		}
	}

}
