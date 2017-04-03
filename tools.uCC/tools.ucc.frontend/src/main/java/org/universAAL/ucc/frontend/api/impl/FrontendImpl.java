package org.universAAL.ucc.frontend.api.impl;

import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.deploymanager.uapp.model.AalUapp;
import org.universAAL.middleware.deploymanager.uapp.model.Bundle;
import org.universAAL.middleware.deploymanager.uapp.model.DeploymentUnit;
import org.universAAL.middleware.deploymanager.uapp.model.Feature;
import org.universAAL.middleware.deploymanager.uapp.model.Part.PartRequirements;
import org.universAAL.middleware.deploymanager.uapp.model.ReqType;
import org.universAAL.ucc.model.usrv.AalUsrv;
import org.universAAL.ucc.model.usrv.AalUsrv.Srv;
import org.universAAL.ucc.model.usrv.ApplicationType;
import org.universAAL.middleware.deploymanager.uapp.model.Part;
import org.universAAL.middleware.managers.api.InstallationResults;
import org.universAAL.middleware.managers.api.InstallationResultsDetails;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.middleware.util.Constants;
import org.universAAL.ontology.profile.Profilable;
import org.universAAL.ontology.profile.Profile;
import org.universAAL.ontology.profile.User;
import org.universAAL.ontology.profile.service.ProfilingService;
import org.universAAL.ontology.profile.ui.mainmenu.MenuEntry;
import org.universAAL.ontology.profile.ui.mainmenu.MenuProfile;
//import org.universAAL.middleware.interfaces.mpa.model.Part;
import org.universAAL.ucc.controller.install.UsrvInfoController;
import org.universAAL.ucc.frontend.api.IFrontend;
import org.universAAL.ucc.model.AALService;
import org.universAAL.ucc.model.AppItem;
import org.universAAL.ucc.model.Provider;
import org.universAAL.ucc.model.RegisteredService;
import org.universAAL.ucc.model.UAPP;
import org.universAAL.ucc.model.UAPPPart;
import org.universAAL.ucc.model.UAPPReqAtom;
import org.universAAL.ucc.model.install.License;
import org.universAAL.ucc.database.parser.ParserService;
import org.universAAL.ucc.service.api.IServiceManagement;
import org.universAAL.ucc.service.impl.Model;
import org.universAAL.ucc.service.manager.Activator;
import org.universAAL.ucc.windows.DeinstallWindow;
import org.universAAL.ucc.windows.LicenceWindow;
import org.universAAL.ucc.windows.NoConfigurationWindow;
import org.universAAL.ucc.windows.NotificationWindow;
import org.universAAL.ucc.windows.UccUI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.vaadin.ui.Window;

/**
 * Implements the install and de-install processes. Is interface for the
 * DeployManagerService to trigger the different processes like installation and
 * de-installation.
 * 
 * @author Nicole Merkle
 * 
 *         modified by Shanshan, 13-03-2013
 * 
 */

public class FrontendImpl implements IFrontend {

	private static String usrvLocalStore;

	private static String uappURI;
	private static String userSession;
	private String base;
	private ResourceBundle bundle;
	private AALService aal;

	public FrontendImpl() {
		base = "resources.ucc";
		bundle = ResourceBundle.getBundle(base);
		usrvLocalStore = Activator.getTempUsrvFiles().getAbsolutePath();
	}

