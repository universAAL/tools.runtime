package org.universAAL.ucc.configuration.api;

import java.io.File;
import java.io.FileFilter;
import java.util.LinkedList;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.ucc.configuration.model.configurationinstances.ConfigOption;
import org.universAAL.ucc.configuration.model.configurationinstances.ConfigurationInstance;
import org.universAAL.ucc.configuration.model.configurationinstances.ObjectFactory;
import org.universAAL.ucc.configuration.model.configurationinstances.Value;
import org.universAAL.ucc.configuration.storage.exceptions.NoConfigurationFoundException;
import org.universAAL.ucc.configuration.storage.interfaces.ConfigurationInstancesStorage;
import org.universAAL.ucc.configuration.storage.interfaces.StorageChangedListener;
import org.universAAL.ucc.configuration.storage.internal.Activator;
import org.universAAL.ucc.configuration.storage.servicetracker.StorageServiceTracker;

/**
 * 
 * This configuration preferences class loads the configuration instance for the given bundle from the configuration instances storage.
 * It could be asked for a configuration option value by the id of this configuration option.
 * 
 * @author Sebastian.Schoebinge
 *
 */
public class ConfigPreferences implements StorageChangedListener{
	
//	private static Logger logger = LoggerFactory.getLogger(ConfigPreferences.class);
	
	private ConfigurationInstance config;
//	private String basedir = "C:\\tmpConfigFiles/";
	
	ConfigurationInstancesStorage storage;
	Bundle bundle;
	
	public File[] getOnlyFileChildren(String basedir){
		FileFilter fileFilter = new FileFilter() {
		    public boolean accept(File file) {
		        return file.isFile();
		    }
		};
		File dir = new File(basedir);
		return dir.listFiles(fileFilter);
	}
	
	/**
	 * Get the storage service and add the current instance as change listener.
	 * @param bundle is the bundle for which the configuration was defined.
	 */
	public ConfigPreferences(Bundle bundle){
		this.bundle = bundle;
		BundleContext context = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
		new StorageServiceTracker(context, ConfigurationInstancesStorage.class, null, this).open();
	}
	
	private void loadConfig() {
		if(storage == null){
			return;
		}
		try {
			config = storage.getConfigurationForBundle(bundle);
		} catch (NoConfigurationFoundException e) {
			LogUtils.logInfo(Activator.getContext(), this.getClass(), "loadConfig",
					new Object[] { "no configuration fount, create an empty one." }, null);
			config = new ObjectFactory().createConfigurationInstance();
		}
	}
	
	
	/**
	 * 
	 * @param id
	 * @param defaultValue
	 * @return the value for the given id or the defaultValue.
	 */
	public Value getValue(String id, Value defaultValue){
		for(ConfigOption option : config.getConfigOption()){
			if(option.getId().equals(id)){
				if(option.getValue().size() > 0){
					
					LogUtils.logInfo(Activator.getContext(), this.getClass(), "getValue",
							new Object[] { "return value: " + option.getValue().get(0).getValue() + " for id: " + id }, null);
					return option.getValue().get(0);
				}
			}
		}
		LogUtils.logInfo(Activator.getContext(), this.getClass(), "getValue",
				new Object[] { "return default value: " + defaultValue.getValue() + " for id: " + id }, null);
		
		return defaultValue;		
	}

	public List<Value> getValue(String id){
		for(ConfigOption option : config.getConfigOption()){
			if(option.getId().equals(id)){
				return option.getValue();
			}
		}
		
		LogUtils.logInfo(Activator.getContext(), this.getClass(), "getValue",
				new Object[] { "return default value for id: " + id }, null);

		return new LinkedList<Value>();		
	}
	
	public String getString(String id, String defaultValue){
		Value v = new ObjectFactory().createValue();
		v.setValue(defaultValue);
		Value result = getValue(id, v);
		return result.getValue();
	}
	
	public int getInt(String id, int defaultValue){
		Value v = new ObjectFactory().createValue();
		v.setValue(defaultValue+"");
		Value result = getValue(id, v);
		try{
			return Integer.parseInt(result.getValue());
		} catch(NumberFormatException e){
			LogUtils.logError(Activator.getContext(), this.getClass(), "getInt",
					new Object[] { "return default value for id: " + id }, null);
			return defaultValue;
		}
	}

	public double getDouble(String id, double defaultValue) {
		Value v = new ObjectFactory().createValue();
		v.setValue(defaultValue+"");
		Value result = getValue(id, v);
		try{
			return Double.parseDouble(result.getValue());
		} catch(NumberFormatException e){
			
			LogUtils.logError(Activator.getContext(), this.getClass(), "getInt",
					new Object[] {"return default value for id: " + id }, null);
			
			return defaultValue;
		}
	}


	
	public void storageChanged() {
		loadConfig();
	}

	public void setStorage(ConfigurationInstancesStorage storage) {
		if(storage != null){
			this.storage = storage;
			storage.addListener(this);
			loadConfig();
		}
	}
}
