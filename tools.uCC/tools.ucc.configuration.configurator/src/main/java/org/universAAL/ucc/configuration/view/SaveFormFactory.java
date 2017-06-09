package org.universAAL.ucc.configuration.view;

import com.vaadin.data.Item;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.TextField;

/**
 * 
 * This is a factory class for the form to save an configuration instance.
 * 
 * @author Sebastian Schoebinger
 *
 */

@SuppressWarnings("serial")
public class SaveFormFactory implements FormFieldFactory {

	public Field createField(Item item, Object propertyId, Component uiContext) {

		String pid = (String) propertyId;
		if ("useCaseId".equals(pid)) {
			TextField field = new TextField("Usecase bundle name");
			field.setReadOnly(true);
			return field;
		} else if ("id".equals(pid)) {
			TextField textField = new TextField("ID");
			textField.setRequired(true);
			textField.setRequiredError("Give an id");
			textField.setImmediate(true);
			textField.setValidationVisible(true);
			return textField;
		} else if ("version".equals(pid)) {
			return new TextField("Version");
		} else if ("author".equals(pid)) {
			return new TextField("Author");
		} else if ("primary".equals(pid)) {
			return new CheckBox("Primary");
		} else if ("secondary".equals(pid)) {
			return new CheckBox("Secondary");
		}
		return null;
	}

}
