package org.universAAL.tools.ucc.configuration.model.servicetracker;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.tools.ucc.configuration.model.Activator;
import org.universAAL.tools.ucc.configuration.model.ConfigurationOption;
import org.universAAL.tools.ucc.configuration.model.interfaces.OnConfigurationChangedListener;
import org.universAAL.tools.ucc.configuration.model.interfaces.OnConfigurationChangedListenerFactory;

public class ListenerServiceTracker extends ServiceTracker {

	BundleContext context;
	ConfigurationOption option;
	OnConfigurationChangedListener listener;

	public ListenerServiceTracker(BundleContext context, String name, ConfigurationOption option) {
		super(context, name, null);
		this.option = option;
		this.context = context;
		LogUtils.logInfo(Activator.getContext(), this.getClass(), "ListenerServiceTracker",
				new Object[] { "new ServiceTracker for name:" + name }, null);

	}

	@Override
	public Object addingService(ServiceReference reference) {
		LogUtils.logInfo(Activator.getContext(), this.getClass(), "addingService",
				new Object[] { "Service added: " + reference.getClass().toString() }, null);

		try {
			Object o = context.getService(reference);
			if (o instanceof OnConfigurationChangedListener) {
				listener = (OnConfigurationChangedListener) o;
				LogUtils.logInfo(Activator.getContext(), this.getClass(), "addingService",
						new Object[] { "loaded: " + listener.getClass() }, null);

				option.addListener(listener);
			} else if (o instanceof OnConfigurationChangedListenerFactory) {
				OnConfigurationChangedListenerFactory factory = (OnConfigurationChangedListenerFactory) o;
				listener = factory.create();
				LogUtils.logInfo(Activator.getContext(), this.getClass(), "addingService",
						new Object[] { "loaded: " + listener.getClass() }, null);

				option.addExternalListener(listener);
			}
		} catch (ClassCastException e) {
			LogUtils.logInfo(Activator.getContext(), this.getClass(), "addingService",
					new Object[] { "Listener cannot casted to OnConfigurationModelChangedListener!" }, null);
		}
		return super.addingService(reference);
	}

	@Override
	public void removedService(ServiceReference reference, Object service) {
		option.removeExternalListener(listener);
		super.removedService(reference, service);
	}

}
