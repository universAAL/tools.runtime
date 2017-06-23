package org.universAAL.tools.ucc.controller.ustore.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class Parser {

	static final String SERVICE = "service";
	static final String TITLE = "title";
	static final String SUBTITLE = "subtitle";
	static final String CATEGORY = "category";
	static final String RATING = "rating";
	static final String DETAILS = "details";
	static final String IMAGE = "image";

	public List<Service> readServices(File servicesFile, String searchstring) {
		List<Service> services = new ArrayList<Service>();
		try {
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			InputStream in = new FileInputStream(servicesFile);
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

			Service service = null;

			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();

				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					if (startElement.getName().getLocalPart() == (SERVICE)) {
						service = new Service();
					}

					if (event.isStartElement()) {
						if (event.asStartElement().getName().getLocalPart().equals(TITLE)) {
							event = eventReader.nextEvent();
							service.setTitle(event.asCharacters().getData());
							continue;
						}
					}
					if (event.asStartElement().getName().getLocalPart().equals(SUBTITLE)) {
						event = eventReader.nextEvent();
						service.setSubtitle(event.asCharacters().getData());
						continue;
					}
					if (event.asStartElement().getName().getLocalPart().equals(CATEGORY)) {
						event = eventReader.nextEvent();
						service.setCategory(event.asCharacters().getData());
						continue;
					}
					if (event.asStartElement().getName().getLocalPart().equals(RATING)) {
						event = eventReader.nextEvent();
						service.setRating(event.asCharacters().getData());
						continue;
					}
					if (event.asStartElement().getName().getLocalPart().equals(DETAILS)) {
						event = eventReader.nextEvent();
						service.setDetails(event.asCharacters().getData());
						continue;
					}
					if (event.asStartElement().getName().getLocalPart().equals(IMAGE)) {
						event = eventReader.nextEvent();
						service.setImage(event.asCharacters().getData());
						continue;
					}
				}
				if (event.isEndElement()) {
					EndElement endElement = event.asEndElement();
					if (endElement.getName().getLocalPart() == (SERVICE)) {
						if (service.containsSubstring(searchstring))
							services.add(service);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		return services;
	}

}
