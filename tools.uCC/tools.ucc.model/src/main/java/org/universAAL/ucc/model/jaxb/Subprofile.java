package org.universAAL.ucc.model.jaxb;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;

public class Subprofile {
	private ArrayList<SimpleObject> simpleObjects = new ArrayList<SimpleObject>();
	private ArrayList<EnumObject> enums = new ArrayList<EnumObject>();
	private ArrayList<CollectionValues> collections = new ArrayList<CollectionValues>();
	private String name;

	@XmlElementWrapper(name = "simpleObjects")
	@XmlElements({ @XmlElement(name = "string", type = StringValue.class),
			@XmlElement(name = "integer", type = IntegerValue.class),
			@XmlElement(name = "boolean", type = BooleanValue.class),
			@XmlElement(name = "double", type = DoubleValue.class),
			@XmlElement(name = "calendar", type = CalendarValue.class) })
	public ArrayList<SimpleObject> getSimpleObjects() {
		return simpleObjects;
	}

	public void setSimpleObjects(ArrayList<SimpleObject> simpleObjects) {
		this.simpleObjects = simpleObjects;
	}

	@XmlElementWrapper(name = "collections")
	@XmlElement(name = "collection")
	public ArrayList<CollectionValues> getCollections() {
		return collections;
	}

	public void setCollections(ArrayList<CollectionValues> collections) {
		this.collections = collections;
	}

	@XmlAttribute(name = "name", required = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElementWrapper(name = "enumObjects")
	@XmlElement(name = "enumObject")
	public ArrayList<EnumObject> getEnums() {
		return enums;
	}

	public void setEnums(ArrayList<EnumObject> enums) {
		this.enums = enums;
	}

}
