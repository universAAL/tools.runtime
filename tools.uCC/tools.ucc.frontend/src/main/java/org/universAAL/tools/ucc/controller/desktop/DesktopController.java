package org.universAAL.tools.ucc.controller.desktop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.universAAL.tools.ucc.client.util.UstoreUtil;
import org.universAAL.tools.ucc.database.space.DataAccess;
import org.universAAL.tools.ucc.model.jaxb.EnumObject;
import org.universAAL.tools.ucc.model.jaxb.OntologyInstance;
import org.universAAL.tools.ucc.model.jaxb.SimpleObject;
import org.universAAL.tools.ucc.model.jaxb.StringValue;
import org.universAAL.tools.ucc.model.jaxb.Subprofile;
import org.universAAL.tools.ucc.model.preferences.Preferences;
import org.universAAL.tools.ucc.service.manager.Activator;
import org.universAAL.tools.ucc.startup.api.Setup;
import org.universAAL.tools.ucc.startup.model.UserAccountInfo;
import org.universAAL.tools.ucc.windows.PreferencesWindow;
import org.universAAL.tools.ucc.windows.SearchWindow;
import org.universAAL.tools.ucc.windows.ToolWindow;
import org.universAAL.tools.ucc.windows.UccUI;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

/**
 * Controlls the whole rendering of the uCC.
 *
 * @author Nicole Merkle
 *
 */

public class DesktopController implements Button.ClickListener {
	private UccUI app;
	private Window main;
	private String base;
	private ResourceBundle bundle;
	private Setup setup;
	private BundleContext bc;
	private String user;
	private String pwd;
	private static boolean admin;
	private static String currentUser;
	private static String currentPassword;
	// public static WebConnector web;

