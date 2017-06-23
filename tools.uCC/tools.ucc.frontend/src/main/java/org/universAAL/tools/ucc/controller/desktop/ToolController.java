package org.universAAL.tools.ucc.controller.desktop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBException;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.universAAL.tools.ucc.configuration.view.WhichBundleShouldBeConfiguredWindow;
import org.universAAL.tools.ucc.controller.install.AALServiceReceiver;
import org.universAAL.tools.ucc.controller.install.DeinstallController;
import org.universAAL.tools.ucc.controller.ustore.services.PurchasedServicesController;
import org.universAAL.tools.ucc.frontend.api.IFrontend;
import org.universAAL.tools.ucc.frontend.api.impl.FrontendImpl;
import org.universAAL.tools.ucc.model.RegisteredService;
import org.universAAL.tools.ucc.model.preferences.Preferences;
import org.universAAL.tools.ucc.service.impl.Model;
import org.universAAL.tools.ucc.service.manager.Activator;
import org.universAAL.tools.ucc.windows.AddNewHardwareWindow;
import org.universAAL.tools.ucc.windows.AddNewPersonWindow;
import org.universAAL.tools.ucc.windows.BrowseServicesWindow;
import org.universAAL.tools.ucc.windows.DeinstallWindow;
import org.universAAL.tools.ucc.windows.HumansWindow;
import org.universAAL.tools.ucc.windows.RoomsWindow;
import org.universAAL.tools.ucc.windows.ToolWindow;
import org.universAAL.tools.ucc.windows.UccUI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

/**
 * Controlls the ToolWindow.
 * 
 * @author Nicole Merkle
 *
 */

public class ToolController implements Button.ClickListener, Upload.FinishedListener, Upload.FailedListener {
	private UccUI app;
	private ToolWindow toolWin;
	private Window installWindow;
	private String base;
	private ResourceBundle res;
	private ServiceReference ref;
	private BundleContext bc;
	private IFrontend frontend;

	public ToolController(UccUI app, ToolWindow toolWin) {
		this.app = app;
		this.toolWin = toolWin;
		base = "resources.ucc";
		res = ResourceBundle.getBundle(base);
		File f = Activator.getTempUsrvFiles();
		if (!f.exists()) {
			f.mkdir();
		}
		frontend = new FrontendImpl();
		bc = Activator.bc;// FrameworkUtil.getBundle(getClass()).getBundleContext();
	}

