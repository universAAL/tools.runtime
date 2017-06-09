package org.universAAL.ucc.windows;

import java.io.IOException;
import java.text.ParseException;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBException;

import org.universAAL.ucc.controller.aalspace.AddNewHardwareController;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class AddNewHardwareWindow extends Window {
	private UccUI app;
	private VerticalLayout layout;
	private HardwareWindow hWindow;
	private String base;
	private ResourceBundle bundle;

	public AddNewHardwareWindow(HardwareWindow win, RoomsWindow rWin, UccUI app)
			throws JAXBException, IOException, ParseException {
		base = "resources.ucc";
		bundle = ResourceBundle.getBundle(base);
		setCaption(bundle.getString("add.new.hw"));
		this.app = app;
		this.hWindow = win;
		center();
		setWidth(500, Sizeable.UNITS_PIXELS);
		setHeight(400, Sizeable.UNITS_PIXELS);
		layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.setMargin(true);
		setContent(layout);
		new AddNewHardwareController(this, hWindow, rWin, app);
	}

	public void addWindowContent(Component c) {
		layout.addComponent(c);
	}

}
