package org.universAAL.tools.ucc.controller.desktop;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.universAAL.tools.ucc.service.manager.Activator;
import org.universAAL.tools.ucc.startup.api.Setup;
import org.universAAL.tools.ucc.startup.model.Role;
import org.universAAL.tools.ucc.startup.model.UserAccountInfo;
import org.universAAL.tools.ucc.windows.AccountWindow;
import org.universAAL.tools.ucc.windows.UccUI;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.Notification;

/**
 * Controller for AccountWindow.
 *
 * @author Nicole Merkle
 *
 */

public class AccountWindowController implements Button.ClickListener {
	private UccUI app;
	private AccountWindow win;
	private ResourceBundle bundle;
	private String base;
	private BundleContext context;
	private Setup setup;

	public AccountWindowController(AccountWindow win, UccUI app) {
		base = "resources.ucc";
		bundle = ResourceBundle.getBundle(base);
		this.app = app;
		this.win = win;
		win.getSave().addListener(this);
		win.getReset().addListener(this);
		context = Activator.bc;// FrameworkUtil.getBundle(getClass()).getBundleContext();
		ServiceReference ref = context.getServiceReference(Setup.class.getName());
		setup = (Setup) context.getService(ref);
		context.ungetService(ref);
	}

	public void buttonClick(ClickEvent event) {
		if (event.getButton() == win.getSave()) {
			if (win.getUser().getValue().equals("") || win.getPwd().getValue().equals("")
					|| win.getConfirm().getValue().equals("")) {
				app.getMainWindow().showNotification(bundle.getString("input.empty"),
						Notification.TYPE_HUMANIZED_MESSAGE);
			} else {
				boolean inDB = false;
				for (UserAccountInfo us : setup.getUsers()) {
					if (us.getName().equals(win.getUser().getValue())) {
						inDB = true;
					}
				}
				if (!inDB && win.getPwd().getValue().equals(win.getConfirm().getValue())) {
					UserAccountInfo user = new UserAccountInfo();
					user.setName(win.getUser().getValue().toString());
					user.setPassword(win.getPwd().getValue().toString());
					List<Role> roles = new ArrayList<Role>();
					roles.add(Role.ENDUSER);
					user.setRole(roles);
					user.setChecked(win.getCheck().booleanValue());
					setup.saveUser(user);
					if (win.getCheck().getValue().equals(true)) {
						app.getUser().setValue(win.getUser().getValue());
						app.getPwd().setValue(win.getPwd().getValue());
					}

					app.getMainWindow().removeWindow(win);
				} else {
					app.getMainWindow().showNotification(bundle.getString("confirmation.fail"),
							Notification.TYPE_HUMANIZED_MESSAGE);
					win.getPwd().setValue("");
					win.getConfirm().setValue("");
				}
			}
		}
		if (event.getButton() == win.getReset()) {
			win.getUser().setValue("");
			win.getPwd().setValue("");
			win.getConfirm().setValue("");
			win.getCheck().setValue(false);
		}
	}
}
