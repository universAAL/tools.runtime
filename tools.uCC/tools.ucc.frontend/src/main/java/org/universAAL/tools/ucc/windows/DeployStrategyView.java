package org.universAAL.tools.ucc.windows;

import java.util.Arrays;
import java.util.ResourceBundle;

import com.vaadin.ui.Form;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class DeployStrategyView extends VerticalLayout {
	private Form form;
	private TextField txt;
	private OptionGroup options;
	private String base;
	private ResourceBundle bundle;

	public DeployStrategyView(String name, String serviceId, String uaapPath) {
		base = "resources.ucc";
		bundle = ResourceBundle.getBundle(base);
		addComponent(new Label("<b>" + bundle.getString("header.deploy.strategy") + "</b>", Label.CONTENT_XHTML));
		setMargin(true);
		setSpacing(true);
		form = new Form();
		txt = new TextField(bundle.getString("application.label"));
		txt.setWidth("14em");
		txt.setValue(name);
		form.addField(bundle.getString("application.label"), txt);
		addComponent(form);
		options = new OptionGroup("", Arrays.asList(
				new String[] { bundle.getString("opt.available.nodes"), bundle.getString("opt.selected.nodes") }));
		options.select(bundle.getString("opt.available.nodes"));
		options.setImmediate(true);
		options.setNullSelectionAllowed(false);
		addComponent(options);
		setWidth("425px");
	}

	public TextField getTxt() {
		return txt;
	}

	public void setTxt(TextField txt) {
		this.txt = txt;
	}

	public OptionGroup getOptions() {
		return options;
	}

	public void setOptions(OptionGroup options) {
		this.options = options;
	}

}
