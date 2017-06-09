package org.universAAL.ucc.configuration.view;

import com.vaadin.terminal.UserError;
import com.vaadin.ui.PasswordField;

import org.universAAL.ucc.configuration.controller.VaadinConfigurationController;
import org.universAAL.ucc.configuration.model.ConfigOptionRegistry;
import org.universAAL.ucc.configuration.model.ConfigurationOption;
import org.universAAL.ucc.configuration.model.SimpleConfigItemTypes;
import org.universAAL.ucc.configuration.model.SimpleConfigurationOption;
import org.universAAL.ucc.configuration.model.configurationinstances.ObjectFactory;
import org.universAAL.ucc.configuration.model.configurationinstances.Value;
import org.universAAL.ucc.configuration.model.exceptions.ValidationException;
import org.universAAL.ucc.configuration.model.interfaces.OnConfigurationChangedListener;
import org.universAAL.ucc.configuration.model.validators.DoubleValidator;
import org.universAAL.ucc.configuration.model.validators.IntegerValidator;

public class SimpleConfiguratorPasswordField extends PasswordField implements OnConfigurationChangedListener {

	public VaadinConfigurationController controller;

	ObjectFactory factory;
	SimpleConfigurationOption configOption;

	public SimpleConfiguratorPasswordField(SimpleConfigurationOption option, VaadinConfigurationController controller) {
		super(option.getLabel());

		this.controller = controller;

		factory = new ObjectFactory();
		configOption = option;
		configOption.addListener(this);

		setDefaultValidators(configOption);

		setImmediate(true);
		addListener(new ValueChangeListener() {

			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
				try {
					Value v = factory.createValue();
					v.setValue((String) event.getProperty().getValue());
					configOption.setValue(v);
					setComponentError(null);
					SimpleConfiguratorPasswordField.this.controller.checkDependencies();

				} catch (ValidationException e) {
					// Set the error message of the validators as the error
					// message of the text field
					setComponentError(new UserError(e.getMessage()));
				}
			}

		});
	}

	/**
	 * Set default validators for double and integer.
	 * 
	 * @param option
	 */
	private void setDefaultValidators(SimpleConfigurationOption option) {
		if (option.getType().equals(SimpleConfigItemTypes.INTEGER)) {
			configOption.addValidator(IntegerValidator.class.getName(),
					new IntegerValidator("Please use only numbers"));
		} else if (option.getType().equals(SimpleConfigItemTypes.DOUBLE)) {
			configOption.addValidator(DoubleValidator.class.getName(),
					new DoubleValidator("Please use only floating point numbers"));
		}

		setRequired(configOption.isRequired());
	}

	public SimpleConfigurationOption getConfigOption() {
		return configOption;
	}

	/**
	 * return true if the TextField is valid and the configuration option.
	 */
	public boolean isValid() {
		if (!super.isValid()) {
			return false;
		}

		return configOption.isValid();

	}

	public String getID() {
		return configOption.getId();
	}

	@Override
	public void detach() {
		super.detach();
		configOption.removeListener(this);
	}

	public void configurationChanged(ConfigOptionRegistry registry, ConfigurationOption option) {
		setComponentError(null);
		try {
			option.doDeepValidation();
		} catch (ValidationException e) {
			setComponentError(new UserError(e.getMessage()));
		}
		setEnabled(configOption.isActive());
		setRequired(configOption.isRequired());
		setValue(configOption.getValue());
	}

}
