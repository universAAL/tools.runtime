package org.universAAL.tools.ucc.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.universAAL.tools.ucc.service.api.IServiceManagement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ServiceManagment implements IServiceManagement {

	/**
	 * return a list of installed services in the following format: <services>
	 * <service>serviceId</service> <service>serviceId</service> ... </services>
	 */
	public String getInstalledServices() {
		if (new File(Model.SERVICEFILENAME).exists()) {
			String services = "<services>";
			Document doc = Model.getSrvDocument();
			NodeList nodeList = doc.getElementsByTagName("service");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Element element = (Element) nodeList.item(i);
				services = services + "<serviceId>" + element.getAttribute("serviceId") + "</serviceId>";
			}

			// return list;
			services += "</services>";
			return services;

		} else {
			// return new ArrayList<String>();
			return "<services></services>";
		}
	}

	/**
	 * get installed units for a service in the following format: <serviceUnits>
	 * <unit><id>bundleId</id><version>bundleVersion</version></unit>
	 * <unit><id>bundleId</id><version>bundleVersion</version></unit> ...
	 * </serviceUnits>
	 */
	public String getInstalledUnitsForService(String serviceId) {
		if (new File(Model.SERVICEFILENAME).exists()) {
			String services = "<serviceUnits>";
			Document doc = Model.getSrvDocument();
			Element serviceEl = getService(serviceId, doc);
			System.err.println(serviceEl.getNodeName());
			NodeList nodeList = serviceEl.getElementsByTagName("bundle");
			System.out.println("[ServiceManagement.getInstalledUnitsForService] the number of nodes for bundle: "
					+ nodeList.getLength());
			for (int i = 0; i < nodeList.getLength(); i++) {
				Element element = (Element) nodeList.item(i);
				System.err.println(element.getNodeName() + " " + element.getNodeValue());
				services = services + "<unit><id>" + element.getAttribute("id") + "</id><version>"
						+ element.getAttribute("version") + "</version></unit>";

			}
			services += "</serviceUnits>";
			return services;

		} else {
			return "<serviceUnits></serviceUnits>";
		}
	}

	public static Element getService(String serviceId, Document doc) {
		NodeList nodeList = doc.getChildNodes();
		System.out.println("[nodeList] the number of child nodes: " + nodeList.getLength());
		for (int i = 0; i < nodeList.getLength(); i++) {
			System.err.println(i);
			Node el = nodeList.item(i);
			NodeList nl = el.getChildNodes();
			System.err.println("Services: " + nl.getLength());
			for (int j = 0; j < nl.getLength(); j++) {
				if (nl.item(j).getNodeName().equals("service")) {
					Element elem = (Element) nl.item(j);
					System.err.println("Service Id: " + elem.getAttribute("serviceId"));
					if (elem.getAttribute("serviceId").equals(serviceId)) {

						return elem;
					}
				}
			}
		}
		System.out.println("[ServiceManagement.getService] there is no service with id: " + serviceId);
		return null;
	}

	public boolean isServiceId(String serviceId) {
		if (new File(Model.SERVICEFILENAME).exists()) {
			Document doc = Model.getSrvDocument();
			Element ser = getService(serviceId, doc);
			if (ser != null)
				return true;
		}
		return false;
	}

	/**
	 * get the list of appIds that are installed for a service
	 * 
	 * @param serviceId
	 * @return
	 */
	public List<String> getInstalledApps(String serviceId) {
		if (new File(Model.SERVICEFILENAME).exists()) {
			List<String> list = new ArrayList<String>();
			Document doc = Model.getSrvDocument();
			Element serviceEl = getService(serviceId, doc);
			if (serviceEl != null) {
				NodeList nodeList = serviceEl.getElementsByTagName("application");
				for (int i = 0; i < nodeList.getLength(); i++) {
					Element element = (Element) nodeList.item(i);
					list.add(element.getAttribute("appId"));
				}
			}

			return list;
		} else {
			return new ArrayList<String>();
		}
	}

	/**
	 * set userID value for the MenuEntry
	 */
	public void addUserIDToMenuEntry(String serviceId, String userID) {
		if (new File(Model.SERVICEFILENAME).exists()) {
			Document doc = Model.getSrvDocument();
			Element el = getService(serviceId, doc);
			NodeList nodeList = el.getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++) {
				Element entry = (Element) nodeList.item(i);
				if (entry.getNodeName().equals("menuEntry")) {
					if (entry.getAttribute("userID").equals("") || entry.getAttribute("userID") == null)
						entry.setAttribute("userID", userID);

				}
			}
			try {
				TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc),
						new StreamResult(Model.SERVICEFILENAME));
			} catch (TransformerConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerFactoryConfigurationError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
