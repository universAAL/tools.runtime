package org.universAAL.ucc.windows;

import java.util.ResourceBundle;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

public class NoConfigurationWindow extends Window implements Button.ClickListener {
	private Panel panel;
	private Button ok;
	private String base;
	private ResourceBundle bundle;

	public NoConfigurationWindow(String msg) {
		base = "resources.ucc";
		bundle = ResourceBundle.getBundle(base);
		// setCaption(bundle.getString("result.installation"));
		setWidth("425px");
		setHeight("300px");
		Label label = new Label("<b>" + msg + "</b>", Label.CONTENT_XHTML);
		panel = new Panel();
		panel.setStyleName(Reindeer.PANEL_LIGHT);
		panel.setWidth("300px");
		VerticalLayout vl = new VerticalLayout();
		vl.setSizeFull();
		vl.setSpacing(true);
		vl.setMargin(true);
		VerticalLayout pl = (VerticalLayout) panel.getContent();
		pl.setSpacing(true);
		pl.setMargin(true);
		pl.addComponent(label);
		vl.addComponent(panel);
		vl.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
		ok = new Button(bundle.getString("ok.button"));
		ok.addListener(this);
		vl.addComponent(ok);
		vl.setComponentAlignment(ok, Alignment.BOTTOM_CENTER);
		setContent(vl);
		center();
		setResizable(false);
		setModal(true);
		setClosable(false);
	}

	public void buttonClick(ClickEvent event) {
		close();

	}

}