	public void buttonClick(ClickEvent event) {
		if (event.getButton() == toolWin.getuStoreButton()) {
			Embedded em = new Embedded("", new ExternalResource(createLink()));
			em.setType(Embedded.TYPE_BROWSER);
			em.setWidth("100%");
			em.setHeight("850px");
			Window w = new Window("uStore");
			w.setWidth("1250px");
			w.setHeight("800px");
			VerticalLayout v = new VerticalLayout();
			w.center();
			v.addComponent(em);
			w.setContent(v);
			app.getMainWindow().removeWindow(toolWin);
			app.getMainWindow().addWindow(w);
		}
		if (event.getButton() == toolWin.getOpenAAL()) {
			// Embedded em = new Embedded("", new ExternalResource(
			// "http://wiki.openaal.org"));
			// em.setType(Embedded.TYPE_BROWSER);
			// em.setWidth("100%");
			// em.setHeight("800px");
			// Window w = new Window("openAAL");
			// w.setWidth("1250px");
			// w.setHeight("800px");
			// VerticalLayout v = new VerticalLayout();
			// w.center();
			// v.addComponent(em);
			// w.setContent(v);
			BrowseServicesWindow pw = new BrowseServicesWindow(app);
			PurchasedServicesController pc = new PurchasedServicesController(pw, app);
			app.getMainWindow().removeWindow(toolWin);
			app.getMainWindow().addWindow(pw);
		}
		if (event.getButton() == toolWin.getInstallButton()) {
			// Later uncomment again only for testing commented out!
			Upload up = new Upload("", new AALServiceReceiver());
			up.setButtonCaption(res.getString("install.button"));
			up.addListener((Upload.FinishedListener) this);
			up.addListener((Upload.FailedListener) this);
			installWindow = new Window(res.getString("install.win.caption"));
			installWindow.setResizable(false);
			installWindow.center();
			installWindow.setWidth("400px");
			VerticalLayout v = new VerticalLayout();
			v.setSizeFull();
			v.setSpacing(true);
			v.setMargin(true);
			v.addComponent(up);
			installWindow.setContent(v);

			app.getMainWindow().removeWindow(toolWin);
			app.getMainWindow().addWindow(installWindow);
		}
		if (event.getButton() == toolWin.getLogoutButton()) {
			DesktopController.setCurrentPassword("");
			DesktopController.setCurrentUser("");
			// if(!DesktopController.web.getSocket().isClosed()) {
			// try {
			// DesktopController.web.getSocket().close();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			// }
			app.close();
		}

		if (event.getButton() == toolWin.getUninstallButton()) {
			app.getMainWindow().removeWindow(toolWin);
			List<RegisteredService> ids = new ArrayList<RegisteredService>();
			Document doc = Model.getSrvDocument();
			NodeList nodeList = doc.getElementsByTagName("service");
			for (int i = 0; i < nodeList.getLength(); i++) {
				RegisteredService srv = new RegisteredService();
				Element element = (Element) nodeList.item(i);
				System.err.println(element.getAttribute("serviceId"));
				srv.setServiceId(element.getAttribute("serviceId"));
				NodeList srvChilds = element.getChildNodes();
				for (int j = 0; j < srvChilds.getLength(); j++) {
					Node n = srvChilds.item(j);
					if (n.getNodeName().equals("application")) {
						Element e = (Element) n;
						srv.getAppId().add(e.getAttribute("appId"));
					}
					if (n.getNodeName().equals("bundle")) {
						Element b = (Element) n;
						srv.getBundleId().add(b.getAttribute("id"));
						srv.setBundleVersion(b.getAttribute("version"));
					}
					if (n.getNodeName().equals("menuEntry")) {
						Element e = (Element) n;
						srv.setMenuName(e.getAttribute("entryName"));
						srv.setIconURL(e.getAttribute("iconURL"));
						srv.setProvider(e.getAttribute("vendor"));
						srv.setServiceClass(e.getAttribute("serviceClass"));
						srv.setUserID(e.getAttribute("userID"));
					}
				}
				ids.add(srv);
			}
			DeinstallWindow dw = new DeinstallWindow(ids);
			app.getMainWindow().addWindow(dw);
			DeinstallController dc = new DeinstallController(dw, app);
			// frontend.uninstallService(Activator.getSessionKey(), "28002");
			// frontend.getInstalledUnitsForService(Activator.getSessionKey(),
			// "28002");
		}

		if (event.getButton() == toolWin.getPersonButton()) {
			AddNewPersonWindow apw = null;
			try {
				apw = new AddNewPersonWindow(null, null, app);
			} catch (JAXBException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			app.getMainWindow().removeWindow(toolWin);
			app.getMainWindow().addWindow(apw);
		}
		if (event.getButton() == toolWin.getConfigButton()) {
			AddNewHardwareWindow anhw = null;
			try {
				anhw = new AddNewHardwareWindow(null, null, app);
			} catch (JAXBException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			app.getMainWindow().removeWindow(toolWin);
			app.getMainWindow().addWindow(anhw);
		}
		if (event.getButton() == toolWin.getEditHW()) {
			RoomsWindow hardWare = null;
			try {
				hardWare = new RoomsWindow(app);
			} catch (JAXBException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			app.getMainWindow().removeWindow(toolWin);
			app.getMainWindow().addWindow(hardWare);

		}

		if (event.getButton() == toolWin.getEditPerson()) {
			HumansWindow hw = null;
			try {
				hw = new HumansWindow(app);
			} catch (JAXBException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			app.getMainWindow().removeWindow(toolWin);
			app.getMainWindow().addWindow(hw);
		}
		if (event.getButton() == toolWin.getEditUC()) {
			WhichBundleShouldBeConfiguredWindow uc = new WhichBundleShouldBeConfiguredWindow("Use Cases");
			app.getMainWindow().removeWindow(toolWin);
			app.getMainWindow().addWindow(uc);

		}

	}

	private String createLink() {
		String url = "";
		String shop = "";
		Preferences pref = new Preferences();
		Properties prop = new Properties();
		Reader reader = null;
		try {
			reader = new FileReader(Activator.getSetupProps());
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			prop.load(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}
		pref.setAdmin(prop.getProperty("admin"));
		pref.setLanguage(prop.getProperty("lang"));
		pref.setPwd(prop.getProperty("pwd"));
		pref.setShopIp(prop.getProperty("shopUrl"));
		pref.setUccIp(prop.getProperty("uccUrl"));
		pref.setUccPort(prop.getProperty("uccPort"));
		// pref.setWsPort(prop.getProperty("storePort"));
		shop = pref.getShopIp();
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (pref.getShopIp().contains("https")) {
			shop = pref.getShopIp().substring(pref.getShopIp().lastIndexOf("//") + 2);
		} else if (pref.getShopIp().contains("http")) {
			shop = pref.getShopIp().substring(pref.getShopIp().lastIndexOf("//") + 1);
		}
		if (!DesktopController.getCurrentUser().equals("") && !DesktopController.getCurrentPassword().equals("")) {
			url = "https://" + DesktopController.getCurrentUser() + ":" + DesktopController.getCurrentPassword() + "@"
					+ shop;
		} else {
			url = "http://" + shop;
		}
		System.err.println(url);
		return url;
	}

	public void uploadFailed(FailedEvent event) {
		app.getMainWindow().removeWindow(installWindow);
		app.getMainWindow().showNotification(res.getString("break.note"), Notification.TYPE_ERROR_MESSAGE);

	}

	public void uploadFinished(FinishedEvent event) {
		try {
			app.getMainWindow().removeWindow(installWindow);
			String f = event.getFilename();
			String file = f.substring(0, f.lastIndexOf("."));

			System.err.println("The local file: " + file);
			frontend.installService(Activator.getSessionKey(), file, "");
			// iw.installProcess(System.getenv("systemdrive")+"/tempUsrvFiles/");
			// File licenceFile = new
			// File(System.getenv("systemdrive")+"/"+dir+"/config/hwo.usrv.xml");
			// DocumentBuilderFactory fact =
			// DocumentBuilderFactory.newInstance();
			// File l = null;
			// LicenceWindow lw = null;
			// String txt = "";
			// String appName = "";
			// String slaName = "";
			// License license = null;
			// ArrayList<License> licenseList = new ArrayList<License>();
			// ArrayList<File> list = new ArrayList<File>();
			// AALService aal = null;
			// try {
			// DocumentBuilder builder = fact.newDocumentBuilder();
			// Document doc = builder.parse(licenceFile);
			// for(int k = 0; k <
			// doc.getElementsByTagName("usrv:srv").getLength();
			// k++) {
			// aal = new AALService();
			// for(int ac = 0; ac <
			// doc.getElementsByTagName("usrv:application").getLength(); ac++) {
			// UAPP uap = new UAPP();
			// Node node =
			// (Node)doc.getElementsByTagName("usrv:application").item(ac);
			// NodeList nodeList = node.getChildNodes();
			// for(int b = 0; b < node.getChildNodes().getLength(); b++) {
			//
			// if(nodeList.item(b).getNodeName().equals("usrv:artifactID")) {
			// uap.setServiceId(nodeList.item(b).getTextContent());
			// System.err.println(uap.getServiceId());
			// }
			// if(nodeList.item(b).getNodeName().equals("usrv:location")) {
			// uap.setUappLocation(nodeList.item(b).getTextContent());
			// }
			// if(nodeList.item(b).getNodeName().equals("usrv:name")) {
			// uap.setName(nodeList.item(b).getTextContent());
			// System.err.println(uap.getName());
			// }
			//
			// }
			// aal.getUaapList().add(uap);
			// }
			// aal.setName(doc.getElementsByTagName("usrv:name").item(0).getTextContent());
			// aal.setProvider(doc.getElementsByTagName("usrv:serviceProvider").item(0).getTextContent());
			// aal.setDescription(doc.getElementsByTagName("usrv:description").item(0).getTextContent());
			// aal.setMajor(Integer.parseInt(doc.getElementsByTagName("usrv:major").item(0).getTextContent()));
			// aal.setMinor(Integer.parseInt(doc.getElementsByTagName("usrv:minor").item(0).getTextContent()));
			// aal.setMicro(Integer.parseInt(doc.getElementsByTagName("usrv:micro").item(0).getTextContent()));
			// String h =
			// doc.getElementsByTagName("usrv:tags").item(0).getTextContent();
			// for(String t : h.split(",")) {
			// aal.getTags().add(t);
			// }
			// license = new License();
			// for(int s = 0; s <
			// doc.getElementsByTagName("usrv:sla").getLength();
			// s++) {
			// Node node = (Node) doc.getElementsByTagName("usrv:sla").item(s);
			// NodeList nodeList = node.getChildNodes();
			// for(int c = 0; c < nodeList.getLength(); c++) {
			// if(nodeList.item(c).getNodeName().equals("usrv:name")) {
			// slaName = nodeList.item(c).getTextContent();
			// license.setAppName(slaName);
			// }
			// if(nodeList.item(c).getNodeName().equals("usrv:link")) {
			// String link = nodeList.item(c).getTextContent();
			// link = link.substring(link.lastIndexOf("/"));
			// File file = new
			// File(System.getenv("systemdrive")+"/"+dir+"/licenses"+link);
			// license.getSlaList().add(file);
			// }
			// }
			// }
			//
			// for(int i = 0; i <
			// doc.getElementsByTagName("usrv:license").getLength(); i++) {
			// Node n = (Node) doc.getElementsByTagName("usrv:license").item(i);
			// NodeList nlist = n.getChildNodes();
			//
			// for(int j = 0; j < nlist.getLength(); j++) {
			// if(nlist.item(j).getNodeName().equals("usrv:link")) {
			// txt = nlist.item(j).getTextContent();
			// txt = txt.substring(txt.lastIndexOf("/"));
			// l = new
			// File(System.getenv("systemdrive")+"/"+dir+"/licenses"+txt);
			// list.add(l);
			// }
			//
			// }
			// }
			// license.setLicense(list);
			// licenseList.add(license);
			// aal.setLicenses(license);
			// }
			// lw = new LicenceWindow(app, licenseList, aal);
			// app.getMainWindow().addWindow(lw);
			// } catch (ParserConfigurationException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (SAXException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// UsrvInformationWindow info = new UsrvInformationWindow();
			// UsrvInfoController infoController = new UsrvInfoController(aal,
			// lw,
			// app);
			// app.getMainWindow().addWindow(lw);
			// app.getMainWindow().addWindow(info);
			// ToDo: install AAL services with DeployManager and delete temp
			// usrv
			// file with unziped folders

		} catch (Throwable t) {
			t.printStackTrace();
			throw new RuntimeException(t);
		}
	}

}
