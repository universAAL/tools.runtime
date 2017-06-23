package org.universAAL.tools.ucc.windows;

import java.util.ResourceBundle;

import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

public class UsrvInformationWindow extends Window {
	private Button ok;
	private Button cancel;
	private TextField nameTxt;
	private TextField version;
	private TextField usrvDescription;
	private TextField provider;
	private ListSelect tags;
	private String base;
	private ResourceBundle bundle;
	private Form form;

	public UsrvInformationWindow() {
		base = "resources.ucc";
		bundle = ResourceBundle.getBundle(base);
		form = new Form();
		form.setSizeFull();
		form.setWriteThrough(false);
		setCaption(bundle.getString("usrv.info"));
		setWidth("425px");
		setHeight("325px");
		center();
		setModal(true);
		setClosable(false);
		nameTxt = new TextField(bundle.getString("name.label"));
		nameTxt.setImmediate(true);
		nameTxt.setWidth("14em");
		form.addField(bundle.getString("name.label"), nameTxt);

		version = new TextField(bundle.getString("version.label"));
		version.setImmediate(true);
		version.setWidth("14em");
		form.addField(bundle.getString("version.label"), version);

		usrvDescription = new TextField(bundle.getString("description.label"));
		usrvDescription.setImmediate(true);
		usrvDescription.setWidth("14em");
		form.addField(bundle.getString("description.label"), usrvDescription);

		provider = new TextField(bundle.getString("provider.label"));
		provider.setImmediate(true);
		provider.setWidth("14em");
		form.addField(bundle.getString("provider.label"), provider);

		tags = new ListSelect(bundle.getString("tags.label"));
		tags.setImmediate(true);
		tags.setWidth("14em");

		ok = new Button(bundle.getString("ok.button"));
		cancel = new Button(bundle.getString("cancel.button"));
		HorizontalLayout hl = new HorizontalLayout();
		hl.setMargin(true);
		hl.setSpacing(true);
		hl.addComponent(ok);
		hl.addComponent(cancel);
		form.setFooter(hl);
		addComponent(form);
	}

	public Button getOk() {
		return ok;
	}

	public void setOk(Button ok) {
		this.ok = ok;
	}

	public TextField getNameTxt() {
		return nameTxt;
	}

	public void setNameTxt(TextField nameTxt) {
		this.nameTxt = nameTxt;
	}

	public TextField getVersion() {
		return version;
	}

	public void setVersion(TextField version) {
		this.version = version;
	}

	public TextField getProvider() {
		return provider;
	}

	public void setProvider(TextField provider) {
		this.provider = provider;
	}

	public ListSelect getTags() {
		return tags;
	}

	public void setTags(ListSelect tags) {
		this.tags = tags;
	}

	public Button getCancel() {
		return cancel;
	}

	public void setCancel(Button cancel) {
		this.cancel = cancel;
	}

	public TextField getUsrvDescription() {
		return usrvDescription;
	}

	public void setUsrvDescription(TextField usrvDescription) {
		this.usrvDescription = usrvDescription;
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}

}
