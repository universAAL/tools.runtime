package org.universAAL.ucc.configuration.model.servicetracker;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.ucc.configuration.model.Activator;
import org.universAAL.ucc.configuration.model.ConfigurationOption;
import org.universAAL.ucc.configuration.model.interfaces.ConfigurationValidator;
import org.universAAL.ucc.configuration.model.interfaces.ConfigurationValidatorFactory;

public class ValidationServiceTracker extends ServiceTracker {

	BundleContext context;
	ConfigurationOption option;
	ConfigurationValidator validator;
	String[] attributes;

	public ValidationServiceTracker(BundleContext context, String name, ConfigurationOption option,
			String[] attributes) {
		super(context, name, null);
		this.option = option;
		this.context = context;
		this.attributes = attributes;
		LogUtils.logInfo(Activator.getContext(), this.getClass(), "ValidationServiceTracker",
				new Object[] { "new validation service tracker created!" }, null);

	}

	@Override
	public Object addingService(ServiceReference reference) {
		LogUtils.logInfo(Activator.getContext(), this.getClass(), "addingService",
				new Object[] { "Service added: " + reference.getClass().toString() }, null);

		try {
			Object o = context.getService(reference);
			if (o instanceof ConfigurationValidator) {
				validator = (ConfigurationValidator) o;
				validator.setAttributes(attributes);
				LogUtils.logInfo(Activator.getContext(), this.getClass(), "addingService",
						new Object[] { "loaded: " + validator.getClass() }, null);

				option.addValidator(validator.getClass().getName(), validator);
			} else if (o instanceof ConfigurationValidatorFactory) {
				ConfigurationValidatorFactory factory = (ConfigurationValidatorFactory) o;
				validator = factory.create();
				validator.setAttributes(attributes);
				LogUtils.logInfo(Activator.getContext(), this.getClass(), "addingService",
						new Object[] { "loaded: " + validator.getClass() }, null);

				option.addValidator(validator.getClass().getName(), validator);
			}
		} catch (ClassCastException e) {
			LogUtils.logError(Activator.getContext(), this.getClass(), "addingService",
					new Object[] { "Listener cannot casted to ConfigurationValidator!" }, null);

		}
		return super.addingService(reference);
	}

	@Override
	public void removedService(ServiceReference reference, Object service) {
		option.removeValidator(validator);
		super.removedService(reference, service);
	}

}
