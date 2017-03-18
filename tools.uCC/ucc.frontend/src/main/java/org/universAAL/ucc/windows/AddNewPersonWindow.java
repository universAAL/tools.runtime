package org.universAAL.ucc.windows;

import java.io.IOException;
import java.text.ParseException;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBException;

import org.universAAL.ucc.controller.aalspace.AddNewPersonController;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class AddNewPersonWindow extends Window {
	private UccUI app;
	private VerticalLayout layout;
	private HumansWindow hWindow;
	private String base;
	private ResourceBundle bundle;
	
	public AddNewPersonWindow(HumansWindow win, SelectUserWindow sel, UccUI app) throws JAXBException, IOException, ParseException {
		base = "resources.ucc";
		bundle = ResourceBundle.getBundle(base);
		setCaption(bundle.getString("add.new.person"));
		this.app = app;
		this.hWindow = win;
		center();
		setWidth(500, Sizeable.UNITS_PIXELS);
		setHeight(400, Sizeable.UNITS_PIXELS);
		layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.setMargin(true);
		setContent(layout);
		new AddNewPersonController(this, hWindow, sel, app);
	}

public void addWindowContent(Component c) {
		layout.addComponent(c);
	}

}
