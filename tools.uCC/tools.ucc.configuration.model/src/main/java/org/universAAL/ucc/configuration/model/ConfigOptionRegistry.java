package org.universAAL.ucc.configuration.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.ucc.configuration.model.interfaces.ModelRegistryChangedListener;

/**
 * 
 * The registry class to manage the configuration options. Every configuration
 * option adds itself to this registry. You can ask the registry for
 * configuration options by its id's.
 * 
 * @author Sebastian.Schoebinge
 *
 */

public class ConfigOptionRegistry {

	HashMap<String, ConfigurationOption> mRegistry;
	LinkedList<ModelRegistryChangedListener> listeners;

	public ConfigOptionRegistry() {

		mRegistry = new HashMap<String, ConfigurationOption>();
		listeners = new LinkedList<ModelRegistryChangedListener>();
	}

	public void register(ConfigurationOption configOption) {
		if (configOption != null) {
			if (!mRegistry.containsKey(configOption.getId())) {
				mRegistry.put(configOption.getId(), configOption);

				LogUtils.logInfo(Activator.getContext(), this.getClass(), "register",
						new Object[] { "model registered: " + configOption.getId() }, null);

				updateListeners();
			}
		}
	}

	public ConfigurationOption getConfigOptionForId(String id) {
		if (mRegistry.containsKey(id)) {
			return mRegistry.get(id);
		}
		return null;
	}

	public void removeConfigOption(String id) {
		mRegistry.remove(id);
		LogUtils.logInfo(Activator.getContext(), this.getClass(), "removeConfigOption",
				new Object[] { "model removed: " + id }, null);

		updateListeners();
	}

	public Collection<ConfigurationOption> getAll() {
		ArrayList<ConfigurationOption> retList = new ArrayList<ConfigurationOption>(mRegistry.values());
		Collections.sort(retList);
		return retList;
	}

	public void removeAll() {
		mRegistry.clear();
		LogUtils.logInfo(Activator.getContext(), this.getClass(), "removeAll", new Object[] { "registry cleared" },
				null);

		updateListeners();
	}

	public void addListener(ModelRegistryChangedListener listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}

	public void removeListener(ModelRegistryChangedListener listener) {
		listeners.remove(listener);
	}

	public void removeAllListeners() {
		listeners.clear();
	}

	private void updateListeners() {
		for (ModelRegistryChangedListener listener : listeners) {
			listener.modelRegistryChanged();
		}
	}

	public int size() {
		return mRegistry.size();
	}

	public boolean isEmpty() {
		return mRegistry.isEmpty();
	}
}
