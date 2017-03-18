package org.universAAL.ucc.windows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.universAAL.ucc.controller.desktop.DesktopController;
import org.universAAL.ucc.service.manager.Activator;
import org.universAAL.ucc.startup.api.Setup;
import org.universAAL.ucc.startup.model.Role;
import org.universAAL.ucc.startup.model.UserAccountInfo;

import com.vaadin.Application;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.Reindeer;

public class UccUI extends Application {
	private static final long serialVersionUID = 1L;
	private Window mainWindow;
	private Button startButton;
	private Button searchButton;
	private Button adminButton;
	private Button bell;
	private ToolWindow startWindow;
	private VerticalSplitPanel vs;
	private VerticalLayout vLog;
	private static Window loginWindow;
	private Button login;
	private DesktopController desk;
	private String basename;
	private ResourceBundle res;
	private static UccUI uccUI;
	private Button link;
	private TextField user;
	private PasswordField pwd;
	private BundleContext context;

	public static UccUI getInstance() {
		return uccUI;
	}

	@Override
	public void init() {
		setTheme("editortheme");
		uccUI = this;
		Properties prop = new Properties();
		Reader reader = null;
		try {
			reader = new FileReader(Activator.getModuleConfigHome().getAbsolutePath()+"/setup/setup.properties");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			prop.load(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String lang = prop.getProperty("lang");
		System.out.println("---------------------------------> LANGUAGE  " +lang);
		if(lang != null && !lang.equals("")) {
			if(lang.equals("de")) {
				Locale.setDefault(Locale.GERMAN);
			} else {
				Locale.setDefault(Locale.ENGLISH);
			}
		} else {
			Locale.setDefault(Locale.ENGLISH);
		}
		// Creating tempUsrvFiles directory
		File file = new File(Activator.getModuleConfigHome().getAbsolutePath() + "/tempUsrvFiles");
		if (!file.exists()) {
			file.mkdir();
			System.err.println("tempUsrvFiles was created!");
		}
		basename = "resources.ucc";
		res = ResourceBundle.getBundle(basename);
		mainWindow = new Window("uCC");
		mainWindow.setSizeFull();
		setMainWindow(mainWindow);
		createLogin();
	}

	public VerticalSplitPanel createContent(DesktopController desk) {
		vs = new VerticalSplitPanel();
		vs.setSizeFull();
		vs.setSplitPosition(45, Sizeable.UNITS_PIXELS);
		vs.setLocked(true);
		vs.setStyleName(Reindeer.SPLITPANEL_SMALL);
		HorizontalLayout mainLayout = new HorizontalLayout();
		mainLayout.setSizeFull();
		mainLayout.setStyleName("mainoverview");

		startButton = new Button();
		startButton.setDescription(res.getString("start.desc"));
		VerticalLayout startLayout = new VerticalLayout();
		startLayout.setStyleName("buttonstyle");
		startButton.setIcon(new ThemeResource("img/Zahnraeder-01-01.png"));
		startLayout.addComponent(startButton);
		startLayout.setComponentAlignment(startButton, Alignment.TOP_LEFT);
		
		HorizontalLayout uhl = new HorizontalLayout();
		uhl.setWidth("100%");
		uhl.setStyleName("uAAL");
		uhl.setSpacing(true);
		uhl.setMargin(true);
		Label uAAL = new Label("universAAL Control Center", Label.CONTENT_XHTML);
		uhl.addComponent(uAAL);
		uhl.setComponentAlignment(uAAL, Alignment.MIDDLE_RIGHT);
		mainLayout.setSpacing(true);
		vs.setFirstComponent(mainLayout);

		HorizontalLayout hl = new HorizontalLayout();
		hl.setStyleName("buttonstyle");
		bell = new Button();
		bell.setDescription(res.getString("notification.desc"));
		bell.setIcon(new ThemeResource("img/Bell-01-01.png"));
		hl.addComponent(bell);
		adminButton = new Button();
		adminButton.setDescription(res.getString("options.desc"));
		adminButton.setIcon(new ThemeResource("img/Person black-01-01.png"));
		searchButton = new Button();
		searchButton.setDescription(res.getString("search.desc"));
		searchButton.setIcon(new ThemeResource("img/Lupe-01-01.png"));
		hl.addComponent(adminButton);
		hl.addComponent(searchButton);
		mainLayout.addComponent(startLayout);
		mainLayout.setComponentAlignment(startLayout, Alignment.TOP_LEFT);
		mainLayout.addComponent(uhl);
		mainLayout.setComponentAlignment(uhl, Alignment.MIDDLE_CENTER);
		mainLayout.addComponent(hl);
		mainLayout.setComponentAlignment(hl, Alignment.TOP_RIGHT);
		startButton.addListener(desk);
		searchButton.addListener(desk);
		adminButton.addListener(desk);
		return vs;
	}

	public Window createLogin() {
		context = FrameworkUtil.getBundle(getClass()).getBundleContext();
		ServiceReference ref = context.getServiceReference(Setup.class.getName());
		Setup su = (Setup) context.getService(ref);
		context.ungetService(ref);
		desk = new DesktopController(this);
		vLog = new VerticalLayout();
		vLog.setSizeFull();
		mainWindow.setContent(vLog);
		loginWindow = new Window();
		loginWindow.setStyleName(Reindeer.WINDOW_LIGHT);
		loginWindow.setClosable(false);
		loginWindow.setDraggable(false);
		loginWindow.center();
		loginWindow.setResizable(false);
		VerticalLayout vm = new VerticalLayout();
		vm.setSpacing(true);
		vm.setMargin(true);
		vm.setSizeFull();
		Embedded uaal = new Embedded("", new ThemeResource(
				"img/Konfiguration72.png"));
		vm.addComponent(uaal);
		Label wel = new Label(res.getString("welcome.label"),
				Label.CONTENT_XHTML);
		vm.addComponent(wel);
		HorizontalLayout hl = new HorizontalLayout();
		hl.setMargin(true);
		hl.setSpacing(true);
		user = new TextField(res.getString("user.label"));
		hl.addComponent(user);
		pwd = new PasswordField(res.getString("pwd.label"));
		hl.addComponent(pwd);
		List<UserAccountInfo> cu = su.getUsers(Activator.getModuleConfigHome().getAbsolutePath()+"/user/users.xml");
		if(!cu.isEmpty()) {
		for(UserAccountInfo u : cu) {
			if(u.isChecked() && (u.getRole().contains(Role.ENDUSER) || u.getRole().contains(Role.ASSISTEDPERSON))) {
				user.setValue(u.getName());
				pwd.setValue(u.getPassword());
			}
		}
		} 
//		else {
//			getMainWindow().showNotification(res.getString("create.account"), Notification.TYPE_HUMANIZED_MESSAGE);
//		}
		login = new Button(res.getString("login.label"));
		hl.addComponent(login);
		hl.setComponentAlignment(login, Alignment.BOTTOM_RIGHT);
//		link = new Button(res.getString("create.account"));
//		link.addListener(desk);
//		hl.addComponent(link);
//		hl.setComponentAlignment(link, Alignment.BOTTOM_RIGHT);
		login.addListener(desk);
		vm.addComponent(hl);
		loginWindow.setWidth("425px");
		loginWindow.setHeight("250px");
		loginWindow.setContent(vm);
		mainWindow.addWindow(loginWindow);
		return loginWindow;
	}

	public Button getLink() {
		return link;
	}

	public void setLink(Button link) {
		this.link = link;
	}

	public Button getStartButton() {
		return startButton;
	}

	public void setStartButton(Button startButton) {
		this.startButton = startButton;
	}

	public ToolWindow getStartWindow() {
		return startWindow;
	}

	public void setStartWindow(ToolWindow startWin) {
		this.startWindow = startWin;
	}

	public Button getSearchButton() {
		return searchButton;
	}

	public void setSearchButton(Button search) {
		this.searchButton = search;
	}

	public Button getLogin() {
		return login;
	}

	public void setLogin(Button b) {
		this.login = b;
	}

	public Window getLoginWindow() {
		return loginWindow;
	}

	public VerticalLayout getVLog() {
		return vLog;
	}

	public void setVLog(VerticalLayout vlog) {
		this.vLog = vlog;
	}

	public VerticalSplitPanel getVs() {
		return vs;
	}

	public void setVs(VerticalSplitPanel vs) {
		this.vs = vs;
	}

	public Button getAdminButton() {
		return adminButton;
	}

	public void setAdminButton(Button adminButton) {
		this.adminButton = adminButton;
	}

	public Button getBell() {
		return bell;
	}

	public void setBell(Button bell) {
		this.bell = bell;
	}

	public TextField getUser() {
		return user;
	}

	public void setUser(TextField user) {
		this.user = user;
	}

	public PasswordField getPwd() {
		return pwd;
	}

	public void setPwd(PasswordField pwd) {
		this.pwd = pwd;
	}
	
	

}
