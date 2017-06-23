package org.universAAL.tools.ucc.windows;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import org.universAAL.tools.ucc.controller.ustore.services.Parser;
import org.universAAL.tools.ucc.controller.ustore.services.PopupService;
import org.universAAL.tools.ucc.controller.ustore.services.Service;
import org.universAAL.tools.ucc.service.manager.Activator;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class BrowseServicesWindow extends Window {
	private final GridLayout grid;
	private final TextField searchfield;
	private Button searchbutton;
	private UccUI app;
	private HashMap<String, Button> buttons;
	private List<PopupService> popups;
	private String base;
	private ResourceBundle bundle;
	private Button back;

	public BrowseServicesWindow(UccUI app) {
		base = "resources.ucc";
		bundle = ResourceBundle.getBundle(base);
		this.app = app;
		final VerticalLayout vl1 = new VerticalLayout();
		vl1.setMargin(true);
		vl1.setSpacing(true);
		vl1.setSizeFull();
		setContent(vl1);

		setCaption(bundle.getString("free"));

		HorizontalLayout hl1 = new HorizontalLayout();
		hl1.setSizeFull();

		TabSheet tabsheet = new TabSheet();
		tabsheet.setSizeFull();

		searchfield = new TextField();
		searchfield.setInputPrompt("Search");
		searchbutton = new Button(bundle.getString("search.button"));

		vl1.addComponent(hl1);
		vl1.addComponent(tabsheet);
		vl1.setExpandRatio(hl1, 0.5f);
		vl1.setExpandRatio(tabsheet, 9.5f);

		hl1.addComponent(searchfield);
		hl1.addComponent(searchbutton);
		hl1.setExpandRatio(searchbutton, 0.5f);
		hl1.setExpandRatio(searchfield, 9.5f);
		hl1.setComponentAlignment(searchbutton, Alignment.MIDDLE_RIGHT);
		hl1.setComponentAlignment(searchfield, Alignment.MIDDLE_RIGHT);

		grid = new GridLayout();
		grid.setColumns(5);
		grid.setWidth("100%");

		back = new Button(bundle.getString("back.button"));
		back.setEnabled(false);
		vl1.addComponent(back);
		vl1.setComponentAlignment(back, Alignment.BOTTOM_RIGHT);

		fillGrid(grid, "");

		tabsheet.addTab(grid, bundle.getString("aal.services"));
		center();
		setWidth("950px");
		setHeight("750px");
	}

	public void fillGrid(GridLayout grid, String searchstring) {
		int y = 1;
		int x = 1;
		buttons = new HashMap<String, Button>();
		popups = new ArrayList<PopupService>();
		// Path to the xml-file including the services
		File xmlsrc = new File(new File(Activator.getConfigHome(), "service-xml"), "sample.xml");

		// generate the service-list
		Parser read = new Parser();
		List<Service> readServices = read.readServices(xmlsrc, searchstring);
		PopupService popup = null;
		// built the grid with all services
		for (int i = 0; i < readServices.size();) {
			Service services = readServices.get(i);

			ThemeResource tr = new ThemeResource("img/" + services.getImage());
			grid.setRows(y);

			Label title = new Label("<h2>" + services.getTitle() + "<h2>");
			title.setContentMode(Label.CONTENT_XHTML);
			Label subtitle = new Label("<b>" + services.getSubtitle() + "</b>");
			subtitle.setContentMode(Label.CONTENT_XHTML);
			Label category = new Label("<i>" + services.getCategory() + "</i>");
			category.setContentMode(Label.CONTENT_XHTML);
			Label rating = new Label(services.getRating());
			Button install = new Button(bundle.getString("install.button"));
			install.setEnabled(false);
			Button img = new Button();
			img.setIcon(tr);
			img.setImmediate(true);
			buttons.put(services.getTitle(), img);
			VerticalLayout vla = new VerticalLayout();
			vla.setStyleName("menubutton");
			vla.addComponent(title);
			vla.addComponent(img);
			vla.addComponent(subtitle);
			vla.addComponent(category);
			vla.addComponent(rating);
			VerticalLayout vlb = new VerticalLayout();
			vlb.addComponent(install);

			final VerticalLayout vl = new VerticalLayout();
			vl.addComponent(vla);
			vl.addComponent(vlb);
			vl.setExpandRatio(vla, 4);
			vl.setExpandRatio(vlb, 1);

			vl.setWidth("180");
			vl.setHeight("250");
			vla.setExpandRatio(title, 1);
			vla.setExpandRatio(img, 2.5f);
			vla.setExpandRatio(subtitle, 0.5f);
			vla.setExpandRatio(category, 0.5f);
			vla.setExpandRatio(rating, 0.5f);

			vl.setDescription("<b>" + title + "</b>" + services.getDetails().substring(0, 100) + "...");

			popup = new PopupService();
			popup.setTitle(services.getTitle());
			popup.setImg(services.getImage());
			popup.setCategory(services.getCategory());
			popup.setSubtitle(services.getSubtitle());
			popup.setRating(services.getRating());
			popup.setDetails(services.getDetails());
			popups.add(popup);

			grid.addComponent(vl);

			i++;
			x++;

			if (i % 5 == 0) {
				y++;
				x = 1;
			}
		}

	}

	public GridLayout getGrid() {
		return grid;
	}

	public TextField getSearchfield() {
		return searchfield;
	}

	public HashMap<String, Button> getButtons() {
		return buttons;
	}

	public void setButtons(HashMap<String, Button> buttons) {
		this.buttons = buttons;
	}

	public List<PopupService> getPopups() {
		return popups;
	}

	public void setPopups(List<PopupService> popups) {
		this.popups = popups;
	}

	public Button getSearchbutton() {
		return searchbutton;
	}

	public void setSearchbutton(Button searchbutton) {
		this.searchbutton = searchbutton;
	}

	public Button getBack() {
		return back;
	}

	public void setBack(Button back) {
		this.back = back;
	}
}
