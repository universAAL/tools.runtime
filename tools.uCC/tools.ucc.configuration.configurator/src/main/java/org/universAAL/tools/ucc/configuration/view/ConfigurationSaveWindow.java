package org.universAAL.tools.ucc.configuration.view;

import java.util.List;
import java.util.Vector;

import com.vaadin.data.Validator.EmptyValueException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.tools.ucc.configuration.beans.ConfigurationSaveOptions;
import org.universAAL.tools.ucc.configuration.controller.VaadinConfigurationController;
import org.universAAL.tools.ucc.configuration.exception.ConfigurationInstanceAlreadyExistsException;
import org.universAAL.tools.ucc.configuration.internal.Activator;
import org.universAAL.tools.ucc.configuration.model.ConfigurationOption;

/**
 *
 * This is a special window to ask for extra information about the instance
 * which should be saved.
 *
 * @author Sebastian Schoebinger
 *
 */

@SuppressWarnings("serial")
public class ConfigurationSaveWindow extends Window {

	public VaadinConfigurationController controller;
	public ConfigurationSaveOptions configSaveOptions;

	public ConfigurationSaveWindow(final VaadinConfigurationController controller,
			List<ConfigurationOption> configOptions) {
		this.controller = controller;
		setModal(true);

		configSaveOptions = new ConfigurationSaveOptions();
		configSaveOptions.setId(controller.getConfigurator().getConfigInstance().getId());
		configSaveOptions.setUseCaseId(controller.getConfigurator().getConfigDefinition().getBundlename());
		configSaveOptions.setAuthor(controller.getConfigurator().getConfigDefinition().getAuthor());
		configSaveOptions.setVersion(controller.getConfigurator().getConfigDefinition().getVersion());
		if (controller.getConfigurator().getConfigInstance().isIsPrimary() != null) {
			configSaveOptions.setPrimary(controller.getConfigurator().getConfigInstance().isIsPrimary());
		}
		if (controller.getConfigurator().getConfigInstance().isIsSecondary() != null) {
			configSaveOptions.setSecondary(controller.getConfigurator().getConfigInstance().isIsSecondary());
		}
		if (controller.getConfigurator().getConfigInstance().getAuthor() != null) {
			configSaveOptions.setAuthor(controller.getConfigurator().getConfigInstance().getAuthor());
		}
		if (controller.getConfigurator().getConfigInstance().getVersion() != null) {
			configSaveOptions.setVersion(controller.getConfigurator().getConfigInstance().getVersion());
		}

		final Form saveForm = new Form();
		saveForm.setCaption("Save options");
		saveForm.setDescription("The following fields describe how the configuration instace will be saved.");
		saveForm.setFormFieldFactory(new SaveFormFactory());

		// Have a button bar in the footer.
		HorizontalLayout okbar = new HorizontalLayout();
		okbar.setHeight("25px");
		saveForm.getFooter().addComponent(okbar);

		// Add an Ok (commit), Reset (discard), and Cancel buttons
		// for the form.
		Button okbutton = new Button("OK");
		Button cancel = new Button("Cancel");

		final List<ConfigurationOption> buttonConfigOptions = configOptions;

		okbutton.setClickShortcut(KeyCode.ENTER);
		okbutton.addListener(new Button.ClickListener() {

			public void buttonClick(ClickEvent event) {
				try {
					if (!saveForm.isValid()) {
						saveForm.setValidationVisible(true);
					} else {
						saveForm.commit();
						try {
							ConfigurationSaveWindow.this.controller.saveConfiguration(buttonConfigOptions,
									configSaveOptions, false);
							ConfigurationSaveWindow.this.getParent().showNotification("Configuration saved!",
									Window.Notification.TYPE_HUMANIZED_MESSAGE);
							ConfigurationSaveWindow.this.close();
						} catch (ConfigurationInstanceAlreadyExistsException e) {
							ConfigurationSaveWindow.this.getParent().addWindow(new YesNoDialog("File already exists",
									"Do you want to save anyway?", new YesNoDialog.Callback() {

										public void onDialogResult(boolean resultIsYes) {
											if (resultIsYes) {
												try {
													ConfigurationSaveWindow.this.controller.saveConfiguration(
															buttonConfigOptions, configSaveOptions, true);
													ConfigurationSaveWindow.this.getParent().showNotification(
															"Configuration saved!",
															Window.Notification.TYPE_HUMANIZED_MESSAGE);
													ConfigurationSaveWindow.this.close();
												} catch (ConfigurationInstanceAlreadyExistsException e) {
													LogUtils.logError(Activator.getContext(), this.getClass(),
															"onDialogResult",
															new Object[] { "Save the configuration failed." }, null);

												}
											} else {
												// Nothing
											}
										}
									}));
						}
					}
				} catch (EmptyValueException e) {

					LogUtils.logError(Activator.getContext(), this.getClass(), "buttonClick",
							new Object[] { "Missing value in save form." }, null);
				}
			}
		});

		cancel.addListener(new Button.ClickListener() {

			public void buttonClick(ClickEvent event) {
				ConfigurationSaveWindow.this.close();
			}
		});

		okbar.addComponent(okbutton);
		okbar.setComponentAlignment(okbutton, Alignment.TOP_RIGHT);
		okbar.addComponent(cancel);

		BeanItem<ConfigurationSaveOptions> item = new BeanItem<ConfigurationSaveOptions>(configSaveOptions);

		saveForm.setItemDataSource(item);

		Vector<String> order = new Vector<String>();
		order.add("useCaseId");
		order.add("id");
		order.add("author");
		order.add("version");
		order.add("primary");
		order.add("secondary");
		saveForm.setVisibleItemProperties(order);

		addComponent(saveForm);
		setWidth("350px");
	}

}
