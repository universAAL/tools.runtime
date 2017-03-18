package org.universAAL.ucc.windows;

import java.util.ResourceBundle;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

public class SearchWindow extends Window {
	private TextField txt;
	private Button button;
	private String base;
	private ResourceBundle res;

	public SearchWindow(UccUI app) {
		base = "resources.ucc";
		res = ResourceBundle.getBundle(base);
		setCaption(res.getString("search.desc"));
		setStyleName(Reindeer.WINDOW_LIGHT);
		setWidth("250px");
		this.setDraggable(false);
		this.setResizable(false);
		setPositionX(app.getMainWindow().getBrowserWindowWidth() - 250);
		setPositionY(45);
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		hl.setMargin(true);
		txt = new TextField();
		txt.setEnabled(false);
		hl.addComponent(txt);
		button = new Button(res.getString("search.button"));
		button.setEnabled(false);
		hl.addComponent(button);
		setContent(hl);
	}

}