	public DesktopController(UccUI app) {
		base = "resources.ucc";
		bundle = ResourceBundle.getBundle(base);
		this.app = app;
		this.main = app.getMainWindow();
		currentUser = "";
		currentPassword = "";
		// new UstoreUtil();
		Properties prop = new Properties();
		Reader reader = null;
		try {
			reader = new FileReader(Activator.getSetupProps());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			prop.load(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}
		user = prop.getProperty("admin");
		pwd = prop.getProperty("pwd");
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		bc = Activator.bc;// FrameworkUtil.getBundle(getClass()).getBundleContext();
		ServiceReference refer = bc.getServiceReference(Setup.class.getName());
		setup = (Setup) bc.getService(refer);
		bc.ungetService(refer);
	}

	public void buttonClick(ClickEvent event) {
		if (event.getButton() == app.getStartButton()) {
			if (main.getChildWindows().size() > 0) {
				for (Window w : main.getChildWindows()) {
					if (w instanceof ToolWindow) {
						main.removeWindow(w);
					} else {
						ToolWindow startWindow = new ToolWindow(app);
						if (admin) {
							startWindow.getuStoreButton().setEnabled(false);
							startWindow.getOpenAAL().setEnabled(false);
							startWindow.getConfigButton().setEnabled(true);
							startWindow.getEditHW().setEnabled(true);
							startWindow.getEditPerson().setEnabled(true);
							startWindow.getEditUC().setEnabled(true);
							startWindow.getPersonButton().setEnabled(true);
							startWindow.getInstallButton().setEnabled(true);
						} else {
							startWindow.getuStoreButton().setEnabled(true);
							startWindow.getOpenAAL().setEnabled(false);
							startWindow.getConfigButton().setEnabled(false);
							startWindow.getEditHW().setEnabled(false);
							startWindow.getEditPerson().setEnabled(false);
							startWindow.getEditUC().setEnabled(true);
							startWindow.getPersonButton().setEnabled(false);
							startWindow.getInstallButton().setEnabled(true);
						}
						main.addWindow(startWindow);
					}
				}
			} else {
				ToolWindow startWin = new ToolWindow(app);
				if (admin) {
					startWin.getuStoreButton().setEnabled(false);
					startWin.getOpenAAL().setEnabled(false);
					startWin.getConfigButton().setEnabled(true);
					startWin.getEditHW().setEnabled(true);
					startWin.getEditPerson().setEnabled(true);
					startWin.getEditUC().setEnabled(true);
					startWin.getPersonButton().setEnabled(true);
					startWin.getInstallButton().setEnabled(true);
				} else {
					startWin.getuStoreButton().setEnabled(true);
					startWin.getOpenAAL().setEnabled(true);
					startWin.getConfigButton().setEnabled(false);
					startWin.getEditHW().setEnabled(false);
					startWin.getEditPerson().setEnabled(false);
					startWin.getEditUC().setEnabled(true);
					startWin.getPersonButton().setEnabled(false);
					startWin.getInstallButton().setEnabled(true);
				}
				main.addWindow(startWin);
			}
		}
		if (event.getButton() == app.getSearchButton()) {
			SearchWindow sWin = new SearchWindow(app);
			main.addWindow(sWin);
		}
		if (event.getButton() == app.getLogin()) {
			if (app.getUser().getValue().equals("") || app.getPwd().getValue().equals("")) {
				app.getMainWindow().showNotification(bundle.getString("input.empty"));
			} else {
				if (app.getUser().getValue().equals(user) && app.getPwd().getValue().equals(pwd)) {
					adminLogin();
				} else {
					boolean in = false;
					// Later comment out, only CHE is to be used
					List<UserAccountInfo> users = setup.getUsers();
					if (users.size() <= 1) {
						// AAL Space test
						DataAccess da = Activator.getDataAccess();
						ArrayList<OntologyInstance> ontList = da.getEmptyCHEFormFields("User");
						String uname = "";
						String pw = "";
						for (OntologyInstance o : ontList) {

							for (Subprofile s : o.getSubprofiles()) {
								for (SimpleObject sim : s.getSimpleObjects()) {
									StringValue st = (StringValue) sim;
									if (st.getName().equals("username")) {
										uname = st.getValue();
									}
									if (st.getName().equals("password")) {
										pw = st.getValue();
									}
									if (!uname.equals("") && !pw.equals("")) {
										if (uname.equals(app.getUser().getValue())
												&& pw.equals(app.getPwd().getValue())) {
											userLogin();
											currentUser = app.getUser().getValue().toString();
											currentPassword = app.getPwd().getValue().toString();
											in = true;
										}
									}
								}
							}
						}
					} // Later comment out, only CHE is to be used
					else {
						for (UserAccountInfo u : users) {
							if (u.getName().equals(app.getUser().getValue())
									&& u.getPassword().equals(app.getPwd().getValue())) {
								userLogin();
								currentUser = app.getUser().getValue().toString();
								currentPassword = app.getPwd().getValue().toString();
								in = true;
							}
						}
					}
					if (!in) {
						app.getMainWindow().showNotification(bundle.getString("login.account.fail"),
								Notification.TYPE_ERROR_MESSAGE);
						app.getUser().setValue("");
						app.getPwd().setValue("");
					}
				}
			}

		}
		if (event.getButton() == app.getAdminButton()) {
			Preferences pref = new Preferences();
			Properties prop = new Properties();
			Reader reader = null;
			try {
				reader = new FileReader(Activator.getSetupProps());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				prop.load(reader);
			} catch (IOException e) {
				e.printStackTrace();
			}
			pref.setAdmin(prop.getProperty("admin"));
			pref.setPwd(prop.getProperty("pwd"));
			pref.setShopIp(prop.getProperty("shopUrl"));
			pref.setUccIp(prop.getProperty("uccUrl"));
			pref.setUccPort(prop.getProperty("uccPort"));
			// pref.setWsPort(prop.getProperty("storePort"));
			pref.setLanguage(prop.getProperty("lang"));
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			PreferencesWindow p = new PreferencesWindow(app, pref);
			app.getMainWindow().addWindow(p);

		}

		// if(event.getButton() == app.getLink()) {
		// AccountWindow aw = new AccountWindow();
		// new AccountWindowController(aw, app);
		// app.getMainWindow().addWindow(aw);
		// }

	}

	private void adminLogin() {
		admin = true;
		main.removeWindow(app.getLoginWindow());
		main.removeComponent(app.getVLog());
		main.setContent(app.createContent(this));
		app.getMainWindow().showNotification(bundle.getString("login.success"), Notification.TYPE_HUMANIZED_MESSAGE);
		// web = WebConnector.getInstance();
		// web.startListening();

	}

	private void userLogin() {
		admin = false;
		main.removeWindow(app.getLoginWindow());
		main.removeComponent(app.getVLog());
		main.setContent(app.createContent(this));
		app.getMainWindow().showNotification(bundle.getString("login.success"), Notification.TYPE_HUMANIZED_MESSAGE);
		// web = WebConnector.getInstance();
		// web.startListening();

	}

	public static String getCurrentUser() {
		return currentUser;
	}

	public static String getCurrentPassword() {
		return currentPassword;
	}

	public static void setCurrentUser(String currentUser) {
		DesktopController.currentUser = currentUser;
	}

	public static void setCurrentPassword(String currentPassword) {
		DesktopController.currentPassword = currentPassword;
	}

}
