package org.universAAL.ucc.configuration.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.osgi.framework.FrameworkUtil;

import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.ucc.configuration.model.configurationdefinition.Category;
import org.universAAL.ucc.configuration.model.configurationdefinition.ConfigItem;
import org.universAAL.ucc.configuration.model.configurationdefinition.Dependency;
import org.universAAL.ucc.configuration.model.configurationdefinition.Item;
import org.universAAL.ucc.configuration.model.configurationdefinition.OnConfigurationModelChangedListener;
import org.universAAL.ucc.configuration.model.configurationdefinition.Validator;
import org.universAAL.ucc.configuration.model.configurationdefinition.Validators;
import org.universAAL.ucc.configuration.model.configurationinstances.ConfigOption;
import org.universAAL.ucc.configuration.model.configurationinstances.ObjectFactory;
import org.universAAL.ucc.configuration.model.configurationinstances.Value;
import org.universAAL.ucc.configuration.model.exceptions.ValidationException;
import org.universAAL.ucc.configuration.model.interfaces.ConfigurationValidator;
import org.universAAL.ucc.configuration.model.interfaces.OnConfigurationChangedListener;
import org.universAAL.ucc.configuration.model.servicetracker.ListenerServiceTracker;
import org.universAAL.ucc.configuration.model.servicetracker.ValidationServiceTracker;

/**
 * 
 * This class is an abstraction of the configuration items in XML configuration definition file.
 * It holds the configuration definition of the item, the validators and some other logic.
 * 
 * @author Sebastian.Schoebinge
 *
 */

public abstract class ConfigurationOption implements Comparable<ConfigurationOption> {
	
	protected ConfigItem configItem;
	ConfigOption configOption;
	ConfigOptionRegistry configRegestry;
	protected List<Value> values;
	Category category;
	
	HashMap<String,ConfigurationValidator> validators;
	List<OnConfigurationChangedListener> listeners;
	List<OnConfigurationChangedListener> externalListeners;
	int order;
	
	boolean isValid;
	
	/**
	 * Load validators and on configuration changed listeners.
	 * Resister at the configuration option registry
	 * @param configItem
	 * @param category
	 * @param configOptionRegistry
	 */
	
	public ConfigurationOption(ConfigItem configItem, Category category, ConfigOptionRegistry configOptionRegistry) {
		LogUtils.logInfo(Activator.getContext(), this.getClass(), "ConfigurationOption",
				new Object[] { "create configuration option: " + configItem.getId() }, null);

		this.category = category;
		this.configItem = configItem;
		configOption = new ObjectFactory().createConfigOption();
		configOption.setId(configItem.getId());
		
		validators = new HashMap<String,ConfigurationValidator>();
		listeners = new LinkedList<OnConfigurationChangedListener>();
		externalListeners = new LinkedList<OnConfigurationChangedListener>();
		
		values = configOption.getValue();
		
		this.configRegestry = configOptionRegistry;
		order = configRegestry.size() + 1;
		configRegestry.register(this);
		setValidators();
		setOnChonfigurationModelChangedListener();
	}
	
	
	/**
	 * Set the value, execute validators and update the listeners.
	 * @param value
	 * @throws ValidationException
	 */
	public void setValue(Value value) throws ValidationException{
		if(value == null || value.getValue() == null){
			return;
		}
		resetIsValidFlag();
		if(values.size() > 0){
			values.clear();
		}
		values.add(value);
		
		doDeepValidation();
		
		updateExternalListeners();
		
		updateListeners();
	}
	
	
	private void resetIsValidFlag() {
		isValid = false;
	}


	/**
	 * Set value for multiple selection.
	 * @param configValues
	 * @throws ValidationException
	 */
	public void setValue(List<Value> configValues) throws ValidationException {
		resetIsValidFlag();
		values.clear();
		values.addAll(configValues);
		
		doDeepValidation();
		updateExternalListeners();
		
		updateListeners();
	}
	
	public String getValue() {
		if(values.size() > 0){
			return values.get(0).getValue();
		}
		return "";
	}
	
	public int getValuesCount(){
		return values.size();
	}
	
	/**
	 * Executes the validators and if an error accrues the ValidationException will be thrown.
	 * @throws ValidationException
	 */
	public void doDeepValidation() throws ValidationException {
		for(ConfigurationValidator validator: validators.values()){
			for(Value value : values){
				try{
					validator.validate(configRegestry,value);
				}catch(Exception e){
					if(e instanceof ValidationException){
						throw (ValidationException)e;
					}
					LogUtils.logInfo(Activator.getContext(), this.getClass(), "doDeepValidation",
							new Object[] { "Validation failed: " + e.getMessage() }, null);
				}
			}
		}
	}
	
