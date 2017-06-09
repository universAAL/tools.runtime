package org.universAAL.ucc.windows;

import java.util.Locale;
import java.util.ResourceBundle;

import org.universAAL.ucc.controller.preferences.PreferencesController;
import org.universAAL.ucc.model.preferences.Preferences;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

public class PreferencesWindow extends Window {
	private Button save;
	private Button reset;
	private TextField userTxt;
	private PasswordField pwdTxt;
	// private TextField portTxt;
	private TextField urlTxt;
	private TextField userTxt2;
	private TextField uccPortTxt;
	private NativeSelect langSelect;
	private String base;
	private ResourceBundle bundle;
	private UccUI app;
	private PreferencesController con;
	private HorizontalLayout hl;

	public PreferencesWindow(UccUI app, Preferences pref) {
		base = "resources.ucc";
		bundle = ResourceBundle.getBundle(base);
		this.app = app;
		setCaption(bundle.getString("preferences.capt"));
		setStyleName(Reindeer.WINDOW_LIGHT);
		setDraggable(false);
		setResizable(false);
		VerticalLayout vl = new VerticalLayout();
		vl.setSizeFull();
		vl.setSpacing(true);
		vl.setMargin(true);
		Label sep0 = new Label("<hr/>", Label.CONTENT_XHTML);
		vl.addComponent(sep0);
		// ustore interface access
		Label header = new Label("<b>" + bundle.getString("login.info.label") + "</b>", Label.CONTENT_XHTML);
		vl.addComponent(header);

		userTxt = new TextField(bundle.getString("admin.label"));
		userTxt.setImmediate(true);
		userTxt.setWidth("14em");
		userTxt.setValue(pref.getAdmin());
		vl.addComponent(userTxt);

		pwdTxt = new PasswordField(bundle.getString("pwd.label"));
		pwdTxt.setImmediate(true);
		pwdTxt.setWidth("14em");
		pwdTxt.setValue(pref.getPwd());
		vl.addComponent(pwdTxt);

		Label sep1 = new Label("<hr/>", Label.CONTENT_XHTML);
		vl.addComponent(sep1);

		Label sep12 = new Label("<b>" + bundle.getString("header.pref") + "</b>", Label.CONTENT_XHTML);
		vl.addComponent(sep12);

		urlTxt = new TextField(bundle.getString("url.label"));
		urlTxt.setImmediate(true);
		urlTxt.setWidth("14em");
		urlTxt.setValue(pref.getShopIp());
		vl.addComponent(urlTxt);

		// portTxt = new TextField(bundle.getString("port.label"));
		// portTxt.setImmediate(true);
		// portTxt.setValue(pref.getWsPort());
		// vl.addComponent(portTxt);

		Label sep2 = new Label("<hr/>", Label.CONTENT_XHTML);
		vl.addComponent(sep2);

		// ustore plugin account
		Label plugLabel = new Label("<b>" + bundle.getString("ucc.access.label") + "</b>", Label.CONTENT_XHTML);
		vl.addComponent(plugLabel);

		userTxt2 = new TextField(bundle.getString("ucc.ip.label"));
		userTxt2.setImmediate(true);
		userTxt2.setWidth("14em");
		userTxt2.setValue(pref.getUccIp());
		vl.addComponent(userTxt2);

		uccPortTxt = new TextField(bundle.getString("ucc.port.label"));
		uccPortTxt.setImmediate(true);
		uccPortTxt.setWidth("14em");
		uccPortTxt.setValue(pref.getUccPort());
		vl.addComponent(uccPortTxt);

		Label sep3 = new Label("<hr/>", Label.CONTENT_XHTML);
		vl.addComponent(sep3);
		Label genLabel = new Label("<b>" + bundle.getString("general.label") + "</b>", Label.CONTENT_XHTML);
		vl.addComponent(genLabel);

		langSelect = new NativeSelect(bundle.getString("lang.label"));
		langSelect.setWidth("14em");
		langSelect.addItem(bundle.getString("english"));
		langSelect.addItem(bundle.getString("german"));
		if (pref.getLanguage() == null || pref.getLanguage().equals("")) {
			if (Locale.getDefault().getLanguage().equals("de")) {
				langSelect.select(bundle.getString("german"));
			} else {
				langSelect.select(bundle.getString("english"));
			}
		} else {
			if (pref.getLanguage().equals("de"))
				langSelect.select(bundle.getString("german"));
			else
				langSelect.select(bundle.getString("english"));
		}

		vl.addComponent(langSelect);
		Label sep4 = new Label("<hr/>", Label.CONTENT_XHTML);
		vl.addComponent(sep4);

		hl = new HorizontalLayout();
		save = new Button(bundle.getString("save.button"));
		reset = new Button(bundle.getString("reset.button"));
		hl.addComponent(save);
		hl.addComponent(reset);
		vl.addComponent(hl);
		vl.setComponentAlignment(hl, Alignment.BOTTOM_RIGHT);
		setContent(vl);
		setWidth("265px");
		setPositionX(app.getMainWindow().getBrowserWindowWidth() - 325);
		setPositionY(45);
		con = new PreferencesController(app, this);
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

	public TextField getUserTxt() {
		return userTxt;
	}

	public void setUserTxt(TextField userTxt) {
		this.userTxt = userTxt;
	}

	public PasswordField getPwdTxt() {
		return pwdTxt;
	}

	public void setPwdTxt(PasswordField pwdTxt) {
		this.pwdTxt = pwdTxt;
	}

	// public TextField getPortTxt() {
	// return portTxt;
	// }
	//
	// public void setPortTxt(TextField portTxt) {
	// this.portTxt = portTxt;
	// }

	public TextField getUrlTxt() {
		return urlTxt;
	}

	public void setUrlTxt(TextField urlTxt) {
		this.urlTxt = urlTxt;
	}

	public NativeSelect getLangSelect() {
		return langSelect;
	}

	public void setLangSelect(NativeSelect langSelect) {
		this.langSelect = langSelect;
	}

	public HorizontalLayout getHl() {
		return hl;
	}

	public void setHl(HorizontalLayout hl) {
		this.hl = hl;
	}

	public TextField getUserTxt2() {
		return userTxt2;
	}

	public void setUserTxt2(TextField userTxt2) {
		this.userTxt2 = userTxt2;
	}

	public TextField getUccPortTxt() {
		return uccPortTxt;
	}

	public void setUccPortTxt(TextField uccPortTxt) {
		this.uccPortTxt = uccPortTxt;
	}

}
