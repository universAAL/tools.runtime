package org.universAAL.tools.ucc.configuration.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.ListSelect;

import org.universAAL.tools.ucc.configuration.controller.VaadinConfigurationController;
import org.universAAL.tools.ucc.configuration.model.ConfigOptionRegistry;
import org.universAAL.tools.ucc.configuration.model.ConfigurationOption;
import org.universAAL.tools.ucc.configuration.model.MapConfigurationOption;
import org.universAAL.tools.ucc.configuration.model.configurationdefinition.Option;
import org.universAAL.tools.ucc.configuration.model.configurationinstances.ObjectFactory;
import org.universAAL.tools.ucc.configuration.model.configurationinstances.Value;
import org.universAAL.tools.ucc.configuration.model.exceptions.ValidationException;
import org.universAAL.tools.ucc.configuration.model.interfaces.OnConfigurationChangedListener;

public class MultiselectionList extends ListSelect implements OnConfigurationChangedListener {

	MapConfigurationOption configOption;
	VaadinConfigurationController controller;
	ObjectFactory factory;
	BeanItemContainer<Option> b;

	public MultiselectionList(VaadinConfigurationController controller, MapConfigurationOption mapOption,
			ConfigOptionRegistry registry) {
		super(mapOption.getLabel());
		b = new BeanItemContainer<Option>(Option.class);
		b.addAll(mapOption.getOptions());
		setContainerDataSource(b);

		this.controller = controller;
		factory = new ObjectFactory();

		configOption = mapOption;

		setSelection();

		configOption.addListener(this);

		addListener(new Property.ValueChangeListener() {

			public void valueChange(Property.ValueChangeEvent event) {

				setComponentError(null);

				try {

					if (isMultiSelect()) {
						// if(event.getProperty().getValue() != null) {
						Collection<Option> values = (Collection<Option>) event.getProperty().getValue();
						List<Value> configValues = new LinkedList<Value>();
						for (Option option : values) {
							Value v = factory.createValue();
							v.setKey(option.getKey());
							v.setValue(option.getValue());
							configValues.add(v);
						}
						configOption.setValue(configValues);

					} else {
						Value v = factory.createValue();
						Option p = (Option) event.getProperty().getValue();
						v.setKey(p.getKey());
						v.setValue(p.getValue());
						configOption.setValue(v);
					}
					// }
				} catch (ValidationException e) {
					// Set the error of the validators as error message of this
					// list select.
					setComponentError(new UserError(e.getMessage()));
				}
				// Check the dependencies of all other configuration options
				// after this configuration option was changed.
				MultiselectionList.this.controller.checkDependencies();
			}

		});

		setImmediate(true);
		setNullSelectionAllowed(true);
		setRequired(false);
		setMultiSelect(true);
	}

	/**
	 * If the is valid method of the list select is valid and the configuration
	 * option is valid return true else false.
	 */
	public boolean isValid() {
		if (!super.isValid()) {
			return false;
		}

		return configOption.isValid();
	}

	@Override
	public void detach() {
		super.detach();
		configOption.removeListener(this);
	}

	private void setSelection() {
		List<Option> selects = new ArrayList<Option>();
		for (Value value : configOption.getValues()) {
			for (Option option : (Collection<Option>) b.getItemIds()) {
				if (value.getValue().equals(option.getValue())) {
					selects.add(option);
				}
			}
		}
		if (selects.size() > 1) {
			setValue(selects);
		} else if (selects.size() == 1) {
			setValue(selects.get(0));
		}
	}

	public ConfigurationOption getConfigOption() {
		return configOption;
	}

	public void configurationChanged(ConfigOptionRegistry registry, ConfigurationOption option) {
		setEnabled(true);
		setRequired(false);
		setMultiSelect(true);

		setSelection();
	}

}
