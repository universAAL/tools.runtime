package org.universAAL.ucc.windows;

import java.io.IOException;
import java.text.ParseException;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBException;

import org.universAAL.ucc.controller.aalspace.RoomsWindowController;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

public class RoomsWindow extends Window {
	private Tree userTree;
	private HorizontalSplitPanel split;
	private UccUI app;
	private RoomsWindowController rwc;
	private ResourceBundle bundle;
	private String base;

	public RoomsWindow(UccUI app) throws JAXBException, IOException, ParseException {
		base = "resources.ucc";
		bundle = ResourceBundle.getBundle(base);
		StringBuffer breadcrump = new StringBuffer();
		int counter = 0;
		for (Window w : app.getMainWindow().getChildWindows()) {
			counter++;
			if (counter == app.getMainWindow().getChildWindows().size())
				breadcrump.append(w.getCaption() + " > ");
		}

		breadcrump.append(bundle.getString("rooms.view"));
		setCaption(breadcrump.toString());
		center();
		this.app = app;
		setWidth(500, Sizeable.UNITS_PIXELS);
		setHeight(365, Sizeable.UNITS_PIXELS);
		userTree = new Tree();
		userTree.setImmediate(true);
		userTree.setSelectable(true);
		userTree.setNullSelectionAllowed(false);
		split = new HorizontalSplitPanel();
		split.setMargin(true);
		split.setStyleName(Reindeer.SPLITPANEL_SMALL);
		split.setSplitPosition(200, Sizeable.UNITS_PIXELS);
		split.setLocked(true);
		setContent(split);
		rwc = new RoomsWindowController(this, app);
	}

	public RoomsWindowController getRwc() {
		return rwc;
	}

	public void setRwc(RoomsWindowController rwc) {
		this.rwc = rwc;
	}

	public void addFirstComponent(Component c) {
		split.setFirstComponent(c);
	}

	public void addSecondComponent(Component c) {
		split.setSecondComponent(c);
	}

	public Tree getUserTree() {
		return userTree;
	}

	public void setUserTree(Tree userTree) {
		this.userTree = userTree;
	}

}
