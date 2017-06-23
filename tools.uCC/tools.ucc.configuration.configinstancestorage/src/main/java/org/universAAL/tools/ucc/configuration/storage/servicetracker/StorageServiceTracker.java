package org.universAAL.tools.ucc.configuration.storage.servicetracker;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.tools.ucc.configuration.api.ConfigPreferences;
import org.universAAL.tools.ucc.configuration.storage.interfaces.ConfigurationInstancesStorage;
import org.universAAL.tools.ucc.configuration.storage.internal.Activator;

public class StorageServiceTracker extends ServiceTracker {

	private ConfigPreferences pref;

	public StorageServiceTracker(BundleContext context, Class<ConfigurationInstancesStorage> clazz,
			ServiceTrackerCustomizer customizer, ConfigPreferences pref) {
		super(context, clazz.getName(), customizer);

		LogUtils.logInfo(Activator.getContext(), this.getClass(), "SotrageServiceTracker",
				new Object[] { "new storage service tracker created!" }, null);

		this.pref = pref;
	}

	@Override
	public Object addingService(ServiceReference reference) {
		LogUtils.logInfo(Activator.getContext(), this.getClass(), "addingService",
				new Object[] { "Service added: " + reference.getClass().toString() }, null);

		try {
			Object o = context.getService(reference);
			if (o instanceof ConfigurationInstancesStorage) {
				pref.setStorage((ConfigurationInstancesStorage) o);
			}
		} catch (ClassCastException e) {
			LogUtils.logError(Activator.getContext(), this.getClass(), "addingService",
					new Object[] { "Service cannot casted to ConfigurationInstancesStorage!" }, null);
		}
		return super.addingService(reference);
	}

}
