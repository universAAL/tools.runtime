package org.universAAL.tools.makrorecorder.osgi.pattern;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.universAAL.middleware.rdf.Resource;
import org.universAAL.tools.makrorecorder.osgi.Activator;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author maxim djakow
 */
public class PatternXMLParser {

	public static Pattern genPattern(File xml) {
		Pattern pattern = null;
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xml);
			doc.getDocumentElement().normalize();

			pattern = new Pattern();

			Node root = doc.getDocumentElement();
			pattern.setName(root.getAttributes().getNamedItem("name").getTextContent());

			if (root.getAttributes().getNamedItem("active").getTextContent() == "true")
				pattern.activate();
			else
				pattern.deactivate();

			pattern.setDescription(doc.getElementsByTagName("description").item(0).getTextContent());

			NodeList nl = doc.getElementsByTagName("resource");
			for (int i = 0; i < nl.getLength(); i++) {
				Node n = nl.item(i);
				String rStr = n.getTextContent();
				Object o = Activator.getSerializer().deserialize(rStr);
				if (o instanceof Resource) {
					if (n.getParentNode().getNodeName() == "input")
						pattern.addInput((Resource) o);
					else if (n.getParentNode().getNodeName() == "output")
						pattern.addOutput((Resource) o);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pattern;
	}

	public static void genXML(Pattern pattern) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("pattern");
			doc.appendChild(rootElement);

			Attr name = doc.createAttribute("name");
			name.setValue(pattern.getName());
			rootElement.setAttributeNode(name);

			Attr active = doc.createAttribute("active");
			if (pattern.isActive())
				active.setValue("true");
			else
				active.setValue("false");
			rootElement.setAttributeNode(active);

			Element description = doc.createElement("description");
			description.appendChild(doc.createTextNode(pattern.getDescription()));
			rootElement.appendChild(description);

			Element input = doc.createElement("input");
			for (Resource r : pattern.getInput()) {
				Element resource = doc.createElement("resource");
				String seri = Activator.getSerializer().serialize(r);
				resource.appendChild(doc.createTextNode(seri));
				input.appendChild(resource);
			}
			rootElement.appendChild(input);

			Element output = doc.createElement("output");
			for (Resource r : pattern.getOutput()) {
				Element resource = doc.createElement("resource");
				String seri = Activator.getSerializer().serialize(r);
				resource.appendChild(doc.createTextNode(seri));
				output.appendChild(resource);
			}
			rootElement.appendChild(output);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(
					pattern.getFile().toString().replace(Pattern.patternFileType, ".xml"));

			transformer.transform(source, result);

		} catch (DOMException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
}
