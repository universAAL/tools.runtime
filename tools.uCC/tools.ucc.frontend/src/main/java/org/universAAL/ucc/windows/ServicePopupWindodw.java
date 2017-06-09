package org.universAAL.ucc.windows;

import java.util.List;
import java.util.ResourceBundle;

import org.universAAL.ucc.controller.ustore.services.PopupService;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ServicePopupWindodw extends Window {
	private String base;
	private ResourceBundle bundle;

	public ServicePopupWindodw(String serv, List<PopupService> popups) {
		base = "resources.ucc";
		bundle = ResourceBundle.getBundle(base);
		setCaption(serv);

		final VerticalLayout vl = new VerticalLayout();
		vl.setMargin(true);
		vl.setSizeFull();
		setContent(vl);

		HorizontalLayout hl1 = new HorizontalLayout();
		hl1.setSizeFull();
		PopupService p = null;
		for (PopupService ps : popups) {
			if (ps.getTitle().equals(serv)) {
				p = ps;
			}
		}
		Panel panel = new Panel(bundle.getString("description.label"));
		Label details = new Label("<font size = 3>" + p.getDetails() + "</font size>");
		details.setContentMode(Label.CONTENT_XHTML);

		panel.addComponent(details);
		panel.setSizeFull();

		vl.addComponent(hl1);
		vl.setExpandRatio(hl1, 1);
		vl.addComponent(panel);
		vl.setExpandRatio(panel, 4);
		ThemeResource tr = new ThemeResource("img/" + p.getImg());
		// String basepath = VaadinService.getCurrent()
		// .getBaseDirectory().getAbsolutePath();

		// Image img = new Image("",imgsrc);
		Button img = new Button();
		img.setIcon(tr);
		final VerticalLayout vl1 = new VerticalLayout();
		vl1.setSizeFull();
		vl1.setStyleName("menubutton");
		vl1.addComponent(img);
		vl1.setComponentAlignment(img, Alignment.TOP_LEFT);

		Label title = new Label(p.getTitle());
		Label subtitle = new Label(p.getSubtitle());
		Label category = new Label(p.getCategory());
		final VerticalLayout vl2 = new VerticalLayout();
		vl2.addComponent(title);
		vl2.addComponent(subtitle);
		vl2.addComponent(category);
		vl2.setSizeFull();
		vl2.setExpandRatio(title, 1);
		vl2.setExpandRatio(subtitle, 1);
		vl2.setExpandRatio(category, 1);

		Label rating = new Label(p.getRating());
		Button install = new Button(bundle.getString("install.button"));
		install.setEnabled(false);
		final VerticalLayout vl3 = new VerticalLayout();
		vl3.setSizeFull();
		vl3.addComponent(rating);
		vl3.addComponent(install);
		vl3.setComponentAlignment(rating, Alignment.TOP_LEFT);
		vl3.setComponentAlignment(install, Alignment.BOTTOM_LEFT);

		hl1.addComponent(vl1);
		hl1.addComponent(vl2);
		hl1.addComponent(vl3);
		setWidth("600px");
		setHeight("500px");
	}

}