	public boolean installService(String sessionkey, String serviceId,
			String serviceLink) {
		startUCC();
		aal = new AALService();
		// check for sessionkey
		// if(sessionkey.equals(DesktopController.getSessionKey())) {
		// downloads a usrv-file from the given download-uri
		// TO be unmarked
		System.err
				.println("[[FrontendImpl]] SessionKey: " + sessionkey
						+ " Service-Link: " + serviceLink + " Service-ID: "
						+ serviceId);

		System.err.println("The service link from ustore: " + serviceLink);

		if (serviceLink != null && !serviceLink.equals("")) {
			try {
				downloadUsrvFile(serviceLink, serviceId + ".usrv");
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
		System.out.println("Using the usrfile:"
				+ System.getProperty("uAAL.uCC.usrvfile", usrvLocalStore
						+ serviceId + ".usrv"));
		File temp = new File(System.getProperty("uAAL.uCC.usrvfile",
				usrvLocalStore + serviceId + ".usrv"));
		if (temp.exists()) {
			try {
				extractFolder(temp.getAbsolutePath(), usrvLocalStore);
			} catch (ZipException e2) {
				e2.printStackTrace();
			} catch (IOException e2) {
				e2.printStackTrace();
			}

			// Copy uapp files to C:/tempUsrvFiles/hwo_uapp/
			uappURI = createUAPPLocation(usrvLocalStore + "bin", serviceId
					+ "_temp");

			// extract available uapp files
			File usrv = new File(uappURI);
			File[] uapps = usrv.listFiles();
			for (File cur : uapps) {
				try {
					extractFolder(usrvLocalStore + serviceId + "_temp" + "/"
							+ cur.getName(), usrvLocalStore + serviceId
							+ "_temp" + "/");
				} catch (ZipException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			// parse uapp.config.xml
			// Get uapp.xml name out of the file extension
			File f = new File(usrvLocalStore + serviceId + "_temp" + "/config");
			File[] confi = f.listFiles();
			String configFileName = "";
			for (File cf : confi) {
				if (cf.getName().contains(".xml")) {
					configFileName = cf.getName();
					System.err.println(configFileName);
				}
			}
			
				parseUappConfiguration(usrvLocalStore + serviceId + "_temp"
						+ "/config/" + configFileName, serviceId);
			
			return true;
			// } else {
			// //TODO: SessionKey was not right, what todo?
			// return false;
			// }
		}
		return false;
	}

	/**
	 * Downloads usrv file from the given download uri
	 * 
	 * @param downloadUri
	 *            link from where to download the usrv file
	 */
	private String downloadUsrvFile(String downloadUri, String filename)
			throws IOException {
		System.out
				.println("[FrontendImpl.downloadUsrvFile] the usrv file name is: "
						+ filename);
		URL url = new URL(downloadUri);
		URLConnection con = url.openConnection();
		InputStream in = new BufferedInputStream(con.getInputStream());
		FileOutputStream out = new FileOutputStream(new File(Activator.getTempUsrvFiles(), filename));
		byte[] chunk = new byte[153600];
		int chunkSize;
		while ((chunkSize = in.read(chunk)) > 0) {
			out.write(chunk, 0, chunkSize);
			chunk = new byte[153600];
		}
		out.flush();
		out.close();
		in.close();

		return filename;
	}
	
	private String downloadFile(String uri, String filepath) throws IOException {
		URL url = new URL(uri);
		URLConnection con = url.openConnection();
		InputStream in = new BufferedInputStream(con.getInputStream());
		FileOutputStream out = new FileOutputStream(new File(Activator.getTempUsrvFiles(), filepath));
		byte[] chunk = new byte[153600];
		int chunkSize;
		while ((chunkSize = in.read(chunk)) > 0) {
			out.write(chunk, 0, chunkSize);
			chunk = new byte[153600];
		}
		out.flush();
		out.close();
		in.close();

		return new File(Activator.getTempUsrvFiles(), filepath).getAbsolutePath();
	}

	/**
	 * Parses the given configuration xml from an uapp file to get some
	 * information from the uapp file
	 * 
	 */
	private ArrayList<UAPP> parseUappConfiguration(String f, String serviceId) {
		ArrayList<UAPP> appsList = new ArrayList<UAPP>();
		File l = null;
		String txt = "";
		String slaName = "";
		License license = null;
		ArrayList<License> licenseList = new ArrayList<License>();
		ArrayList<File> list = new ArrayList<File>();
		
		// Read uapp config xml
		AalUapp uapp = null;
		System.err.println(f);
		ParserService ps = Activator.getParserService();
		if (ps != null)
			System.err.println("Got ParserService");
		uapp = ps.getUapp(f);
		System.err.println(uapp.getApp().getAppId());
		String app_ontology_uri = "";
		String icon_path = "";
		String icon_name = "";
		String menuName = "";
		if(uapp.getApp().getMenuEntry() != null) {
			app_ontology_uri = uapp.getApp().getMenuEntry().getServiceUri();
			if(uapp.getApp().getMenuEntry().getIcon().getName() != null 
					&& !uapp.getApp().getMenuEntry().getIcon().getName().equals("")) {
				icon_name = uapp.getApp().getMenuEntry().getIcon().getName();
				aal.setIconPath(icon_name);
			}
			if(uapp.getApp().getMenuEntry().getIcon().getPath() != null 
					&& !uapp.getApp().getMenuEntry().getIcon().getPath().equals("")) {
				icon_path = uapp.getApp().getMenuEntry().getIcon().getPath();
				aal.setIconPath(icon_path);
			}
			menuName = uapp.getApp().getMenuEntry().getMenuName();
			System.err.println(icon_path);
			System.err.println(app_ontology_uri);
			System.err.println(menuName);
		}
		aal.setMenuName(menuName);
		aal.setOntologyUri(app_ontology_uri);
		List<Part> parts = uapp.getApplicationPart().getPart();
		
		//Creating an new UAPP on uCC side
		UAPP up = new UAPP();
		up.setLocation(uappURI);
		up.setAppID(uapp.getApp().getAppId());
		up.setName(uapp.getApp().getName());
		Provider provider = new Provider(uapp.getApp().getApplicationProvider()
				.getContactPerson(), uapp.getApp().getApplicationProvider()
				.getPhone(), uapp.getApp().getApplicationProvider().getEmail(),
				uapp.getApp().getApplicationProvider().getWebAddress());
		//Setting the provider
		up.setProvider(provider);
		String version = String.valueOf(uapp.getApp().getVersion().getMajor())
				.concat(".")
				.concat(String.valueOf(uapp.getApp().getVersion().getMicro())).concat(".")
				.concat(String.valueOf(uapp.getApp().getVersion().getMinor()));
		System.err.println("Version of usrv: "+version);
		//Setting the version
		up.setVersion(version);
		
		//Setting the uapp parts
		for (Part p : parts) {
			UAPPPart ua = new UAPPPart();
			ua.setUappLocation(uappURI);
			System.err.println("The Parts-Location: "+ uappURI);
			System.err.println("Part-ID: "+p.getPartId());
			ua.setAppId(p.getPartId());
			ua.setPart(p);
			ua.setBundleId(p.getBundleId());
			System.err.println(p.getBundleId());
			ua.setBundleVersion(p.getBundleVersion());
			System.err.println("Deployment-UNIT_SIZE: "+p.getDeploymentUnit().size());
			// Here starts the error and breaks the parsing
			// Getting DeploymentUnit
			for (DeploymentUnit du : p.getDeploymentUnit()) {
				// Getting ContainerUnits
				if (du.isSetContainerUnit()) {
					// Karaf features
					if (du.getContainerUnit().isSetKaraf()) {
						if (du.getContainerUnit().getKaraf().getFeatures() == null) {
							System.err.println("No features for " + du.getId());
							continue;
						}
						for (Serializable so : du.getContainerUnit().getKaraf()
								.getFeatures().getRepositoryOrFeature()) {
							if (so instanceof Feature) {
								Feature feat = (Feature) so;
								for (Serializable dco : feat
										.getDetailsOrConfigOrConfigfile()) {
									if (dco instanceof Bundle) {
										Bundle b = (Bundle) dco;
										System.err.println("Bundle-Value: "
												+ b.getValue());
										ua.setUappLocation(b.getValue().trim());
										System.err.println("Bundle-Value: "
												+ b.getValue());
									}
								}
							}
						}
						System.err.println("Featuresize: "
								+ du.getContainerUnit().getKaraf()
										.getFeatures().getRepositoryOrFeature()
										.size());

						System.err.println("Feauture: "
								+ du.getContainerUnit().getKaraf()
										.getFeatures().getRepositoryOrFeature()
										.get(0).toString());
					}
					// Android app
					if (du.getContainerUnit().isSetAndroid()) {
						for (String loc : du.getContainerUnit().getAndroid()
								.getLocation()) {
							ua.setUappLocation(loc);
							System.err.println(loc);
						}
					}
					// Equinox Container as runtime
					if (du.getContainerUnit().isSetEquinox()) {
						// TODO: Parsing for Equinox Container
					}
					// Felix Container as runtime
					if (du.getContainerUnit().isSetFelix()) {
						// TODO: Parsing for Felix
					}
					if (du.getContainerUnit().isSetTomcat()) {
						// TODO: Parsing for Tomcat
					}
					if (du.getContainerUnit().isSetOsgiAndroid()) {
						// TODO: Parsing for OSGI Android
					}
				}
				// OS Unit
				if (du.isSetOsUnit()) {
					// TODO: Parse Values for OSUnit
				}
				// PlatformUnit
				if (du.isSetPlatformUnit()) {
					// TODO: Parse Values for PlatformUnit
				}

			}

			// Getting UAPPReqAtom for validation
			UAPPReqAtom atom = null;
			List<String>atomValues = new ArrayList<String>();
			PartRequirements pr = p.getPartRequirements();
			for (ReqType rt : pr.getRequirement()) {
				atom = new UAPPReqAtom();
				if (rt.isSetReqAtom()) {
					System.err.println("ReqAtom Name: "
							+ rt.getReqAtom().getReqAtomName());
					atom.setName(rt.getReqAtom().getReqAtomName());
					System.err.println("ReqAtom Value: "
							+ rt.getReqAtom().getReqAtomValue());
					// List<String> ll = new ArrayList<String>();
					// ll.add(rt.getReqAtom().getReqAtomValue());
					atomValues.add(rt.getReqAtom().getReqAtomValue());
//					atom.setValue(rt.getReqAtom().getReqAtomValue());
					if (rt.getReqAtom().getReqCriteria() != null) {
						System.err.println("ReqAtom Criteria: "
								+ rt.getReqAtom().getReqCriteria().value());
						atom.setCriteria(rt.getReqAtom().getReqCriteria()
								.value());
					}
				}
				if (rt.isSetReqGroup()) {
					for (ReqType rType : rt.getReqGroup().getRequirement()) {
						if (rType.isSetReqAtom()) {
							System.err.println(rType.getReqAtom()
									.getReqAtomName());
							System.err.println(rType.getReqAtom()
									.getReqAtomValue());
							System.err.println(rType.getReqAtom()
									.getReqCriteria());
						}
					}
				}
				ua.addReqAtoms(atom);
			}
			if(atom != null) {
				atom.setValue(atomValues);
			}

			ua.setAppId(uapp.getApp().getAppId());
			ua.setDescription(uapp.getApp().getDescription());
			ua.setMultipart(uapp.getApp().isMultipart());
			ua.setName(uapp.getApp().getName());

			if (uapp.getApp().isSetVersion()) {
				if (uapp.getApp().getVersion().isSetMajor()) {
					ua.setMajor(uapp.getApp().getVersion().getMajor());
					aal.setMajor(ua.getMajor());
					System.err.println(ua.getMajor());
				}
				if (uapp.getApp().getVersion().isSetMinor()) {
					ua.setMinor(uapp.getApp().getVersion().getMinor());
					aal.setMinor(ua.getMinor());
					System.err.println(ua.getMinor());
				}
				if (uapp.getApp().getVersion().isSetMicro()) {
					ua.setMicro(uapp.getApp().getVersion().getMicro());
					aal.setMicro(ua.getMicro());
					System.err.println(ua.getMicro());
				}

			}
			up.addPart(ua.getPart().getPartId(), ua);
			
		}
		//Adding a uAAP 
		appsList.add(up);
		
			// Creating license files
			for (AalUapp.App.Licenses ls : uapp.getApp().getLicenses()) {
				license = new License();
				if (ls.isSetSla()) {
					slaName = ls.getSla().getName();
					System.err.println("SLA-Name: " + slaName);
					license.setAppName(slaName);
					if (ls.getSla().isSetLink()
							&& !ls.getSla().getLink().trim().isEmpty()) {
						// try {
						// URL slaContent = new URL(ls.getSla().getLink());
						// slaContent.get
						// } catch (MalformedURLException e) {
						// e.printStackTrace();
						// }
						try {
							String link = ls.getSla().getLink();
							System.err.println(link);
							if(link.contains("./")) {
							link = link.substring(link.indexOf("./"));
							System.err.println(link);
							File file = new File(usrvLocalStore + serviceId
									+ "_temp" + link);
							license.getSlaList().add(file);
							} else if(link.contains("http://")) {
								link = "http"+link.substring(link.indexOf(":"));
								try {
								String filePath = downloadFile(link,serviceId+"_temp/license"+link.substring(link.lastIndexOf("/")));
								System.err.println("SLA-PATH: "+filePath);
								File sl = new File(filePath);
								license.getSlaList().add(sl);
								} catch(UnknownHostException io) {
									NoConfigurationWindow nw = new NoConfigurationWindow(bundle.getString("unknown.host.exception"));
									UccUI.getInstance().getMainWindow().addWindow(nw);
									io.printStackTrace();
									return null;
								}
							}
						} catch (Throwable t) {
							NoConfigurationWindow nw = new NoConfigurationWindow(bundle.getString("no.license"));
							UccUI.getInstance().getMainWindow().addWindow(nw);
							t.printStackTrace();
							return null;
						}
					}

				}
				if (ls.isSetLicense()) {
					for (org.universAAL.middleware.deploymanager.uapp.model.LicenseType lt : ls
							.getLicense()) {

						System.err.println("LicenseType is set!!! "
								+ lt.getLink());
						if (lt.isSetLink() && !lt.getLink().trim().isEmpty()) {
							try {
								txt = lt.getLink();
								System.err.println(txt);
								if(txt.contains("./")) {
									txt = txt.substring(txt.indexOf("./"));
								 
								System.err.println(txt);
								l = new File(usrvLocalStore + serviceId
										+ "_temp" + txt);
								list.add(l);
							} else if(txt.contains("http://")) {
								txt = "http"+txt.substring(txt.indexOf(":"));
								try {
									String filePath = downloadFile(txt, serviceId+"_temp/license"+txt.substring(txt.lastIndexOf("/")));
									System.err.println("FILE-PATH from license: "+filePath);
									File lic = new File(filePath);
									list.add(lic);
								} catch(UnknownHostException uhe) {
										NoConfigurationWindow nw = new NoConfigurationWindow(bundle.getString("unknown.host.exception"));
										UccUI.getInstance().getMainWindow().addWindow(nw);
										uhe.printStackTrace();
										return null;
								}
							}
							} catch (Throwable t) {
								NoConfigurationWindow nw = new NoConfigurationWindow(bundle.getString("no.license"));
								UccUI.getInstance().getMainWindow().addWindow(nw);
								t.printStackTrace();
								return null;
							}
						}

					}
				}

//			}
			license.setLicense(list);
			licenseList.add(license);
			aal.setLicenses(license);

		}
		System.err.println("Size of APP-List "+appsList.size());
		aal.setUaapList(appsList);
		parseConfiguration(serviceId + "_temp", appsList, licenseList, aal);
		
		return appsList;

	}

	/**
	 * Parses the given configuration xml from the usrv file to get some
	 * information about the usrv.
	 * 
	 * @return AALService with some information about the usrv file
	 * @throws SAXException
	 * @throws IOException
	 */
	private AALService parseConfiguration(String f, ArrayList<UAPP> apps,
			ArrayList<License> licenseList, AALService aal) {

		// Parsing usrv.xml
		ParserService ps = Activator.getParserService();
		// Getting usrv.xml
		File configFile = new File(usrvLocalStore + "config");
		File[] confis = configFile.listFiles();
		String configFileName = "";
		for (File cf : confis) {
			if (cf.getName().contains(".xml")) {
				configFileName = cf.getName();
			}
		}
		AalUsrv usrv = ps.getUsrv(usrvLocalStore + "config/" + configFileName);
//		List<ApplicationType> xmlApps = usrv.getComponents().getApplication();
//		for (ApplicationType xmlApp : xmlApps) {
//			UAPP modelUAAP = new UAPP();
//			modelUAAP.setName(xmlApp.getName());
//			Provider provider = new Provider(usrv.getSrv().getServiceProvider().getOrganizationName(), usrv.getSrv().getServiceProvider().getPhone(), 
//					usrv.getSrv().getServiceProvider().getEmail(), usrv.getSrv().getServiceProvider().getWebAddress());
//			modelUAAP.setProvider(provider);
//			String version = usrv.getSrv().getVersion().getMajor()+ "."+ usrv.getSrv().getVersion().getMicro() + "." + usrv.getSrv().getVersion().getMinor();
//			modelUAAP.setVersion(version);
//			for(Map.Entry<String, UAPPPart> part : modelUAAP.getParts().entrySet()) {
//				modelUAAP.getParts().put(part.getKey(), part.getValue());
//				aal.getUaapList().add( modelUAAP );
//			}
//		}
		/*
		for (UAPP up : aal.getUaapList()) {
			for (Map.Entry<String, UAPPPart> ua : up.getParts().entrySet()) {
				System.err.println(ua.getValue().getAppId());
				aal.getUaapList().add(up);
			}
		}
		*/
		if (usrv.isSetSrv()) {
			if (usrv.getSrv().isSetServiceId()) {
				System.err.println("Service-ID: "
						+ usrv.getSrv().getServiceId());
				aal.setServiceId(usrv.getSrv().getServiceId());
			}
			if (usrv.getSrv().isSetName()) {
				aal.setName(usrv.getSrv().getName());
				System.err.println("Service-Name: " + usrv.getSrv().getName());
			}
			if (usrv.getSrv().isSetServiceProvider()) {
				aal.setProvider(usrv.getSrv().getServiceProvider()
						.getOrganizationName());
				System.err.println("ServiceProvider: " + aal.getProvider());
			}
			if (usrv.getSrv().isSetDescription()) {
				aal.setDescription(usrv.getSrv().getDescription());
				System.err.println("Description: " + aal.getDescription());
			}

//			 if(usrv.getSrv().isSetVersion()) {
//			 if(usrv.getSrv().getVersion().isSetMajor()) {
//			 aal.setMajor(usrv.getSrv().getVersion().getMajor());
//			 System.err.println(aal.getMajor());
//			 }
//			 if(usrv.getSrv().getVersion().isSetMinor()) {
//			 aal.setMinor(usrv.getSrv().getVersion().getMinor());
//			 System.err.println(aal.getMinor());
//			 }
//			 if(usrv.getSrv().getVersion().isSetMicro()) {
//			 aal.setMicro(usrv.getSrv().getVersion().getMicro());
//			 System.err.println(aal.getMicro());
//			 }
//			 }
			if (usrv.getSrv().isSetTags()) {
				aal.getTags().add(usrv.getSrv().getTags());
				System.err.println("Tags: " + aal.getTags());
			}

			System.err.println("SET LicenseWindow");
			
			boolean isAlreadyInstalled = Activator.getMgmt().isServiceId(aal.getServiceId());
			
			if(!isAlreadyInstalled) {
			LicenceWindow lw = null;
			for(UAPP installingApp : aal.getUaapList()) {
			try {
				lw = new LicenceWindow(UccUI.getInstance(), licenseList, aal, installingApp);
			} catch (IOException e) {
				e.printStackTrace();
			}
			new UsrvInfoController(aal, lw, UccUI.getInstance());
			}
		} else {
			NoConfigurationWindow ncw = new NoConfigurationWindow(bundle.getString("srv.already.exists"));
			UccUI.getInstance().getMainWindow().addWindow(ncw);
			return null;
		}
		}
		return aal;
	}

	private String createUAPPLocation(String path, String newPath) {
		File pa = new File(path);
		File[] dirs = pa.listFiles();
		File rootFile = new File(usrvLocalStore + newPath);
		rootFile.mkdir();
		for (int i = 0; i < dirs.length; i++) {
			File f = new File(usrvLocalStore + newPath + "/"
					+ dirs[i].getName());
			System.err.println("Dir-Name: " + dirs[i].getName());
			if (dirs[i].isDirectory()) {
				f.mkdir();
			}
			dirs[i].renameTo(f);
			System.err.println(f.getAbsolutePath());
		}
		System.err.println("UAPP Path: " + usrvLocalStore + newPath);
		return usrvLocalStore + newPath;
	}

	/**
	 * Uninstalls the a installed AAL service.
	 */
	public void uninstallService(String sessionKey, String serviceId) {
		// get the list of uapps installed for this serviceId
		// TODO: List<String appId> getInstalledApps(String serviceId)
		// for each uapp, call ucc.controller.requestToUninstall(serviceId,
		// appId)
		// update the service registration
		IServiceManagement sm = Activator.getMgmt();
		List<String> uappList = sm.getInstalledApps(serviceId);
		if (uappList != null) {
			System.err.println("Size of apps to uninstall: " + uappList.size());
			for (String del : uappList) {
				System.err.println("Apps to delete: " + del);
				InstallationResultsDetails result = Activator.getDeinstaller()
						.requestToUninstall(serviceId, del);
				System.err.println("Uninstall Result: " + result.getGlobalResult().toString());
				if (result.getGlobalResult() == InstallationResults.SUCCESS) {
					//My changes
					String entryName = "";
					String userID = "";
					String serviceClass = "";
					String icon = "";
					String vendor = "";
//					List<RegisteredService> ids = new ArrayList<RegisteredService>();
					Document doc = Model.getSrvDocument();
					NodeList nodeList = doc.getElementsByTagName("service");
					for (int i = 0; i < nodeList.getLength(); i++) {
						Element usrv = (Element)nodeList.item(i);
						if(usrv.getAttribute("serviceId").equals(serviceId)) {
							RegisteredService srv = new RegisteredService();
							Element element = (Element) nodeList.item(i);
							System.err.println(element.getAttribute("serviceId"));
							srv.setServiceId(element.getAttribute("serviceId"));
							NodeList srvChilds = element.getChildNodes();
							for(int j = 0; j < srvChilds.getLength(); j++) {
								Node n = srvChilds.item(j);
//								if(n.getNodeName().equals("application")) {
//									Element e = (Element)n;
//									srv.getAppId().add(e.getAttribute("appId"));
//								}
								
								if(n.getNodeName().equals("menuEntry")) {
									Element e = (Element)n;
									entryName = e.getAttribute("entryName");
									icon = e.getAttribute("iconURL");
									vendor = e.getAttribute("vendor");
									serviceClass = e.getAttribute("serviceClass");
									userID = e.getAttribute("userID");
								}
						}
//						ids.add(srv);
					}
				}
					removeEntry(userID, entryName, vendor, serviceClass, icon);
						//My changes
					Activator.getReg().unregisterService(serviceId);
					NoConfigurationWindow nw = new NoConfigurationWindow(bundle.getString("success.uninstall.msg"));
					UccUI.getInstance().getMainWindow().addWindow(nw);
				} else if (result.getGlobalResult() == InstallationResults.MISSING_PEER) {
					NoConfigurationWindow nw = new NoConfigurationWindow(
							bundle.getString("uninstall.failure")
									+ "<br>Error: Missing peer");
					UccUI.getInstance().getMainWindow().addWindow(nw);
				} else {
					NoConfigurationWindow nw = new NoConfigurationWindow(
							bundle.getString("uninstall.failure"));
					UccUI.getInstance().getMainWindow().addWindow(nw);
				}
			}
		} else {
			NoConfigurationWindow nw = new NoConfigurationWindow(
					bundle.getString("no.service"));
			UccUI.getInstance().getMainWindow().addWindow(nw);
		}

	}

	public void update(String sessionKey, String serviceId, String serviceLink) {
		uninstallService(sessionKey, serviceId);
		installService(sessionKey, serviceId, serviceLink);
	}

	public String getInstalledServices(String sessionKey) {
		String services = Activator.getMgmt().getInstalledServices();
		System.out
				.println("[FrontendImpl.getInstalledServices] the services installed: "
						+ services);
		return services;
	}

	public String getInstalledUnitsForService(String sessionKey,
			String serviceId) {
		String units = Activator.getMgmt().getInstalledUnitsForService(
				serviceId);
		System.out
				.println("[FrontendImpl.getInstalledUnitsForServices] the units installed: "
						+ units);
		return units;
	}

	public static String getUappURI() {
		return uappURI;
	}

//	public static void setUappURI(String uappURI) {
//		FrontendImpl.uappURI = uappURI;
//	}

	static public void extractFolder(String zipFile, String destdir)
			throws ZipException, IOException {
		System.out.println("[Installer.extractFolder] the zip file is: "
				+ zipFile + " and dest dir: " + destdir);
		int BUFFER = 2048;
		File file = new File(zipFile);

		ZipFile zip = new ZipFile(file);
		String newPath = destdir;

		new File(newPath).mkdir();
		Enumeration<? extends ZipEntry> zipFileEntries = zip.entries();

		// Process each entry
		while (zipFileEntries.hasMoreElements()) {
			// grab a zip file entry
			ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
			String currentEntry = entry.getName();
			File destFile = new File(newPath, currentEntry);
			// destFile = new File(newPath, destFile.getName());
			File destinationParent = destFile.getParentFile();

			// create the parent directory structure if needed
			destinationParent.mkdirs();

			if (!entry.isDirectory()) {
				BufferedInputStream is = new BufferedInputStream(
						zip.getInputStream(entry));
				int currentByte;
				// establish buffer for writing file
				byte data[] = new byte[BUFFER];

				// write the current file to disk
				FileOutputStream fos = new FileOutputStream(destFile);
				BufferedOutputStream dest = new BufferedOutputStream(fos,
						BUFFER);

				// read and write until last byte is encountered
				while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
					dest.write(data, 0, currentByte);
				}
				dest.flush();
				dest.close();
				is.close();
			}

		}
	}

	/**
	 * Returns a generated Sessionkey for uStore
	 */
	public String getSessionKey(String username, String password) {
		SecureRandom sr = new SecureRandom();
		userSession = new BigInteger(130, sr).toString(32);
		return userSession;
	}

	public static String getUserSession() {
		return userSession;
	}

	public void startUCC() {
		if (UccUI.getInstance() == null) {
			System.err.println("UCC is null so not running");
			// Opens a browser window and loads the ucc site
			Desktop desk = Desktop.getDesktop();
			try {
				desk.browse(new URI("http://127.0.0.1:8080/ucc"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void removeEntry(String userID, String entryName, String vendor,
		    String serviceClass, String iconURL) {
		if ("".equals(iconURL))
		    iconURL = null;

		MenuEntry me = new MenuEntry(null);
		me.setVendor(new Resource(vendor));
		me.setServiceClass(new Resource(serviceClass));
		Resource pathElem = new Resource(iconURL);
		pathElem.setResourceLabel(entryName);
		me.setPath(new Resource[] { pathElem });

		ServiceRequest sr = new ServiceRequest(new ProfilingService(), null);
		sr.addValueFilter(new String[] { ProfilingService.PROP_CONTROLS },
			new User(Constants.uAAL_MIDDLEWARE_LOCAL_ID_PREFIX+userID));
		sr.addValueFilter(new String[] { ProfilingService.PROP_CONTROLS,
			Profilable.PROP_HAS_PROFILE, Profile.PROP_HAS_SUB_PROFILE,
			MenuProfile.PROP_ENTRY }, me);
		sr.addRemoveEffect(new String[] { ProfilingService.PROP_CONTROLS,
			Profilable.PROP_HAS_PROFILE, Profile.PROP_HAS_SUB_PROFILE,
			MenuProfile.PROP_ENTRY });

		ServiceResponse res = Activator.getSc().call(sr);
		if (res.getCallStatus() == CallStatus.succeeded) {
		    LogUtils.logDebug(Activator.getmContext(), FrontendImpl.class, "removeEntry",
			    new Object[] {
				    "existing menu entry " + entryName + " for user ",
				    Constants.uAAL_MIDDLEWARE_LOCAL_ID_PREFIX+userID, " added." }, null);
		} else {
		    LogUtils.logDebug(Activator.getmContext(), FrontendImpl.class, "removeEntry",
			    new Object[] { "callstatus is not succeeded" }, null);
		}
	    }

}
