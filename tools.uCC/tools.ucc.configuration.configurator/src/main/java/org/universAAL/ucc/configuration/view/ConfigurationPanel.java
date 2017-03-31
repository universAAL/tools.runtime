package org.universAAL.ucc.configuration.view;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;

import org.universAAL.ucc.configuration.model.ConfigurationOption;
import org.universAAL.ucc.configuration.model.configurationdefinition.Category;
import org.universAAL.ucc.configuration.model.configurationinstances.ObjectFactory;

/**
 * 
 * The configuration panel represents the categories from the configuration definition.
 * 
 * @author Sebastian Schoebinger
 *
 */

@SuppressWarnings("serial")
public class ConfigurationPanel extends Panel{
	ObjectFactory factory;
	
	
	public ConfigurationPanel(Category category) {
		super(category.getLabel());
		factory = new ObjectFactory();
	}
	
	/**
	 * 
	 * @return a list of all configuration options on this panel.
	 */
	public List<ConfigurationOption> getConfigOptions(){
		List<ConfigurationOption> configOptions = new LinkedList<ConfigurationOption>();
		Iterator<Component> i = getComponentIterator();
		while(i.hasNext()){
			Component component = i.next();
			if(component.isEnabled()){
				if(component instanceof SimpleConfiguratorTextField){
					if(((SimpleConfiguratorTextField) component).configOption.hasValue()){
						configOptions.add(((SimpleConfiguratorTextField) component).getConfigOption());
					}
				}else if(component instanceof ConfigurationListSelect){
					if(((ConfigurationListSelect) component).configOption.hasValue()){
						configOptions.add(((ConfigurationListSelect) component).getConfigOption());
					}
				} else if(component instanceof SimpleConfiguratorPasswordField) {
					if(((SimpleConfiguratorPasswordField)component).configOption.hasValue()) {
						configOptions.add(((SimpleConfiguratorPasswordField)component).getConfigOption());
					}
				} else if(component instanceof MultiselectionList) {
					if(((MultiselectionList)component).configOption.hasValue()) {
						configOptions.add(((MultiselectionList)component).getConfigOption());
					}
				}
			}
		}
		return configOptions;
	}
	
	/**
	 * 
	 * @return true if all configuration options on this panel are valid else false.
	 */
	public boolean isValid(){
		Iterator<Component> i = getComponentIterator();
		while(i.hasNext()){
			Component component = i.next();
			if(component.isEnabled()){
				if(component instanceof SimpleConfiguratorTextField){
					if(!((SimpleConfiguratorTextField) component).isValid()){
						return false;
					}
				}else if(component instanceof ConfigurationListSelect){
					if(!((ConfigurationListSelect) component).isValid()){
						return false;
					}
				} else if(component instanceof SimpleConfiguratorPasswordField) {
					if(!((SimpleConfiguratorPasswordField)component).isValid()) {
						return false;
					}
				} else if(component instanceof MultiselectionList) {
					if(!((MultiselectionList)component).isValid()) {
						return false;
					}
				}
			}
		}
		return true;
	}

}
