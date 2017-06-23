package org.universAAL.tools.ucc.controller.install;

import java.util.ResourceBundle;

import org.universAAL.tools.ucc.frontend.api.IFrontend;
import org.universAAL.tools.ucc.frontend.api.impl.FrontendImpl;
import org.universAAL.tools.ucc.model.AppItem;
import org.universAAL.tools.ucc.model.RegisteredService;
import org.universAAL.tools.ucc.service.manager.Activator;
import org.universAAL.tools.ucc.windows.DeinstallWindow;
import org.universAAL.tools.ucc.windows.UccUI;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.Notification;

public class DeinstallController implements Button.ClickListener {
	private DeinstallWindow win;
	private UccUI app;
	private IFrontend front;
	private String base;
	private ResourceBundle bundle;

	public DeinstallController(DeinstallWindow window, UccUI app) {
		base = "resources.ucc";
		bundle = ResourceBundle.getBundle(base);
		this.win = window;
		this.app = app;
		this.win.getDel().addListener(this);
		this.win.getCancel().addListener(this);
		front = new FrontendImpl();
	}

	public void buttonClick(ClickEvent event) {
		if (win.getDel() == event.getButton()) {
			if (win.getList().getValue() != null) {
				// RegisteredService srv = (RegisteredService)
				// win.getList().getValue();
				AppItem part = (AppItem) win.getList().getValue();
				app.getMainWindow().removeWindow(win);
				front.uninstallService(Activator.getSessionKey(), part.getServiceId());

			} else {
				app.getMainWindow().showNotification("", bundle.getString("select.usrv"),
						Notification.TYPE_HUMANIZED_MESSAGE);
			}
		}

		if (win.getCancel() == event.getButton()) {
			app.getMainWindow().removeWindow(win);

		}

	}

}