	public void addListener(OnConfigurationChangedListener listener){
		if(listener != null){
			listeners.add(listener);
			LogUtils.logInfo(Activator.getContext(), this.getClass(), "addListener",
					new Object[] { "Listener added: " + listener.getClass().getName() }, null);
		}
	}
	
	public void addExternalListener(OnConfigurationChangedListener listener){
		if(listener != null){
			externalListeners.add(listener);
			LogUtils.logInfo(Activator.getContext(), this.getClass(), "addExternalListener",
					new Object[] { "Listener added: " + listener.getClass().getName() }, null);
		}
	}

	public boolean isActive() {
		return configItem.isActive();
	}

	public List<Value> getValues() {
		return new LinkedList<Value>(values);
	}
	
	
	/**
	 * Loads the Validators specified by the configuration definition.
	 * This in done by reflection.
	 */
	private void setValidators() {
		Validators tmpValidators = configItem.getValidators();
		if(tmpValidators != null){
			for(Validator validator : tmpValidators.getValidator()){
				String className = validator.getClazz();
				if(className != null){					
					try {
						Class c = Class.forName(className);
//						Class c = ConfigurationValidator.class.getClassLoader().getParent().loadClass(className);
						for(Class theInterface : c.getInterfaces()){
							if(theInterface.getName().equals(ConfigurationValidator.class.getName())){
								try {
									ConfigurationValidator valInstance = (ConfigurationValidator)c.newInstance();
									valInstance.setAttributes(validator.getAttribute().toArray(new String[validator.getAttribute().size()]));
									addValidator(className, valInstance);
								} catch (InstantiationException e) {
									LogUtils.logError(Activator.getContext(), this.getClass(), "setValidators",
											new Object[] { e.toString() }, null);

								} catch (IllegalAccessException e) {
									LogUtils.logError(Activator.getContext(), this.getClass(), "setValidators",
											new Object[] { e.toString() }, null);

								}
							}
						}
					} catch (ClassNotFoundException e) {
						if(!"".equals(className)){
							LogUtils.logInfo(Activator.getContext(), this.getClass(), "setValidators",
									new Object[] { "Class not found. Register service tacker for ValidatorCclass: " + className }, null);

							ValidationServiceTracker serviceTracker = new ValidationServiceTracker(
									Activator.getBundleContext()
									//FrameworkUtil.getBundle(getClass()).getBundleContext()
									, className, this, validator.getAttribute().toArray(new String[validator.getAttribute().size()]));
							serviceTracker.open();
						}
					}	
				}
			}
		}
	}
	
	
	/**
	 * Loads the OnConfigurationModelChangedListener specified by the configuration definition.
	 * This in done by reflection.
	 */
	private void setOnChonfigurationModelChangedListener() {
		OnConfigurationModelChangedListener listener = configItem.getOnConfigurationModelChangedListener();
		if(listener != null){
			String className = listener.getClazz();
			if(className != null){
				try {
					Class c = Class.forName(className);
					for(Class theInterface : c.getInterfaces()){
						if(theInterface.getName().equals(OnConfigurationChangedListener.class.getName())){
							try {
								OnConfigurationChangedListener modListener = (OnConfigurationChangedListener)c.newInstance();
								addListener(modListener);
							} catch (InstantiationException e) {
								LogUtils.logError(Activator.getContext(), this.getClass(), "setOnChonfigurationModelChangedListener",
										new Object[] { e.toString() }, null);

							} catch (IllegalAccessException e) {
								LogUtils.logError(Activator.getContext(), this.getClass(), "setOnChonfigurationModelChangedListener",
										new Object[] { e.toString() }, null);

							}
						}
					}
				} catch (ClassNotFoundException e) {
					if(!"".equals(className)){
						LogUtils.logError(Activator.getContext(), this.getClass(), "setOnChonfigurationModelChangedListener",
								new Object[] { "Class not found. Register service tracker for class:" + className }, null);

						ListenerServiceTracker serviceTracker = new ListenerServiceTracker(
								Activator.getBundleContext()
								//FrameworkUtil.getBundle(getClass()).getBundleContext()
								, className, this);
						serviceTracker.open();
					}
				}
			}
		}
	}
	
