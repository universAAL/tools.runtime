package org.universAAL.tools.ucc.controller.ustore.services;

import java.util.Map;

import org.universAAL.tools.ucc.windows.BrowseServicesWindow;
import org.universAAL.tools.ucc.windows.ServicePopupWindodw;
import org.universAAL.tools.ucc.windows.UccUI;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class PurchasedServicesController implements Button.ClickListener {
	private BrowseServicesWindow win;
	private UccUI app;

	public PurchasedServicesController(BrowseServicesWindow win, UccUI app) {
		this.win = win;
		this.app = app;
		win.getSearchbutton().addListener(this);
		win.getBack().addListener(this);
		for (Map.Entry<String, Button> b : win.getButtons().entrySet()) {
			Button temp = (Button) b.getValue();
			temp.addListener(this);
		}
	}

	public void buttonClick(ClickEvent event) {
		if (win.getSearchbutton() == event.getButton()) {
			win.getGrid().removeAllComponents();
			win.fillGrid(win.getGrid(), win.getSearchfield().getValue().toString());
			win.getBack().setEnabled(true);
			for (Map.Entry<String, Button> t : win.getButtons().entrySet()) {
				Button temp = (Button) t.getValue();
				temp.addListener(this);
			}
		} else if (win.getBack() == event.getButton()) {
			win.getGrid().removeAllComponents();
			win.fillGrid(win.getGrid(), "");
			win.getBack().setEnabled(false);

		} else {

			for (Map.Entry<String, Button> bt : win.getButtons().entrySet()) {
				Button temp = (Button) bt.getValue();
				if (temp == event.getButton()) {
					ServicePopupWindodw popWin = new ServicePopupWindodw(bt.getKey().toString(), win.getPopups());
					app.getMainWindow().addWindow(popWin);
				}
			}
		}
	}

}
