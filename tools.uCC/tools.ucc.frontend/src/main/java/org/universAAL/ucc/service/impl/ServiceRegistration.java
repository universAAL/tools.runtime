package org.universAAL.ucc.service.impl;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.universAAL.ucc.service.api.IServiceRegistration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ServiceRegistration implements IServiceRegistration {

	public boolean unregisterService(String serviceId) {
		Document doc = Model.getSrvDocument();
		Element root = doc.getDocumentElement();
		root.removeChild(ServiceManagment.getService(serviceId, doc));

		try {
			TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc),
					new StreamResult(Model.SERVICEFILENAME));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	/**
	 * TODO: register the installed bundles? Can use Map<bundleId,
	 * bundleVersion> as the second parameter Can call this (registerService)
	 */
	public boolean registerApp(String serviceId, String appId, String entryName, String vendorURL, String serviceClass,
			String iconURL) {
		return addAppToXML(serviceId, appId, entryName, vendorURL, serviceClass, iconURL);
	}

	public boolean registerBundle(String serviceId, String bundleId, String bundleVersion) {
		return addBundleToXML(serviceId, bundleId, bundleVersion);
	}

	/**
	 * Register app together with the installed bundles for a service. Use
	 * Map<bundleId, bundleVersion> as the second parameter Can call this
	 * (registerService)
	 */
	/*
	 * public boolean registerAppAndBundles(String serviceId, String appId, Map
	 * bundles) { return addAppAndBundlesToXML(serviceId, appId, bundles); }
	 * 
	 * 
	 * private boolean addAppAndBundlesToXML(String serviceId, String appId, Map
	 * bundles) { boolean success = true; try {
	 * 
	 * Document doc = Model.getSrvDocument();
	 * 
	 * Element element = ServiceManagment.getService(serviceId, doc); if
	 * (element==null) { element = doc.createElement("service");
	 * doc.getDocumentElement().appendChild(element);
	 * element.setAttribute("serviceId", serviceId); } Element appRoot =
	 * doc.createElement("application"); element.appendChild(appRoot);
	 * 
	 * appRoot.setAttribute("appId", appId); Iterator entries =
	 * bundles.entrySet().iterator(); while (entries.hasNext()) { Map.Entry
	 * entry = (Map.Entry) entries.next(); String bundleId =
	 * (String)entry.getKey(); String bundleVersion = (String)entry.getValue();
	 * System
	 * .out.println("[ServiceRegistration.addAppAndBundlesToXML] bundleId = " +
	 * bundleId + ", bundleVersion = " + bundleVersion); Element bundleEl =
	 * doc.createElement("bundle"); bundleEl.setAttribute("id", bundleId);
	 * bundleEl.setAttribute("version", bundleVersion);
	 * appRoot.appendChild(bundleEl); }
	 * 
	 * TransformerFactory.newInstance().newTransformer().transform( new
	 * DOMSource(doc), new StreamResult(Model.SERVICEFILENAME));
	 * 
	 * } catch (Exception e){ e.printStackTrace(); success = false; } return
	 * success; }
	 */

	private boolean addAppToXML(String serviceId, String appId, String entryName, String vendorURL, String serviceClass,
			String iconURL) {
		boolean success = true;
		try {
			Document doc = Model.getSrvDocument();

			Element element = ServiceManagment.getService(serviceId, doc);
			if (element == null) {
				element = doc.createElement("service");
				doc.getDocumentElement().appendChild(element);
				element.setAttribute("serviceId", serviceId);
			}
			Element appRoot = doc.createElement("application");
			element.appendChild(appRoot);

			appRoot.setAttribute("appId", appId);

			// Nicole changes for MenuEntry information
			Element menuEntry = doc.createElement("menuEntry");
			element.appendChild(menuEntry);
			menuEntry.setAttribute("entryName", entryName);
			menuEntry.setAttribute("userID", "");
			menuEntry.setAttribute("vendor", vendorURL);
			menuEntry.setAttribute("serviceClass", serviceClass);
			menuEntry.setAttribute("iconURL", iconURL);

			TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc),
					new StreamResult(Model.SERVICEFILENAME));

		} catch (Exception e) {
			e.printStackTrace();
			success = false;
		}
		return success;
	}

	// Not needed?
	/*
	 * public boolean registerService(String serviceId) { return
	 * addServiceToXML(serviceId); }
	 */

	/*
	 * private boolean addServiceToXML(String serviceId) { // add the initial
	 * service element boolean success = true; Document doc =
	 * Model.getSrvDocument(); Element appRoot = doc.createElement("service");
	 * doc.getDocumentElement().appendChild(appRoot);
	 * appRoot.setAttribute("serviceId", serviceId); try {
	 * TransformerFactory.newInstance().newTransformer().transform( new
	 * DOMSource(doc), new StreamResult(Model.SERVICEFILENAME)); } catch
	 * (Exception e) { e.printStackTrace(); success = false; }
	 * 
	 * return success; }
	 */

	private boolean addBundleToXML(String appId, String bundleId, String bundleVersion) {
		boolean success = true;
		try {
			Document doc = Model.getSrvDocument();

			Element element = ServiceManagment.getService(appId, doc);
			if (element == null) {
				element = doc.createElement("application");
				doc.getDocumentElement().appendChild(element);
				element.setAttribute("appId", appId);
			}
			Element bundleRoot = doc.createElement("bundle");
			element.appendChild(bundleRoot);

			bundleRoot.setAttribute("id", bundleId);
			bundleRoot.setAttribute("version", bundleVersion);

			TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc),
					new StreamResult(Model.SERVICEFILENAME));

		} catch (Exception e) {
			e.printStackTrace();
			success = false;
		}
		return success;
	}

	/*
	 * private boolean registered(String serviceId) { Document doc =
	 * Model.getSrvDocument(); NodeList nodeList = doc.getChildNodes(); for(int
	 * i = 0; i < nodeList.getLength() ; i++){ Element el = (Element)
	 * nodeList.item(i); if(el.getAttribute("serviceId").equals(serviceId)){
	 * System
	 * .out.println("[ServiceRegistration.registered] has a service with id: " +
	 * serviceId); return true; } } System.out.println(
	 * "[ServiceRegistration.registered] there is no service with id: " +
	 * serviceId); return false; }
	 */
}
