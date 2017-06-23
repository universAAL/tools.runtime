package org.universAAL.tools.ucc.windows;

import java.util.ResourceBundle;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class NotificationWindow extends Window implements Button.ClickListener {
	private Button ok;
	private String base;
	private ResourceBundle bundle;

	public NotificationWindow(String note) {
		base = "resources.ucc";
		bundle = ResourceBundle.getBundle(base);
		setWidth("300px");
		setHeight("175px");
		Panel p = new Panel();
		p.setSizeFull();
		Label l = new Label("<b>" + note + "</b>", Label.CONTENT_XHTML);
		VerticalLayout vl = (VerticalLayout) p.getContent();
		vl.setSpacing(true);
		vl.setMargin(true);
		vl.addComponent(l);
		ok = new Button(bundle.getString("ok.button"));
		ok.addListener(this);
		vl.addComponent(ok);
		vl.setComponentAlignment(ok, Alignment.BOTTOM_CENTER);
		setContent(p);
		center();
		setModal(true);
		setClosable(false);
	}

	public void buttonClick(ClickEvent event) {
		if (event.getButton() == ok) {
			close();
		}

	}

}
