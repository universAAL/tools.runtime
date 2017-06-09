package org.universAAL.ucc.windows;

import java.util.ResourceBundle;

import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

public class AccountWindow extends Window {
	private TextField user;
	private PasswordField pwd;
	private PasswordField confirm;
	private Button save;
	private Button reset;
	private CheckBox check;
	private String base;
	private ResourceBundle bundle;

	public AccountWindow() {
		base = "resources.ucc";
		bundle = ResourceBundle.getBundle(base);
		setCaption(bundle.getString("create.new.account"));
		setWidth("375px");
		setHeight("300px");
		center();
		setStyleName(Reindeer.WINDOW_LIGHT);
		VerticalLayout vl = new VerticalLayout();
		vl.setSizeFull();
		vl.setMargin(true);
		user = new TextField(bundle.getString("user.name"));
		pwd = new PasswordField(bundle.getString("pwd.label"));
		confirm = new PasswordField(bundle.getString("confirm.pwd"));
		check = new CheckBox(bundle.getString("save.button"));
		vl.addComponent(user);
		vl.addComponent(pwd);
		vl.addComponent(confirm);
		HorizontalLayout hl = new HorizontalLayout();
		hl.setMargin(true);
		hl.setSpacing(true);
		save = new Button(bundle.getString("save.button"));
		reset = new Button(bundle.getString("reset.button"));
		hl.addComponent(save);
		hl.addComponent(reset);
		vl.addComponent(check);
		vl.addComponent(hl);
		setContent(vl);
	}

	public TextField getUser() {
		return user;
	}

	public void setUser(TextField user) {
		this.user = user;
	}

	public PasswordField getPwd() {
		return pwd;
	}

	public void setPwd(PasswordField pwd) {
		this.pwd = pwd;
	}

	public PasswordField getConfirm() {
		return confirm;
	}

	public void setConfirm(PasswordField confirm) {
		this.confirm = confirm;
	}

	public Button getSave() {
		return save;
	}

	public void setSave(Button save) {
		this.save = save;
	}

	public Button getReset() {
		return reset;
	}

	public void setReset(Button reset) {
		this.reset = reset;
	}

	public CheckBox getCheck() {
		return check;
	}

	public void setCheck(CheckBox check) {
		this.check = check;
	}

}