	public boolean isRequired() {
		return new Cardinality(configItem.getCardinality()).isRequired();
	}
	
	public String getId(){
		return configOption.getId();
	}

	/**
	 * The configuration option is valid if all validators valid and if it is required it also needs a value.
	 * @return
	 */
	public boolean isValid() {
		if(isValid){
			return true;
		}
		if(!allValidatorsValid()){
			return false;
		}
		if(isRequired() && !hasValue()){
			return false;
		}
		isValid = true;
		return true;
	}
	
	public boolean hasValue() {
		if(values.size() < 1){
			return false;
		}else{
			return !values.get(0).getValue().equals("");
		}
	}
	
	/**
	 * Executes all validators.
	 * @return
	 */
	private boolean allValidatorsValid() {
		for(ConfigurationValidator validator: validators.values()){
			for(Value value : values){
				if(!validator.isValid(configRegestry, value)){
					LogUtils.logInfo(Activator.getContext(), this.getClass(), "allValidatorValid",
							new Object[] { "Validator failed: " + validator.getClass().getName() + " with input: " + value.getValue() }, null);

					return false;
				}
			}
		}
		return true;
	}

	public Category getCategory(){
		return category;
	}

	public void setCardinality(Cardinality cardinality) {
		resetIsValidFlag();
		configItem.setCardinality(cardinality.toString());
		updateExternalListeners();
		updateListeners();
	}

	public ConfigOption getConfigOption() {
		return configOption;
	}

	public void addValidator(String key, ConfigurationValidator validator){
		if(validator != null){
			resetIsValidFlag();
			validators.put(key, validator);
			LogUtils.logInfo(Activator.getContext(), this.getClass(), "addValidator",
					new Object[] { "Validator added: " + validator.getClass().getName() }, null);

		}
	}
	
	protected void updateListeners() {
		for(OnConfigurationChangedListener listener: new LinkedList<OnConfigurationChangedListener>(listeners)){
			listener.configurationChanged(configRegestry, this);
		}
	}
	
	protected void updateExternalListeners() {
		for(OnConfigurationChangedListener listener: new LinkedList<OnConfigurationChangedListener>(externalListeners)){
			try{
				listener.configurationChanged(configRegestry, this);
			}catch(Exception e){
				LogUtils.logInfo(Activator.getContext(), this.getClass(), "updateExternalListeners",
						new Object[] { "OnConfigurationModelChangedListener error: " + e.getMessage() }, null);

			}
		}
	}
	
	/**
	 * If the configuration option has dependencies then this method checks all configuration options
	 * on which this configuration option depends and if they are valid then this configuration option will be activated.
	 * If not it will be disabled.	
	 */
	public void checkDependencies() {
		LogUtils.logInfo(Activator.getContext(), this.getClass(), "checkDependencies",
				new Object[] { "check dependencies for model: " + getId() }, null);

		Dependency dep = configItem.getDependencies();
		if(dep == null){
			return;
		}
		for (Item item : configItem.getDependencies().getItem()){
			ConfigItem tmpItem = (ConfigItem)item.getId();
			ConfigurationOption cOpt =  configRegestry.getConfigOptionForId(tmpItem.getId());
			if(cOpt != null){
				if(!cOpt.isValid() || !cOpt.isActive()){
					setIsActive(false);
					return;
				}
			}
		}
		setIsActive(true);
	}
	
	public int getOrder() {
		return order;
	}
	
	public String getDescription() {
		return configItem.getDescription();
	}

	public String getLabel() {
		return configItem.getLabel();
	}

	
	public int compareTo(ConfigurationOption o) {
		if(o.getOrder() == getOrder()){
			return 0;
		}else if(o.getOrder() > getOrder()){
			return -1;
		}else {
			return 1;
		}
	}
	
	public void removeListener(OnConfigurationChangedListener listener) {
		listeners.remove(listener);
	}

	public void setIsActive(boolean active) {
		resetIsValidFlag();
		configItem.setActive(active);
		updateExternalListeners();
		updateListeners();
	}


	public void removeValidator(ConfigurationValidator validator) {
		resetIsValidFlag();
		validators.remove(validator);
	}


	public void updateValidatorAttributesForId(String name, String[] attributes) {
		validators.get(name).setAttributes(attributes);
		updateListeners();
	}


	public void removeExternalListener(OnConfigurationChangedListener listener) {
		externalListeners.remove(listener);		
	}
	
}
